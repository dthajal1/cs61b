public class HW0 {
    /* Returns the max value given the non-empty array using for loops.*/
    public static int max(int[] a) {
        int maxVal = a[0];
        for (int i = 0; i < a.length; i +=1) {
            if (a[i] > maxVal)
                maxVal = a[i];
        }
        return maxVal;
    }
    /* Returns the max value given the non-empty array using while loops.*/
    public static int whileMax(int[] a) {
        int maxVal = a[0];
        int counter = 0;
        while (counter < a.length) {
            if (a[counter] > maxVal)
                maxVal = a[counter];
            counter += 1;
        }
        return maxVal;
    }
    /* Returns true if there are three integers (not necessarily distinct) that sum up to zero. */
    public static boolean threeSum(int[] a) {
        for (int i = 0; i < a.length; i +=1) {
            for (int j= 0; j < a.length; j +=1) {
                for (int k=0; k < a.length; k+=1) {
                    if (a[i]+a[j]+a[k] == 0)
                        return true;
                }
            }
        }
        return false;
    }
    /* Returns true if there are three distinct that sum up to zero */
    public static boolean threeSumDistinct(int[] a) {
        for (int i = 0; i < a.length; i +=1) {
            for (int j= 0; j < a.length; j +=1) {
                for (int k=0; k < a.length; k+=1) {
                    if (a[i]+a[j]+a[k] == 0 && a[i] != a[j] && a[j] != a[k])
                        return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        //call the max function to see if it returns the value that we want
        int [] numbers = new int[]{-6, 2, 4};
        System.out.println(max(numbers));
        System.out.println(whileMax(numbers));
        System.out.println(threeSumDistinct(numbers));
        System.out.println(threeSum(numbers));
    }
}