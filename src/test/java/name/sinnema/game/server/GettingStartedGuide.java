package name.sinnema.game.server;

import static org.junit.Assert.assertEquals;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

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
    MvcResult state = client.perform(
        get("/"))
        .andExpect(status().isOk())
        .andDo(gettingStartedGuide.document(
            relaxedLinks(
                linkWithRel(LinkRelations.PLAYERS).description("The <<resources-players,Players resource>>"))))
        .andReturn();
    String playersUri = getLinkUri(state, LinkRelations.PLAYERS);
    state = client.perform(
        get(playersUri))
        .andExpect(status().isOk())
        .andReturn();
    PlayerDto player = new PlayerDto();
    player.setName("armin");
    state = client.perform(
        post(playersUri)
            .content(mapper.writeValueAsBytes(player))
            .contentType(MediaTypes.HAL_JSON))
        .andExpect(status().isCreated())
        .andReturn();
    String playerUri = getCreatedUri(state);
    state = client.perform(get(playerUri))
        .andExpect(status().isOk())
        .andReturn();
    assertEquals("Player name", "armin", getResponseField(state, "name"));
    client.perform(
        get(playersUri))
        .andExpect(status().isOk());
  }

  private String getLinkUri(MvcResult state, String rel) throws UnsupportedEncodingException {
    return getResponseField(state, "_links." + rel + ".href");
  }

  private String getResponseField(MvcResult state, String jsonPath) throws UnsupportedEncodingException {
    return JsonPath.parse(state.getResponse().getContentAsString()).read(jsonPath);
  }

  private String getCreatedUri(MvcResult state) {
    return state.getResponse().getHeader("Location");
  }

}
