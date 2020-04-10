/** HW #7, Two-sum problem.
 * @author Diraj Thajali
 */
public class Sum {

    /** Returns true iff A[i]+B[j] = M for some i and j. */
    public static boolean sumsTo(int[] A, int[] B, int m) {
        SortingAlgorithm a = new MySortingAlgorithms.QuickSort();
        a.sort(A, A.length);
        SortingAlgorithm b = new MySortingAlgorithms.QuickSort();
        a.sort(B, B.length);
        for (int first : A) {
            for (int second : B) {
                if (first + second == m) {
                    return true;
                }
            }
        }
        return false;
    }

}
