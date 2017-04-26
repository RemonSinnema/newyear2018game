package name.sinnema.game.tictactoe.tasks;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import name.sinnema.game.tictactoe.actions.Kill;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.thucydides.core.annotations.Step;


public class Stop implements Task {

  @Override
  @Step("{0} stops the game")
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(Kill.theServer());
  }

  public static Performable theGame() {
    return instrumented(Stop.class);
  }

}
