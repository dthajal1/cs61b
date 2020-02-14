package arrays;

/* NOTE: The file Arrays/Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2 */

/** Array utilities.
 *  @author
 */
class Arrays {

    /*
    Just for testing purpose
     */
    public static void main(String[] args) {
        int[] a = new int[] {1, 2, 3};
        int[][] result = new int[8][];
        result[0] = a;
        System.out.println(result[0]);

    }

    /* C1. */
    /** Returns a new array consisting of the elements of A followed by the
     *  the elements of B. */
    static int[] catenate(int[] A, int[] B) {
        /* *Replace this body with the solution. */
        int[] result = new int[A.length + B.length];
        for (int i = 0; i < A.length; i += 1) {
            result[i] = A[i];
        }
        for (int j = 0; j < B.length; j += 1) {
            result[A.length + j] = B[j];
        }
        return result;
    }

    /* C2. */
    /** Returns the array formed by removing LEN items from A,
     *  beginning with item #START. */
    static int[] remove(int[] A, int start, int len) {
        /* *Replace this body with the solution. */
        int[] result = new int[A.length - len];
        int i = 0;
        int count = 0;
        while (i < A.length) {
            if (A[i] == start) {
                while (len > 0) {
                    len -= 1;
                    i += 1;
                }
            } else {
                result[count] = A[i];
                count += 1;
                i += 1;
            }
        }
        return result;
    }

    /* C3. */
    /** Returns the array of arrays formed by breaking up A into
     *  maximal ascending lists, without reordering.
     *  For example, if A is {1, 3, 7, 5, 4, 6, 9, 10}, then
     *  returns the three-element array
     *  {{1, 3, 7}, {5}, {4, 6, 9, 10}}. */
    static int[][] naturalRuns(int[] A) {
        /* *Replace this body with the solution. */
        if (A == null) {
            return new int[][]{};
        }
        int counter = 0;
        for (int i = 0; i < A.length - 1; i += 1) {
            if (A[i] >= A[i + 1]) {
                counter += 1;
            }
        }
        int[][] result = new int[counter + 1][];
        int count = 0;
        int i = 0;
        int[] ans = new int[]{A[0]};
        while (i < A.length - 1) {
            if (A[i] < A[i + 1]) {
                ans = catenate(ans, new int[]{A[i + 1]});
                i += 1;
            } else {
                result[count] = ans;
                ans = new int[]{A[i + 1]};
                i += 1;
                count += 1;
            }
        }
        result[count] = ans;
        return result;
    }
}
