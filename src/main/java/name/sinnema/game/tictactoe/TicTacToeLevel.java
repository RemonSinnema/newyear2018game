package name.sinnema.game.tictactoe;

import java.util.List;
import java.util.stream.Collectors;

import name.sinnema.game.engine.Level;
import name.sinnema.game.engine.Move;
import name.sinnema.game.engine.Player;
import name.sinnema.game.engine.World;


class TicTacToeLevel implements Level {

  private final TicTacToeWorld world = new TicTacToeWorld();
  private Player firstPlayer;

  @Override
  public World getWorld() {
    return world;
  }

  @Override
  public List<Move> getMovesFor(Player player) {
    Mark mark = markFor(player);
    return world.emptyCellIndexes()
        .map(index -> new PlaceMark(index, mark))
        .collect(Collectors.toList());
  }

  private Mark markFor(Player player) {
    if (firstPlayer == null) {
      firstPlayer = player;
    }
    return player == firstPlayer ? Mark.CROSS : Mark.NOUGHT;
  }

  @Override
  public void move(Player player, Move move) {
    PlaceMark placeMark = (PlaceMark)move;
    world.set(placeMark.getIndex(), placeMark.getMark());
  }

  @Override
  public boolean isComplete() {
    return !world.emptyCellIndexes().findAny().isPresent()
        || world.hasRowWhereAllCellsAreMarkedTheSame();
  }

}
