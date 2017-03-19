package name.sinnema.game.server;

import org.springframework.hateoas.core.Relation;


@Relation(collectionRelation = "players")
public class PlayerDto {

  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

}
