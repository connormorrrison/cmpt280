package lib280.graph;

import lib280.exception.AfterTheEnd280Exception;

public class GraphMatrixRepEdgeIterator280<V extends Vertex280, E extends Edge280<V>> extends GraphEdgeIterator280<V, E> {

    public GraphMatrixRepEdgeIterator280(GraphMatrixRep280<V, E> graph) {
        super(graph);
    }

    protected int nextNonNullAdjIndex() {
        int result = adjIndex + 1;
        while ((result <= graph.capacity())
                && (((GraphMatrixRep280<V, E>) graph).adjMatrixEntry(iterationIndex, result) == null)) {
            result++;
        }

        return result;
    }

    @Override
    public void search(V u, V v) {
        item = ((GraphMatrixRep280<V, E>) graph).adjMatrixEntry(u.index(), v.index());
        iterationIndex = u.index();
        adjIndex = v.index();
    }

    @Override
    public void goFirst(V v) {
        // Record the vertex whose edges are about to be iterated
        iterationIndex = v.index();
        adjIndex = 0;

        // Move to the first edge.
        goForth();
    }

    @Override
    public void goForth() throws AfterTheEnd280Exception {
        if (after())
            throw new AfterTheEnd280Exception(
                    "Cannot go to next edge since already after.");

        // Find the next adjacent vertex
        adjIndex = nextNonNullAdjIndex();

        // If there wasn't another one, place the edge cursor in the 'after' position.
        if (after()) {
            item = null;
        }
        else {  // Otherwise, set the edge cursor to the next edge.
            item = ((GraphMatrixRep280<V, E>) graph).adjMatrixEntry(iterationIndex, adjIndex);
        }
    }

    @Override
    public boolean after() {
        return adjIndex > graph.capacity();
    }
}
