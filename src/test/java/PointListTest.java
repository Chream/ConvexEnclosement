import static org.junit.Assert.*;
import org.junit.*;

public class PointListTest {


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
    public void testsortedAdd() {
        PointList list = new PointList(3);
        list.sortedAdd(line.p1, 1, x, y);
        list.sortedAdd(line.p1, 5, x, y);
        list.sortedAdd(line.p1, 2, x, y);
        int[] expected = new int[] {1,2,5};
        assertArrayEquals("failure, sortedAdd failure.",
                          expected, list.data);
    }
}
