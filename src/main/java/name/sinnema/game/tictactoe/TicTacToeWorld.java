package name.sinnema.game.tictactoe;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import name.sinnema.game.engine.World;


public class TicTacToeWorld implements World {

  private final List<Mark> cells = new ArrayList<>();
  private final Collection<Collection<Integer>> rows = new ArrayList<>();

  public TicTacToeWorld() {
    this(3);
  }

  public TicTacToeWorld(int size) {
    initCells(size);
    initRows(size);
  }

  private void initCells(int size) {
    Stream.generate(() -> Mark.NONE)
        .limit(size * size)
        .forEach(m -> cells.add(m));
  }

  private void initRows(int size) {
    addRows(size);
    addColumns(size);
    addTopLeftDiagonal(size);
    addTopRightDiagonal(size);
  }

  private void addRows(int size) {
    for (int rowIndex = 0; rowIndex < size; rowIndex++) {
      Collection<Integer> row = new ArrayList<>();
      for (int columnIndex = 0; columnIndex < size; columnIndex++) {
        row.add(rowIndex * size + columnIndex);
      }
      rows.add(row);
    }
  }

  private void addColumns(int size) {
    for (int columnIndex = 0; columnIndex < size; columnIndex++) {
      Collection<Integer> row = new ArrayList<>();
      for (int rowIndex = 0; rowIndex < size; rowIndex++) {
        row.add(columnIndex + size * rowIndex);
      }
      rows.add(row);
    }
  }

  private void addTopLeftDiagonal(int size) {
    Collection<Integer> row = new ArrayList<>();
    int index = 0;
    for (int rowIndex = 0; rowIndex < size; rowIndex++) {
      row.add(index);
      index += size + 1;
    }
    rows.add(row);
  }

  private void addTopRightDiagonal(int size) {
    Collection<Integer> row = new ArrayList<>();
    int index = size - 1;
    for (int rowIndex = 0; rowIndex < size; rowIndex++) {
      row.add(index);
      index += size - 1;
    }
    rows.add(row);
  }

  public Mark get(int index) {
    return cells.get(index);
  }

  public void set(int index, Mark marker) {
    cells.set(index, marker);
  }

  public Stream<Integer> emptyCellIndexes() {
    Collection<Integer> result = new ArrayList<>();
    for (int i = 0; i < cells.size(); i++) {
      if (cells.get(i) == Mark.NONE) {
        result.add(i);
      }
    }
    return result.stream();
  }

  public boolean hasRowWhereAllCellsAreMarkedTheSame() {
    return rows.stream().anyMatch(row -> areRowCellsMarkedTheSame(row));
  }

  private boolean areRowCellsMarkedTheSame(Collection<Integer> row) {
    Iterator<Integer> indexes = row.iterator();
    Mark first = get(indexes.next());
    if (first == Mark.NONE) {
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
