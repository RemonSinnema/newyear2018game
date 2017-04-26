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

import name.sinnema.game.engine.Player;
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
    addStandardLinks(result);
    if (game.canStart()) {
      addStartLink(result);
    } else if (game.isOver()) {
      gameDto.setLevel(game.getCurrentLevel());
      if (game.getWinningPlayer() != null) {
        addPlayerLink(game.getWinningPlayer(), LinkRelations.WINNING_PLAYER, result);
      }
      addWorldLink(result);
    } else if (game.isStarted()) {
      gameDto.setLevel(game.getCurrentLevel());
      addPlayerLink(game.getCurrentPlayer(), LinkRelations.CURRENT_PLAYER, result);
      addWorldLink(result);
      addMovesLink(result);
    }
    return result;
  }

  private void addStandardLinks(Resource<GameDto> gameResource) {
    gameResource.add(
        linkTo(GameController.class).withSelfRel(),
        linkTo(PlayersController.class).withRel(LinkRelations.PLAYERS));
  }

  private void addStartLink(Resource<GameDto> gameResource) {
    gameResource.add(new Link(
        MvcUriComponentsBuilder
            .fromMethodCall(on(GameController.class).startGame())
            .build()
            .encode()
            .toUri()
            .toString(),
        LinkRelations.START));
  }

  private void addMovesLink(Resource<GameDto> gameResource) {
    gameResource.add(new Link(
        MvcUriComponentsBuilder
            .fromMethodCall(on(MovesController.class).getMoves())
            .build()
            .encode()
            .toUri()
            .toString(),
        LinkRelations.MOVES));
  }

  private void addWorldLink(Resource<GameDto> gameResource) {
    gameResource.add(new Link(
        MvcUriComponentsBuilder
            .fromController(WorldController.class)
            .build()
            .encode()
            .toUri()
            .toString(),
        LinkRelations.WORLD));
  }

  private void addPlayerLink(Player player, String linkRelation, Resource<GameDto> gameResource) {
    int index = game.getPlayers().indexOf(player);
    gameResource.add(new Link(
        MvcUriComponentsBuilder
            .fromMethodCall(on(PlayersController.class).getPlayer(index))
            .build()
            .encode()
            .toUri()
            .toString(),
        linkRelation));
  }

  @RequestMapping(method = RequestMethod.POST)
  public ResourceSupport startGame() {
    game.start();
    return getGame();
  }

}
