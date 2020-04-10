import org.junit.Test;
import static org.junit.Assert.*;

public class MySortingTest {

    public SortingAlgorithm selection = new MySortingAlgorithms.SelectionSort();
    public SortingAlgorithm insertion = new MySortingAlgorithms.InsertionSort();
    public SortingAlgorithm merge = new MySortingAlgorithms.MergeSort();
    public SortingAlgorithm LSD = new MySortingAlgorithms.LSDSort();


    int[] array = new int[] {32, 15, 2, 17, 19, 26, 41, 17, 17};
    int[] result = new int[] {2, 15, 17, 17, 17, 19, 26, 32, 41};

    @Test
    public void testSelection() {
        int[] test = new int[array.length];
        System.arraycopy(array, 0, test, 0, array.length);
        selection.sort(test, 9);
        for (int i = 0; i < test.length; i += 1) {
            assertEquals(String.format("item at index %s should be %s", i, result[i]), test[i], result[i]);
        }
    }

    @Test
    public void testInsertion() {
        int[] test = new int[array.length];
        System.arraycopy(array, 0, test, 0, array.length);
        insertion.sort(test, 9);
        for (int i = 0; i < test.length; i += 1) {
            assertEquals(String.format("item at index %s should be %s", i, result[i]), test[i], result[i]);
        }
    }

    @Test
    public void testMerge() {
        int[] test = new int[array.length];
        System.arraycopy(array, 0, test, 0, array.length);
        merge.sort(test, 9);
        for (int i = 0; i < test.length; i += 1) {
            assertEquals(String.format("item at index %s should be %s", i, result[i]), test[i], result[i]);
        }
    }
}
