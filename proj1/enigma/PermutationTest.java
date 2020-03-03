package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Diraj Thajali
 */
public class PermutationTest {

    /** @return a Permutation with cycles as its cycles and alphabet as
     * its alphabet. */
    public Permutation getNewPermutation(String cycles, Alphabet alphabet) {
        return new Permutation(cycles, alphabet);
    }

    /** @return an Alphabet with chars as its characters. */
    public Alphabet getNewAlphabet(String chars) {
        return new Alphabet(chars);
    }
    /** @return a default Alphabet with characters ABCD...Z. */
    public Alphabet getNewAlphabet() {
        return new Alphabet();
    }


    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testPermute() {
        Permutation p = getNewPermutation("(BACD)(EFG)(I)", getNewAlphabet("ABCDEFGHI"));
        assertEquals('A', p.permute('B'));
        assertEquals('B', p.permute('D'));
        assertEquals('H', p.permute('H'));
        assertEquals('E', p.permute('G'));
        assertEquals('I', p.permute('I'));
        assertEquals(2, p.permute(0));
        assertEquals(1, p.permute(3));
        assertEquals(4, p.permute(6));
        assertEquals(7, p.permute(7));
        assertEquals(8, p.permute(8));
        assertEquals( 3, p.permute(11));
        assertEquals(8, p.permute(-1));
        assertEquals(7, p.permute(-2));
        assertEquals(5, p.permute(-5));
    }

    @Test
    public void testSize() {
        Alphabet alpha = getNewAlphabet("ABCD");
        assertEquals(4, alpha.size());
        Alphabet a = getNewAlphabet("");
        assertEquals(0, a.size());
    }

    @Test
    public void testInvert() {
        Permutation per = getNewPermutation("(DARBC)  (J)", getNewAlphabet("ABCDRFJ"));
        assertEquals('B', per.invert('C'));
        assertEquals('C', per.invert('D'));
        assertEquals('F', per.invert('F'));
        assertEquals('J', per.invert('J'));
        assertEquals('A', per.invert('R'));
        assertEquals(1, per.invert(2));
        assertEquals(5, per.invert(5));
        assertEquals(6, per.invert(6));
        assertEquals(4, per.invert(1));
        assertEquals(5, per.invert(12));
        assertEquals(4, per.invert(8));
        assertEquals(6, per.invert(-1));
        assertEquals(1, per.invert(-5));
        assertEquals(6, per.invert(-8));

    }

    @Test
    public void testDerangement() {
        Alphabet a = getNewAlphabet("ABCDE");
        Permutation p = getNewPermutation("(ABC) (DE)", a);
        assertEquals(true, p.derangement());
        Alphabet b = getNewAlphabet("ABCDEF");
        Permutation q = getNewPermutation("(ABC) (DE)", b);
        assertEquals(false, q.derangement());
        Alphabet c = getNewAlphabet("ABCDEF");
        Permutation r = getNewPermutation("(ABC) (DE) (F)", c);
        assertEquals(false, r.derangement());



    }

//    @Test
//    public void testCheckPerm() {
//        Alphabet alpha = getNewAlphabet("ABCD");
//        Permutation perm = getNewPermutation("(AB) (CD)", alpha);
//        checkPerm("abc", "ABCD", "BADC");
//    }


}
