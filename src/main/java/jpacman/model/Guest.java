package jpacman.model;

/**
 * Class containing responsibilities shared by all Guests.
 *
 * @author Arie van Deursen; Jul 28, 2003
 * @version $Id: Guest.java,v 1.9 2008/02/11 13:05:20 arie Exp $
 */
public abstract class Guest {

    /**
     * The cell the guest is occupying.
     */
    private Cell location;

    /**
     * The character code representing the player guest type.
     */
    public static final char PLAYER_TYPE = 'P';

    /**
     * The character code representing the monster guest type.
     */
    public static final char MONSTER_TYPE = 'M';

    /**
     * The character code representing the food guest type.
     */
    public static final char FOOD_TYPE = 'F';

    /**
     * The character code representing the wall guest type.
     */
    public static final char WALL_TYPE = 'W';

    /**
     * The character code representing an empty cell.
     */
    public static final char EMPTY_TYPE = '0';

    /**
     * Create a new Guest satisfying the class invariant.
     */
    public Guest() {
        location = null;
        assert guestInvariant();
    }

     /**
      * @return true iff the invariant for cell association holds.
      */
     protected boolean guestInvariant() {
            return (location == null) || (location.getInhabitant() == this);
     }


    /**
     * @return The location of this guest.
     */
    public Cell getLocation() {
        return location;
    }

    /**
     * Occupy a non-null, empty cell.
     * Precondition: the current Guest must not
     * have occupied another cell, and the target cell should be empty.
     * Postcondition: both the cell and the guest
     * have changed their pointers to reflect the occupation.
     *
     * @param cell
     *            New location for this guest.
     */
    public void occupy(Cell cell) {
        assert guestInvariant();
        assert cell.getInhabitant()==null;
        assert location==null;

        location = cell;
        cell.setGuest(this);

        assert cell.getInhabitant()==this;
        assert location==cell;
        assert guestInvariant();
    }

    /**
     * Remove the occupation link with this Guest's Cell association.
     *
     * @return The location that is now free, or null if the Guest didn't occupy
     *         a cell.
     */
    public Cell deoccupy() {
        assert guestInvariant();

        Cell oldLocation = location;
        location = null;
        if (oldLocation != null) {
            oldLocation.free();
        }

        assert location==null;
        assert oldLocation == null || oldLocation.getInhabitant()==null;
        assert guestInvariant();
        return oldLocation;
    }

    /**
     * The player would like to visit the cell occupied by this guest. Indicate
     * whether this is possible, and modify the move's state to indicate what
     * the effect of such a move would be. Precondition: the move object is non
     * null and initializing.
     * <p>
     *
     * @param aMove
     *            theMove move object representing intended move and its
     *            effects.
     *
     * @return True iff this guest has no objection to the player taking his
     *         place.
     */
    protected abstract boolean meetPlayer(PlayerMove aMove);


    /**
     * Return a character code representing the type of guest.
     *
     * @return Type code for this guest.
     */
    public abstract char guestType();

}
