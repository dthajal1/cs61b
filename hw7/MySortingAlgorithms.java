import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Note that every sorting algorithm takes in an argument k. The sorting 
 * algorithm should sort the array from index 0 to k. This argument could
 * be useful for some of your sorts.
 *
 * Class containing all the sorting algorithms from 61B to date.
 *
 * You may add any number instance variables and instance methods
 * to your Sorting Algorithm classes.
 *
 * You may also override the empty no-argument constructor, but please
 * only use the no-argument constructor for each of the Sorting
 * Algorithms, as that is what will be used for testing.
 *
 * Feel free to use any resources out there to write each sort,
 * including existing implementations on the web or from DSIJ.
 *
 * All implementations except Counting Sort adopted from Algorithms,
 * a textbook by Kevin Wayne and Bob Sedgewick. Their code does not
 * obey our style conventions.
 */
public class MySortingAlgorithms {

    /**
     * Java's Sorting Algorithm. Java uses Quicksort for ints.
     */
    public static class JavaSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            Arrays.sort(array, 0, k);
        }

        @Override
        public String toString() {
            return "Built-In Sort (uses quicksort for ints)";
        }
    }

    /** Insertion sorts the provided data. */
    public static class InsertionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            insertionSort(array, k, 0, 1);
        }

        private void insertionSort(int[] array, int k, int startWith, int count) {
            if (k <= count) {
                return;
            }
            if (array[startWith] > array[startWith + 1]) {
                swap(array, startWith, startWith + 1);
                for (int j = startWith; j > 0; j -=1) {
                    if (array[j] > array[j - 1]) {
                        break;
                    }
                    swap(array, j, j - 1);
                }
            }
            insertionSort(array, k, startWith + 1, count + 1);
        }


        @Override
        public String toString() {
            return "Insertion Sort";
        }
    }

    /**
     * Selection Sort for small K should be more efficient
     * than for larger K. You do not need to use a heap,
     * though if you want an extra challenge, feel free to
     * implement a heap based selection sort (i.e. heapsort).
     */
    public static class SelectionSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            selectionSort(array, k, 0, 1);
        }

        private void selectionSort(int[] array, int k, int swapWithIndex, int count) {
            if (k <= count) {
                return;
            }
            int minIndex = findMinIndex(array, k, swapWithIndex);
            swap(array, minIndex, swapWithIndex);
            selectionSort(array, k, swapWithIndex + 1, count + 1);
        }

        public int findMinIndex(int[] array, int to, int from) {
            int min = array[from];
            int result = from;
            for (int i = from; i < to; i += 1) {
                if (array[i] < min) {
                    result = i;
                    min = array[i];
                }
            }
            return result;
        }

        @Override
        public String toString() {
            return "Selection Sort";
        }
    }

    /** Your mergesort implementation. An iterative merge
      * method is easier to write than a recursive merge method.
      * Note: I'm only talking about the merge operation here,
      * not the entire algorithm, which is easier to do recursively.
      */
    public static class MergeSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            int[] tempArray = new int[array.length];
            sortHelper(array, tempArray, 0, k - 1);
        }

        private void sortHelper(int[] arr, int[] tempArr, int low, int high) {
            if (low >= high) {
                return;
            }
            int half = (low + high) / 2;
            sortHelper(arr, tempArr, low, half);
            sortHelper(arr, tempArr, half + 1, high);
            mergeSort(arr, tempArr, low, half, high);
        }

        private void mergeSort(int[] arr, int[] tempArr, int leftStart, int leftEnd, int rightEnd) {
            int left = leftStart;
            int right = leftEnd + 1;
            int index = leftStart;
            while (left <= leftEnd && right <= rightEnd) {
                if (arr[left] < arr[right]) {
                    tempArr[index] = arr[left];
                    left += 1;
                } else {
                    tempArr[index] = arr[right];
                    right += 1;
                }
                index += 1;
            }
            System.arraycopy(arr, left, tempArr, index, leftEnd - left + 1);
            System.arraycopy(arr, right, tempArr, index, rightEnd - right + 1);
            System.arraycopy(tempArr, leftStart, arr, leftStart, rightEnd - leftStart + 1);
        }

        @Override
        public String toString() {
            return "Merge Sort";
        }
    }

    /**
     * Your Counting Sort implementation.
     * You should create a count array that is the
     * same size as the value of the max digit in the array.
     */
    public static class CountingSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME: to be implemented
        }

        // may want to add additional methods

        @Override
        public String toString() {
            return "Counting Sort";
        }
    }

    /** Your Heapsort implementation.
     */
    public static class HeapSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Heap Sort";
        }
    }

    /** Your Quicksort implementation.
     */
    public static class QuickSort implements SortingAlgorithm {
        @Override
        public void sort(int[] array, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "Quicksort";
        }
    }

    /* For radix sorts, treat the integers as strings of x-bit numbers.  For
     * example, if you take x to be 2, then the least significant digit of
     * 25 (= 11001 in binary) would be 1 (01), the next least would be 2 (10)
     * and the third least would be 1.  The rest would be 0.  You can even take
     * x to be 1 and sort one bit at a time.  It might be interesting to see
     * how the times compare for various values of x. */

    /**
     * LSD Sort implementation.
     */
    public static class LSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            int W = 4;
            int R = 1 << 8;
            int mask = R - 1;
            int[] temp = new int[k];
            for (int d = 0; d < W; d++) {
                int[] count = new int[R + 1];
                for (int i = 0; i < k; i += 1) {
                    int c = (a[i] >> 8 * d) & mask;
                    count[c + 1] += 1;
                }
                for (int r = 0; r < R; r += 1)
                    count[r + 1] += count[r];
                if (d == W - 1) {
                    int shift1 = count[R] - count[R / 2];
                    int shift2 = count[R / 2];
                    for (int r = 0; r < R / 2; r++)
                        count[r] += shift1;
                    for (int r = R / 2; r < R; r++)
                        count[r] -= shift2;
                }
                for (int i = 0; i < k; i++) {
                    int c = (a[i] >> 8 * d) & mask;
                    temp[count[c]++] = a[i];
                }
                System.arraycopy(temp, 0, a, 0, k);
            }
        }

        @Override
        public String toString() {
            return "LSD Sort";
        }
    }

    /**
     * MSD Sort implementation.
     */
    public static class MSDSort implements SortingAlgorithm {
        @Override
        public void sort(int[] a, int k) {
            // FIXME
        }

        @Override
        public String toString() {
            return "MSD Sort";
        }
    }

    /** Exchange A[I] and A[J]. */
    private static void swap(int[] a, int i, int j) {
        int swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }

}
