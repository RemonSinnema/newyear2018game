package name.sinnema.game.tictactoe;

import java.io.OutputStream;

import org.springframework.stereotype.Service;

import name.sinnema.game.engine.Renderer;
import name.sinnema.game.engine.World;


@Service
public class TicTacToeWorldRenderer implements Renderer<World> {

  @Override
  public String getMediaType() {
    // TODO: Implement
    return null;
  }

  @Override
  public void render(World world, OutputStream outputStream) {
    // TODO: Implement
  }

}
