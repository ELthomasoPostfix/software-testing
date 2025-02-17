package jpacman.model;


/**
 * A Cell keeps track of its (x,y) position on the board, and the potential
 * Guest occupying the Cell. It's responsibilities include identifying
 * neighbouring cells that fall within the Board's borders (to support moving
 * guests on the board), and keeping the Cell-Guest association consistent.
 *
 * @author Arie van Deursen; Jul 27, 2003
 * @version $Id: Cell.java,v 1.9 2008/02/03 19:43:38 arie Exp $
 */
public class Cell {

    /**
     * The x and y coordinates of the cell.
     */
    private int x, y;

    /**
     * The board the cell lives on.
     */
    private Board board;

    /**
     * The guest occupying the cell, null if not occupied.
     */
    private Guest inhabitant;

    /**
     * Create a new cell at a given position on the board.
     *
     * @param xCoordinate
     *            The X coordinate
     * @param yCoordinate
     *            The Y coordinate
     * @param b
     *            The board
     */
    public Cell(int xCoordinate, int yCoordinate, Board b) {
        x = xCoordinate;
        y = yCoordinate;
        this.board = b;
        this.inhabitant = null;
        assert invariant();
    }

    /**
     * Conjunction of all invariants.
     *
     * @return true iff all invariants hold.
     */
    protected boolean invariant() {
        return guestInvariant() && boardInvariant();
    }

    /**
     * A Cell should always be part of the Board given at construction.
     *
     * @return true iff this is the case.
     */
    protected boolean boardInvariant() {
        return board != null && board.withinBorders(x, y);
    }

     /**
     * @return true iff the invariant for guest association holds.
     */
     public boolean guestInvariant() {
            return (inhabitant == null) || (inhabitant.getLocation() == this);
     }



    /**
     * Return the inhabitant of this cell.
     *
     * @return The Guest hosted by this Cell, or null if the Cell is free.
     */
    public Guest getInhabitant() {
        return inhabitant;
    }


    /**
     * Modify the guest of this cell. This method is needed by the Guest's
     * occupy method which keeps track of the links in the Cell-Guest
     * association.
     * Precondition: the guest's location should be set at this Cell,
     * and the current cell should not be occupied already
     * by some other guest.
     * <p>
     * Observe that the
     * class invariant doesn't hold at method entry -- therefore it's not a
     * public method. On method exit, however, it is valid again.
     *
     * @param aGuest
     *            The new guest of this cell.
     */
    void setGuest(Guest aGuest) {
        assert aGuest.getLocation()==this;
        assert inhabitant==null;

        inhabitant = aGuest;

        assert getInhabitant()==aGuest;
        assert invariant();
    }


    /**
     * Remove the inhabitant from this Cell. This method assumes (precondition)
     * that the inhabitant (if any) has already removed it's link to this cell.
     * (Only) to be used by Guest.deoccupy().
     * <p>
     * Upon method entry, the class invariant doesn't hold, but on
     * method exit it does.
     */
    void free() {
        assert inhabitant==null || inhabitant.getLocation()==null;

        inhabitant = null;

        assert inhabitant==null;
        assert invariant();
    }

    /**
     * Determine iff this cell is occupied.
     *
     * @return true iff the cell is occupied.
     */
    public boolean isOccupied() {
        return inhabitant != null;
    }


    /**
     * Get the horizontal position of this cell.
     *
     * @return the cell's X coordinate
     */
    public int getX() {
        assert invariant();
        return x;
    }

    /**
     * Get the vertical position of this cell.
     *
     * @return the cell's Y coordinate.
     */
    public int getY() {
        assert invariant();
        return y;
    }

    /**
     * Return the cell located at position (x + dx, y + dy), or null if this
     * cell would fall beyond the borders of the Board.
     *
     * @param dx
     *            The x offset
     * @param dy
     *            The y offset
     * @return null or the cell at (x+dx,y+dy).
     */
    public Cell cellAtOffset(int dx, int dy) {
        assert invariant();
        Cell result = null;
        int newx = x + dx;
        int newy = y + dy;
        if (getBoard().withinBorders(newx, newy)) {
            result = getBoard().getCell(newx, newy);
        }
        assert invariant();
        return result;
    }

    /**
     * Return the board this cell is part of.
     *
     * @return This cell's board.
     */
    public Board getBoard() {
        assert invariant();
        return board;
    }
}
