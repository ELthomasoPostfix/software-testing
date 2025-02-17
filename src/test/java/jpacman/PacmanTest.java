package jpacman;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import jpacman.controller.AbstractMonsterController;
import jpacman.controller.IMonsterController;
import jpacman.controller.Pacman;
import jpacman.model.Engine;
import jpacman.model.GameTestCase;
import junit.framework.JUnit4TestAdapter;
import junit.framework.TestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Systematic acceptance test for Pacman.
 * It follows the test suite design document,
 * which in turn follows the Pacman use cases.
 *
 * The test cases have been derived by translating the use
 * cases into test-ready decision tables and state machines,
 * both available in the JPacman documentation.
 *
 * @author Arie van Deursen; 5-sep-2004
 * @version $Id: PacmanTest.java,v 1.14 2008/02/10 19:28:20 arie Exp $
 */
public class PacmanTest extends GameTestCase {

    /**
     * The full game, including a gui.
     */
    private Pacman myPacman;

    /**
     * Set up the full game, making use of the
     * game model created in the superclass.
     * @throws IOException if images can't be loaded.
     */
    @Before public void setup() throws IOException {
        myPacman = new Pacman(new Engine(theGame),
                new EmptyMonsterController());
        myPacman.start();
        assertTrue(myPacman.getEngine().inPlayingState());
    }

    /**
     * And, at the end, nicely close the windows.
     */
    @After public void tearDown() {
        myPacman.exit();
    }

    /**
     * Top level test running the default GUI, map, monstercontroller,
     * observers, etc.
     * Simple "smoke test" that just executes various methods to make sure that
     * they don't crash immediately.
     * More elaborate tests are conducted elsewhere.
     * The name "alpha-omega" refers to the full cycle this test suite
     * attempts to make (Binder's terminology).
     * @throws InterruptedException if threads don't work well
     * @throws IOException  If the file systems is wrong
     */
    @Test public void testTopLevelAlphaOmega()
    throws InterruptedException, IOException {
        Pacman p = new Pacman();
        p.start();
        p.left();
        p.right();
        final int nrOfMonsterMoves = 10;
        Thread.sleep(AbstractMonsterController.DELAY * nrOfMonsterMoves);
        p.up();
        p.down();
        p.quit();
        p.start();
        p.left();
        p.right();
        p.up();
        p.down();
        p.exit();
    }

     // The remaining acceptance test suite heavily depends on
     // proper implementation of the functionality that has been
     // left out as an exercise, and hence has been omitted from
     // this version.


    /**
     * A monster controller that doesn't move any monsters,
     * allowing us to do the moving ourselves in this test suite.
     */
    private class EmptyMonsterController implements IMonsterController {
        public void doTick() { }
        public void start() { }
        public void stop() { }
    }

    /**
     * @return The underlying engine.
     */
    private Engine getEngine() {
        return myPacman.getEngine();
    }

    /**
     * Create a JUnit 3.8 Suite object that can be used to exercise
     * the JUnit 4 test suite from the command line or from ant.
     * @return A Junit 3.8 test suite containing all test cases.
     */
    public static junit.framework.Test suite() {
        TestSuite s = new TestSuite();
        s.addTest(new JUnit4TestAdapter(PacmanTest.class));
        return s;
    }


    /**
     * Actually exercise the Pacman test suite.
     *
     * @param args All arguments are ignored.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(jpacman.PacmanTest.suite());
    }
}
