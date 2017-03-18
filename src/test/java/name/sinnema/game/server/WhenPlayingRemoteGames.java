package name.sinnema.game.server;

import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
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
import org.springframework.hateoas.MediaTypes;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jayway.jsonpath.JsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = GameApplication.class)
public class WhenPlayingRemoteGames {

  @Rule
  public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("build/apis-docs-fragments");
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;
  private RestDocumentationResultHandler documentationHandler;

  @Before
  public void init() {
    documentationHandler = document("{method-name}/{step}",
        preprocessRequest(prettyPrint()),
        preprocessResponse(prettyPrint()));
    mockMvc = MockMvcBuilders.webAppContextSetup(context)
        .apply(documentationConfiguration(restDocumentation))
        .defaultRequest(get("/").accept(MediaTypes.HAL_JSON))
        .alwaysDo(documentationHandler)
        .build();
  }

  @Test
  public void index() throws Exception {
    MvcResult result = mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andDo(documentationHandler.document(
            links(
                linkWithRel("games").description("The <<resources-games,Games resource>>"))))
        .andReturn();
    mockMvc.perform(get(getLinkUri(result, "games")))
        .andExpect(status().isOk());
  }

  private String getLinkUri(MvcResult result, String rel) throws UnsupportedEncodingException {
    return JsonPath.parse(result.getResponse().getContentAsString()).read("_links." + rel + ".href");
  }

}
