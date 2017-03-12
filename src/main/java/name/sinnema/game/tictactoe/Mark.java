package name.sinnema.game.tictactoe;


public enum Mark {

  NONE(" "), CROSS("X"), NOUGHT("O");

  private final String text;

  Mark(String text) {
    this.text = text;
  }

  @Override
  public String toString() {
    return text;
  }

}