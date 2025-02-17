package jpacman.model;

/**
 * A monster on the board.
 *
 * @author Arie van Deursen; Jul 28, 2003
 * @version $Id: Monster.java,v 1.7 2008/02/07 08:40:42 arie Exp $
 */
public class Monster extends MovingGuest {

    /**
     * Create a new monster, not occupying a cell yet.
     */
    public Monster() {
        super();
    }

    /**
     * The player decided to bumb into this monster. Modify the move's state
     * reflecting the fact that this will cause the player to die.
     *
     * @param theMove
     *            move object representing intended move and its effects.
     * @return false, the player cannot occupy the monster's cell.
     *
     * @see jpacman.model.Guest#meetPlayer(jpacman.model.PlayerMove)
     */
    @Override
    protected boolean meetPlayer(PlayerMove theMove) {
        assert guestInvariant();
        assert theMove != null;
        assert !theMove.initialized();
        theMove.die();
        return false;
    }

    /**
     * @see jpacman.model.Guest#guestType()
     * @return Character encoding for a monster.
     */
    @Override
    public char guestType() {
        return Guest.MONSTER_TYPE;
    }

}
