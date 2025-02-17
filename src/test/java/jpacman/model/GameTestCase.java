package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;

/**
 * This is a JUnit test class, intended for use in
 * cases where a filled Pacman board is necessary.
 * This class creates such a filled board, and offers
 * various instance variables to access parts of the
 * board. Typical clients of this class will inherit
 * the game and its content from this class.
 * <p>
 * @author Arie van Deursen; Aug 23, 2003
 * @version $Id: GameTestCase.java,v 1.6 2008/02/10 12:51:55 arie Exp $
 */
public abstract class GameTestCase {

    /**
     * The game that we're setting up for testing purposes.
     */
    protected Game theGame;

    /**
     * The player of the game.
     */
    protected Player thePlayer;

    /**
     * One of the monsters in the game.
     */
    protected Monster theMonster;

    /**
     * A food element.
     */
    protected Food theFood;

    /**
     * Series of cells containing differen types of guests.
     */
    protected Cell playerCell, wallCell, monsterCell, foodCell, emptyCell;

    /**
     * Simple map that can be used for various testing purposes.
     * It contains every type of guest, as well as several empty cells.
     */
    public static final String[] SIMPLE_MAP =
        new String[]{
        "0W0",
        "FP0",
        "FM0",
        "0WM"
    };


    /**
     * Setup a game that can be used in test cases
     * defined in subclasses of this class.
     */
    @Before
    public void setUpGame() {
        theGame = new Game(SIMPLE_MAP);

        Board theBoard = theGame.getBoard();

        wallCell = theBoard.getCell(1, 0);
        assertTrue(wallCell.getInhabitant() instanceof Wall);
        assertEquals(Guest.WALL_TYPE, wallCell.getInhabitant().guestType());

        monsterCell = theBoard.getCell(1, 2);
        assertTrue(monsterCell.getInhabitant() instanceof Monster);
        assertEquals(Guest.MONSTER_TYPE,
                monsterCell.getInhabitant().guestType());

        foodCell = theBoard.getCell(0, 1);
        assertTrue(foodCell.getInhabitant() instanceof Food);
        assertEquals(Guest.FOOD_TYPE, foodCell.getInhabitant().guestType());

        playerCell = theBoard.getCell(1, 1);
        assertTrue(playerCell.getInhabitant() instanceof Player);
        assertEquals(Guest.PLAYER_TYPE, playerCell.getInhabitant().guestType());

        emptyCell = theBoard.getCell(2, 1);
        assertNull(emptyCell.getInhabitant());

        thePlayer = theGame.getPlayer();
        theMonster = (Monster) monsterCell.getInhabitant();
        theFood = (Food) foodCell.getInhabitant();

        assertTrue(theGame.getMonsters().contains(theMonster));
        assertEquals(2, theGame.getMonsters().size());
    }
}
