package jpacman.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Specialize the general MoveTest test suite to one
 * that is tailored to MonsterMoves.
 * Thanks to inheritance, all test cases from MoveTest
 * are also methods in MonsterMoveTest, thus helping us
 * to test conformance with Liskov's Substitution Principle (LSP)
 * of the Move hierarchy.
 * <p>
 * @author Thomaso Gueutal, Niels Van der Planken; Ma 15, 2025.
 */
public class MonsterMoveTest extends MoveTest {

    /**
     * The move the monster would like to make.
     */
    private MonsterMove aMonsterMove;

    /**
     * Simple test of a few getters.
     */
    @Test
    public void testSimpleGetters() {
        MonsterMove monsterMove = new MonsterMove(theMonster, foodCell);
        assertEquals(theMonster, monsterMove.getMonster());
        assertFalse(monsterMove.movePossible());
        assertFalse(monsterMove.playerDies());
        assertTrue(monsterMove.invariant());
    }


    /**
     * Create a move object that will be tested.
     *  @see jpacman.model.MoveTest#createMove(jpacman.model.Cell)
     *  @param target The cell to be occupied by the move.
     *  @return The move to be tested.
     */
    @Override
    protected MonsterMove createMove(Cell target) {
        aMonsterMove = new MonsterMove(theMonster, target);
        return aMonsterMove;
    }
}
