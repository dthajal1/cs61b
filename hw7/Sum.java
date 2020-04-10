import java.util.HashSet;

/** HW #7, Two-sum problem.
 * @author Diraj Thajali
 */
public class Sum {
    public static SortingAlgorithm a = new MySortingAlgorithms.QuickSort();

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        HashSet<Integer> first = new HashSet<>();
        for (int i : B) {
            first.add(i);
        }
        HashSet<Integer> seen = new HashSet<>();
        for (int value : A) {
            int needed = m - value;
            if (first.contains(needed)) {
                return true;
            }
        }
        return false;  // REPLACE WITH YOUR ANSWER
    }

}
