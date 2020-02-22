/** Functions to increment and sum the elements of a WeirdList. */
class WeirdListClient {

    /** Return the result of adding N to each element of L. */
    static WeirdList add(WeirdList L, int n) {
        Add renaldo = new Add(n);
        return L.map(renaldo);
    }

    /** Return the sum of all the elements in L. */
    static int sum(WeirdList L) {
        Add weirdSum = new Add(0);
        L.map(weirdSum);
        return weirdSum.total;
    }



//
    private static class Add implements IntUnaryFunction {
        int N;
        int total;
        public Add(int n) {
            N = n;
            total = 0;
        }

        public int apply(int x) {
            total += x;
            return x + N;
        }

    }


    /* IMPORTANT: YOU ARE NOT ALLOWED TO USE RECURSION IN ADD AND SUM
     *
     * As with WeirdList, you'll need to add an additional class or
     * perhaps more for WeirdListClient to work. Again, you may put
     * those classes either inside WeirdListClient as private static
     * classes, or in their own separate files.

     * You are still forbidden to use any of the following:
     *       if, switch, while, for, do, try, or the ?: operator.
     *
     * HINT: Try checking out the IntUnaryFunction interface.
     *       Can we use it somehow?
     */
}

