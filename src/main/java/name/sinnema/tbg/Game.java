package name.sinnema.tbg;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Game {

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
    if (getCurrentLevel().isComplete()) {
      currentLevelIndex++;
    }
    currentPlayerIndex = ++currentPlayerIndex % players.size();
  }

  public boolean isOver() {
    return currentLevelIndex >= levels.size();
  }

  public Level getCurrentLevel() {
    return levels.get(currentLevelIndex);
  }

  public void move(Move move) {
    if (!getCurrentLevel().getMovesFor(getCurrentPlayer()).contains(move)) {
      throw new IllegalArgumentException("Illegal move: " + move);
    }
    getCurrentLevel().move(getCurrentPlayer(), move);
  }

  public World getWorld() {
    return getCurrentLevel().getWorld();
  }

}
