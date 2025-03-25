package lib280.graph;

import lib280.base.LinearIterator280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ItemNotFound280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class GraphVertexIterator280<V extends Vertex280> implements LinearIterator280<V> {

    protected Graph280<V, ? extends Edge280<V>> graph;

    /** The current item at the vertex cursor. */
    protected V item;

    /** The index of the current item at the vertex cursor */
    protected int itemIndex;

    /**
     * Creates a new GraphVertexIterator object.
     * @param graph The graph to iterate over its vertices.
     */
    public GraphVertexIterator280(Graph280<V, ? extends Edge280<V>> graph) {
        this.graph = graph;
        if (! graph.isEmpty()) goFirst();
    }

    /** Find the next nonempty location in the vertex array. */
    protected int nextNonNullVertexIndex() {
        int i = itemIndex + 1;
        while (i <= graph.capacity() && graph.vertex(i) == null)
            i++;
        return i;
    }

    /**
     * Position the vertex cursor at the vertex identified by the given index.
     *
     * @param idx Index of the vertex at which to position the cursor.
     */
    public void goIndex(int idx) throws ItemNotFound280Exception {
        itemIndex = idx;
        if (idx >= 1 && idx <= graph.capacity()) {
            if (graph.vertex(idx) == null)
                throw new ItemNotFound280Exception(
                        "Vertex with index idx does not exist.");
            item = graph.vertex(idx);
        }
        else {
            item = null;
        }
    }

    /**
     * Position the vertex cursor at the specified vertex.
     *
     * @param newVertex The vertex at which the vertex cursor is to be positioned.
     */
    public void goVertex(V newVertex) {
        item = newVertex;
        itemIndex = newVertex.index();
    }

    @Override
    public V item() throws NoCurrentItem280Exception {
        if (! itemExists()) throw new ItemNotFound280Exception("A current item must exist");
        return item;
    }

    /**
     * Obtain the index of the vertex at the vertex cursor.
     *
     * @return The index of the vertex at the vertex cursor.
     */
    public int itemIndex() {
        return itemIndex;
    }


    @Override
    public boolean itemExists() {
        return item != null;
    }

    @Override
    public boolean before() {
        return itemIndex < 1;
    }

    @Override
    public boolean after() {
        return itemIndex > graph.capacity();
    }

    @Override
    public void goForth() throws AfterTheEnd280Exception {
        if (after())
            throw new AfterTheEnd280Exception(
                    "Cannot advance the vertex cursor, it is already after the last vertex.");

        if(before()) goFirst();
        else goIndex(nextNonNullVertexIndex());
    }

    @Override
    public void goFirst() throws ContainerEmpty280Exception {
        itemIndex = 0;
        goIndex(nextNonNullVertexIndex());
        if (itemExists())
            item = graph.vertex(itemIndex);
    }

    @Override
    public void goBefore() {
        itemIndex = 0;
        item = null;
    }

    @Override
    public void goAfter() {
        itemIndex = graph.capacity() + 1;
        item = null;
    }

    @Override
    public String toString() {
        String res = "GraphVertexIterator @ ";
        if (itemExists()) res += item();
        else res += "<NONE>";
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    public GraphVertexIterator280<V> clone() {
        try {
            return (GraphVertexIterator280<V>) super.clone();
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
        if ( other instanceof GraphVertexIterator280) {
            GraphVertexIterator280<V> otherIter = (GraphVertexIterator280<V>) other;
            return (otherIter.graph == graph)
                && (otherIter.itemIndex == itemIndex);
        }
        else {
            return false;
        }
    }
}
