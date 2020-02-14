package lists;

/* NOTE: The file Utils.java contains some functions that may be useful
 * in testing your answers. */

/** HW #2, Problem #1. */

/** List problem.
 *  @author
 */
class Lists {
    /*
    Just for testing purposes
     */
//    public static void main(String[] args){
////        IntList j = new IntList(1, null);
////        int[][] in = new int[][] {{1,2}, {3, 4}};
////        IntListList L = IntListList.list(in);
////        System.out.println(L.head);
////        System.out.println(L.tail);
//        int[] res = new int[] {1, 3, 7, 5, 4, 6, 9};
//        IntList L = IntList.list(res);
//        System.out.println(naturalRuns(L));
//
//    }

    /* B. */
    /** Return the list of lists formed by breaking up L into "natural runs":
     *  that is, maximal strictly ascending sublists, in the same order as
     *  the original.  For example, if L is (1, 3, 7, 5, 4, 6, 9, 10, 10, 11),
     *  then result is the four-item list
     *            ((1, 3, 7), (5), (4, 6, 9, 10), (10, 11)).
     *  Destructive: creates no new IntList items, and may modify the
     *  original list pointed to by L. */
    static IntListList naturalRuns(IntList L) {
        /* *Replace this body with the solution. */
        if (L == null || L.tail == null) {
            return IntListList.list(L);
        }
        IntList fPointer = L;
        IntList sPointer = L;
        IntListList newL = new IntListList();
        IntListList newLPointer = newL;
        while (fPointer.tail.tail != null) {
            if (fPointer == sPointer) {
                sPointer = sPointer.tail;
            }
            if (fPointer.head < fPointer.tail.head) {
                fPointer = fPointer.tail;
                sPointer = sPointer.tail;
            } else {
                newLPointer.head = L;
                newLPointer.tail = new IntListList();
                newLPointer = newLPointer.tail;
                IntList point = fPointer;
                L = sPointer;
                sPointer = sPointer.tail;
                fPointer = fPointer.tail;
                point.tail = null;
            }
        }
        if (fPointer.head < sPointer.head) {
            fPointer = fPointer.tail;
            newLPointer.head = L;
        } else {
            newLPointer.head = L;
            newLPointer.tail = new IntListList();
            newLPointer = newLPointer.tail;
            fPointer.tail = null;
            L = sPointer;
            newLPointer.head = L;
        }
        return newL;
    }
}

