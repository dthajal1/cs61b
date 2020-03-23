import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * Test of a BST-based String Set.
 * @author Diraj Thajali
 */
public class ECHashStringSetTest  {

    @Test
    public void testSmallHashStringSet() {
        ECHashStringSet a = new ECHashStringSet();
        a.put("I");
        assertTrue(a.contains("I"));
        a.put("am");
        a.put("learn");
        a.put("ing");
        a.put("a");
        a.put("lot");
        assertTrue(a.contains("ing"));
        assertFalse(a.contains("bad"));
    }

    @Test
    public void testLargeHashStringSet() {
        TreeSet<String> b = new TreeSet<>();
        ECHashStringSet a = new ECHashStringSet();
        for (int i = 0; i < 1000000; i += 1) {
            String t = StringUtils.randomString(3);
            b.add(t);
            a.put(t);
        }

        for (String s : b) {
            assertTrue(a.contains(s));
        }
    }
}
