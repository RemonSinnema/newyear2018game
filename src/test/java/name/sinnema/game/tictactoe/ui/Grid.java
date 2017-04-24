package name.sinnema.game.tictactoe.ui;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.screenplay.targets.Target;


public class Grid extends PageObject {

  public static Target cell(int row, int column) {
    return Target.the("cell " + row + "," + column).locatedBy("#cell_" + row + "_" + column);
  }

}
