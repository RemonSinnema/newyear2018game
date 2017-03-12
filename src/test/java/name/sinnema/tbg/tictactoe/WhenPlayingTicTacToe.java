package name.sinnema.tbg.tictactoe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import name.sinnema.tbg.Level;
import name.sinnema.tbg.Move;
import name.sinnema.tbg.Player;
import name.sinnema.tbg.TurnbasedGame;
import name.sinnema.tbg.World;
import name.sinnema.tbg.tictactoe.TicTacToeWorld.Marker;


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
      assertEquals("Move", PlaceMarker.class, move.getClass());
      PlaceMarker placeMarker = (PlaceMarker)move;
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
    assertMarker(Marker.CROSS, 0);

    move = game.getCurrentMoves().get(0);
    assertEquals("Move #2", "Place O at 1", move.toString());
    game.move(move);
    assertMarker(Marker.NOUGHT, 1);

    game.move(game.getCurrentMoves().get(0));
    assertMarker(Marker.CROSS, 2);
  }

  private void assertMarker(Marker expected, int position) {
    assertEquals("Marker at " + position, expected, ((TicTacToeWorld)game.getCurrentWorld()).get(position));
  }

  @Test
  public void shouldEndGameWhenThreeInARow() {
    game.start();

    game.move(game.getCurrentMoves().get(4));
    game.move(game.getCurrentMoves().get(0));
    game.move(game.getCurrentMoves().get(0));
    game.move(game.getCurrentMoves().get(0));
    game.move(game.getCurrentMoves().get(3));

    assertTrue("Game should be over", game.isOver());
  }

}
