package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.imposters.ByteBuddyClassImposteriser;



/**
 * Mock-based test suite for methods working directly on Cells.
 *
 * @author Thomas Gueutal, Niels Van der Planken; March 2, 2025
 */
@RunWith(JMock.class)
public class CellMockTest {
    /**
     * The JMock mockery context.
     */
    Mockery context = new JUnit4Mockery() {{
        setImposteriser(ByteBuddyClassImposteriser.INSTANCE);
    }};

    /** Set up the mocking for testing ´Cell.adjacent(Cell´.
     *
     * This function reduces boilerplate for the setup and
     * expectation steps of mocking ´Cell.adjacent(Cell´.
     * Note that parameterized unit tests would be a better,
     * but more complicated solution to the same problem.
     */
    public Board setUpAdjacentMock(int xa, int ya, int xb, int yb) {
        /* Setup. */
        Board mockBoard = context.mock(Board.class);

        /* Expectations. */
        context.checking(new Expectations() {{
            // CellA constructor Cell.invariant() call.
            oneOf(mockBoard).withinBorders(xa, ya); will(returnValue(true));
            // CellB constructor Cell.invariant() call.
            oneOf(mockBoard).withinBorders(xb, yb); will(returnValue(true));
            // CellA.adjacent(CellB) Cell.invariant() call.
            oneOf(mockBoard).withinBorders(xa, ya); will(returnValue(true));
            oneOf(mockBoard).withinBorders(xa, ya); will(returnValue(true));
        }});

        return mockBoard;
    }

    @Test
    public void testAdjacentAbove() {
        Board mockBoard = setUpAdjacentMock(2,2, 1,2);

        /* Execute. */
        Cell cellA = new Cell(2, 2, mockBoard);
        Cell cellB = new Cell(1, 2, mockBoard);
        assertTrue(cellA.adjacent(cellB));
    }

    @Test
    public void testAdjacentBelow() {
        Board mockBoard = setUpAdjacentMock(2,2, 3,2);

        /* Execute. */
        Cell cellA = new Cell(2, 2, mockBoard);
        Cell cellB = new Cell(3, 2, mockBoard);
        assertTrue(cellA.adjacent(cellB));
    }

    @Test
    public void testAdjacentLeft(){
        Board mockBoard = setUpAdjacentMock(1,2, 1,1);

        /* Execute. */
        Cell cellA = new Cell(1, 2, mockBoard);
        Cell cellB = new Cell(1, 1, mockBoard);
        assertTrue(cellA.adjacent(cellB));
    }

    @Test
    public void testAdjacentRight(){
        Board mockBoard = setUpAdjacentMock(1,1, 1,2);

        /* Execute. */
        Cell cellA = new Cell(1, 1, mockBoard);
        Cell cellB = new Cell(1, 2, mockBoard);
        assertTrue(cellA.adjacent(cellB));
    }

    @Test
    public void testNotAdjacentDiagonalLeft() {
        Board mockBoard = setUpAdjacentMock(1,1, 2,2);

        /* Execute. */
        Cell cellA = new Cell(1, 1, mockBoard);
        Cell cellB = new Cell(2, 2, mockBoard);
        assertFalse(cellA.adjacent(cellB));
    }

    @Test
    public void testNotAdjacentDiagonalRight() {
        Board mockBoard = setUpAdjacentMock(1,2, 2,1);

        /* Execute. */
        Cell cellA = new Cell(1, 2, mockBoard);
        Cell cellB = new Cell(2, 1, mockBoard);
        assertFalse(cellA.adjacent(cellB));
    }

    @Test
    public void testNotAdjacentFarAwayRow() {
        Board mockBoard = setUpAdjacentMock(1,1, 1,5);

        /* Execute. */
        Cell cellA = new Cell(1, 1, mockBoard);
        Cell cellB = new Cell(1, 5, mockBoard);
        assertFalse(cellA.adjacent(cellB));
    }
}
