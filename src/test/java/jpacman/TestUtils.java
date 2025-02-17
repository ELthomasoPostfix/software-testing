package jpacman;

import org.junit.Test;

/**
 * Utility methods that help in testing.
 *
 * @author Arie van Deursen, TU Delft, 2006
 * @version $Id: TestUtils.java,v 1.6 2008/02/10 19:51:11 arie Exp $
 */

public final class TestUtils {

    /**
     * Work around surefire 2.3 issue.
     */
    @Test public void testDummy() { }

    /**
     * A helper method that just tells us whether assertions are enabled
     * or not.
     * @return True iff assertions are enabled.
     */
    public static boolean assertionsEnabled() {
        boolean hasAssertEnabled = false;
        //notice the intentional side effect!
        assert hasAssertEnabled = true;
        return hasAssertEnabled;
    }
}
