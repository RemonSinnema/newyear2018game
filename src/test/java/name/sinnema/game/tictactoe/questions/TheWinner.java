package name.sinnema.game.tictactoe.questions;

import name.sinnema.game.tictactoe.ui.GameOverPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;


public class TheWinner implements Question<String> {

  @Override
  public String answeredBy(Actor actor) {
    return Text.of(GameOverPage.WINNER).viewedBy(actor).asString();
  }

  public static Question<String> ofTheGame() {
    return new TheWinner();
  }

}
