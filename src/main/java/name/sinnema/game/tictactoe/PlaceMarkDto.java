package name.sinnema.game.tictactoe;

import name.sinnema.game.server.MoveDto;


public class PlaceMarkDto extends MoveDto {

  private int index;
  private String mark;

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    this.index = index;
  }

  public String getMark() {
    return mark;
  }

  public void setMark(String mark) {
    this.mark = mark;
  }

}
