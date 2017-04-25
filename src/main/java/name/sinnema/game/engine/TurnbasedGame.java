package name.sinnema.game.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.atteo.evo.inflector.English;


/**
 * A game where players take turns making one or move moves.
 */
public class TurnbasedGame {

  private final List<Player> players = new ArrayList<>();
  private final List<Level> levels = new ArrayList<>();
  private final int minPlayers;
  private final int maxPlayers;
  private int currentPlayerIndex;
  private int currentLevelIndex = -1;
  private Player winningPlayer;

  public TurnbasedGame() {
    this(1, Integer.MAX_VALUE);
  }

  public TurnbasedGame(int minPlayers, int maxPlayers) {
    this.minPlayers = minPlayers;
    this.maxPlayers = maxPlayers;
  }

  /**
   * Add a player.
   * @param player The player to add
   * @return
   */
  public int add(Player player) {
    if (getPlayers().size() >= maxPlayers) {
      throw new UnsupportedOperationException(String.format("Cannot play this game with more than %d %s",
          maxPlayers, English.plural("player", maxPlayers)));
    }
    players.add(player);
    players.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
    return players.indexOf(player);
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
    if (!canStart()) {
      throw new IllegalStateException("Cannot start game");
    }
    currentPlayerIndex = 0;
    currentLevelIndex = 0;
  }

  /**
   * Returns whether the game is set up and ready to start.
   * @return Whether the game is set up and ready to start
   */
  public boolean canStart() {
    if (levels.isEmpty()) {
      return false;
    }
    if (players.size() < minPlayers) {
      return false;
    }
    return !isStarted();
  }

  /**
   * Returns whether the game has started.
   * @return Whether the game has started
   */
  public boolean isStarted() {
    return currentLevelIndex >= 0;
  }

  /**
   * Returns the player who's turn it is to make a move.
   * @return The player who's turn it is to make a move
   */
  public Player getCurrentPlayer() {
    return isOver() ? null : players.get(currentPlayerIndex);
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
      winningPlayer = getPlayers().get(currentPlayerIndex);
    } else if (getCurrentMoves().isEmpty()) {
      currentLevelIndex++;
    } else {
      currentPlayerIndex = ++currentPlayerIndex % players.size();
    }
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
    return isOver() ? currentLevelIndex : currentLevelIndex + 1; // Report 1-based level for human consumption
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
    return winningPlayer;
  }

}
