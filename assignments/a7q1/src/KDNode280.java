import lib280.base.NDPoint280;

public class KDNode280 {
    NDPoint280 item;
    KDNode280 leftChild;
    KDNode280 rightChild;

    /**
     * Creates a new KDNode280 with a given item
     * @param item the NDPoint280 to store in this node
     */
    public KDNode280(NDPoint280 item) {
        this.item = item;
        this.leftChild = null;
        this.rightChild = null;
    }

    /**
     * Get the item stored in this node
     * @return the NDPoint280 stored in this node
     */
    public NDPoint280 getItem() {
        return item;
    }

    /**
     * Set the item stored in this node
     * @param item the NDPoint280 to store in this node
     */
    public void setItem(NDPoint280 item) {
        this.item = item;
    }

    /**
     * Get the left child of this node
     * @return the left child of this node
     */
    public KDNode280 getLeftChild() {
        return leftChild;
    }

    /**
     * Set the left child of this node
     * @param leftChild the node to set as the left child
     */
    public void setLeftChild(KDNode280 leftChild) {
        this.leftChild = leftChild;
    }

    /**
     * Get the right child of this node
     * @return the right child of this node
     */
    public KDNode280 getRightChild() {
        return rightChild;
    }

    /**
     * Set the right child of this node
     * @param rightChild the node to set as the right child
     */
    public void setRightChild(KDNode280 rightChild) {
        this.rightChild = rightChild;
    }
}
