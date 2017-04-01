package name.sinnema.game.engine;


/**
 * A potential action that a player can perform in a game.
 */
public interface Move {

  // This is a marker interface. Every game defines its own moves.

  /**
   * Returns a human readable description of the move.
   * @returns A human readable description of the move
   */
  String getDescription();

}
