package name.sinnema.game.engine;

import java.util.Objects;


/**
 * Someone who actively participates in a game.
 */
public class Player {

  private final String name;

  public Player(String name) {
    this.name = name;
  }

  /**
   * Returns the player's name.
   * @return The player's name
   */
  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Player) {
      Player other = (Player)obj;
      return Objects.equals(name, other.name);
    }
    return false;
  }

  @Override
  public String toString() {
    return name;
  }

}
