package name.sinnema.game.engine;


/**
 * Game where each player is allowed to make exactly one move per turn.
 */
public class SingleMovePerTurnGame extends TurnbasedGame {

  @Override
  public void move(Move move) {
    super.move(move);
    super.endTurn();
  }

  @Override
  public void endTurn() {
    throw new UnsupportedOperationException();
  }

}
