package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.Before;

/**
 * Specialize the general MoveTest test suite to one
 * that is tailored to MonsterMoves.
 * Thanks to inheritance, all test cases from MoveTest
 * are also methods in MonsterMoveTest, thus helping us
 * to test conformance with Liskov's Substitution Principle (LSP)
 * of the Move hierarchy.
 * <p>
 * @author Thomas Gueutal, Niels Van der Planken; Ma 15, 2025.
 */
public class MonsterMoveTest extends MoveTest {

    /*
    * Variables we need
    * */
    private Board board;
    private Monster monster;
    private MonsterMove aMonsterMove;
    private Cell emptyCell;
    private Cell wallCell;
    private Cell foodCell;
    private Cell monsterCell;
    private Cell playerCell;
    private Player player;
    private Food food;
    private Wall wall;

    @Before
    public void setUp(){
        /* The Board is configured according to the drawing below.
            01234
            .....0
            ..P..1
            .FME.2
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
        playerCell  = board.getCell(2, 1); // Up
        monsterCell = board.getCell(2, 2); // Middle of the board
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
        MonsterMove monsterMove = new MonsterMove(monster, foodCell);
        assertEquals(monster, monsterMove.getMonster());
        assertFalse(monsterMove.movePossible());
        assertFalse(monsterMove.playerDies());
        assertTrue(monsterMove.invariant());
    }


    /**
     * Create a move object that will be tested.
     *  @see jpacman.model.MoveTest#createMove(jpacman.model.Cell)
     *  @param target The cell to be occupied by the move.
     *  @return The move to be tested.
     */
    @Override
    protected MonsterMove createMove(Cell target) {
        aMonsterMove = new MonsterMove(theMonster, target);
        return aMonsterMove;
    }

    /**
     * Test to see if moving to an empty cell is allowed
     */
    @Test
    public void testMonsterMoveToEmptyCell() {
        aMonsterMove = new MonsterMove(monster, emptyCell);
        assertTrue(aMonsterMove.movePossible());
    }

    /**
     * Test to see if moving into a wall is not allowed
     */
    @Test
    public void testMonsterMoveToWall(){
        aMonsterMove = new MonsterMove(monster, wallCell);
        assertFalse(aMonsterMove.movePossible());
    }

    /**
     * Test to see if moving into a food token is not allowed.
     */
    @Test
    public void testMonsterMoveToFood(){
        aMonsterMove = new MonsterMove(monster, foodCell);
        assertFalse(aMonsterMove.movePossible());
    }

    @Test
    public void testMonsterMoveToAnotherMonster(){
        aMonsterMove = new MonsterMove(monster, monsterCell);
        assertFalse(aMonsterMove.movePossible());
    }

    @Test
    public void testMonsterMoveToPlayer(){
        aMonsterMove = new MonsterMove(monster, playerCell);
        assertTrue(aMonsterMove.playerDies());
        assertFalse(aMonsterMove.movePossible());
    }

    @Test
    public void testMonsterMoveOutOfBounds(){
        // A NULL Cell represents an out-of-bounds Cell w.r.t. the Board.
        aMonsterMove = new MonsterMove(monster, null);
        assertFalse(aMonsterMove.movePossible());
    }


}
