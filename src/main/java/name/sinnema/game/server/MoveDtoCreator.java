package name.sinnema.game.server;

import name.sinnema.game.engine.Move;


public interface MoveDtoCreator {

  boolean supports(Move move);

  MoveDto createFrom(Move move);

}
