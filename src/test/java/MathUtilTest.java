import static org.junit.Assert.*;
import org.junit.*;

public class MathUtilTest  {
    int[] x, y;

    @Before
    public void classInit() {
        x = new int [] {5,7,3,9,6,10,6,3,5,7};
        y = new int [] {2,0,4,10,6,3,8,5,6,2};
    }

    @Test
    public void testInit() {
        int[] expectedX = new int[] {5,7,3,9,6,10,6,3,5,7};
        int[] expectedY = new int[] {2,0,4,10,6,3,8,5,6,2};
        assertArrayEquals("failure, arrays not properly initialized.",
                          expectedX, x);
        assertArrayEquals("failure, arrays not properly initialized.",
                          expectedY, y);
    }

    @Test
    public void testFindMinMaxIndicies() {
        int[] minMaxPointsX = MathUtil.getMinMaxIndicies(x, 0, x.length);
        int[] minMaxPointsY = MathUtil.getMinMaxIndicies(y, 0, y.length);
        int[] expectedXInd = new int[] {2, 5};
        int[] expectedYInd = new int[] {1, 3};
        assertArrayEquals(expectedXInd, minMaxPointsX);
        assertArrayEquals(expectedYInd, minMaxPointsY);
    }

    @Test
    public void testFindMinMaxValues() {
        int[] minMaxValuesX = MathUtil.getMinMaxValues(x, 0, x.length);
        int[] minMaxValuesY = MathUtil.getMinMaxValues(y, 0, y.length);

        int[] expectedXVal = new int[] {3, 10};
        int[] expectedYVal = new int[] {0, 10};

        assertArrayEquals(expectedXVal, minMaxValuesX);
        assertArrayEquals(expectedYVal, minMaxValuesY);

        minMaxValuesX = MathUtil.getMinMaxValues(x, 0, x.length);
        minMaxValuesY = MathUtil.getMinMaxValues(y, 0, y.length);
        assertArrayEquals(expectedXVal, minMaxValuesX);
        assertArrayEquals(expectedYVal, minMaxValuesY);
    }
}
