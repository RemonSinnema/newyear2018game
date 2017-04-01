package name.sinnema.game.engine;

import java.io.OutputStream;


public interface WorldRenderer {

  String getMediaType();

  void render(World world, OutputStream outputStream);

}
