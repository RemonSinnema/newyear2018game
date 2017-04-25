package name.sinnema.game.server;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import name.sinnema.game.engine.Move;


@Service
public class MoveDtoFactory {

  @Autowired(required = false)
  private final Collection<MoveDtoCreator> creators = Collections.emptyList();

  public MoveDto newInstanceFor(Move move) {
    return creators.stream()
        .filter(creator -> creator.supports(move))
        .findAny()
        .map(creator -> creator.createFrom(move))
        .orElse(new MoveDto());

  }

}
