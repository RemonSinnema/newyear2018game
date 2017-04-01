package name.sinnema.game.server;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import name.sinnema.game.engine.TurnbasedGame;
import name.sinnema.game.engine.WorldRenderer;


@Controller
@RequestMapping(path = "/world")
public class WorldController {

  @Autowired
  private TurnbasedGame game;
  @Autowired
  private WorldRenderer renderer;

  @RequestMapping(method = RequestMethod.GET)
  public void getWorld(HttpServletResponse response) {
    response.setContentType(renderer.getMediaType());
    try {
      renderer.render(game.getCurrentWorld(), response.getOutputStream());
    } catch (IOException e) {
      try {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
      } catch (IOException ignored) {
        // Nothing we can do at this point
      }
    }
  }

}
