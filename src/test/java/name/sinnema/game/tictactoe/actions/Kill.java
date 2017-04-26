package name.sinnema.game.tictactoe.actions;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import org.springframework.web.client.RestTemplate;

import name.sinnema.game.tictactoe.ui.Launch;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Performable;
import net.thucydides.core.annotations.Step;


public class Kill implements Interaction {

  @Override
  @Step("Stop game server")
  public <T extends Actor> void performAs(T actor) {
    RestTemplate client = new RestTemplate();
    client.postForLocation(Launch.BASE_URL + "shutdown", null);
  }

  public static Performable theServer() {
    return instrumented(Kill.class);
  }

}
