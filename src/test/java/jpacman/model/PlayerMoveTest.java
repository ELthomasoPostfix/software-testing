package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.Before;


/**
 * Specialize the general MoveTest test suite to one
 * that is tailored to PlayerMoves.
 * Thanks to inheritance, all test cases from MoveTest
 * are also methods in PlayerMoveTest, thus helping us
 * to test conformance with Liskov's Substitution Principle (LSP)
 * of the Move hierarchy.
 * <p>
 * @author Arie van Deursen; August 21, 2003.
 * @version $Id: PlayerMoveTest.java,v 1.8 2008/02/10 19:51:11 arie Exp $
 */
public class PlayerMoveTest extends MoveTest {

    /*
    * Variables we need
    * */
    private Board board;
    private Player player;
    private PlayerMove aPlayerMove;
    private Cell emptyCell;
    private Cell wallCell;
    private Cell foodCell;
    private Cell monsterCell;
    private Cell playerCell;
    private Monster monster;
    private Food food;
    private Wall wall;
    private Cell originalCell;

    @Before
    public void setUp(){
        /* The Board is configured according to the drawing below.
            01234
            .....0
            ..M..1
            .FPE.2
            ..W..3
            .....4

            . = empty cell
            E = empty cell
            W = wall cell
            F = food cell
            M = monster cell
            P = player cell
        */

        board = new Board(5, 5); // A 5x5 board
        monsterCell = board.getCell(2, 1); // Up
        playerCell  = board.getCell(2, 2); // Middle of the board
        wallCell    = board.getCell(2, 3); // Down
        emptyCell   = board.getCell(3, 2); // Right
        foodCell    = board.getCell(1, 2); // Left

        player = new Player();
        monster = new Monster();
        food = new Food();
        wall = new Wall();

        // Place objects in the board
        wall.occupy(wallCell);
        food.occupy(foodCell);
        monster.occupy(monsterCell);
        player.occupy(playerCell);
    }

    /**
     * Simple test of a few getters.
     */
    @Test
    public void testSimpleGetters() {
        PlayerMove playerMove = new PlayerMove(player, foodCell);
        assertEquals(player, playerMove.getPlayer());
        assertTrue(playerMove.movePossible());
        assertFalse(playerMove.playerDies());
        assertEquals(1, playerMove.getFoodEaten());
        assertTrue(playerMove.invariant());
    }


    /**
     * Create a move object that will be tested.
     *  @see jpacman.model.MoveTest#createMove(jpacman.model.Cell)
     *  @param target The cell to be occupied by the move.
     *  @return The move to be tested.
     */
    @Override
    protected PlayerMove createMove(Cell target) {
        aPlayerMove = new PlayerMove(thePlayer, target);
        return aPlayerMove;
    }

    /**
     * Test to see if moving to an empty cell is allowed
     */
    @Test
    public void testPlayerMoveToEmptyCell() {
        aPlayerMove = new PlayerMove(player, emptyCell);
        assertTrue(aPlayerMove.movePossible());
    }

    /**
     * Test to see if moving into a wall is not allowed
     */
    @Test
    public void testPlayerMoveToWall(){
        aPlayerMove = new PlayerMove(player, wallCell);
        assertFalse(aPlayerMove.movePossible());
    }

    /**
     * Test to see if moving into a food token is allowed and that the food gets eaten properly
     */
    @Test
    public void testPlayerMoveToFood(){
        aPlayerMove = new PlayerMove(player, foodCell);
        assertEquals(0, player.getPointsEaten());
        assertTrue(aPlayerMove.movePossible());
        // apply the move
        aPlayerMove.apply();
        // check that the food is eaten and that we got a point
        assertEquals(1, player.getPointsEaten());
    }

    @Test
    public void testPlayerMoveToMonster(){
        aPlayerMove = new PlayerMove(player, monsterCell);
        assertTrue(aPlayerMove.playerDies());
        assertFalse(aPlayerMove.movePossible());
    }

    @Test
    public void testPlayerMoveToAnotherPlayer(){
        aPlayerMove = new PlayerMove(player, playerCell);
        assertFalse(aPlayerMove.movePossible());
    }

    @Test
    public void testPlayerMoveOutOfBounds(){
        // A NULL Cell represents an out-of-bounds Cell w.r.t. the Board.
        aPlayerMove = new PlayerMove(player, null);
        assertFalse(aPlayerMove.movePossible());
    }

    @Test
    public void testUndoSinglePlayerMove(){
        originalCell = player.getLocation();
        aPlayerMove = new PlayerMove(player, emptyCell);
        // aPlayerMove.precomputeEffects();
        aPlayerMove.apply();
        assertEquals(emptyCell, player.getLocation());
        assertTrue(aPlayerMove.moveDone());
        // Now we undo the move
        aPlayerMove.undo();

        // Verify that the player is back at the original cell
        assertEquals(originalCell, player.getLocation());
        assertFalse(emptyCell.isOccupied());
    }

    @Test
    public void testUndoEatingFruitPlayer(){
        // store the original position
        originalCell = player.getLocation();
        // make the move
        aPlayerMove = new PlayerMove(player, foodCell);
        aPlayerMove.apply();
        // check that the position changed and that the score went up
        assertEquals(1, player.getPointsEaten());
        assertEquals(foodCell, player.getLocation());
        // now undo the move
        aPlayerMove.undo();
        // Verify that the player is back at the original cell, we still have food and the score went down
        assertEquals(0, player.getPointsEaten());
        assertEquals(originalCell, player.getLocation());
        assertTrue(foodCell.isOccupied());
        assertEquals(foodCell.getInhabitant().guestType(), 'F');
    }


}
