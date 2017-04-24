package name.sinnema.game.tictactoe.ui;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import name.sinnema.game.tictactoe.TicTacToeApplication;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Performable;
import net.thucydides.core.annotations.Step;


public class Server {

  public static final String URL = "http://localhost:8080/tictactoe";

  public static Performable start() {
    return instrumented(StartServer.class);
  }


  public static class StartServer implements Interaction {

    @Override
    @Step("Start server")
    public <T extends Actor> void performAs(T actor) {
      Thread serverThread = new Thread(() -> TicTacToeApplication.main(new String[] { }));
      serverThread.setDaemon(true);
      serverThread.start();
      waitForServer();
    }

    private void waitForServer() {
      RestTemplate template = new RestTemplate();
      while (!isStarted(template)) {
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          // Ignore
        }
      }
    }

    private boolean isStarted(RestTemplate template) {
      try {
        ResponseEntity<String> response = template.getForEntity(URL, String.class);
        return response.getStatusCode().is2xxSuccessful();
      } catch (RestClientException e) {
        return false;
      }
    }

  }

}
