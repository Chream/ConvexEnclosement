public class ConvexEnclosementPara extends ConvexEnclosement {
    int threadCount;
    volatile int threadId = 0;
    boolean debug = false;

    public ConvexEnclosementPara(int[] x, int[] y, int threadCount) {
        super(x, y);
        this.threadCount = threadCount;
    }

    public ConvexEnclosementPara(int[] x, int[] y, int threadCount, boolean debug) {
        super(x, y, debug);
        this.threadCount = threadCount;
        this.debug = debug;
    }

    // Main entry mathod.
    @Override
    public PointList encloseMax() {
        int minxPoint, maxxPoint;
        StraightLine line;
        int[] minmax = MathUtil.getMinMaxIndicies(x, 0, n);
        minxPoint = minmax[0];
        maxxPoint = minmax[1];
        // Create main collection object.
        // All enclosure points will be collected here.
        PointList enclosurePointList = new PointList((int)(2 * Math. sqrt(n)));
        // Create collection for tracking how many points to search through.
        // This will be all points in this case.
        PointList applicablePoints = new PointList();
        applicablePoints.createRangePointList(0, n);

        /*
         * All threads are dependant an a previous thread.
         * This thread (prevThread) is passed to each new thread as they
         * are created. Each thread will wait until its previous thread
         * is done before appending its result to the the main container.
         */
        // Første halvdel til høyre for linjen.
        StraightLine line1 = new StraightLine(maxxPoint, minxPoint);
        Thread t1 =
            new Thread(new ConstructExtensionPointsWorker(line1, enclosurePointList,
                                                          applicablePoints, null, 2, threadId++));

        // Andre halvdel til venstre for linjen (Sett i samme retning).
        StraightLine line2 = new StraightLine(minxPoint, maxxPoint);
        Thread t2 =
            new Thread(new ConstructExtensionPointsWorker(line2, enclosurePointList,
                                                          applicablePoints, t1, 2, threadId++));

        t1.start();
        t2.start();

        // Wait for the last thread to complete.
        joinThread(t2);
        return enclosurePointList;
    }

    public static void joinThread(Thread t1) {
        try {
            t1.join();
        }
        catch (InterruptedException  e) {
            System.out.println("Error " + e.getMessage());
            e.printStackTrace();
            return;
        }
    }

    class ConstructExtensionPointsWorker implements Runnable {

        StraightLine line;
        PointList container;
        PointList applicablePoints;
        Thread prevThread;
        int level;
        int threadId;

        public ConstructExtensionPointsWorker(StraightLine line,
                                              PointList container,
                                              PointList applicablePoints,
                                              Thread prevThread,
                                              int level,
                                              int threadId) {
            this.line = line;
            this.container = container;
            this.prevThread = prevThread;
            this.applicablePoints = applicablePoints;
            this.level = level;
            this.threadId = threadId;
        }

        @Override
        public void run() {
            if (debug) {
                System.out.printf("Started thread %d on line (%d, %d) on level %d.%n",
                                  threadId, line.p1, line.p2, level);
            }
            constructExtendedLineRightPara(line, container, prevThread, level, applicablePoints);
        }

        public void constructExtendedLineRightPara(StraightLine line, PointList container,
                                                   Thread prevThread, int level,
                                                   PointList applicablePoints) {
            int furthestPoint = -1, distance, maxDistance = 0;
            PointList newPointLine = new PointList();
            PointList nextApplicablePoints = new PointList();

            /*
             * Collect all points on the current line (sorted from the start of the line).
             * Collect all points to the right of the line. (Used for the next recursion/thread)
             * If a further point (to the right) is found then save this and break.
             */
            for (int pointInd = 0; pointInd < applicablePoints.size(); pointInd++) {
                int pointNr = applicablePoints.get(pointInd);
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
                /*
                 * Return points on line.
                 * This breanch represents when a true enclosing line has been bound
                 * and all that is left is addding the other points on the line.
                 */
                if (prevThread != null ) {
                    joinThread(prevThread);
                }
                container.append(newPointLine);

                if (debug) {
                    System.out.printf ("Thread %d added %d items in thread.%n",
                                       threadId, newPointLine.size());
                }
            } else {
                /*
                 * This branch handles the case where a new extremum point is
                 * found. It will create the new lines and start 1 new thread on
                 * this and recurse on the other.
                 * The saved points to the left of the old line are passed to both
                 * instances as to not need to search all points again.
                 */
                // Edge case for adding the original starting point.
                nextApplicablePoints.add(line.p1);

                // Construct new lines thread for new line.
                // Recurse on the other line with this thread as to not start too many threads.
                StraightLine newLine1 = new StraightLine(line.p1, furthestPoint);
                StraightLine newLine2 = new StraightLine(furthestPoint, line.p2);

                // Create new branch on top of branching tree.
                if (nextApplicablePoints.size() < (n / threadCount)) {
                    /*
                     * This branch creates the new thread if not too many have
                     * already been created. It will recurse on the other line.
                     */
                    Thread newThreadBranch =
                        new Thread(new ConstructExtensionPointsWorker(newLine1,
                                                                      container,
                                                                      nextApplicablePoints,
                                                                      prevThread,
                                                                      level + 1,
                                                                      threadId++));

                    // Start new branch for one of the lines.
                    newThreadBranch.start();
                    // Recurse on the other line. (Saves thread creation).
                    constructExtendedLineRightPara(newLine2,
                                                   container,
                                                   newThreadBranch,
                                                   level + 1,
                                                   nextApplicablePoints);
                } else {
                    /*
                     * This branch means that enough threads have been created
                     * and the "old" sequential behaviour is done. The thread (this)
                     * will recurse on both new lines.
                     */
                    PointList pointList1 =
                        constructExtendedLineRight(newLine1, nextApplicablePoints);
                    PointList pointList2 =
                        constructExtendedLineRight(newLine2, nextApplicablePoints);
                    if (prevThread != null) {
                        // Wait for dependant thread and append to main container.
                        joinThread(prevThread);
                    }
                    // Append to main container.
                    container.append(pointList1);
                    container.append(pointList2);

                    if (debug) {
                        System.out.printf ("Thread %d added %d items in recurse on level %d.%n",
                                           threadId, pointList1.size() + pointList2.size(), level);
                    }
                }
            }
            return;
        }
    }
}
