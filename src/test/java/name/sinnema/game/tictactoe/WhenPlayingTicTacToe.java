package name.sinnema.game.tictactoe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import name.sinnema.game.engine.Level;
import name.sinnema.game.engine.Move;
import name.sinnema.game.engine.Player;
import name.sinnema.game.engine.TurnbasedGame;
import name.sinnema.game.engine.World;


public class WhenPlayingTicTacToe {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private final TurnbasedGame game = new TicTacToe("yiri", "armin");

  @Test
  public void shouldHaveExactlyTwoPlayers() {
    List<Player> players = game.getPlayers();
    assertEquals("# Players", 2, players.size());
    assertEquals("Player #1", "armin", players.get(0).getName());
    assertEquals("Player #2", "yiri", players.get(1).getName());

    thrown.expect(UnsupportedOperationException.class);
    game.add(new Player("ray"));
  }

  @Test
  public void shouldHaveExactlyOneLevel() {
    List<Level> levels = game.getLevels();
    assertEquals("# Levels", 1, levels.size());
    assertEquals("Level", TicTacToeLevel.class, levels.get(0).getClass());

    thrown.expect(UnsupportedOperationException.class);
    game.add(mock(Level.class));
  }

  @Test
  public void shouldStartWithEmptyWorld() {
    game.start();

    World world = game.getCurrentWorld();
    assertNotNull("Missing world", world);
    assertEquals("World", TicTacToeWorld.class, world.getClass());
  }

  @Test
  public void shouldAllowPlacingMarkerInEmptyFields() {
    game.start();

    List<Move> moves = game.getCurrentMoves();
    assertEquals("# moves", 9, moves.size());
    Collection<Integer> freeCellIndexes = new HashSet<>();
    moves.forEach(move -> {
      assertEquals("Move", PlaceMark.class, move.getClass());
      PlaceMark placeMarker = (PlaceMark)move;
      assertTrue(freeCellIndexes.add(placeMarker.getIndex()));
    });
    assertEquals("# free cells", 9, freeCellIndexes.size());
    assertEquals("Min free cell", 0, Collections.min(freeCellIndexes).intValue());
    assertEquals("Maz free cell", 8, Collections.max(freeCellIndexes).intValue());

    game.move(moves.get(3));
    assertEquals("# remaining moves", 8, game.getCurrentMoves().size());
  }

  @Test
  public void shouldAutomaticallyTurnAfterMove() {
    game.start();
    Player player = game.getCurrentPlayer();

    game.move(game.getCurrentMoves().get(0));
    assertNotSame("Current player", player, game.getCurrentPlayer());

    thrown.expect(UnsupportedOperationException.class);
    game.endTurn();
  }

  @Test
  public void shouldUseCrossesForFirstPlayerAndNoughtsForSecond() {
    game.start();

    Move move = game.getCurrentMoves().get(0);
    assertEquals("Move #1", "Place X at 0", move.toString());
    game.move(move);
    assertMarker(Mark.CROSS, 0);

    move = game.getCurrentMoves().get(0);
    assertEquals("Move #2", "Place O at 1", move.toString());
    game.move(move);
    assertMarker(Mark.NOUGHT, 1);

    game.move(game.getCurrentMoves().get(0));
    assertMarker(Mark.CROSS, 2);
  }

  private void assertMarker(Mark expected, int position) {
    assertEquals("Marker at " + position, expected, ((TicTacToeWorld)game.getCurrentWorld()).get(position));
  }

  @Test
  public void shouldWinGameWhenPlayerMarksLine() {
    game.start();

    executeMove(0);
    executeMove(0);
    executeMove(2);
    executeMove(2);
    executeMove(4);

    assertTrue("Game should be over", game.isOver());
    assertTrue("There should be no more moves", game.getCurrentMoves().isEmpty());
    assertNull("Current player", game.getCurrentPlayer());
    assertSame("Winning player", game.getPlayers().get(0), game.getWinningPlayer());
  }

  private void executeMove(int moveIndex) {
    game.move(game.getCurrentMoves().get(moveIndex));
  }

  @Test
  public void shouldEndInDrawWhenNoMoreMoves() {
    game.start();

    executeMove(4);
    executeMove(0);
    executeMove(0);
    executeMove(4);
    executeMove(1);
    executeMove(1);
    executeMove(2);
    executeMove(0);
    executeMove(0);

    assertTrue("There should be no more moves", game.getCurrentMoves().isEmpty());
    assertTrue("Game should be over", game.isOver());
    assertNull("Current player", game.getCurrentPlayer());
    assertNull("Winning player", game.getWinningPlayer());
  }

}
