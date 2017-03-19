package name.sinnema.game.server;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import name.sinnema.game.engine.TurnbasedGame;


@RestController
@RequestMapping(path = "/", produces = MediaTypes.HAL_JSON_VALUE)
public class GameController {

  @Autowired
  private TurnbasedGame game;

  @RequestMapping(method = RequestMethod.GET)
  public Resource<GameDto> getGame() {
    GameDto gameDto = new GameDto();
    Resource<GameDto> result = new Resource<>(gameDto);
    result.add(
        linkTo(GameController.class).withSelfRel(),
        linkTo(PlayersController.class).withRel(LinkRelations.PLAYERS));
    if (game.canStart()) {
      result.add(new Link(
          MvcUriComponentsBuilder
              .fromMethodCall(on(GameController.class).startGame())
              .build()
              .encode()
              .toUri()
              .toString(),
          LinkRelations.START));
    } else if (game.isStarted()) {
      gameDto.setLevel(game.getCurrentLevel());
      int index = game.getPlayers().indexOf(game.getCurrentPlayer());
      result.add(new Link(
          MvcUriComponentsBuilder
              .fromMethodCall(on(PlayersController.class).getPlayer(index))
              .build()
              .encode()
              .toUri()
              .toString(),
          LinkRelations.CURRENT_PLAYER));
      result.add(new Link(
          MvcUriComponentsBuilder
              .fromMethodCall(on(MovesController.class).getMoves())
              .build()
              .encode()
              .toUri()
              .toString(),
          LinkRelations.MOVES));
    }
    return result;
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResourceSupport startGame() {
    game.start();
    return getGame();
  }

}
