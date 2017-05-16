import static org.junit.Assert.*;
import org.junit.*;
import java.util.Random;

public class ConvexEnclosementParaTest {

    int[] simpleX, simpleY, largeX, largeY;
    int threadCount = 8;
    ConvexEnclosementPara ceSimplePara, ceLargePara;
    ConvexEnclosement ceSimpleSeq, ceLargeSeq;
    PointList expectedSimple;
    int n = 10000;

    @Before
    public void setUp() {
        simpleX = new int[] {0,6,1,5,3};
        simpleY = new int[] {5,1,1,5,3};

        largeX = new int[n];
        largeY = new int[n];
        Random r = new Random(1234);
        for (int i = 0; i < n; i++) {
            largeX[i] = r.nextInt(n);
            largeY[i] = r.nextInt(n);
        }

        expectedSimple = new PointList(4);
        expectedSimple.add(1);
        expectedSimple.add(3);
        expectedSimple.add(0);
        expectedSimple.add(2);
        ceSimplePara = new ConvexEnclosementPara(simpleX, simpleY, threadCount);
        ceSimpleSeq = new ConvexEnclosement(simpleX, simpleY);

        ceLargePara = new ConvexEnclosementPara(largeX, largeY, threadCount);
        ceLargeSeq = new ConvexEnclosement(largeX, largeY);

    }

    @Test
    public void testSimpleCompare() {
        PointList resultSeq = ceSimpleSeq.encloseMax();
        PointList resultPara = ceSimplePara.encloseMax();
        assertArrayEquals("failure, enclosure fail in simple seq test in para tests.",
                          expectedSimple.data, resultSeq.data);
        assertArrayEquals("failure, enclosure fail in simple parallel test.",
                          expectedSimple.data, resultPara.data);
        assertArrayEquals("failure, enclosure fail in simple parallel test.",
                          resultSeq.data, resultPara.data);
    }

    @Test
    public void testLargeCompareSeqPara() {
        PointList seqResult = ceLargeSeq.encloseMax();
        PointList paraResult = ceLargePara.encloseMax();
        assertArrayEquals("failure, seq and para not equal for large test. (many points)",
                          seqResult.data, paraResult.data);
    }
}
