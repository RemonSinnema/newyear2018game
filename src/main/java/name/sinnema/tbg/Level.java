package name.sinnema.tbg;

import java.util.List;


/**
 * Section of a game.
 */
public interface Level {

  /**
   * The world of the game as it is now.
   */
  World getWorld();

  /**
   * Return the legal actions a given player can perform in the current state of the world.
   * @param player The player for which to return legal moves.
   * @return The legal moves for the given player
   */
  List<Move> getMovesFor(Player player);

  /**
   * Affect changes into the world by having a player make a move.
   * @param player The player who's turn it is to move
   * @param move The move that the player selected
   */
  void move(Player player, Move move);

  /**
   * Returns whether the level has ended.
   * @return Whether the level has ended
   */
  boolean isComplete();

}
