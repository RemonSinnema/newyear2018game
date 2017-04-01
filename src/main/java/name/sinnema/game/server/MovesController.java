package name.sinnema.game.server;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import name.sinnema.game.engine.Move;
import name.sinnema.game.engine.TurnbasedGame;


@RestController
@RequestMapping(path = "/moves", produces = MediaTypes.HAL_JSON_VALUE)
public class MovesController {

  @Autowired
  private TurnbasedGame game;

  @RequestMapping(method = RequestMethod.GET)
  public Resources<Resource<MoveDto>> getMoves() {
    return new Resources<>(
        game.getCurrentMoves().stream()
            .map(move -> getMoveResource(move))
            .collect(Collectors.toList()),
        linkTo(MovesController.class).withSelfRel());
  }

  private Resource<MoveDto> getMoveResource(Move move) {
    Resource<MoveDto> result = new Resource<>(moveToDto(move));
    result.add(new Link(getMoveUri(move).toString(), Link.REL_SELF));
    return result;
  }

  private MoveDto moveToDto(Move move) {
    MoveDto result = new MoveDto();
    result.setDescription(move.getDescription());
    return result;
  }

  private URI getMoveUri(Move move) {
    return MvcUriComponentsBuilder
        .fromMethodCall(on(MovesController.class).makeMove(game.getCurrentMoves().indexOf(move)))
        .build()
        .encode()
        .toUri();
  }

  @RequestMapping(path = "/{index}", method = RequestMethod.POST)
  public ResponseEntity<Object> makeMove(@PathVariable("index") int index) {
    List<Move> moves = game.getCurrentMoves();
    if (0 > index || index >= moves.size()) {
      throw new ResourceNotFoundException();
    }
    game.move(moves.get(index));
    HttpHeaders headers = new HttpHeaders();
    URI gameUri = MvcUriComponentsBuilder
        .fromMethodCall(on(GameController.class).getGame())
        .build()
        .encode()
        .toUri();
    headers.setLocation(gameUri);
    return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER);
  }

}
