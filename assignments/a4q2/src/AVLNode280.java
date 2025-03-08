public class AVLNode280<I extends Comparable<I>> {
    I data;
    AVLNode280<I> left, right;
    int leftHeight, rightHeight;

    /**
     * Constructs an AVL tree node
     * Initializes tree with the given data
     * Initializes left and right subtree heights to zero
     * @param data the value stored in this node
     */
    public AVLNode280(I data) {
        this.data = data;
        this.leftHeight = 0;
        this.rightHeight = 0;
    }

    /**
     * Get the height of the left subtree
     * @return the height of the left subtree
     */
    public int getLeftHeight() {
        return leftHeight;
    }

    /**
     * Set the height of the left subtree
     * @param height the new height of the left subtree
     */
    public void setLeftHeight(int height) {
        this.leftHeight = height;
    }

    /**
     * Get the height of the right subtree
     * @return the height of the right subtree
     */
    public int getRightHeight() {
        return rightHeight;
    }

    /**
     * Set the height of the right subtree
     * @param height the new height of the right subtree
     */
    public void setRightHeight(int height) {
        this.rightHeight = height;
    }

    /**
     * Get the height of the current node
     * @return the height of the current node based on its subtrees
     */
    public int getHeight() {
        return Math.max(leftHeight, rightHeight) + 1 ;
    }
}