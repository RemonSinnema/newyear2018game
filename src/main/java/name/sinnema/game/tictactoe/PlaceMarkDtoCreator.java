package name.sinnema.game.tictactoe;

import org.springframework.stereotype.Service;

import name.sinnema.game.engine.Move;
import name.sinnema.game.server.MoveDto;
import name.sinnema.game.server.MoveDtoCreator;


@Service
public class PlaceMarkDtoCreator implements MoveDtoCreator {

  @Override
  public boolean supports(Move move) {
    return move instanceof PlaceMark;
  }

  @Override
  public MoveDto createFrom(Move move) {
    PlaceMarkDto result = new PlaceMarkDto();
    PlaceMark placeMark = (PlaceMark)move;
    result.setIndex(placeMark.getIndex());
    result.setMark(placeMark.getMark().toString());
    return result;
  }

}
