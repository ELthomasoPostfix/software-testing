package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Observable;

import org.junit.Before;
import org.junit.Test;

/**
 * Test that the observer update method
 * is properly called upon changes.
 *
 * @author Arie van Deursen; Aug 5, 2003
 * @version $Id: ObserverTest.java,v 1.5 2008/02/08 12:30:19 arie Exp $
 */
public class ObserverTest extends GameTestCase {

    /**
     * The Engine acts as the subject in the observer pattern.
     */
    private Engine theEngine;

    /**
     * Attach the engine to a dedicated mock-observer counting observations.
     */
    private MyObserver theObserver;

    /**
     * Initialize engine and attach the observer to it,
     * making use of superclass game.
     */
    @Before public void setUp() {
        assertNotNull(theGame);
        theEngine = new Engine(theGame);
        assertTrue(theEngine.inStartingState());
        theObserver = new MyObserver();
        theEngine.addObserver(theObserver);
    }

    /**
     * Simple observer which counts the number of events it sees.
     */
    private class MyObserver implements java.util.Observer {
       /**
         * The update counter.
         */
        private int nrOfUpdates = 0;
        /**
         * Make an observation, and update the counter.
         * @param obs The observable (the engine);
         * @param o remaining info, ignored.
         */
        public void update(Observable obs, Object o) {
            nrOfUpdates++;
        }
    }

    /**
     * See if update events are triggered at all relevant
     * places in the engine.
     */
    @Test public void testUpdates() {
        int expectedUpdates = 0;
        assertEquals(expectedUpdates, theObserver.nrOfUpdates);

        theEngine.start();
        expectedUpdates++;
        assertEquals(expectedUpdates, theObserver.nrOfUpdates);
        assertTrue(theEngine.inPlayingState());

        theEngine.movePlayer(1, 0);
        expectedUpdates++;
        assertTrue(theEngine.inPlayingState());
        assertEquals(expectedUpdates, theObserver.nrOfUpdates);

        theEngine.quit();
        expectedUpdates++;
        assertTrue(theEngine.inHaltedState());
        assertEquals(expectedUpdates, theObserver.nrOfUpdates);

        theEngine.start();
        expectedUpdates++;
        assertTrue(theEngine.inPlayingState());
        assertEquals(expectedUpdates, theObserver.nrOfUpdates);

         // todo: test updates for monster moves as well.

    }
}
