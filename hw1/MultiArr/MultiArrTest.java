import static org.junit.Assert.*;
import org.junit.Test;

public class MultiArrTest {

    @Test
    public void testMaxValue() {
        int[][] arr = {{1, 2}, {4, 8}};
        int[][] arr2 = {{6, 20}, {-5, 9}};
        assertEquals(8, MultiArr.maxValue(arr));
        assertEquals(20, MultiArr.maxValue(arr2));

    }

    @Test
    public void testAllRowSums() {
        assertArrayEquals(new int[]{2, 0}, MultiArr.allRowSums(new int[][]{{1, 1},{0,0}}));

        assertArrayEquals(new int[]{3, 7}, MultiArr.allRowSums(new int[][]{{1, 2}, {3, 4}}));

    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MultiArrTest.class));
    }
}
