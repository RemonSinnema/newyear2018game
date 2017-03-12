package name.sinnema.tbg.tictactoe;

import java.util.List;
import java.util.stream.Collectors;

import name.sinnema.tbg.Level;
import name.sinnema.tbg.Move;
import name.sinnema.tbg.Player;
import name.sinnema.tbg.World;
import name.sinnema.tbg.tictactoe.TicTacToeWorld.Marker;


class TicTacToeLevel implements Level {

  private final TicTacToeWorld world = new TicTacToeWorld();
  private Player crossPlayer;

  @Override
  public World getWorld() {
    return world;
  }

  @Override
  public List<Move> getMovesFor(Player player) {
    Marker marker = markerFor(player);
    return world.emptyCells()
        .map(index -> new PlaceMarker(index, marker))
        .collect(Collectors.toList());
  }

  @Override
  public void move(Player player, Move move) {
    PlaceMarker placeMarker = (PlaceMarker)move;
    world.set(placeMarker.getIndex(), placeMarker.getMarker());
  }

  private Marker markerFor(Player player) {
    if (crossPlayer == null) {
      crossPlayer = player;
    }
    return player == crossPlayer ? Marker.CROSS : Marker.NOUGHT;
  }

  @Override
  public boolean isComplete() {
    return world.hasCompletelyMarkedLine();
  }

}
