package name.sinnema.tbg;

import java.util.Collection;


public interface Level {

  World getWorld();

  Collection<Move> getMovesFor(Player player);

  void move(Player player, Move move);

  boolean isComplete();

}
