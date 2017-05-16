README for ConvexEnclosure.

TO BUILD (The easy way!):

   ./gradlew fatjar

TO BUILD (The boring way):

   javac -d build/classes/main @sources.txt

TO RUN (The easy way:
   java -jar build/libs/ConvexEnclosement-all-1.0.jar -b 6 -m both -n NUM

TO RUN (The boring way):
   cd build/classes/main/
   java Oblig4 -b 6 -m both -n NUM

Different options exists for running the the program (parameters enclosed in [..] are optional):

-n NUM                   // Defines how many points to create and enclose.
-m seq|para|both         // MODE : Defines which alorithm to use for enclosement.
-b [NUM]                 // BENCHMARK : Runs a benchmark and returns the time taken for each mode selected.
                         // NUM defines how many iterations (median is taken for result).
-dr                      // DRAW : Will draw the resulting enclosure.
-I NUM                   // NUMBER OF THREADS : NUM specifies the maximum number of threads.
-d                       // DEBUG : Will print debugging information. (including the upto 1000 first points of enclosure).

TO USE AS LIBRARY:
   put jar file in classpath.
