package name.sinnema.game.tictactoe;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@RequestMapping(path = "/tictactoe")
public class TicTacToeController {

  @RequestMapping(method = RequestMethod.GET)
  public void getWorld(HttpServletResponse response) {
    response.setContentType(MediaType.APPLICATION_XHTML_XML_VALUE);
    try {
      try (InputStream input = getClass().getResourceAsStream("/tictactoe/index.html")) {
        IOUtils.copy(input, response.getOutputStream());
      }
    } catch (IOException e) {
      try {
        response.sendError(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
      } catch (IOException ignored) {
        // Nothing we can do at this point
      }
    }
  }

}
