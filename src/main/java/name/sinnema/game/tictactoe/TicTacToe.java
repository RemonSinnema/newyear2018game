package name.sinnema.game.tictactoe;

import name.sinnema.game.engine.Level;
import name.sinnema.game.engine.Player;
import name.sinnema.game.engine.SingleMovePerTurnGame;


public class TicTacToe extends SingleMovePerTurnGame {

  public TicTacToe(String player1, String player2) {
    this();
    super.add(new Player(player1));
    super.add(new Player(player2));
  }

  public TicTacToe() {
    super(2, 2);
    super.add(new TicTacToeLevel());
  }

  @Override
  public void add(Level level) {
    throw new UnsupportedOperationException("Cannot add levels to this game");
  }

}
