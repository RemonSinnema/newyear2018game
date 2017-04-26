package name.sinnema.game.tictactoe.questions;

import name.sinnema.game.tictactoe.ui.GameOverPage;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Question;
import net.serenitybdd.screenplay.questions.Text;
import net.serenitybdd.screenplay.questions.Visibility;


public class The {

  public static class TheWinner implements Question<String> {

    @Override
    public String answeredBy(Actor actor) {
      return Text.of(GameOverPage.WINNER).viewedBy(actor).asString();
    }

  }


  public static class EndsInDraw implements Question<Boolean> {

    @Override
    public Boolean answeredBy(Actor actor) {
      return Visibility.of(GameOverPage.IS_DRAW).viewedBy(actor).asBoolean();
    }

  }

  public static Question<String> winner() {
    return new TheWinner();
  }

  public static Question<Boolean> gameEndsIn() {
    return new EndsInDraw();
  }

}
