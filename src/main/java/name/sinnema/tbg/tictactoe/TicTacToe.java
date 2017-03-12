package name.sinnema.tbg.tictactoe;

import name.sinnema.tbg.Level;
import name.sinnema.tbg.Move;
import name.sinnema.tbg.Player;
import name.sinnema.tbg.TurnbasedGame;


public class TicTacToe extends TurnbasedGame {

  public TicTacToe(String player1, String player2) {
    super.add(new Player(player1));
    super.add(new Player(player2));
    super.add(new TicTacToeLevel());
  }

  @Override
  public void add(Player player) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void add(Level level) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void endTurn() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void move(Move move) {
    super.move(move);
    super.endTurn();
  }

}
