package jpacman.model;

/**
 * Class to represent the effects of moving the player.
 *
 * @author Arie van Deursen; Aug 18, 2003
 * @version $Id: PlayerMove.java,v 1.6 2008/02/11 13:05:20 arie Exp $
 */
public class PlayerMove extends Move {

    /**
     * The player wishing to move.
     */
    private Player thePlayer;

    /**
     * The amount of food that will be eaten if this move is
     * successful.
     */
    private int foodEaten = 0;

    /**
     * Create a move for the given player to a given target cell.
     *
     * @param player
     *            the Player to be moved
     * @param newCell
     *            the target location.
     * @see jpacman.model.Move
     */
    public PlayerMove(Player player, Cell newCell) {
        // preconditions checked in super method,
        // and cannot be repeated here ("super(...)" must be 1st stat.).
        super(player, newCell);
        thePlayer = player;
        precomputeEffects();
        assert invariant();
    }

    /**
     * Verify that the food eaten remains non negative, the player/mover equal
     * and non-null.
     *
     * @return true iff the invariant holds.
     */
    public boolean invariant() {
        return moveInvariant() && foodEaten >= 0 && thePlayer != null
        && getMovingGuest().equals(thePlayer);
    }

    /**
     * Attempt to move the player towards a target guest.
     * @param targetGuest The guest that the player will meet.
     * @return true if the move is possible, false otherwise.
     * @see jpacman.model.Move#tryMoveToGuest(jpacman.model.Guest)
     */
    @Override
    protected boolean tryMoveToGuest(Guest targetGuest) {
        assert tryMoveToGuestPrecondition(targetGuest)
            : "percolated precondition";
        return targetGuest.meetPlayer(this);
    }

    /**
     * Return the player initiating this move.
     *
     * @return The moving player.
     */
    public Player getPlayer() {
        assert invariant();
        return thePlayer;
    }

    /**
     * Return the food that would be eaten if this move is applied.
     * Precondition: the move is initialized.
     *
     * @return The food that would be eaten.
     */
    public int getFoodEaten() {
        assert invariant();
        return foodEaten;
    }

    /**
     * Set the amount of food eaten to a given value.
     *
     * @param food
     *            the amount of food.
     */
    protected void setFoodEaten(int food) {
        assert invariant() : "Invariant before set food.";
        foodEaten = food;
        assert invariant() : "Invariant in set Food Eaten";
    }

    /**
     * Actually apply the move, assuming it is possible.
     */
    @Override
    public void apply() {
        assert invariant();
        assert movePossible();
        super.apply();
        int oldFood = getPlayer().getPointsEaten();
        getPlayer().eat(foodEaten);
        assert getPlayer().getPointsEaten() == oldFood + foodEaten;
        assert invariant();
    }

}
