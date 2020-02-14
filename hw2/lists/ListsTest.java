package lists;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *
 *  @author Diraj Thajali
 */

public class ListsTest {
    /** FIXME
     */

    @Test
    public void testNaturalRuns() {
        int [] arr1 = new int[] {1, 2, 8, 4, 6, 1, 2, 5, 8, 3, 2};
        int [][] res1 = new int[][] {{1, 2, 8}, {4, 6}, {1, 2, 5, 8}, {3}, {2}};
        IntList L = IntList.list(arr1);
        IntListList result = IntListList.list(res1);

        assertEquals(result, Lists.naturalRuns(L));

        int [] arr2 = new int[] {};
        int[][] res2 = new int[][] {{}};
        IntList L1 = IntList.list(arr2);
        IntListList result1 = IntListList.list(res2);

        assertEquals(result1, Lists.naturalRuns(L1));


        int [] arr3 = new int[] {-1, -2, 0, 3, 9, 9, 6, 8};
        int[][] res3 = new int[][] {{-1}, {-2, 0, 3, 9}, {9}, {6, 8}};
        IntList L2 = IntList.list(arr3);
        IntListList result2 = IntListList.list(res3);

        assertEquals(result2, Lists.naturalRuns(L2));



    }

    // It might initially seem daunting to try to set up
    // IntListList expected.
    //
    // There is an easy way to get the IntListList that you want in just
    // few lines of code! Make note of the IntListList.list method that
    // takes as input a 2D array.

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ListsTest.class));
    }
}
