package name.sinnema.game.tictactoe.ui;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;


public class TheSetupPage extends PageObject {

  public static final Target START_BUTTON = Target.the("the 'Start' button").locatedBy("#start");

  public static Target playerField(int index) {
    return Target.the("the 'Player #" + index + "' field").locatedBy("#player" + index);
  }

}
