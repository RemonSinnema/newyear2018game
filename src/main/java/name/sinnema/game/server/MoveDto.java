package name.sinnema.game.server;

import org.springframework.hateoas.core.Relation;


@Relation(collectionRelation = "moves")
public class MoveDto {

  private String description;

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

}
