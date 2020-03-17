import org.junit.Test;
import static org.junit.Assert.*;

/** Tests of BitExercise
 *  @author Zoe Plaxco
 */
public class BitExerciseTest {

    @Test
    public void testLastBit() {
        int two = BitExercise.lastBit(6);
        assertEquals(2, two);
        int four = BitExercise.lastBit(100);
        assertEquals(4, four);
    }

    @Test
    public void testPowerOfTwo() {
        boolean powOfTwo = BitExercise.powerOfTwo(32);
        assertTrue(powOfTwo);
        boolean notPower = BitExercise.powerOfTwo(7);
        assertFalse(notPower);
        boolean a = BitExercise.powerOfTwo(0);
        assertFalse(a);
        boolean d = BitExercise.powerOfTwo(-1);
        assertFalse(d);
        boolean b = BitExercise.powerOfTwo(1);
        assertTrue(b);
        boolean c = BitExercise.powerOfTwo(6);
        assertFalse(c);
    }

    @Test
    public void testAbsolute() {
        int seven = BitExercise.absolute(-7);
        assertEquals(7, seven);
        int hundred = BitExercise.absolute(100);
        assertEquals(100, hundred);
        int negative = BitExercise.absolute(-100);
        assertEquals(100, negative);
        int zero = BitExercise.absolute(0);
        assertEquals(0,zero);
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(BitExerciseTest.class));
    }
}

