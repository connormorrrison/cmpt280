package lib280.graph;

import lib280.base.Cursor280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.NoCurrentItem280Exception;

public abstract class GraphEdgeIterator280<V extends Vertex280, E extends Edge280<V>> implements Cursor280<E> {

    protected Graph280<V, E> graph;

    /**
     * The current item at the edge cursor - used to iterate over edges incident
     * on a vertex.
     */
    protected E item;

    /** The index of the vertex over whose edges eItem is iterating. */
    protected int iterationIndex;

    /** The index of the adjacent vertex during edge iteration */
    protected int adjIndex;

    protected GraphEdgeIterator280(Graph280<V, E> graph) {
        this.graph = graph;
    }

    /**
     * Determine if there is an edge from vertex u to vertex v and locate the
     * edge cursor there. If there is no such edge, the edge cursor is not
     * located at any edge.
     *
     * @param u Source vertex of desired edge.
     * @param v Destination vertex of desired edge.
     *
     */
    public abstract void search(V u, V v);

    /**
     * Set the edge cursor to the first edge incident on a given vertex (prepare
     * to iterate over edges of vertex v)
     *
     * @param v
     *            Vertex over whose edges to iterate.
     */
    public abstract void goFirst(V v);

    /**
     * Go to the next edge of the edge list over which the edge cursor is
     * iterating.
     *
     * @precond !after()
     * @throws AfterTheEnd280Exception
     */
    public abstract void goForth() throws AfterTheEnd280Exception;

    /**
     * Are we after the end of the edge list being iterated?
     *
     * @return true if the edge cursor is past the end of the edge list, false
     *         otherwise.
     */
    public abstract boolean after();

    /**
     * Get the current edge at the edge cursor.
     *
     * @return The edge at the edge cursor.
     */
    public E item() {
        if (!itemExists())
            throw new NoCurrentItem280Exception(
                    "The edge cursor is not at an edge.");

        return item;
    }

    /**
     * Check if there is the edge cursor is on an edge.
     *
     * @return Returns true if the edge cursor is at an edge, false otherwise.
     */
    public boolean itemExists() {
        return item != null;
    }

    /**
     * Obtain the vertex over whose edges the edge cursor is iterating.
     *
     * @return The index of the vertex over whose edges the edge cursor is
     *         iterating.
     */
    public int iterationIndex() {
        return iterationIndex;
    }

    /**
     * Get the vertex at the other end of the current edge.
     *
     * @precond this.itemExists()
     * @return The vertex at the other end of the current edge.
     * @throws NoCurrentItem280Exception
     */
    public V itemAdjacentVertex() throws NoCurrentItem280Exception {
        if (!itemExists())
            throw new NoCurrentItem280Exception(
                    "There is no current edge: there is no adjacent vertex if there is no current edge.");

        return item.other(graph.vertex(iterationIndex));
    }

    @Override
    public String toString() {
        String res = "GraphEdgeIterator @ ";
        if (itemExists()) res += item();
        else res += "<NONE>";
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    public GraphEdgeIterator280<V, E> clone() {
        try {
            return (GraphEdgeIterator280<V, E>) super.clone();
        }
        catch (CloneNotSupportedException e) {
            // Should not occur: this is a Cursor280, which implements Cloneable
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other)
    {
        if ( other instanceof GraphEdgeIterator280) {
            GraphEdgeIterator280<V, E> otherIter = (GraphEdgeIterator280<V, E>) other;
            return (otherIter.graph == graph)
                    && (otherIter.iterationIndex == iterationIndex)
                    && (otherIter.adjIndex == adjIndex);
        }
        else {
            return false;
        }
    }
}
