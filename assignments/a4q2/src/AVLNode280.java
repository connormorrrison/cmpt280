public class AVLNode280<I extends Comparable<I>> {
    I data;
    AVLNode280<I> left, right;
    int leftHeight, rightHeight;

    public AVLNode280(I data) {
        this.data = data;
        this.leftHeight = 0;
        this.rightHeight = 0;
    }

    public int getLeftHeight() {
        return leftHeight;
    }

    public void setLeftHeight(int height) {
        this.leftHeight = height;
    }

    public int getRightHeight() {
        return rightHeight;
    }

    public void setRightHeight(int height) {
        this.rightHeight = height;
    }

    public int getHeight() {
        return Math.max(leftHeight, rightHeight) + 1 ;
    }
}