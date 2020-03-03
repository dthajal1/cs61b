package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Diraj Thajali
 */
public class MovingRotorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                         ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                         ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS, with given NOTCHES. */
    private void setRotor(String name, HashMap<String, String> rotors,
                          String notches) {
        rotor = new MovingRotor(name, new Permutation(rotors.get(name), UPPER),
                                notches);
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA, "");
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
    }

    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA, "");
        rotor.advance();
        checkRotor("Rotor I advanced", UPPER_STRING, NAVALB_MAP.get("I"));
    }

    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA, "");
        rotor.set(25);
        checkRotor("Rotor I set", UPPER_STRING, NAVALZ_MAP.get("I"));
    }

    @Test
    public void checkMovingRotor() {
        Alphabet al = new Alphabet("ABCDEFGH");
        Permutation p = new Permutation("(ABC) (DEF) (GH)", al);
        MovingRotor a = new MovingRotor("I am a Moving Rotor!", p, "C");
        assertEquals(6, a.convertForward(7));
        assertEquals(0, a.convertBackward(1));
        a.advance();
        assertEquals(0, a.convertForward(7));
        assertEquals(0, a.convertBackward(1));
        assertFalse(a.atNotch());
        a.advance();
        assertTrue(a.atNotch());
        a.advance();
        assertFalse(a.atNotch());
        assertTrue(a.rotates());
        a.set('H');
        a.advance();
        a.advance();
        assertFalse(a.atNotch());
        a.advance();
        assertTrue(a.atNotch());
        a.set(2);
        assertTrue(a.atNotch());
        assertFalse(a.reflecting());
        assertEquals(2, a.convertForward(1));
        assertEquals(3, a.convertBackward(1));
        assertEquals(7, a.convertBackward(0));
    }

    @Test
    public void checkFixedRotor() {
        Alphabet al = new Alphabet("ABCDEF");
        Permutation p = new Permutation("(ABC) (DE) (F)", al);
        FixedRotor a = new FixedRotor("I am a Fixed Rotor!", p);
        assertFalse(a.rotates());
        assertFalse(a.reflecting());
        a.advance();
        assertFalse(a.atNotch());
        assertEquals(1, a.convertForward(0));
        assertEquals(0, a.convertBackward(7));
    }

    @Test
    public void checkReflector() {
        Alphabet al = new Alphabet("ABCDEF");
        Permutation p = new Permutation("(ABCD) (EF)", al);
        Reflector a = new Reflector("I am a Reflector!", p);
        assertFalse(a.rotates());
        assertTrue(a.reflecting());

    }
//    @Test(expected = EnigmaException.class)
//    public void testNotInAlphabet() {
//        Permutation p = getNewPermutation("(BACD)", getNewAlphabet("ABCD"));
//        p.invert('F');
//    }

}
