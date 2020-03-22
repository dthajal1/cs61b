import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

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
}
