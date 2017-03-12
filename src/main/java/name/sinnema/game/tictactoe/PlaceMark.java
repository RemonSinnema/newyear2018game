package name.sinnema.game.tictactoe;

import java.util.Objects;

import name.sinnema.game.engine.Move;


public class PlaceMark implements Move {

  private final int index;
  private final Mark mark;

  public PlaceMark(int index, Mark mark) {
    this.index = index;
    this.mark = mark;
  }

  public int getIndex() {
    return index;
  }

  public Mark getMark() {
    return mark;
  }

  @Override
  public int hashCode() {
    return Objects.hash(index, mark);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PlaceMark) {
      PlaceMark other = (PlaceMark)obj;
      return other.index == index && other.mark == mark;
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("Place %s at %d", mark, index);
  }

}
