

import lib280.graph.Edge280;
import lib280.graph.GraphAdjListRep280;
import lib280.graph.Vertex280;


public class UnionFind280 {
	GraphAdjListRep280<Vertex280, Edge280<Vertex280>> G;
	
	/**
	 * Create a new union-find structure.
	 * 
	 * @param numElements Number of elements (numbered 1 through numElements, inclusive) in the set.
	 * @postcond The structure is initialized such that each element is in its own subset.
	 */
	public UnionFind280(int numElements) {
		G = new GraphAdjListRep280<Vertex280, Edge280<Vertex280>>(numElements, true);
		G.ensureVertices(numElements);		
	}
	
	/**
	 * Return the representative element (equivalence class) of a given element.
	 * @param id The elements whose equivalence class we wish to find.
	 * @return The representative element (equivalence class) of the element 'id'.
	 */
	public int find(int id) {
		// TO DO - Write this method
		int vertex = id;

		// Keep following edges until we find a vertex that either:
		// 1. Has no outgoing edges
		// 2. Or points to itself (self-loop)
		while (true) {
			G.eGoFirst(G.vertex(vertex));

			// G.eItemExists() == false -> means there are no outgoing edges from that vertex
			// G.eItemExists() == true -> means there are outgoing edges from that vertex
			// If there are no outgoing edges, we've reached it
			if (!G.eItemExists()) {
				break;
			}

			// Get the vertex this edge points to
			Vertex280 nextVertexObj = G.eItem().secondItem();
			int nextVertex = nextVertexObj.index();

			// If the edge points back to the same vertex (self-loop)
			if (nextVertex == vertex) {
				break;
			}

			// Otherwise, follow the edge to the next vertex
			vertex = nextVertex;
		}

		return vertex;
	}
	
	/**
	 * Merge the subsets containing two items, id1 and id2, making them, and all of the other elemnets in both sets, "equivalent".
	 * @param id1 First element.
	 * @param id2 Second element.
	 */
	public void union(int id1, int id2) {
		// TO DO - Write this method.
		// Find representatives of both sets
		int vec1 = find(id1);
		int vec2 = find(id2);

		// If they're already in the same set, do nothing
		if (vec1 == vec2) {
			return;
		}

		// Connect vec1 to vec2
		G.addEdge(G.vertex(vec1), G.vertex(vec2));
	}

//	/**
//	 * Main method for regression testing
//	 */
//	public static void main(String[] args) {
//		// Regression tests for 'find()'
//		// Test 1
//		UnionFind280 uf = new UnionFind280(10);
//		for (int i = 1; i <= 10; i++) {
//			int rep = uf.find(i);
//			if (rep != i) {
//				System.out.println("Error in Test 1: Expected " + i + " to be its own representative initially, but got " + rep);
//			}
//		}
//
//		// Test 2
//		uf.union(1, 2);
//		int rep1 = uf.find(1);
//		int rep2 = uf.find(2);
//		if (rep1 != rep2) {
//			System.out.println("Error in Test 2: Expected elements 1 and 2 to have the same representative, but got " + rep1 + " and " + rep2);
//		}
//
//		// Test 3
//		uf.union(3, 4);
//		uf.union(4, 5);
//		int rep3 = uf.find(3);
//		int rep5 = uf.find(5);
//		if (rep3 != rep5) {
//			System.out.println("Error in Test 3: Expected elements 3 and 5 to have the same representative, but got " + rep3 + " and " + rep5);
//		}
//		if (rep1 == rep3) {
//			System.out.println("Error in Test 3: Expected elements 1 and 3 to have different representatives, but both have " + rep1);
//		}
//
//
//		// Regression tests for 'union()'
//		// Test 1
//		UnionFind280 uf2 = new UnionFind280(5);
//		uf2.union(1, 2);
//		if (uf2.find(1) != uf2.find(2)) {
//			System.out.println("Error in union Test 1: Expected elements 1 and 2 to be in the same set after union");
//		}
//
//		// Test 2
//		uf2.union(1, 2);  // Should do nothing
//		if (uf2.find(1) != uf2.find(2)) {
//			System.out.println("Error in union Test 2: Expected elements 1 and 2 to still be in the same set");
//		}
//
//		// Test 3
//		uf2.union(3, 4);
//		uf2.union(1, 3);
//		if (uf2.find(1) != uf2.find(4)) {
//			System.out.println("Error in union Test 3: Expected all elements to be in the same set after unions");
//		}
//	}
	
}
