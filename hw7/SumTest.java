import com.sun.xml.internal.xsom.impl.scd.Iterators;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

public class SumTest {
    private static final long RANDOM_SEED = 12345654321L;

    private static Random random = new Random(RANDOM_SEED);

    public static int[] randomArray(int N, int maxInt) {
        int[] result = new int[N];
        for (int i = 0; i < N; i += 1) {
            result[i] = random.nextInt(maxInt);
        }
        return result;
    }

    public static int[] randomAlmostSorted(int N, int maxInt) {
        int[] result = new int[N];
        for (int i = 0; i < N; i += 1) {
            result[i] = random.nextInt(maxInt);
        }
        Arrays.sort(result);

        for (int i = 0; i < N; i += 1) {
            int swapIndex = random.nextInt(N - 1);
            swap(result, swapIndex, swapIndex + 1);
        }
        return result;
    }

    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    @Test
    public void testTwoSums() {


    }
}
