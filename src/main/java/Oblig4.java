// /src/main/java/Oblig4.java
import java.util.*;

public class Oblig4 {
    public static void main(String args[]) {
        final Map<String, List<String>> params = new HashMap<>();
        List<String> options = null;
        for (int i = 0; i < args.length; i++) {
            final String a = args[i];

            if (a.charAt(0) == '-') {
                if (a.length() < 2) {
                    System.err.println("Error at argument " + a);
                    return;
                }

                options = new ArrayList<>();
                params.put(a.substring(1), options);
            }
            else if (options != null) {
                options.add(a);
            }
            else {
                System.err.println("Illegal parameter usage");
                return;
            }
        }
        try {
            int n;
            int threadCount = Runtime.getRuntime().availableProcessors();
            String mode = "seq";
            boolean draw = false;
            boolean benchmark = false;
            boolean debug = false;
            boolean printEnclosure = false;
            try {
                n = Integer.parseInt(params.get("n").get(0));
            }
            catch (Throwable e) {
                System.err.printf("Error. Parameter -n must be specified.%n");
                return;
            }

            try {
                threadCount = Integer.parseInt(params.get("I").get(0));
            }
            catch (IndexOutOfBoundsException | NullPointerException e) {
                threadCount = Runtime.getRuntime().availableProcessors();
            }
            try {
                mode = params.get("m").get(0);
            }
            catch (IndexOutOfBoundsException | NullPointerException e) {
                mode = "seq";
            }
            try {
                if(params.get("dr") != null) draw = true;
            }
            catch (IndexOutOfBoundsException | NullPointerException e) {
                draw = false;
            }
            try {
                if(params.get("b") != null) benchmark = true;
            }
            catch (IndexOutOfBoundsException | NullPointerException e) {
                benchmark = false;
            }
            try {
                if(params.get("d") != null) debug = true;
            }
            catch (Exception e) {
                debug = false;
            }


            // Start of program.

            int[] x = new int[n], y = new int[n];
            NPunkter17 p = new NPunkter17(n);
            p.fyllArrayer(x, y);

            ConvexEnclosement ceSeq = null;
            PointList enclosementSeq = null;
            TegnUt tegnUtSeq;
            long startTime = 0, endTime = 0;
            if(mode.equals("seq") || mode.equals("both")) {
                if (benchmark) {
                    int iterations;
                    try {
                        iterations = Integer.parseInt(params.get("b").get(0));
                    }
                    catch (Throwable e) {
                        iterations = 3;
                    }

                    double[] timeResult = new double[iterations];
                    for (int i = 0; i < iterations; i++) {
                        startTime = System.nanoTime();
                        ceSeq = new ConvexEnclosement(x, y, debug);
                        enclosementSeq = ceSeq.encloseMax();
                        timeResult[i] = (double)(System.nanoTime() - startTime) / 1000000.0;
                    }
                    Arrays.sort(timeResult);
                    System.out.printf ("Sequential enclosure took %f ms for n = %d with %d iterations (median).%n",
                                       timeResult[iterations/2], n, iterations);
                } else {
                    ceSeq = new ConvexEnclosement(x, y, debug);
                    enclosementSeq = ceSeq.encloseMax();
                }
            }

            ConvexEnclosementPara cePara = null;
            PointList enclosementPara = null;
            TegnUt tegnUtPara;
            if(mode.equals("para") || mode.equals("both")) {
                if (benchmark) {
                    int iterations;
                    try {
                        iterations = Integer.parseInt(params.get("b").get(0));
                    }
                    catch (Throwable e) {
                        iterations = 3;
                    }

                    double[] timeResult = new double[iterations];
                    for (int i = 0; i < iterations; i++) {
                        startTime = System.nanoTime();
                        cePara = new ConvexEnclosementPara(x, y, threadCount, debug);
                        enclosementPara = cePara.encloseMax();
                        timeResult[i] = (double)(System.nanoTime() - startTime) / 1000000.0;
                    }
                    Arrays.sort(timeResult);
                    System.out.printf ("Parallel enclosure took %f ms for n = %d with %d iterations (median) and %d threads.%n",
                                       timeResult[iterations/2], n, iterations, threadCount);
                } else {
                    cePara = new ConvexEnclosementPara(x, y, threadCount, debug);
                    enclosementPara = cePara.encloseMax();
                }


                // Common execution.

                // Debug check.
                if(debug && (mode.equals("seq") || mode.equals("both"))) {
                    System.out.printf ("Size Seq = %d.%n",
                                       enclosementSeq.size());
                    System.out.println ("Prining upto 1000 points of sequential enclosure:");
                    for (int i = 0; i < Math.min(enclosementSeq.size(), 1000); i++) {
                        System.out.printf ("P%d = %d, ", i, enclosementSeq.get(i));
                    }
                    System.out.println ("");
                    System.out.println ("Done printing Sequential Enclosure.");

                }
                if(debug && (mode.equals("para") || mode.equals("both"))) {
                    System.out.printf ("Size Para = %d.%n",
                                       enclosementPara.size());
                    System.out.println ("Prining upto 1000 points of parallel enclosure:");
                    for (int i = 0; i < Math.min(enclosementPara.size(), 1000); i++) {
                        System.out.printf ("P%d = %d, ", i, enclosementPara.get(i));
                    }
                    System.out.println ("");
                    System.out.println ("Done printing Parallel Enclosure.");
                }

                // Drawing.
                if(draw && (mode.equals("seq") || mode.equals("both"))) {
                    ceSeq.setMinMax();
                    tegnUtSeq = new TegnUt(ceSeq, enclosementSeq, "Seq");
                }
                if(draw && (mode.equals("para") || mode.equals("both"))) {
                    cePara.setMinMax();
                    tegnUtPara = new TegnUt(cePara, enclosementPara, "Para");
                }
            }
        }
        catch (Exception e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
        }
    }
}
