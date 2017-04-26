package name.sinnema.game.tictactoe.ui;

import net.serenitybdd.screenplay.targets.Target;


public class GameOverPage {

  public static final Target WINNER = Target.the("the 'winner' field").locatedBy("#winner");
  public static final Target IS_DRAW = Target.the("the 'game ends in draw' field").locatedBy("#draw");

}
