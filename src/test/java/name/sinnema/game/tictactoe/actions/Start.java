package name.sinnema.game.tictactoe.actions;

import name.sinnema.game.tictactoe.ui.TheSetupPage;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.thucydides.core.annotations.Step;


public class Start implements Interaction {

  @Override
  @Step("{0} starts the game")
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(Click.on(TheSetupPage.START_BUTTON));
  }

  public static Performable aNewGame() {
    return Instrumented.instanceOf(Start.class).newInstance();
  }

}
