package jpacman;

import junit.framework.JUnit4TestAdapter;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import jpacman.controller.ImageFactoryTest;

import jpacman.model.BoardTest;
import jpacman.model.CellTest;
import jpacman.model.EngineTest;
import jpacman.model.GameTest;
import jpacman.model.ObserverTest;
import jpacman.model.PlayerMoveTest;



/**
 * Test suite containing all Pacman junit test cases.
 *
 * This class is provided so that, e.g., ant's junit
 * task that is still based on junit 3.8.1. can run the
 * test suite as well.
 *
 * @author Arie van Deursen; Aug 1, 2003
 * @version $Id: TestAll.java,v 1.8 2008/02/04 11:00:38 arie Exp $
 */


/**
 * If you'd like your class to be tested,
 * include it below in the list of suite classes.
 */
@RunWith(Suite.class)
@SuiteClasses({
    PacmanTest.class,
    BoardTest.class,
    CellTest.class,
    GameTest.class,
    EngineTest.class,
    ObserverTest.class,
    PlayerMoveTest.class,
    ImageFactoryTest.class
})

public final class TestAll  {

    /**
     * Create a JUnit 3.8 Suite object that can be used to exercise
     * the JUnit 4 test suite from the command line or from ant.
     * @return The overall test suite.
     */
    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(TestAll.class);
    }

    /**
     * Convencience method making it easiest to exercise all Pacman test cases.
     * @param args All arguments are ignored.
     */
    public static void main(String[] args) {
        org.junit.runner.JUnitCore.runClasses(jpacman.TestAll.class);
    }

}
