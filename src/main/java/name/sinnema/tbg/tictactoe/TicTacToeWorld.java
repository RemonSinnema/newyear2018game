package name.sinnema.tbg.tictactoe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import name.sinnema.tbg.World;


public class TicTacToeWorld implements World {

  public enum Marker {

    EMPTY(" "), CROSS("X"), NOUGHT("O");

    private final String text;

    Marker(String text) {
      this.text = text;
    }

    @Override
    public String toString() {
      return text;
    }

  }


  private final List<Marker> markers = new ArrayList<>();
  private final Collection<Collection<Integer>> lines = new ArrayList<>();

  public TicTacToeWorld() {
    this(3);
  }

  public TicTacToeWorld(int size) {
    initMarkers(size);
    initLines(size);
  }

  private void initMarkers(int size) {
    Stream.generate(() -> Marker.EMPTY)
        .limit(size * size)
        .forEach(m -> markers.add(m));
  }

  private void initLines(int size) {
    addRows(size);
    addColumns(size);
    addTopLeftDiagonal(size);
    addTopRightDiagonal(size);
  }

  private void addRows(int size) {
    for (int row = 0; row < size; row++) {
      Collection<Integer> line = new ArrayList<>();
      for (int i = 0; i < size; i++) {
        line.add(row * size + i);
      }
      lines.add(line);
    }
  }

  private void addColumns(int size) {
    for (int column = 0; column < size; column++) {
      Collection<Integer> line = new ArrayList<>();
      for (int i = 0; i < size; i++) {
        line.add(column + size * i);
      }
      lines.add(line);
    }
  }

  private void addTopLeftDiagonal(int size) {
    Collection<Integer> line = new ArrayList<>();
    int index = 0;
    for (int i = 0; i < size; i++) {
      line.add(index);
      index += size + 1;
    }
    lines.add(line);
  }

  private void addTopRightDiagonal(int size) {
    Collection<Integer> line = new ArrayList<>();
    int index = size - 1;
    for (int i = 0; i < size; i++) {
      line.add(index);
      index += size - 1;
    }
    lines.add(line);
  }

  public Marker get(int index) {
    return markers.get(index);
  }

  public void set(int index, Marker marker) {
    markers.set(index, marker);
  }

  public Stream<Integer> emptyCells() {
    Collection<Integer> result = new ArrayList<>();
    for (int i = 0; i < markers.size(); i++) {
      if (markers.get(i) == Marker.EMPTY) {
        result.add(i);
      }
    }
    return result.stream();
  }

  public boolean hasCompletelyMarkedLine() {
    return lines.stream()
        .anyMatch(line -> isLineMarked(line));
  }

  private boolean isLineMarked(Collection<Integer> line) {
    Iterator<Integer> indexes = line.iterator();
    Marker first = get(indexes.next());
    if (first == Marker.EMPTY) {
      return false;
    }
    while (indexes.hasNext()) {
      if (first != get(indexes.next())) {
        return false;
      }
    }
    return true;
  }

}
