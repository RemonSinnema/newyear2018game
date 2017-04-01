package name.sinnema.game.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.relaxedLinks;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import name.sinnema.game.engine.TurnbasedGame;
import name.sinnema.game.engine.World;
import name.sinnema.game.engine.WorldRenderer;
import name.sinnema.game.tictactoe.TicTacToe;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = { GameApplication.class, GettingStartedGuide.class })
public class GettingStartedGuide {

  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/apis-docs-fragments");
  @Autowired
  private WebApplicationContext context;
  private MockMvc client;
  private RestDocumentationResultHandler gettingStartedGuide;
  private final ObjectMapper mapper = new ObjectMapper();
  @Autowired
  private WorldRenderer worldRenderer;

  @Bean
  public WorldRenderer worldRenderer() {
    return mock(WorldRenderer.class);
  }

  @Bean
  public TurnbasedGame game() {
    return new TicTacToe();
  }

  @Before
  public void init() {
    gettingStartedGuide = document("{method-name}/{step}",
        preprocessRequest(prettyPrint()),
        preprocessResponse(prettyPrint()));
    client = MockMvcBuilders.webAppContextSetup(context)
        .apply(documentationConfiguration(restDocumentation))
        .alwaysDo(gettingStartedGuide)
        .build();
  }

  @Test
  @SuppressWarnings("unchecked")
  public void playGame() throws Exception {
    // Add players
    MvcResult state = getGame();
    String playersUri = getPlayersUri(state);
    state = client.perform(
        get(playersUri))
        .andExpect(status().isOk())
        .andReturn();
    state = addPlayer(playersUri, "armin");
    String playerUri = getLocation(state);
    state = client.perform(get(playerUri))
        .andExpect(status().isOk())
        .andReturn();
    assertEquals("Player name", "armin", getResponseField(state, "name", String.class));
    client.perform(
        get(playersUri))
        .andExpect(status().isOk());
    addPlayer(playersUri, "yiri");
    tryAddPlayer(playersUri, "ray")
        .andExpect(status().isForbidden());

    // Start game
    String startUri = getLinkUri(getGame(), LinkRelations.START);
    state = client.perform(post(startUri))
        .andExpect(status().isOk())
        .andReturn();
    assertEquals("Level", 1, getResponseField(state, "level", Integer.class).intValue());
    assertNotNull("Current player", getLinkUri(state, LinkRelations.CURRENT_PLAYER));
    String worldUri = getLinkUri(state, LinkRelations.WORLD);
    when(worldRenderer.getMediaType()).thenReturn("image/png");
    doAnswer(invocation -> {
      OutputStream output = invocation.getArgumentAt(1, OutputStream.class);
      output.write("<Graphic representation of the world>".getBytes(StandardCharsets.UTF_8));
      return null;
    }).when(worldRenderer).render(any(World.class), any(OutputStream.class));
    client.perform(
        get(worldUri))
        .andExpect(status().isOk());
    verify(worldRenderer).render(any(World.class), any(OutputStream.class));

    // Make move
    String movesUri = getLinkUri(getGame(), LinkRelations.MOVES);
    state = client.perform(
        get(movesUri))
        .andExpect(status().isOk())
        .andReturn();
    List<Map<String, Object>> moves = getResponseField(state, "_embedded.moves", List.class);
    assertNotNull("Missing moves", moves);
    System.err.println(moves);

    Map<String, Map<String, String>> links = (Map<String, Map<String, String>>)moves.get(0).get("_links");
    assertNotNull("Missing move links in: " + moves, links);
    String moveUri = links.entrySet().stream()
        .filter(link -> "self".equals(link.getKey()))
        .map(link -> link.getValue().get("href"))
        .findAny()
        .orElseThrow(() -> new AssertionError("Missing move link"));
    state = client.perform(
        post(moveUri))
        .andExpect(status().isSeeOther())
        .andReturn();
    assertEquals("Game URI", "/", URI.create(getLocation(state)).getPath());
  }

  private String getPlayersUri(MvcResult state) throws Exception, UnsupportedEncodingException {
    return getLinkUri(state, LinkRelations.PLAYERS);
  }

  private MvcResult getGame() throws Exception {
    return client.perform(
        get("/"))
            .andExpect(status().isOk())
            .andDo(gettingStartedGuide.document(
                relaxedLinks(
                    linkWithRel(LinkRelations.PLAYERS).description("The <<resources-players,Players resource>>"))))
            .andReturn();
  }

  private MvcResult addPlayer(String playersUri, String name) throws Exception {
    return tryAddPlayer(playersUri, name)
        .andExpect(status().isCreated())
        .andReturn();
  }

  private ResultActions tryAddPlayer(String playersUri, String name) throws Exception {
    PlayerDto player = new PlayerDto();
    player.setName(name);
    return client.perform(
        post(playersUri)
            .content(mapper.writeValueAsBytes(player))
            .contentType(MediaTypes.HAL_JSON));
  }

  private String getLinkUri(MvcResult state, String rel) throws UnsupportedEncodingException {
    return getResponseField(state, "_links." + rel + ".href", String.class);
  }

  private <T> T getResponseField(MvcResult state, String jsonPath, Class<T> type) {
    String json = getResponseBody(state);
    try {
      return JsonPath.parse(json).read(jsonPath, type);
    } catch (PathNotFoundException e) {
      throw new AssertionError("Failed to find path " + jsonPath + " in:\n" + json, e);
    } catch (Exception e) {
      throw new AssertionError("Failed to convert " + jsonPath + " to " + type + ": " + json, e);
    }
  }

  private String getResponseBody(MvcResult state) throws AssertionError {
    try {
      return state.getResponse().getContentAsString();
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("Failed to parse content");
    }
  }

  private String getLocation(MvcResult state) {
    return state.getResponse().getHeader("Location");
  }

}
