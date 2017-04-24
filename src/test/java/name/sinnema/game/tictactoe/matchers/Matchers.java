package name.sinnema.game.tictactoe.matchers;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;

import net.serenitybdd.screenplay.Actor;


public class Matchers {

  public static Matcher<String> is(Actor actor) {
    return new IsEqual<>(actor.getName());
  }

}
