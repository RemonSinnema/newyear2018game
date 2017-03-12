package name.sinnema.tbg;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class TurnbasedGame {

  private final Random random = new SecureRandom();
  private final List<Player> players = new ArrayList<>();
  private final List<Level> levels = new ArrayList<>();
  private int currentPlayerIndex;
  private int currentLevelIndex;

  public void add(Player player) {
    players.add(player);
    players.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));
  }

  public List<Player> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  public void add(Level level) {
    levels .add(level);
  }

  public List<Level> getLevels() {
    return Collections.unmodifiableList(levels);
  }

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

  public Player getCurrentPlayer() {
    return players.get(currentPlayerIndex);
  }

  public void nextTurn() {
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

  public boolean isOver() {
    return currentLevelIndex >= levels.size();
  }

  public int getCurrentLevel() {
    return currentLevelIndex + 1; // Report 1-based level for human consumption
  }

  public void move(Move move) {
    if (!currentLevel().getMovesFor(getCurrentPlayer()).contains(move)) {
      throw new IllegalArgumentException("Illegal move: " + move);
    }
    currentLevel().move(getCurrentPlayer(), move);
  }

  public World getCurrentWorld() {
    return currentLevel().getWorld();
  }

  public List<Move> getCurrentMoves() {
    return currentLevel().getMovesFor(getCurrentPlayer());
  }

}
