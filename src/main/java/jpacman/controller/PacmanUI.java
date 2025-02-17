package jpacman.controller;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jpacman.model.Engine;

/**
 * Construct the top level GUI.
 *
 * @author Arie van Deursen; Aug 17, 2003
 * @version $Id: PacmanUI.java,v 1.7 2008/02/04 10:11:19 arie Exp $
 */
public class PacmanUI extends JFrame implements KeyListener, Observer {

    /**
     * Universal version ID for serialization.
     */
    static final long serialVersionUID = -59470379321937183L;

    /**
     * The underlying model of the game.
     */
    private Engine engine;

    /**
     * The user interface controllers.
     */
    private Pacman controller;

    /**
     * The user interface display.
     */
    private BoardViewer boardViewer;

    /**
     * The status field for te amount of food eaten so far.
     */
    private JTextField eatenField;

    /**
     * The status field indicating what state the game is in.
     */
    private JTextField statusField;

    /**
     * Create a new Pacman top level user interface.
     *
     * @param theEngine Underlying pacman model
     * @param p Top level machine responding to gui requests.
     * @throws IOException If the images needed cannot be found.
     */
    public PacmanUI(Engine theEngine, Pacman p) throws IOException {
        engine = theEngine;
        this.controller = p;
        boardViewer = new BoardViewer(engine);
        engine.addObserver(this);

        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().start();
                // ensure the full window has the focus.
                requestFocusInWindow();
            }
        });
        startButton.requestFocusInWindow();

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().quit();
                // ensure the full window has the focus.
                requestFocusInWindow();
            }
        });



        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                getController().exit();
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(startButton);
        buttonPanel.add(quitButton);
        buttonPanel.add(exitButton);

        JLabel pointsLabel = new JLabel("Food eaten: ");
        final int eatenWidth = 5;
        eatenField = new JTextField("0", eatenWidth);
        eatenField.setEditable(false);
        final int statusWidth = 15;
        statusField = new JTextField("", statusWidth);
        statusField.setEditable(false);
        JPanel statusPanel = new JPanel();
        statusPanel.add(pointsLabel);
        statusPanel.add(eatenField);
        statusPanel.add(statusField);

        JPanel topDown;
        topDown = new JPanel(new BorderLayout());
        topDown.add(statusPanel, BorderLayout.NORTH);
        topDown.add(boardViewer, BorderLayout.CENTER);
        topDown.add(buttonPanel, BorderLayout.SOUTH);

        Container contentPane = getContentPane();
        contentPane.add(topDown);

        attachListeners();
        update(engine, null);
    }

    /**
     * Attach the window and key listeners.
     */
    private void attachListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                getController().exit();
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                getController().start();
            }

            @Override
            public void windowIconified(WindowEvent e) {
                getController().quit();
            }
        });

        // key listeners only work in window with the focus.
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                requestFocusInWindow();
            }
        });
        addKeyListener(this);
    }

    /**
     * @see java.awt.event.KeyListener
     * @param e the key typed.
     */
    public void keyTyped(KeyEvent e) {
        // System.out.println("KEY TYPED");
    }

    /**
     * @see java.awt.event.KeyListener
     * @param e the key released.
     */
    public void keyReleased(KeyEvent e) {
        // System.out.println("KEY RELEASED");
    }

    /**
     * @see java.awt.event.KeyListener
     * @param event the key released.
     */
    public void keyPressed(KeyEvent event) {
        int code;

        code = event.getKeyCode();

        if (code == KeyEvent.VK_UP) {
            getController().up();
        } else if (code == KeyEvent.VK_DOWN) {
            getController().down();
        } else if (code == KeyEvent.VK_LEFT) {
            getController().left();
        } else if (code == KeyEvent.VK_RIGHT) {
            getController().right();
        } else if (code == KeyEvent.VK_Q) {
            getController().quit();
        } else if (code == KeyEvent.VK_E) {
            getController().exit();
        } else if (code == KeyEvent.VK_S) {
            getController().start();
        }
        // else {
        // System.err.println("ignored key press " + code);
        // }
    }

    /**
     * Redraw the board and refresh status related information.
     * @see java.util.Observable
     * @param observable the one being watched
     * @param rest remaining info
     */
    public void update(Observable observable, Object rest) {
        boardViewer.repaint();
        updateStatus();
        updateFood();
    }

    /**
     * Update the display of the total amount of food eaten.
     */
    private void updateFood() {
        int amount = engine.getFoodEaten();
        eatenField.setText(Integer.toString(amount));
    }

    /**
     * Set the status in the GUI depending on the game's state.
     */
    private void updateStatus() {
        String text = null;
        if (engine.inStartingState()) {
            text = "Press start to play";
        }
        if (engine.inPlayingState()) {
            text = "Playing - use arrow keys";
        }
        if (engine.inDiedState()) {
            text = "You died!";
        }
        if (engine.inWonState()) {
            text = "You won!";
        }
        if (engine.inHaltedState()) {
            text = "Suspended";
        }
        assert text != null : "Illegal state";
        statusField.setText(text);
    }

    /**
     * Actually display the GUI on the screen.
     */
    public void display() {
        final int buttonRowHeight = 40;
        setSize(boardViewer.windowWidth(),
                boardViewer.windowHeight()
                + 2 * buttonRowHeight);
        setVisible(true);
    }

    /**
     * @return The controller.
     */
    Pacman getController() {
        return controller;
    }

    /**
     * @return the viewer for the board.
     */
    public BoardViewer getBoardViewer() {
        return boardViewer;
    }
}
