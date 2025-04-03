package lib280.graph;

import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.BeforeTheStart280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.list.LinkedIterator280;
import lib280.list.LinkedList280;

public class GraphAdjListRepEdgeIterator280<V extends Vertex280, E extends Edge280<V>> extends GraphEdgeIterator280<V, E> {

    protected LinkedIterator280<E>[] adjListIt;

    public GraphAdjListRepEdgeIterator280(GraphAdjListRep280<V, E> graph) {
        super(graph);
        adjListIt = new LinkedIterator280[graph.capacity() + 1];
    }

    @Override
    public void search(V u, V v) {
        // Start at the beginning of the adjacency list for u
        goFirst(u);

        // Step through the list until we find an edge with destination node v
        while(itemExists() && adjListIt[iterationIndex].item().secondItem() != v) {
            goForth();
        }
    }

    @Override
    public void goFirst(V v) {
        // Set the vertex whose edges are being iterated.
        iterationIndex = v.index();

        // Set up the edge cursor
        adjListIt[iterationIndex] = ((GraphAdjListRep280<V, E>) graph).adjLists[iterationIndex].iterator();
        adjListIt[iterationIndex].goBefore();

        // Advance to the first item unless the edge list is empty.
        if(!((GraphAdjListRep280<V, E>) graph).adjLists[iterationIndex].isEmpty() )
            goForth();
        else {
            item = null;
            adjIndex = 0;
            adjListIt[iterationIndex].goAfter();
        }
    }

    @Override
    public void goForth() throws AfterTheEnd280Exception, NoCurrentItem280Exception {

        if (adjListIt[iterationIndex] == null)
            throw new NoCurrentItem280Exception(
                    "Edge iterator is not initialized to a vertex.");

        // If we're already after, we can't go any further.
        if (after())
            throw new AfterTheEnd280Exception(
                    "Cannot go to next edge since already after.");


        adjListIt[iterationIndex].goForth();

        // If we end up after, set eItem to null so that eItemExists() will return false
        if (after()) {
            item = null;
            adjIndex = 0;
        }
        else {
            // Otherwise, set the current edge, and the current adjacent vertex.
            item = adjListIt[iterationIndex].item();
            adjIndex = adjListIt[iterationIndex].item().secondItem().index();
        }
    }

    @Override
    public boolean after() throws NoCurrentItem280Exception {
        if (adjListIt[iterationIndex] == null)
            throw new NoCurrentItem280Exception(
                    "Edge iterator is not initialized to a vertex.");

        return adjListIt[iterationIndex].after();
    }
}
