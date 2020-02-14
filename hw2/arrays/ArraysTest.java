package arrays;

import org.checkerframework.common.value.qual.StaticallyExecutable;
import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author FIXME
 */

public class ArraysTest {
    /** FIXME
     */
    @Test
    public void testCatenate(){
        int[] a = new int[] {1, 2, 3, 4};
        int[] b = new int[] {5, 6, 7, 8};
        int[] aWithB = new int[] {1, 2, 3, 4, 5, 6, 7, 8};

        assertArrayEquals(aWithB, Arrays.catenate(a, b));

        int[] c = new int[] {};
        int[] d = new int[] {1, 2, 3};
        int[] cWithD = new int[] {1, 2, 3};

        assertArrayEquals(cWithD, Arrays.catenate(c, d));
    }

    @Test
    public void testRemove(){
        int[] e = new int[] {1, 2, 3, 4, 5, 6, 7};
        int[] removedFromE = new int[] {1, 2, 6, 7};

        assertArrayEquals(removedFromE, Arrays.remove(e, 3, 3));

        int[] f = new int[] {11, 22, -3, 64, 5, 67, 7};
        int[] removedFromF = new int[] {7};

        assertArrayEquals(removedFromF, Arrays.remove(f, 11, 6));
    }

    @Test
    public void testNaturalRuns() {
        int[] arr0 = new int[] {1, 3, 7, 5, 4, 6, 9, 10};
        int[][] res0 = new int[][]{{1, 3, 7}, {5}, {4, 6, 9, 10}};
        assertArrayEquals(res0, Arrays.naturalRuns(arr0));

        int[] arr1 = new int[]{1, 2, 8, 4, 6, 1, 2, 5, 8, 3, 2};
        int[][] res1 = new int[][]{{1, 2, 8}, {4, 6}, {1, 2, 5, 8}, {3}, {2}};
        assertArrayEquals(res1, Arrays.naturalRuns(arr1));


        int [] arr3 = new int[] {-1, -2, 0, 3, 9, 9, 6, 8};
        int[][] res3 = new int[][] {{-1}, {-2, 0, 3, 9}, {9}, {6, 8}};
        assertArrayEquals(res3, Arrays.naturalRuns(arr3));
    }





    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(ArraysTest.class));
    }
}
