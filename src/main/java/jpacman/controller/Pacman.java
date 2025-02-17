package jpacman.controller;

import java.io.IOException;

import jpacman.model.Engine;

/**
 * Top level Pacman class. The main method creates the engine, the GUI, and the
 * controllers; the methods of the Pacman object created get invoked by the GUI
 * and the controllers.
 *
 * @author Arie van Deursen; Aug 31, 2003
 * @version $Id: Pacman.java,v 1.6 2008/02/03 19:43:38 arie Exp $
 */
public class Pacman {

    /**
     * The model of the game.
     */
    private Engine theEngine;

    /**
     * The display of the game.
     */
    private PacmanUI theViewer;

    /**
     * A controller that moves monsters around.
     */
    private IMonsterController monsterTicker;

    /**
     * A controller triggering animations.
     */
    private Animator theAnimator;

    /**
     * Create a default new game, containing an egnine, a gui, and a monster
     * driver.
     *
     * @throws IOException If underlying figures can't be found.
     */
    public Pacman() throws IOException {
        this(new Engine());
        assert invariant();
    }

    /**
     * Create a new game around a given engine, using the default random
     * monster mover.
     *
     * @param e
     *                The Engine to be used.
     * @throws IOException
     *                If animations can't be found.
     */
    public Pacman(Engine e) throws IOException {
        this(e, new RandomMonsterMover(e));
        assert invariant();
    }

    /**
     * Create a new game from a given engine and monster mover.
     *
     * @param e
     *                The Engine to be used, not null.
     * @param m
     *                The monster mover to be used, not null.
     * @throws IOException
     *                If images can't be found.
     */
    public Pacman(Engine e, IMonsterController m) throws IOException {
        assert e != null;
        assert m != null;
        theEngine = e;
        monsterTicker = m;
        theViewer = new PacmanUI(theEngine, this);
        theAnimator = new Animator(theViewer.getBoardViewer());
        theViewer.display();
        assert invariant();
    }

    /**
     * Instance variables that can't be null.
     * @return True iff selected instance variables all aren't null.
     */
    protected boolean invariant() {
        return theEngine != null && monsterTicker != null && theViewer != null;
    }

    /**
     * Start the new game.
     */
    public void start() {
        assert invariant();
        theEngine.start();
        monsterTicker.start();
        theAnimator.start();
        assert invariant();
    }

    /**
     * Halt the pacman game.
     */
    public void quit() {
        assert invariant();
        monsterTicker.stop();
        theEngine.quit();
        theAnimator.stop();
        assert invariant();
    }


    /**
     * Terminate the game.
     */
    public void exit() {
        assert invariant();
        quit();
        theViewer.dispose();
        // No need for a hard exit using, e.g., System.exit(0):
        // we'd like to be able to run a series of pacman's in a single
        // JUnit test suite.
        assert invariant();
    }

    /**
     * Respond to an up request from the GUI.
     */
    public void up() {
        assert invariant();
        theEngine.movePlayer(0, -1);
        assert invariant();
    }

    /**
     * Respond to a down request from the GUI.
     */
    public void down() {
        assert invariant();
        theEngine.movePlayer(0, 1);
        assert invariant();
    }

    /**
     * Respond to a left request from the GUI.
     */
    public void left() {
        assert invariant();
        theEngine.movePlayer(-1, 0);
        assert invariant();
    }

    /**
     * Respond to a right request from the GUI.
     */
    public void right() {
        assert invariant();
        theEngine.movePlayer(1, 0);
        assert invariant();
    }

    /**
     * @return the Engine of this pacman game
     */
    public Engine getEngine() {
        return theEngine;
    }

    /**
     * Start me up.
     *
     * @param args
     *                Are ignored.
     * @throws IOException
     *                 If images can't be found.
     */
    public static void main(String[] args) throws IOException {
        if (args.length > 0) {
            System.err.println("Ignoring command line arguments.");
        }
        new Pacman();
    }
}
