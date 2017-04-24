package name.sinnema.game.engine;

import java.io.OutputStream;


public interface Renderer<T> {

  String getMediaType();

  void render(T world, OutputStream outputStream);

}
