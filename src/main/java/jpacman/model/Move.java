package jpacman.model;

/**
 * A potential move from one cell to another. The responsibilities of this class
 * include indicating the effect of the move (player dies, player wins, ...),
 * whether the move is possible, and actually carrying out the move.
 * <p>
 * This class actually implements the Command design pattern from Gamma et al:
 * The apply() method takes the role of the execution, and ensures that the move
 * is actually done. The way the effects are handled is the responsibility of
 * the clients of this class.
 * <p>
 *
 * @author Arie van Deursen, created August 2003.
 * @version $Id: Move.java,v 1.8 2008/02/04 10:11:19 arie Exp $
 */
public abstract class Move {

    /**
     * The guest who initiated the move.
     */
    private MovingGuest mover = null;


    /**
     * The target cell the mover would like to go to.
     */
    private Cell to = null;

    /**
     * Is the target cell indeed empty and reachable?
     */
    private boolean targetCellAvailable = false;

    /**
     * Is the player going to die as a result of this move?
     */
    private boolean playerDies = false;

    /**
     * Are we ready precomputing all possible effects of this move?
     */
    private boolean initialized = false;

    /**
     * Create a move from a MovingGuest to a particular Cell.
     * Precondition: the moving guest occupies a cell.
     * The target cell can be null, in which case
     * the move simply won't be possible.
     *
     * @param fromGuest
     *            The guest to be moved
     * @param toCell
     *            The target location
     */
    public Move(MovingGuest fromGuest, Cell toCell) {
        assert fromGuest != null;
        assert fromGuest.getLocation() != null;
        assert toCell == null
        || fromGuest.getLocation().getBoard() == toCell.getBoard();
        this.mover = fromGuest;
        this.to = toCell;
        assert moveInvariant() : "Move invariant invalid";
    }

    /**
     * Check that the guest to be moved indeed occupies a cell.
     * Furthermore, moves that cause the player to die are not possible.
     *
     * @return True iff the mover occupies a cell.
     */
    public boolean moveInvariant() {
        return mover != null
            && mover.getLocation() != null
            && (initialized ? !(movePossible() && playerDies) : true);
    }

    /**
     * Compute all possible effects of this move, such as the potential death of
     * the player and whether the move is possible at all. Precondition: not
     * called before. Postcondition: initialization completed.
     */
    protected void precomputeEffects() {
        assert moveInvariant();
        assert !initialized() : "Can't re-initialized the move.";
        boolean cellAvailable = false;
        if (withinBorder()) {
            assert to != null;
            Guest targetGuest = to.getInhabitant();
            if (targetGuest == null) {
                cellAvailable = true;
            } else {
                cellAvailable = tryMoveToGuest(targetGuest);
            }
        }
        this.targetCellAvailable = cellAvailable;
        initialized = true;
        assert initialized();
        assert moveInvariant();
    }

    /**
     * Try to move to an occupied cell.
     * Precondition: targetGuest != null and still initializing
     * (see tryMoveToGuestPrecondition).
     * <p>
     * This method implements a double dispatch: the actual behavior depends on
     * both the subclass of the source and the target guest.
     *
     * @param targetGuest
     *            Guest the mover will meet
     * @return true iff the move is possible.
     */
    protected abstract boolean tryMoveToGuest(Guest targetGuest);

    /**
     * Boolean function representing the precondition of tryMoveToGuest(Guest
     * targetGuest).
     *
     * @param targetGuest The guest this mover is going tomeet.
     * @return true iff the precondition of tryMoveToGuest() holds.
     */
    protected boolean tryMoveToGuestPrecondition(Guest targetGuest) {
        return withinBorder() && targetGuest != null && !initialized();
    }

    /**
     * @return true iff the target cell falls within the borders of the Board.
     */
    private boolean withinBorder() {
        return to != null;
    }

    /**
     * Check if the method has been initialized via the precomputeEffects
     * method.
     *
     * @return true iff precomputeEffects has been called.
     */
    public boolean initialized() {
        return initialized;
    }

    /**
     * Return true iff the player will die because of this move. Precondition:
     * class has been initialized.
     *
     * @return true iff player won't survive this move.
     */
    public boolean playerDies() {
        assert initialized();
        return playerDies;
    }

    /**
     * Return true iff the move is possible. Precondition: class has been
     * initialized.
     *
     * @return true iff the guest can make this move.
     */
    public boolean movePossible() {
        assert initialized() : "run precompute first!";
        return withinBorder()
           && targetCellAvailable
           && !playerDies()
           && !moveDone();
    }

    /**
     * Actually carry out the move. precondition: the move is possible.
     * postcondition: the old cell is empty, the target cell is occupied by the
     * mover.
     */
    protected void apply() {
        assert initialized();
        assert movePossible() : "Cannot execute an impossible move.";

        Cell fromCell = mover.getLocation();
        Guest targetGuest = to.getInhabitant();
        if (targetGuest != null) {
            // actually only necessary for food if
            // the player moves...
            targetGuest.deoccupy();
        }
        mover.deoccupy();
        mover.occupy(to);

        assert fromCell.getInhabitant() == null : "old cell should be freed";
        assert moveDone();
        assert initialized();
    }

    /**
     * Invoke this method while precomputing the effects of this move if it is
     * detected that the player will die because of this move. Precondition:
     * initialization <b>not</b> yet completed.
     */
    protected void die() {
        assert !initialized();
        playerDies = true;
    }

    /**
     * Obtain the guest initiating the move.
     *
     * @return The mover.
     */
    protected MovingGuest getMovingGuest() {
        return mover;
    }

    /**
     * @return true if the move has already been applied.
     */
    boolean moveDone() {
        assert mover != null;
        assert mover.getLocation() != null;
        return to != null && mover.getLocation().equals(to);
    }



}
