import static org.junit.Assert.*;
import org.junit.*;
import java.util.Random;


public class ConvexEnclosementTest {

    int[] x,y;
    ConvexEnclosement ce;
    ConvexEnclosement.StraightLine line;

    @Before
    public void setUp() {
        y = new int[] {1,2,3,4,8,6,7,8,9,10};
        x = new int[] {1,2,3,4,5,6,7,8,9,10};
        ce = new ConvexEnclosement(x,y);
        line = ce.new StraightLine(0, 9);
    }

    @Test
    public void testConstructor() {
        assertTrue("failure, is not instance of ConvexEclosement.",
                   ce instanceof ConvexEnclosement);
        int[] expectedX = new int[] {1,2,3,4,5,6,7,8,9,10};
        int[] expectedY = new int[] {1,2,3,4,8,6,7,8,9,10};
        assertArrayEquals("failure, instance variable x fail.",
                          expectedX, x);
        assertArrayEquals("failure, instance variable y fail.",
                          expectedY, y);
    }
    @Test
    public void testsetMinMax() {
        ce.setMinMax();
        assertEquals("failure, MIN_X fail.",
                     1, ce.MIN_X);
        assertEquals("failure, MAN_X fail.",
                     10, ce.MAX_X);
        assertEquals("failure, MIN_Y fail.",
                     1, ce.MIN_X);
        assertEquals("failure, MAN_Y fail.",
                     10, ce.MAX_Y);
    }

    @Test
    public void testpointdistance() {
        int distance1 = ce.pointDistance(0, 9);
        int distance2 = ce.pointDistance(1, 9);
        int distance3 = ce.pointDistance(0, 1);
        int distance4 = ce.pointDistance(0, 2);
        int distance5 = ce.pointDistance(0, 5);
        assertEquals("failure, distance1 measuremnt fail.",
                     162, distance1);
        assertEquals("failure, distance2 measuremnt fail.",
                     128, distance2);
        assertEquals("failure, distance3 measuremnt fail.",
                     2, distance3);
        assertEquals("failure, distance4 measuremnt fail.",
                     8, distance4);
        assertEquals("failure, distance5 measuremnt fail.",
                     50, distance5);
    }

    /*
     * StarighLine
     */

    @Test
    public void testStraightLine() {
        assertEquals("failure, instance variable p1 fail.",
                     0, line.p1);
        assertEquals("failure, instance variable p2 fail.",
                     9, line.p2);
        assertEquals("failure, instance variable a fail.",
                     -9, line.a);
        assertEquals("failure, instance variable b fail.",
                     9, line.b);
        assertEquals("failure, instance variable c fail.",
                     0, line.c);
    }

    @Test
    public void testdistanceToPoint() {
        assertEquals("failure, distance to point 4 from main line fail.",
                     -45 + 72, line.distanceToPoint(4));
        assertEquals("failure, distance to point 9 from main line fail.",
                     0, line.distanceToPoint(9));
        assertEquals("failure, distance to point 0 from main line fail.",
                     0, line.distanceToPoint(0));
    }


    @Test
    public void testconvexEncloseMaxSimple() {
        PointList enclosure = ce.encloseMax();
        int[] expected = new int[] {9,4,0,1,2,3,5,6,7,8,0,0,0,0,0,0,0,0};
        assertArrayEquals("failure, convexEnclosure fail.",
                          expected, enclosure.data);
    }

    @Test
    public void testconvexEncloseMaxIntermediate() {
        x = new int[] {3,5,8,1,6};
        y = new int[] {0,6,0,4,7};
        int[] minMaxInd = MathUtil.getMinMaxIndicies(x, 0, x.length);
        int[] expectedInd = new int[] {3, 2};
        assertArrayEquals("failure, wrong minmax indicies in intermediate tests.",
                     expectedInd, minMaxInd);
        ce = new ConvexEnclosement(x, y);
        PointList enclosure = ce.encloseMax();
        int[] expected = new int[] {2,4,3,0};
        assertArrayEquals("failure, convexEnclosure fail.",
                          expected, enclosure.data);
    }

    @Test
    public void testconvexEncloseMaxAdvanced() {
        int size = 1000;
        x = new int[size];
        y = new int[size];
        Random r = new Random(1234);
        for (int i = 0; i < size; i++) {
            int nextInt = r.nextInt(900);
            if (nextInt < 100) {
                nextInt += 100;
            }
            x[i] = nextInt;
        }
        for (int i = 0; i < size; i++) {
            int nextInt = r.nextInt(900);
            if (nextInt < 100) {
                nextInt += 100;
            }
            y[i] = nextInt;
        }

        int[] expected = new int[63];
        expected[0] = 3;
        expected[1] = 542;
        expected[2] = 374;
        expected[3] = 865;
        expected[4] = 345;
        x[3] = 975;
        y[3] = 912;
        x[542] = 67;
        y[542] = 945;
        x[374] = 11;
        y[374] = 37;
        x[865] = 975;
        y[865] = 12;
        x[345] = 975;
        y[345] = 68;

        ce = new ConvexEnclosement(x, y, false);
        PointList result = ce.encloseMax();
        assertArrayEquals("failure, could not enclose advanced points.",
                     expected, result.data);
    }
}
