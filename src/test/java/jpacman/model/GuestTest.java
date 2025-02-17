package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;


/**
 * Test the Cell Guest association.
 * Originally set up following Binder's Class Association Test
 * pattern.
 *
 * @author Arie van Deursen; Jul 29, 2003
 * @version $Id: GuestTest.java,v 1.6 2008/02/03 19:43:38 arie Exp $
 */
public class GuestTest {

    /**
     * Two cells that we'll (try to) occupy.
     */
    private Cell theCell, anotherCell;

    /**
     * Two guests that will participate in occupying cells.
     */
    private Guest theGuest, anotherGuest;

    /**
     * Create two cells, and two guests, which we can
     * use for occupying / deoccupying different cells with various guests.
     */
    @Before
    public void setUp() {
        // create the board.
        int width = 2;
        int height = 2;
        Board theBoard = new Board(width, height);

        // obtain two cells.
        int x = 0;
        int y = 0;
        theCell = theBoard.getCell(x, y);
        anotherCell = theBoard.getCell(x + 1, y);
        assertFalse(theCell.equals(anotherCell));

        // create two guests.
        theGuest = new Food();
        anotherGuest = new Food();
        assertFalse(theGuest.equals(anotherGuest));
    }


    /**
     * Test a (short) sequence of occupy / deoccupy calls.
     */
    @Test
    public void testOccupyDeoccupy() {
        // starting situation.
        assertNull(theCell.getInhabitant());

        theGuest.occupy(theCell);
        assertEquals(theGuest, theCell.getInhabitant());
        assertNull(anotherCell.getInhabitant());

        theGuest.deoccupy();
        assertNull(theCell.getInhabitant());
        assertNull(anotherCell.getInhabitant());

        theGuest.occupy(anotherCell);
        assertEquals(theGuest, anotherCell.getInhabitant());
        assertNull(theCell.getInhabitant());
    }

}
