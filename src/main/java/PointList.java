import java.util.stream.IntStream;

public class PointList extends IntList {

    public PointList () {
        super();
    }
    public PointList (int len) {
        super(len);
    }

    public void sortedAdd(int comparePoint, int newPoint, int[] x, int[] y) {
        if (size() + 1 > data.length) extendList();
        int i;
        for (i = size() - 1; i >= 0; i--) {
            if (MathUtil.pointDistance(comparePoint, newPoint, x, y) >=
                MathUtil.pointDistance(comparePoint, data [i], x, y)) {
                break;
            }
            data[i + 1] = data[i];
        }
        data[i + 1] = newPoint;
        len++;
    }

    public void createRangePointList(int start, int end) {
        int[] arr = IntStream.range(start, end).toArray();
        data = arr;
        len = data.length;
    }
}
