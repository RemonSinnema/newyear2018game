package name.sinnema.game.server;

import java.util.Collections;

import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/games")
public class GamesController {

  @RequestMapping(produces = MediaTypes.HAL_JSON_VALUE)
  public Resources<Resource<GameDto>> list() {
    return Resources.wrap(Collections.emptyList());
  }

}
