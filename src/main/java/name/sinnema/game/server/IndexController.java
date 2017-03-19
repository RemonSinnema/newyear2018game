package name.sinnema.game.server;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class IndexController {

  @RequestMapping(method = RequestMethod.GET, produces = MediaTypes.HAL_JSON_VALUE)
  @ResponseBody
  public ResourceSupport index() {
    ResourceSupport index = new ResourceSupport();
    index.add(linkTo(PlayersController.class).withRel(LinkRelations.PLAYERS));
    return index;
  }

}
