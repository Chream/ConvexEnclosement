// /src/main/java/ConvexEnclosement.java
public class ConvexEnclosement {
    int [] x, y;
    int MAX_X, MIN_X, MAX_Y, MIN_Y, n;
    boolean debug = false;

    /* **************
     * Constructors *
     ************** */

    public ConvexEnclosement (int[] x, int[] y) {
        this.x = x;
        this.y = y;
        this.n = x.length;
    }

    public ConvexEnclosement (int[] x, int[] y, boolean debug) {
        this.x = x;
        this.y = y;
        this.n = x.length;
        this.debug = debug;
    }

    /* ***********
     * Interface *
     *********** */

    // Main entry mathod.
    public PointList encloseMax() {
        int minxPoint, maxxPoint;
        StraightLine line;
        int[] minmax = MathUtil.getMinMaxIndicies(x, 0, n);
        minxPoint = minmax[0];
        maxxPoint = minmax[1];
        // Create main collection object.
        // All enclosure points will be collected here.
        PointList enclosurePointList = new PointList((int)(2 * Math. sqrt(n)));
        PointList applicablePoints = new PointList(0);
        applicablePoints.createRangePointList(0, n);

        // Første halvdel til høyre for linjen.
        line = new StraightLine(maxxPoint, minxPoint);
        enclosurePointList.append(constructExtendedLineRight(line, applicablePoints));

        // Andre halvdel til venstre for linjen.
        line = new StraightLine(minxPoint, maxxPoint);
        enclosurePointList.append(constructExtendedLineRight(line, applicablePoints));

        return enclosurePointList;
    }

    // Set variables for graphical output.
    public void setMinMax() {
        int[] minxmaxx = MathUtil.getMinMaxValues(x, 0, n);
        int[] minymaxy = MathUtil.getMinMaxValues(y, 0, n);
        this.MIN_X = minxmaxx[0];
        this.MAX_X = minxmaxx[1];
        this.MIN_Y = minymaxy[0];
        this.MAX_Y = minymaxy[1];
    }

    public PointList constructExtendedLineRight(StraightLine line,
                                                PointList applicablePoints) {
        int furthestPoint = -1, distance, maxDistance = 0;
        PointList newPointLine = new PointList();
        PointList nextApplicablePoints = new PointList(applicablePoints.size() / 2);

        for (int i = 0; i < applicablePoints.size(); i++) {
            int pointNr = applicablePoints.get(i);
            if (pointNr == line.p2) continue;
            distance = line.distanceToPoint(pointNr);
            if (distance == 0 && furthestPoint == -1) {
                newPointLine.sortedAdd(line.p1, pointNr, x, y);
            } else if (distance < 0) {
                nextApplicablePoints.add(pointNr);
                if (distance < maxDistance) {
                    maxDistance = distance;
                    furthestPoint = pointNr;
                }
            }
        }
        if (furthestPoint == -1) {
            return newPointLine;
        } else {
            // Edge case for adding the original starting point.
            nextApplicablePoints.add(line.p1);
            StraightLine newLine1 = new StraightLine(line.p1, furthestPoint);
            StraightLine newLine2 = new StraightLine(furthestPoint, line.p2);
            PointList pointList1 =
                constructExtendedLineRight(newLine1, nextApplicablePoints);
            PointList pointList2 =
                constructExtendedLineRight(newLine2, nextApplicablePoints);
            pointList1.append(pointList2);
            return pointList1;
        }
    }

    public int pointDistance(int p1, int p2) {
        return MathUtil.pointDistance(p1, p2, x, y);
    }

    /* ****************
     * Nested Classes *
     **************** */

    class StraightLine {
        int a, b, c;
        int p1, p2;

        public StraightLine (int p1, int p2) {
            this.a = y[p1] - y[p2];
            this.b = x[p2] - x[p1];
            this.c = (y[p2] * x[p1]) - (y[p1] * x[p2]);
            this.p1 = p1;
            this.p2 = p2;
        }

        public int distanceToPoint(int p) {
            return a * x[p] + b * y[p] + c;
        }
    }

    /* **********
     * Printing *
     ********** */

    public void printPoints() {
        for (int i = 0; i < n; i++) {
            System.out.printf ("P%d=(%d,%d), ", i,  x[i], y[i]);
        }
        System.out.println ("");
    }
}
