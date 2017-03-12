package name.sinnema.tbg;


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
  public String toString() {
    return name;
  }

}
