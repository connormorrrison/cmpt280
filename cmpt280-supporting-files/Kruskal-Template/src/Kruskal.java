import lib280.graph.Vertex280;
import lib280.graph.WeightedEdge280;
import lib280.graph.WeightedGraphAdjListRep280;
import lib280.tree.ArrayedMinHeap280;

public class Kruskal {
	
	public static WeightedGraphAdjListRep280<Vertex280> minSpanningTree(WeightedGraphAdjListRep280<Vertex280> G) {

		// TO DO -- Complete this method.
		// New graph for minimum spanning tree and union find for cycles
		WeightedGraphAdjListRep280<Vertex280> minST = new WeightedGraphAdjListRep280<Vertex280>(G.numVertices(), false);
		minST.ensureVertices(G.numVertices());
		UnionFind280 uf = new UnionFind280(G.numVertices());

		// Min heap for organization
		ArrayedMinHeap280<WeightedEdge280<Vertex280>> edgeHeap = new ArrayedMinHeap280<WeightedEdge280<Vertex280>>(G.numEdges());

		// Add all edges to the min heap
		for(int i = 1; i <= G.numVertices(); i++) {
			G.goVertex(G.vertex(i));
			G.eGoFirst(G.vertex(i));
			while(G.eItemExists()) {
				// Only add edges where first vertex index < second vertex index,
				// preventing duplication
				if(G.eItem().firstItem().index() < G.eItem().secondItem().index()) {
					edgeHeap.insert(G.eItem());
				}
				G.eGoForth();
			}
		}

		// Kruskal's algorithm
		while(!edgeHeap.isEmpty()) {
			// Extract the edge with minimum weight from the heap
			WeightedEdge280<Vertex280> e = edgeHeap.item();
			edgeHeap.deleteItem();

			// Get the vertices this edge connects
			int vertexA = e.firstItem().index();
			int vertexB = e.secondItem().index();

			// If vertices are in different sets, this edge doesn't create a cycle,
			// so add it to minimum tree
			if(uf.find(vertexA) != uf.find(vertexB)) {
				minST.addEdge(minST.vertex(vertexA), minST.vertex(vertexB));
				minST.setEdgeWeight(minST.vertex(vertexA), minST.vertex(vertexB), e.getWeight());

				// Merge sets
				uf.union(vertexA, vertexB);
			}
		}

		return minST;
	}
	
	
	public static void main(String args[]) {
		WeightedGraphAdjListRep280<Vertex280> G = new WeightedGraphAdjListRep280<Vertex280>(1, false);
		G.initGraphFromFile("../cmpt280-supporting-files/Kruskal-template/mst.graph");
		System.out.println(G);
		
		WeightedGraphAdjListRep280<Vertex280> minST = minSpanningTree(G);
		
		System.out.println(minST);
	}
}


