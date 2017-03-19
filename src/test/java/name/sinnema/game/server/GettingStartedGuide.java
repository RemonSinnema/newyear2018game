package name.sinnema.game.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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

import java.io.UnsupportedEncodingException;

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
        .defaultRequest(get("/").accept(MediaTypes.HAL_JSON))
        .alwaysDo(gettingStartedGuide)
        .build();
  }

  @Test
  public void addPlayer() throws Exception {
    String playersUri = getPlayersUri();
    MvcResult state = client.perform(
        get(playersUri))
        .andExpect(status().isOk())
        .andReturn();
    state = addPlayer(playersUri, "armin");
    String playerUri = getCreatedUri(state);
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
  }

  private String getPlayersUri() throws Exception, UnsupportedEncodingException {
    return getLinkUri(getGame(), LinkRelations.PLAYERS);
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
      throw new AssertionError("Failed to find path " + jsonPath + " in:\n" + json);
    }
  }

  private String getResponseBody(MvcResult state) throws AssertionError {
    try {
      return state.getResponse().getContentAsString();
    } catch (UnsupportedEncodingException e) {
      throw new AssertionError("Failed to parse content");
    }
  }

  private String getCreatedUri(MvcResult state) {
    return state.getResponse().getHeader("Location");
  }

  @Test
  public void playGame() throws Exception {
    String startUri = getLinkUri(getGame(), LinkRelations.START);
    MvcResult state = client.perform(post(startUri))
        .andExpect(status().isOk())
        .andReturn();
    assertEquals("Level", 1, getResponseField(state, "level", Integer.class).intValue());
    assertNotNull("Current player", getLinkUri(state, LinkRelations.CURRENT_PLAYER));
    assertNotNull("Moves", getLinkUri(state, LinkRelations.MOVES));
  }

}
