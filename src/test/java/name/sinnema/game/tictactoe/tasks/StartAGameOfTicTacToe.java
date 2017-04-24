package name.sinnema.game.tictactoe.tasks;

import static net.serenitybdd.screenplay.Tasks.instrumented;

import name.sinnema.game.tictactoe.actions.SetPlayer;
import name.sinnema.game.tictactoe.actions.Start;
import name.sinnema.game.tictactoe.ui.Server;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Performable;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.actions.Open;
import net.thucydides.core.annotations.Step;


public class StartAGameOfTicTacToe implements Task {

  private final String opponent;

  @Override
  @Step("{0} starts a game of Tic Tac Toe against #opponent")
  public <T extends Actor> void performAs(T actor) {
    actor.attemptsTo(
      Server.start(),
      Open.url(Server.URL),
      SetPlayer.named(actor.getName()).asFirstPlayer(),
      SetPlayer.named(opponent).asSecondPlayer(),
      Start.aNewGame()
    );
  }

  public static Performable against(Actor opponent) {
    return instrumented(StartAGameOfTicTacToe.class, opponent.getName());
  }

  public StartAGameOfTicTacToe(String opponent) {
    this.opponent = opponent;
  }

}
