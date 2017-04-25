package name.sinnema.game.tictactoe.actions;

import name.sinnema.game.tictactoe.ui.TheSetupPage;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Enter;
import net.thucydides.core.annotations.Step;


public class SetPlayer implements Interaction {

  private final String name;
  private final int index;

  @Override
  @Step("{0} sets player #index to '#name'")
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(Enter.theValue(name).into(TheSetupPage.playerField(index)));
  }

  public static AddNamedPlayer named(String name) {
    return new AddNamedPlayer(name);
  }

  public SetPlayer(String name, int index) {
    this.name = name;
    this.index = index;
  }


  public static class AddNamedPlayer {

    private final String name;

    public AddNamedPlayer(String name) {
      this.name = name;
    }

    public Performable asFirstPlayer() {
      return Instrumented.instanceOf(SetPlayer.class).withProperties(name, 1);
    }

    public Performable asSecondPlayer() {
      return Instrumented.instanceOf(SetPlayer.class).withProperties(name, 2);
    }

  }

}
