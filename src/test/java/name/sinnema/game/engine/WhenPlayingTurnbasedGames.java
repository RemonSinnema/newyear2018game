package name.sinnema.game.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import name.sinnema.game.engine.Level;
import name.sinnema.game.engine.Move;
import name.sinnema.game.engine.Player;
import name.sinnema.game.engine.TurnbasedGame;
import name.sinnema.game.engine.World;


public class WhenPlayingTurnbasedGames {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private final TurnbasedGame game = new TurnbasedGame();

  @Test
  public void shouldAllowMultiplePlayers() {
    Player player1 = new Player("yiri");
    Player player2 = new Player("armin");
    game.add(player1);
    game.add(player2);

    List<Player> players = game.getPlayers();

    assertEquals("# Players", 2, players.size());
    assertEquals("Player #1", "armin", players.get(0).getName());
    assertEquals("Player #2", "yiri", players.get(1).getName());
  }

  @Test
  public void shouldRequireAtLeastOnePlayer() {
    game.add(mock(Level.class));

    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing player(s)");
    game.start();
  }

  @Test
  public void shouldRequireAtLeastOneLevel() {
    game.add(new Player("laurie"));

    thrown.expect(IllegalStateException.class);
    thrown.expectMessage("Missing level(s)");
    game.start();
  }

  @Test
  public void shouldAllowMultipleLevels() {
    Level level1 = mock(Level.class);
    Level level2 = mock(Level.class);
    game.add(level1);
    game.add(level2);

    List<Level> levels = game.getLevels();

    assertEquals("# Levels", 2, levels.size());
    assertSame("Level #1", level1, levels.get(0));
    assertSame("Level #2", level2, levels.get(1));
  }

  @Test
  public void shouldLetEachPlayerPlayInTurn() {
    game.add(mock(Level.class));
    Player player1 = new Player("yiri");
    Player player2 = new Player("armin");
    game.add(player1);
    game.add(player2);

    game.start();

    Player currentPlayer = game.getCurrentPlayer();
    assertTrue("Player #1", player1 == currentPlayer || player2 == currentPlayer);
    currentPlayer = assertNextPlayer("Player #2", currentPlayer, player1, player2);
    assertNextPlayer("Player #3", currentPlayer, player1, player2);
  }

  private Player assertNextPlayer(String message, Player currentPlayer, Player player1, Player player2) {
    game.endTurn();
    Player expected = player1 == currentPlayer ? player2 : player1;
    Player actual = game.getCurrentPlayer();
    assertSame(message, expected, actual);

    return actual;
  }

  @Test
  public void shouldLetPlayerMakeLegalMove() {
    Level level = mock(Level.class);
    game.add(level);
    Player player = new Player("yiri");
    game.add(player);
    Move move1 = mock(Move.class);
    Move move2 = mock(Move.class);
    when(level.getMovesFor(player)).thenReturn(Arrays.asList(move1, move2));
    game.start();

    game.move(move2);

    verify(level).move(player, move2);
  }

  @Test
  public void shouldNotLetPlayerMakeIllegalMove() {
    Level level = mock(Level.class);
    game.add(level);
    Player player = new Player("armin");
    game.add(player);
    Move move1 = mock(Move.class);
    Move move2 = mock(Move.class);
    when(level.getMovesFor(player)).thenReturn(Arrays.asList(move1));
    game.start();

    thrown.expect(IllegalArgumentException.class);
    game.move(move2);
  }

  @Test
  public void shouldUpdateWorldWhenPlayersMove() {
    Level level = mock(Level.class);
    World world1 = mock(World.class);
    World world2 = mock(World.class);
    final AtomicReference<World> currentWorld = new AtomicReference<>(world1);
    when(level.getWorld()).thenAnswer(invocation -> currentWorld.get());
    Move move = mock(Move.class);
    when(level.getMovesFor(any(Player.class))).thenReturn(Arrays.asList(move));
    doAnswer(invocation -> {
      currentWorld.set(world2);
      return null;
    }).when(level).move(any(Player.class), eq(move));
    game.add(level);
    game.add(new Player("laurie"));

    game.start();
    assertSame("Initial world", world1, game.getCurrentWorld());

    game.move(move);
    assertSame("Updated world", world2, game.getCurrentWorld());
  }

  @Test
  public void shouldMoveToNextLevelWhenLevelIsComplete() {
    Level level1 = mock(Level.class);
    Level level2 = mock(Level.class);
    game.add(level1);
    game.add(level2);
    game.add(new Player("laurie"));

    game.start();
    assertSame("Level #1", 1, game.getCurrentLevel());

    when(level1.isComplete()).thenReturn(true);
    game.endTurn();
    assertSame("Level #2", 2, game.getCurrentLevel());
  }

  @Test
  public void shouldEndGameWhenLastLevelIsComplete() {
    Level level = mock(Level.class);
    game.add(level);
    game.add(new Player("ray"));
    game.start();

    assertFalse("At start", game.isOver());

    when(level.isComplete()).thenReturn(true);
    game.endTurn();
    assertTrue("After completing last level", game.isOver());

    thrown.expect(UnsupportedOperationException.class);
    game.endTurn();
  }

}
