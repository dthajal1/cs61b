import static org.junit.Assert.*;
import org.junit.Test;

public class CompoundInterestTest {

    @Test
    public void testNumYears() {
        /** Sample assert statement for comparing integers.

        assertEquals(0, 0); */
        assertEquals(2, CompoundInterest.numYears(2022));
        assertEquals(0, CompoundInterest.numYears(2020));
    }

    @Test
    public void testFutureValue() {
        double tolerance = 0.01;
        assertEquals(12.544, CompoundInterest.futureValue(10, 12.0, 2022), tolerance);

        assertEquals(20.988075, CompoundInterest.futureValue(12, 15, 2024), tolerance);
        /* negative appreciation*/
        assertEquals(7.744, CompoundInterest.futureValue(10, -12, 2022), tolerance);
    }

    @Test
    public void testFutureValueReal() {
        double tolerance = 0.01;
        assertEquals(10, CompoundInterest.futureValueReal(10, 12, 2020, 3), tolerance);

        assertEquals(11.8026496, CompoundInterest.futureValueReal(10, 12, 2022, 3), tolerance);

        assertEquals(7.286329599999999, CompoundInterest.futureValueReal(10, -12, 2022, 3), tolerance);
    }


    @Test
    public void testTotalSavings() {
        double tolerance = 0.01;
        assertEquals(5000, CompoundInterest.totalSavings(5000, 2020, 3), tolerance);

        assertEquals(16550, CompoundInterest.totalSavings(5000, 2022, 10), tolerance);

        assertEquals(13550, CompoundInterest.totalSavings(5000, 2022, -10), tolerance);
    }

    @Test
    public void testTotalSavingsReal() {
        double tolerance = 0.01;

        assertEquals(5000, CompoundInterest.totalSavingsReal(5000, 2020, 10, 10), tolerance);

        assertEquals(13405.5, CompoundInterest.totalSavingsReal(5000, 2022, 10, 10), tolerance);

        assertEquals(10975.5, CompoundInterest.totalSavingsReal(5000, 2022, -10, 10), tolerance);


    }


    /* Run the unit tests in this file. */
    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(CompoundInterestTest.class));
    }
}
