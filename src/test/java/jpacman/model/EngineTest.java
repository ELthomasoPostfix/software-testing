package jpacman.model;

import static org.junit.Assert.assertTrue;

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
        theEngine = new Engine(theGame);
        assertTrue(theEngine.inStartingState());
    }

     // Create state model test cases.
     @Test public void addTestCasesHere() {}


}
