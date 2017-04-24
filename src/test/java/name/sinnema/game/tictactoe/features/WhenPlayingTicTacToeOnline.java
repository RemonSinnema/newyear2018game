package name.sinnema.game.tictactoe.features;

import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import name.sinnema.game.tictactoe.tasks.StartAGameOfTicTacToe;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.abilities.BrowseTheWeb;
import net.thucydides.core.annotations.Managed;


@RunWith(SerenityRunner.class)
public class WhenPlayingTicTacToeOnline {

  private final Actor patty = Actor.named("Patty");
  private final Actor paul = Actor.named("Paul");
  @Managed(driver = "phantomjs")
  private WebDriver theBrowser;

  @Before
  public void init() {
    paul.can(BrowseTheWeb.with(theBrowser));
    patty.can(BrowseTheWeb.with(theBrowser));
  }

  @Test
  public void should_win_game_by_getting_three_identical_marks_in_a_row() throws Exception {
    givenThat(patty).wasAbleTo(StartAGameOfTicTacToe.against(paul));
  }

}
