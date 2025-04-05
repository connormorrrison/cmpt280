 package lib280.graph;

//import java.io.File;
//import java.io.IOException;
//import java.util.Scanner;

 import lib280.base.Pair280;
 import lib280.exception.InvalidArgument280Exception;

 import java.util.InputMismatchException;
 import java.util.Scanner;


 public class NonNegativeWeightedGraphAdjListRep280<V extends Vertex280> extends
         WeightedGraphAdjListRep280<V> {

     public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d,
                                                  String vertexTypeName) {
         super(cap, d, vertexTypeName);
     }

     public NonNegativeWeightedGraphAdjListRep280(int cap, boolean d) {
         super(cap, d);
     }


     @Override
     public void setEdgeWeight(V v1, V v2, double weight) {
         // Overriding this method to throw an exception if a weight is negative will cause
         // super.initGraphFromFile to throw an exception when it tries to set a weight to
         // something negative.

         // Verify that the weight is non-negative
         if(weight < 0) throw new InvalidArgument280Exception("Specified weight is negative.");

         // If it is, then just set the edge weight using the superclass method.
         super.setEdgeWeight(v1, v2, weight);
     }

     @Override
     public void setEdgeWeight(int srcIdx, int dstIdx, double weight) {
         // Get the vetex objects associated with each index and pass off to the
         // version of setEdgeWEight that accepts vertex objects.
         this.setEdgeWeight(this.vertex(srcIdx), this.vertex(dstIdx), weight);
     }


     /**
      * Implementation of Dijkstra's algorithm.
      * @param startVertex Start vertex for the single-source shortest paths.
      * @return An array of size G.numVertices()+1 in which offset k contains the shortest
      *         path from startVertex to k.  Offset 0 is unused since vertex indices start
      *         at 1.
      */
     public Pair280<double[], int[]> shortestPathDijkstra(int startVertex) {
         // TO DO Implement this method
         double[] distances = new double[this.numVertices() + 1];
         boolean[] visited = new boolean[this.numVertices() + 1];
         int[] predecessors = new int[this.numVertices() + 1];

         // Set Dijkstra initial conditions
         for (int i = 1; i <= this.numVertices(); i++) {
             distances[i] = Double.POSITIVE_INFINITY;
             visited[i] = false;
             predecessors[i] = 0;  // 0 indicates no predecessor
         }
         distances[startVertex] = 0;

         // Process vertices
         for (int i = 1; i <= this.numVertices(); i++) {
             // Find unvisited vertex with the smallest tentative distance
             int current = -1;
             double minDistance = Double.POSITIVE_INFINITY;

             for (int j = 1; j <= this.numVertices(); j++) {
                 if (!visited[j] && distances[j] < minDistance) {
                     current = j;
                     minDistance = distances[j];
                 }
             }

             // If all remaining vertices are unreachable, we're done
             if (current == -1 || minDistance == Double.POSITIVE_INFINITY) {
                 break;
             }

             // Mark current as visited
             visited[current] = true;

             this.goVertex(this.vertex(current));
             this.eGoFirst(this.vertex(current));

             while (this.eItemExists()) {
                 WeightedEdge280<V> edge = this.eItem();
                 int neighbor = edge.secondItem().index();
                 double weight = edge.getWeight();

                 // Update if we found shorter path
                 if (distances[current] + weight < distances[neighbor]) {
                     distances[neighbor] = distances[current] + weight;
                     predecessors[neighbor] = current;
                 }

                 this.eGoForth();
             }
         }

         return new Pair280<double[], int[]>(distances, predecessors);
     }

     // Given a predecessors array output from this.shortestPathDijkatra, return a string
     // that represents a path from the start node to the given destination vertex 'destVertex'.
     private static String extractPath(int[] predecessors, int destVertex) {
         // TO DO Implement this method
         // Special case - no predecessor means either it's the start vertex or unreachable
         if (predecessors[destVertex] == 0) {
             return "Not reachable.";
         }

         // Build the path
         StringBuilder path = new StringBuilder();
         int current = destVertex;

         // Using LinkedList to easily build path in correct order
         java.util.LinkedList<Integer> pathList = new java.util.LinkedList<>();
         pathList.add(current);

         // Backtrack through predecessors
         while (predecessors[current] != 0) {
             current = predecessors[current];
             pathList.addFirst(current);
         }

         // Format path as comma-separated string
         for (Integer vertex : pathList) {
             if (path.length() > 0) {
                 path.append(", ");
             }
             path.append(vertex);
         }

         return "The path to " + destVertex + " is: " + path.toString();
     }

     // Regression Test
     public static void main(String args[]) {
         NonNegativeWeightedGraphAdjListRep280<Vertex280> G = new NonNegativeWeightedGraphAdjListRep280<Vertex280>(1, false);

         if( args.length == 0)
             G.initGraphFromFile("../cmpt280-supporting-files/lib280-asn8/src/lib280/graph/weightedtestgraph.gra");
         else
             G.initGraphFromFile(args[0]);

         System.out.println("Enter the number of the start vertex: ");
         Scanner in = new Scanner(System.in);
         int startVertex;
         try {
             startVertex = in.nextInt();
         }
         catch(InputMismatchException e) {
             in.close();
             System.out.println("That's not an integer!");
             return;
         }

         if( startVertex < 1 || startVertex > G.numVertices() ) {
             in.close();
             System.out.println("That's not a valid vertex number for this graph.");
             return;
         }
         in.close();


         Pair280<double[], int[]> dijkstraResult = G.shortestPathDijkstra(startVertex);
         double[] finalDistances = dijkstraResult.firstItem();
         //double correctDistances[] = {-1, 0.0, 1.0, 3.0, 23.0, 7.0, 16.0, 42.0, 31.0, 36.0};
         int[] predecessors = dijkstraResult.secondItem();

         for(int i=1; i < G.numVertices() +1; i++) {
             System.out.println("The length of the shortest path from vertex " + startVertex + " to vertex " + i + " is: " + finalDistances[i]);
 //			if( correctDistances[i] != finalDistances[i] )
 //				System.out.println("Length of path from to vertex " + i + " is incorrect; should be " + correctDistances[i] + ".");
 //			else {
                 System.out.println(extractPath(predecessors, i));
 //			}
         }
     }

 }
