package name.sinnema.game.server;

import java.util.Collections;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/moves", produces = MediaTypes.HAL_JSON_VALUE)
public class MovesController {

  @RequestMapping(method = RequestMethod.GET)
  public Resources<Resource<MoveDto>> getMoves() {
    return Resources.wrap(Collections.emptyList());
  }

}
