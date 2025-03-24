package jpacman.model;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;


/**
 * Systematic testing of the game state transitions.
 *
 * The test makes use of the simple map and its containing monsters
 * and players, as defined in the GameTestCase.
 * <p>
 *
 * @author Arie van Deursen; Aug 5, 2003
 * @version $Id: EngineTest.java,v 1.6 2008/02/04 23:00:12 arie Exp $
 */
public class EngineTest extends GameTestCase {

    /**
     * The engine that we'll push along every possible transition.
     */
    private Engine theEngine;


    /**
     * Set up an Engine, making use of the Game object
     * (with a small map containing all sorts of guests)
     * created in the superclass.
     */
    @Before public void setUp() {
        /** Assume the following map.
                0W0
                FP0
                FM0
                0WM
         */
        theEngine = new Engine(theGame);
        assertTrue(theEngine.inStartingState());
    }


    /** The long path that covers most transitions,
     * and ends in the player reaching the winning end state.
     */
    @Test public void testLongAndWinning() {
        /* Starting --(Start)-> Playing */
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertFalse(theEngine.inStartingState());
        assertTrue(theEngine.inPlayingState());

        /* Playing --(Quit)-> Halted */
        assertTrue(theEngine.inPlayingState());
        theEngine.quit();
        assertFalse(theEngine.inPlayingState());
        assertTrue(theEngine.inHaltedState());

        /* Halted --(Start)-> Playing */
        assertTrue(theEngine.inHaltedState());
        theEngine.start();
        assertFalse(theEngine.inHaltedState());
        assertTrue(theEngine.inPlayingState());

        /* Playing --(PlayerMove)-> Playing */
        assertTrue(theEngine.inPlayingState());
        theEngine.movePlayer(-1, 0); // dx = -1  means  LEFT
        assertTrue(theEngine.inPlayingState());

        /* Playing --(MonsterMove)-> Playing */
        assertTrue(theEngine.inPlayingState());
        theEngine.moveMonster(theMonster, 0, 1);
        assertTrue(theEngine.inPlayingState());

        /* Playing --(PlayerMove)-> Player Won */
        assertTrue(theEngine.inPlayingState());
        theEngine.movePlayer(0, 1); // dy = 1  means  DOWN
        assertFalse(theEngine.inPlayingState());
        assertTrue(theEngine.inWonState());
        assertTrue(theEngine.inGameOverState());
    }

    /** A short path that ends in the player dying by moving onto a monster. */
    @Test public void testPlayerDiesToMonster() {
        /* Starting --(Start)-> Playing */
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertFalse(theEngine.inStartingState());
        assertTrue(theEngine.inPlayingState());

        /* Playing --(PlayerMove)-> Player Died */
        assertTrue(theEngine.inPlayingState());
        // Move the player into the monster.
        theEngine.movePlayer(0, 1); // dy = 1  means  DOWN
        assertFalse(theEngine.inPlayingState());
        assertTrue(theEngine.inDiedState());
        assertTrue(theEngine.inGameOverState());
    }

    /** A short path that ends in a monster killing the player by moving
     * onto them.
     */
    @Test public void testMonsterKillsPlayer() {
        /* Starting --(Start)-> Playing */
        assertTrue(theEngine.inStartingState());
        theEngine.start();
        assertFalse(theEngine.inStartingState());
        assertTrue(theEngine.inPlayingState());

        /* Playing --(MonsterMove)-> Player Died */
        assertTrue(theEngine.inPlayingState());
        // Move the monster into the player.
        theEngine.moveMonster(theMonster, 0, -1); // dy = -1  means  UP
        assertFalse(theEngine.inPlayingState());
        assertTrue(theEngine.inDiedState());
        assertTrue(theEngine.inGameOverState());
    }
}
