// /src/main/java/MathUtil.java

public class MathUtil {

    public static int[] getMinMaxIndicies(int[] a, int start, int end) {
        int maxInd = start, minInd = start;
        int maxVal = a[start], minVal = a[start];
        for(int i = start + 1; i < end; i++) {
            if (a[i] > maxVal) {
                maxInd = i;
                maxVal = a[i];
                continue;
            }
            if (a[i] < minVal) {
                minInd = i;
                minVal = a[i];
            }
        }
        int[] result = new int[2];
        result[0] = minInd;
        result[1] = maxInd;
        return result;
    }

    public static int[] getMinMaxValues(int[] a, int start, int end) {
        int max = a[0], min = a[0];

        for(int i = start; i < end; i++) {
            if (a[i] > max) {
                max = a[i];
                continue;
            }
            if (a[i] < min) {
                min = a[i];
            }
        }
        int[] result = new int[2];
        result[0] = min;
        result[1] = max;
        return result;
    }

    public static int pointDistance(int x1, int y1, int x2, int y2) {
        return (int)(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public static int pointDistance(int p1, int p2, int [] x, int[] y) {
        return MathUtil.pointDistance(x[p1], y[p1], x[p2], y[p2]);
    }
}
