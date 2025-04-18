package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

/**
 * Test suite for methods working directly on Cells.
 *
 * @author Arie van Deursen; Jul 29, 2003
 * @version $Id: CellTest.java,v 1.16 2008/02/10 12:51:55 arie Exp $
 */
public class CellTest {

    /**
     * Width & heigth of board to be used.
     */
    private final int width = 4, height = 5;

    /**
     * The board the cells occur on.
     */
    private Board aBoard;

    /**
     * The "Cell Under Test".
     */
    private Cell aCell;

    /**
     * Actually create the board and the cell. *
     */
    @Before
    public void setUpBoard() {
        aBoard = new Board(width, height);
        // put the cell on an invariant boundary value.
        aCell = new Cell(0, height - 1, aBoard);
    }



    /**
     * Test obtaining a cell at a given offset. Ensure both postconditions
     * (null value if beyond border, value with board) are executed.
     */
    @Test
    public void testCellAtOffset() {
        assertEquals(height - 2, aCell.cellAtOffset(0, -1).getY());
        assertEquals(0, aCell.cellAtOffset(0, -1).getX());
        // assertNull(aCell.cellAtOffset(-1, 0));

        Cell cell11 = aBoard.getCell(1, 1);
        Cell cell12 = aBoard.getCell(1, 2);
        assertEquals(cell12, cell11.cellAtOffset(0, 1));
    }

    @Test
    public void testAdjacentAbove() {
        Cell cellA = new Cell(2, 2, aBoard);
        Cell cellB = new Cell(1, 2, aBoard);
        assertTrue(cellA.adjacent(cellB));
    }

    @Test
    public void testAdjacentBelow() {
        Cell cellA = new Cell(2, 2, aBoard);
        Cell cellB = new Cell(3, 2, aBoard);
        assertTrue(cellA.adjacent(cellB));
    }

    @Test
    public void testAdjacentLeft(){
        Cell cellA = new Cell(1, 2, aBoard);
        Cell cellB = new Cell(1, 1, aBoard);
        assertTrue(cellA.adjacent(cellB));
    }

    @Test
    public void testAdjacentRight(){
        Cell cellA = new Cell(1, 1, aBoard);
        Cell cellB = new Cell(1, 2, aBoard);
        assertTrue(cellA.adjacent(cellB));
    }

    @Test
    public void testNotAdjacentDiagonalLeft() {
        Cell cellA = new Cell(1, 1, aBoard);
        Cell cellB = new Cell(2, 2, aBoard);
        assertFalse(cellA.adjacent(cellB));
    }

    @Test
    public void testNotAdjacentDiagonalRight() {
        Cell cellA = new Cell(1, 2, aBoard);
        Cell cellB = new Cell(2, 1, aBoard);
        assertFalse(cellA.adjacent(cellB));
    }

    /*
    @Test
    public void testNotAdjacentFarAwayRow() {
        Cell cellA = new Cell(1, 1, aBoard);
        Cell cellB = new Cell(1, 5, aBoard);
        assertFalse(cellA.adjacent(cellB));
    }

     */

}
