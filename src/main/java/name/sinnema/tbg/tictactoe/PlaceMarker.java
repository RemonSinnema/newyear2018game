package name.sinnema.tbg.tictactoe;

import java.util.Objects;

import name.sinnema.tbg.Move;
import name.sinnema.tbg.tictactoe.TicTacToeWorld.Marker;


public class PlaceMarker implements Move {

  private final Integer index;
  private final Marker marker;

  public PlaceMarker(Integer index, Marker marker) {
    this.index = index;
    this.marker = marker;
  }

  public Integer getIndex() {
    return index;
  }

  public Marker getMarker() {
    return marker;
  }

  @Override
  public int hashCode() {
    return Objects.hash(index, marker);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PlaceMarker) {
      PlaceMarker other = (PlaceMarker)obj;
      return other.index == index && other.marker == marker;
    }
    return false;
  }

  @Override
  public String toString() {
    return String.format("Place %s at %d", marker, index);
  }

}
