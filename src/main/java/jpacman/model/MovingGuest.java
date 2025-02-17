package jpacman.model;

/**
 * Type for guests with moving capabilities. The actual moving is handled by the
 * Move class and its subclasses.
 *
 * @author Arie van Deursen; Aug 3, 2003
 * @version $Id: MovingGuest.java,v 1.2 2008/02/03 19:01:50 arie Exp $
 */
public abstract class MovingGuest extends Guest {

    /**
     * Constructs a new moving guest.
     */
    public MovingGuest() {
    }
}
