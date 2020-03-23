import java.util.ArrayList;
import java.util.List;

/** A set of String values.
 *  @author Diraj Thajali
 */
class ECHashStringSet implements StringSet {

    ECHashStringSet() {
    }

    @Override
    public void put(String s) {
        if (loadFactor() > 5) {
            resize();
        }
        elements += 1;
        int index = Math.floorMod(s.hashCode(), numBuckets);
        ArrayList<String> temp = new ArrayList<>();
        temp.add(s);
        if (buckets[index] == null) {
            buckets[index] = new ArrayList<ArrayList<String>>();
        }
        buckets[index].add(temp);
    }

    @Override
    public boolean contains(String s) {
        int index = Math.floorMod(s.hashCode(), numBuckets);
        for (ArrayList<String> i: buckets[index]) {
            if (i.get(0).equals(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> result = new ArrayList<>();
        for (ArrayList<ArrayList<String>> b: buckets) {
            if (b != null) {
                for (ArrayList<String> s : b) {
                    for (String a : s) {
                        result.add(a);
                    }
                }
            }
        }
        return result;
    }

    /** Load Factor. */
    private double loadFactor() {
        return Math.floorDiv(elements, numBuckets);
    }

    /** Resize by a factor of 2 once load factor is exceeded. */
    private void resize() {
        numBuckets = numBuckets * 2;
        ArrayList<ArrayList<String>>[] dup = new ArrayList[numBuckets];
        for (ArrayList<ArrayList<String>> b : buckets) {
            if (b != null) {
                for (ArrayList<String> s : b) {
                    int hash = s.get(0).hashCode();
                    int index = Math.floorMod(hash, numBuckets);
                    ArrayList<String> temp = new ArrayList<>();
                    temp.add(s.get(0));
                    if (dup[index] == null) {
                        dup[index] = new ArrayList<ArrayList<String>>();
                    }
                    dup[index].add(temp);
                }
            }
        }
        buckets = dup;
    }

    /** Number of buckets */
    private int numBuckets = 4;

    /** Total number of elements. */
    private int elements = 0;

    /** Values to be stored. */
    private ArrayList<ArrayList<String>>[] buckets = new ArrayList[4];

    public static void main(String[] args) {
//        ArrayList<ArrayList<String>>[] a = new ArrayList[4];
//        ArrayList<ArrayList<String>>[] b = new ArrayList[8];
//        a[0].add(new ArrayList<>());
//        a= b;
//    }
    }
}
