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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import name.sinnema.game.engine.Player;
import name.sinnema.game.engine.TurnbasedGame;


@RestController
@RequestMapping(path = "/players", produces = MediaTypes.HAL_JSON_VALUE)
public class PlayersController {

  @Autowired
  private TurnbasedGame game;

  @RequestMapping(method = RequestMethod.GET)
  public Resources<Resource<PlayerDto>> getPlayers() {
    return new Resources<>(
        game.getPlayers().stream()
            .map(player -> getPlayerResource(player))
            .collect(Collectors.toList()),
        linkTo(PlayersController.class).withSelfRel());
  }

  private Resource<PlayerDto> getPlayerResource(Player player) {
    Resource<PlayerDto> result = new Resource<>(playerToDto(player));
    result.add(new Link(getPlayerUri(player).toString(), Link.REL_SELF));
    return result;
  }

  private PlayerDto playerToDto(Player player) {
    PlayerDto result = new PlayerDto();
    result.setName(player.getName());
    return result;
  }

  private URI getPlayerUri(Player player) {
    return MvcUriComponentsBuilder
        .fromMethodCall(on(PlayersController.class).getPlayer(game.getPlayers().indexOf(player)))
        .build()
        .encode()
        .toUri();
  }

  @RequestMapping(method = RequestMethod.POST, consumes = MediaTypes.HAL_JSON_VALUE)
  public ResponseEntity<Resource<PlayerDto>> addPlayer(@RequestBody PlayerDto dto, UriComponentsBuilder uriBuilder) {
    Player player = dtoToPlayer(dto);
    game.add(player);
    return ResponseEntity.created(getPlayerUri(player))
        .body(getPlayerResource(player));
  }

  private Player dtoToPlayer(PlayerDto dto) {
    return new Player(dto.getName());
  }

  @RequestMapping(path = "/{index}", method = RequestMethod.GET)
  public Resource<PlayerDto> getPlayer(@PathVariable("index") int index) {
    List<Player> players = game.getPlayers();
    if (0 > index || index >= players.size()) {
      throw new ResourceNotFoundException();
    }
    return getPlayerResource(players.get(index));
  }

}
