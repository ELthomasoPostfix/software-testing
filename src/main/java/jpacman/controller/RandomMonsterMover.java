package jpacman.controller;


import jpacman.model.Engine;
import jpacman.model.Monster;

/**
 * Example, simple monster mover that just moves monsters randomly.
 *
 * @author Arie van Deursen; Aug 18, 2003
 * @version $Id: RandomMonsterMover.java,v 1.9 2008/02/08 20:15:19 arie Exp $
 */
public class RandomMonsterMover extends AbstractMonsterController {

    /**
     * Start a new mover with the given engine.
     *
     * @param e
     *            Engine used.
     */
    public RandomMonsterMover(Engine e) {
        super(e);
    }

    /**
     * Local enum for directions.
     */
    private enum Direction { UP, DOWN, LEFT, RIGHT };

    /**
     * Actually conduct a random move in the underlying engine.
     *
     * @see jpacman.controller.IMonsterController#doTick()
     */
    public void doTick() {
        Monster theMonster = getRandomMonster();

        int dx = 0;
        int dy = 0;

        int dir = getRandomizer().nextInt(Direction.values().length);
        Direction d = Direction.values()[dir];
        switch(d) {
        case UP:
            dy = -1;
            break;
        case DOWN:
            dy = 1;
            break;
        case LEFT:
            dx = -1;
            break;
        case RIGHT:
            dx = 1;
            break;
        default:
            assert false;
        }

        assert dy >= -1 && dy <= 1;
        assert
        Math.abs(dx) == 1 && dy == 0
        ||
        Math.abs(dy) == 1 && dx == 0;

        getEngine().moveMonster(theMonster, dx, dy);
    }
}
