package name.sinnema.game.tictactoe.actions;

import name.sinnema.game.tictactoe.ui.Grid;
import net.serenitybdd.core.steps.Instrumented;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Interaction;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.actions.Click;
import net.thucydides.core.annotations.Step;


public class PlaceAMarkAt implements Interaction {

  private final int row;
  private final int column;

  @Override
  @Step("{0} places a mark at (#row, #column)")
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(Click.on(Grid.cell(row, column)));
  }

  public static Performable cell(int row, int column) {
    return Instrumented.instanceOf(PlaceAMarkAt.class).withProperties(row, column);
  }

  public PlaceAMarkAt(int row, int column) {
    this.row = row;
    this.column = column;
  }

}
