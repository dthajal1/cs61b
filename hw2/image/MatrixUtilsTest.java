package image;

import org.junit.Test;
import static org.junit.Assert.*;

/** FIXME
 *  @author Diraj Thajali
 */

public class MatrixUtilsTest {
    /**
     * FIXME
     */
    @Test
    public void testAccumulateVertical() {
        double[][] b = new double[][]{
                {5, 6, 10, 11},
                {21, 3, 2, 16},
                {7, 0, 17, 18},
                {8, 1, 3, 5}
        };
        double[][] Bres = new double[][]{
                {5, 6, 10, 11},
                {26, 8, 8, 26},
                {15, 8, 25, 26},
                {16, 9, 11, 30}
        };

        assertArrayEquals(Bres, MatrixUtils.accumulateVertical(b));

        double[][] a = new double[][]{{1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000},
                {1000000, 29515, 38273, 1000000},
                {1000000, 73403, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000}};
        double[][] Ares = new double[][]{{1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}};

        assertArrayEquals(Ares, MatrixUtils.accumulateVertical(a));
}

    @Test
    public void testAccumulate(){
        double[][] a = new double[][]{{1000000, 1000000, 1000000, 1000000},
                {1000000, 75990, 30003, 1000000},
                {1000000, 30002, 103046, 1000000},
                {1000000, 29515, 38273, 1000000},
                {1000000, 73403, 35399, 1000000},
                {1000000, 1000000, 1000000, 1000000}};
        double[][] Ares = new double[][]{{1000000, 1000000, 1000000, 1000000},
                {2000000, 1075990, 1030003, 2000000},
                {2075990, 1060005, 1133049, 2030003},
                {2060005, 1089520, 1098278, 2133049},
                {2089520, 1162923, 1124919, 2098278},
                {2162923, 2124919, 2124919, 2124919}};

        assertArrayEquals(Ares, MatrixUtils.accumulate(a, MatrixUtils.Orientation.VERTICAL));

        double[][] b = new double[][]{
                {5, 6, 10, 11},
                {21, 3, 2, 16},
                {7, 0, 17, 18},
                {8, 1, 3, 5}
        };
        double[][] Bres = new double[][]{
                {5, 21, 7, 8},
                {11, 8, 7, 8},
                {18, 9, 24, 10},
                {20, 25, 27, 15}
        };

        assertArrayEquals(Bres, MatrixUtils.accumulate(b, MatrixUtils.Orientation.HORIZONTAL));
    }

    public static void main(String[] args) {
        System.exit(ucb.junit.textui.runClasses(MatrixUtilsTest.class));
    }
}
