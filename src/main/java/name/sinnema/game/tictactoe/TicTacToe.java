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
    super.add(new TicTacToeLevel());
  }

  @Override
  public int add(Player player) {
    if (getPlayers().size() >= 2) {
      throw new UnsupportedOperationException();
    }
    return super.add(player);
  }

  @Override
  public void add(Level level) {
    throw new UnsupportedOperationException();
  }

}
