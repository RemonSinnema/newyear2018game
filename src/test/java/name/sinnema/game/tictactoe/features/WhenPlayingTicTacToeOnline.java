package name.sinnema.game.tictactoe.features;

import static name.sinnema.game.tictactoe.matchers.Matchers.is;
import static net.serenitybdd.screenplay.GivenWhenThen.and;
import static net.serenitybdd.screenplay.GivenWhenThen.givenThat;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static net.serenitybdd.screenplay.GivenWhenThen.then;
import static net.serenitybdd.screenplay.GivenWhenThen.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import name.sinnema.game.tictactoe.actions.PlaceAMarkAt;
import name.sinnema.game.tictactoe.questions.TheWinner;
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

    when(patty).attemptsTo(PlaceAMarkAt.cell(1, 1));
    and(paul).attemptsTo(PlaceAMarkAt.cell(1, 2));
    and(patty).attemptsTo(PlaceAMarkAt.cell(2, 2));
    and(paul).attemptsTo(PlaceAMarkAt.cell(2, 3));
    and(patty).attemptsTo(PlaceAMarkAt.cell(3, 3));

    then(paul).should(seeThat(TheWinner.ofTheGame(), is(patty)));
  }

}
