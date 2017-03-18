package name.sinnema.game.engine;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/**
 * A game where players take turns making one or move moves.
 */
public class TurnbasedGame {

  private final Random random = new SecureRandom();
  private final List<Player> players = new ArrayList<>();
  private final List<Level> levels = new ArrayList<>();
  private int currentPlayerIndex;
  private int currentLevelIndex;

  /**
   * Add a player.
   * @param player The player to add
   */
  public void add(Player player) {
    players.add(player);
    players.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
  }

  /**
   * Returns the list of players.
   * @return The list of players
   */
  public List<Player> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  /**
   * Add a level.
   * @param level The level to add
   */
  public void add(Level level) {
    levels .add(level);
  }

  /**
   * Returns the list of levels.
   * @return The list of levels
   */
  public List<Level> getLevels() {
    return Collections.unmodifiableList(levels);
  }

  /**
   * Begin playing the game. A game requires at least one player and one level.
   */
  public void start() {
    if (players.isEmpty()) {
      throw new IllegalStateException("Missing player(s)");
    }
    if (levels.isEmpty()) {
      throw new IllegalStateException("Missing level(s)");
    }
    currentPlayerIndex = random.nextInt(players.size());
    currentLevelIndex = 0;
  }

  /**
   * Returns the player who's turn it is to make a move.
   * @return The player who's turn it is to make a move
   */
  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  /**
   * End the current player's turn and let the next player make moves.
   */
  public void endTurn() {
    if (isOver()) {
      throw new UnsupportedOperationException();
    }
    if (currentLevel().isComplete()) {
      currentLevelIndex++;
    }
    currentPlayerIndex = ++currentPlayerIndex % players.size();
  }

  private Level currentLevel() {
    return levels.get(currentLevelIndex);
  }

  /**
   * Returns whether the game is over.
   * @return Whether the game is over
   */
  public boolean isOver() {
    return currentLevelIndex >= levels.size();
  }

  /**
   * Returns the 1-based index of the current level.
   * @return The 1-based index of the current level
   */
  public int getCurrentLevel() {
    return currentLevelIndex + 1; // Report 1-based level for human consumption
  }

  /**
   * Let the current player make a move.
   * @param move The move the current player wants to make
   */
  public void move(Move move) {
    if (!currentLevel().getMovesFor(getCurrentPlayer()).contains(move)) {
      throw new IllegalArgumentException("Illegal move: " + move);
    }
    currentLevel().move(getCurrentPlayer(), move);
  }

  /**
   * Returns the current state of the world
   * @return The current state of the world
   */
  public World getCurrentWorld() {
    return currentLevel().getWorld();
  }

  /**
   * Returns the legal moves for the current player.
   * @return The legal moves for the current player
   */
  public List<Move> getCurrentMoves() {
    return isOver() ? Collections.emptyList() : currentLevel().getMovesFor(getCurrentPlayer());
  }

  public Player getWinningPlayer() {
    return isOver() ? getCurrentPlayer() : null;
  }

}
