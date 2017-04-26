package name.sinnema.game.tictactoe.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;

import net.serenitybdd.screenplay.Actor;


public class Matchers {

  public static Matcher<String> isNamed(Actor actor) {
    return new IsEqual<>(actor.getName());
  }

  public static Matcher<Boolean> aDraw() {
    return new IsEqual<>(true);
  }

}
