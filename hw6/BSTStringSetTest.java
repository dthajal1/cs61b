import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Test of a BST-based String Set.
 * @author Diraj Thajali
 */
public class BSTStringSetTest  {

    @Test
    public void testBSTStringSet() {
        BSTStringSet b = new BSTStringSet();
        b.put("dog");
        b.put("cat");
        b.put("elf");
        b.put("ant");
        b.put("bird");
        assertTrue(b.contains("dog"));
        assertTrue(b.contains("elf"));
        assertFalse(b.contains("animal"));
        List<String> result = new ArrayList<>();
        result.add("ant");
        result.add("bird");
        result.add("cat");
        result.add("dog");
        result.add("elf");
        assertEquals(result, b.asList());
    }

    @Test
    public void testRandom() {
        BSTStringSet a = new BSTStringSet();
        TreeSet<String> b = new TreeSet<>();
        for (int i = 0; i < 10000000; i += 1) {
            String t = StringUtils.randomString(3);
            b.add(t);
            a.put(t);
        }

        for (String s : b) {
            assertTrue(a.contains(s));
        }
    }
}
