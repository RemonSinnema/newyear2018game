package name.sinnema.tbg;

import java.util.List;


public interface Level {

  World getWorld();

  List<Move> getMovesFor(Player player);

  void move(Player player, Move move);

  boolean isComplete();

}
