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
        if (buckets[index] == null) {
            buckets[index] = new ArrayList<>();
            ArrayList<String> temp = new ArrayList<>();
            temp.add(s);
            buckets[index] = temp;
        } else {
            buckets[index].add(s);
        }
    }

    @Override
    public boolean contains(String s) {
        int index = Math.floorMod(s.hashCode(), numBuckets);
        if (buckets[index] != null) {
            for (String i : buckets[index]) {
                if (i.equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> result = new ArrayList<>();
        for (ArrayList<String> b: buckets) {
            if (b != null) {
                result.addAll(b);
            }
        }
        return result;
    }

    /** Load Factor. */
    private int loadFactor() {
        return Math.floorDiv(elements, numBuckets);
    }

    /** Resize by a factor of 2 once load factor is exceeded. */
    @SuppressWarnings("unchecked")
    private void resize() {
        numBuckets = elements;
        ArrayList<String>[] dup = new ArrayList[numBuckets];
        for (ArrayList<String> b : buckets) {
            if (b != null) {
                for (String s: b) {
                    int index = Math.floorMod(s.hashCode(), numBuckets);
                    if (dup[index] == null) {
                        dup[index] = new ArrayList<>();
                        ArrayList<String> temp = new ArrayList<>();
                        temp.add(s);
                        dup[index] = temp;
                    } else {
                        dup[index].add(s);
                    }
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
    @SuppressWarnings("unchecked")
    private ArrayList<String>[] buckets = new ArrayList[4];
}
