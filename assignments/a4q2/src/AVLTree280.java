import lib280.tree.LinkedSimpleTree280;
import lib280.exception.ContainerEmpty280Exception;

public class AVLTree280<I extends Comparable<I>> extends LinkedSimpleTree280<I> {
    // Root of the AVL tree
    private AVLNode280<I> root;

    /**
     * Inserts the data into the AVL tree
     * @param data the item to be inserted
     */
    public void insert(I data) {
        root = insertRecursive(root, data);
    }

    /**
     * Recursively inserts data into the subtree rooted at 'node'
     */
    private AVLNode280<I> insertRecursive(AVLNode280<I> node, I data) {
        if (node == null) {
            return new AVLNode280<>(data);
        }

        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left = insertRecursive(node.left, data);
        }
        else if (cmp > 0) {
            node.right = insertRecursive(node.right, data);
        }
        else {
            // Duplicate keys are not inserted.
            return node;
        }

        // After standard BST insertion, update heights and restore AVL balance.
        return restoreAVLProperty(node);
    }

    /**
     * Deletes the node containing 'data' from the AVL tree.
     * @param data the item to be deleted
     */
    public void delete(I data) {
        root = deleteRecursive(root, data);
    }

    /**
     * Recursively deletes data from the subtree rooted at 'node'.
     */
    private AVLNode280<I> deleteRecursive(AVLNode280<I> node, I data) {
        if (node == null) return node;

        int cmp = data.compareTo(node.data);
        if (cmp < 0) {
            node.left = deleteRecursive(node.left, data);
        }
        else if (cmp > 0) {
            node.right = deleteRecursive(node.right, data);
        }
        else {
            // Node with only one child or no child
            if (node.left == null || node.right == null) {
                AVLNode280<I> temp = (node.left != null) ? node.left : node.right;
                if (temp == null) {
                    // No child case
                    node = null;
                }
                else {
                    // One child case
                    node = temp;
                }
            }
            else {
                // Node with two children: get the inorder successor (smallest in right subtree)
                AVLNode280<I> temp = minValueNode(node.right);
                node.data = temp.data;
                node.right = deleteRecursive(node.right, temp.data);
            }
        }

        // If node was deleted (or is null now), nothing to rebalance.
        if (node == null) return node;

        // Restore AVL balance after deletion.
        return restoreAVLProperty(node);
    }

    /**
     * Searches for data in the AVL tree.
     * @param data the item to search for
     * @return true if found, false otherwise
     */
    public boolean search(I data) {
        return searchRecursive(root, data) != null;
    }

    private AVLNode280<I> searchRecursive(AVLNode280<I> node, I data) {
        if (node == null) return null;
        int cmp = data.compareTo(node.data);
        if (cmp == 0) return node;
        else if (cmp < 0) return searchRecursive(node.left, data);
        else return searchRecursive(node.right, data);
    }

    /**
     * Returns the node with the minimum value found in 'node's subtree.
     */
    private AVLNode280<I> minValueNode(AVLNode280<I> node) {
        AVLNode280<I> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    // --- Rotation helper methods ---
    private AVLNode280<I> rotateLeft(AVLNode280<I> node) {
        AVLNode280<I> newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    private AVLNode280<I> rotateRight(AVLNode280<I> node) {
        AVLNode280<I> newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    private AVLNode280<I> rotateLeftRight(AVLNode280<I> node) {
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    private AVLNode280<I> rotateRightLeft(AVLNode280<I> node) {
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    /**
     * Recomputes leftHeight and rightHeight for 'node', checks balance,
     * and performs the appropriate rotation(s) if needed.
     */
    private AVLNode280<I> restoreAVLProperty(AVLNode280<I> node) {
        if (node == null) return null;

        // First update the heights from children
        updateHeight(node);

        // Calculate balance factor
        int balance = node.getLeftHeight() - node.getRightHeight();

        // Left heavy
        if (balance > 1) {
            // Left subtree is Left-heavy or balanced => single right rotation
            if (node.left != null && node.left.getLeftHeight() >= node.left.getRightHeight()) {
                node = rotateRight(node);
            }
            // Otherwise => left-right rotation
            else {
                node = rotateLeftRight(node);
            }
        }
        // Right heavy
        else if (balance < -1) {
            // Right subtree is Right-heavy or balanced => single left rotation
            if (node.right != null && node.right.getRightHeight() >= node.right.getLeftHeight()) {
                node = rotateLeft(node);
            }
            // Otherwise => right-left rotation
            else {
                node = rotateRightLeft(node);
            }
        }

        return node;
    }

    /**
     * Updates the leftHeight and rightHeight fields for the given node.
     */
    private void updateHeight(AVLNode280<I> node) {
        if (node == null) return;
        node.setLeftHeight((node.left != null) ? node.left.getHeight() : 0);
        node.setRightHeight((node.right != null) ? node.right.getHeight() : 0);
    }

    // --- Public rotation methods that rotate the entire tree ---
    public void rotateLeft() {
        if (root != null) {
            root = rotateLeft(root);
        }
    }

    public void rotateRight() {
        if (root != null) {
            root = rotateRight(root);
        }
    }

    public void rotateLeftRight() {
        if (root != null) {
            root = rotateLeftRight(root);
        }
    }

    public void rotateRightLeft() {
        if (root != null) {
            root = rotateRightLeft(root);
        }
    }

    // --- Overriding LinkedSimpleTree280 methods to use our AVL root ---
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    @Override
    public I rootItem() {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot access the root of an empty tree.");
        return root.data;
    }

    @Override
    public AVLTree280<I> rootLeftSubtree() {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty tree.");
        AVLTree280<I> leftTree = new AVLTree280<>();
        leftTree.root = root.left;
        return leftTree;
    }

    @Override
    public AVLTree280<I> rootRightSubtree() {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty tree.");
        AVLTree280<I> rightTree = new AVLTree280<>();
        rightTree.root = root.right;
        return rightTree;
    }

    @Override
    public void clear() {
        root = null;
    }

    /**
     * Form a string representation that includes level numbers.
     * Analysis: Time = O(n), where n = number of items in the (sub)tree
     * @param i the level for the root of this (sub)tree
     */
    protected String toStringByLevel(int i) {
        StringBuffer blanks = new StringBuffer((i - 1) * 5);
        for (int j = 0; j < i - 1; j++)
            blanks.append("     ");

        String result = new String();
        if (!isEmpty() && (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty()))
            result += rootRightSubtree().toStringByLevel(i + 1);

        result += "\n" + blanks + i + ": ";
        if (isEmpty())
            result += "-";
        else {
            result += rootItem();
            if (!rootLeftSubtree().isEmpty() || !rootRightSubtree().isEmpty())
                result += rootLeftSubtree().toStringByLevel(i + 1);
        }
        return result;
    }

    /**
     * String representation of the tree, level by level.
     * Analysis: Time = O(n), where n = number of items in the tree
     */
    public String toStringByLevel() {
        return toStringByLevel(1);
    }

    // Main method for regression testing
    public static void main(String[] args) {
        AVLTree280<Integer> avlTree1 = new AVLTree280<>();

        // Regression Test 1
        System.out.println("\n");
        System.out.println("Regression Test 1: Tree 1");

        System.out.println("Initial Tree:");
        System.out.println(avlTree1.toStringByLevel());

        System.out.println("Insert 16:");
        avlTree1.insert(16);
        System.out.println(avlTree1.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 32:");
        avlTree1.insert(32);
        System.out.println(avlTree1.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 64:");
        avlTree1.insert(64);
        System.out.println(avlTree1.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 24:");
        avlTree1.insert(24);
        System.out.println(avlTree1.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 20:");
        avlTree1.insert(20);
        System.out.println(avlTree1.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 22:");
        avlTree1.insert(22);
        System.out.println(avlTree1.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 21:");
        avlTree1.insert(21);
        System.out.println(avlTree1.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 98:");
        avlTree1.insert(98);
        System.out.println(avlTree1.toStringByLevel());

        // Regression Test 2
        System.out.println("\n");
        System.out.println("Regression Test 2: Tree 2");

        AVLTree280<Integer> avlTree2 = new AVLTree280<>();
        avlTree2.insert(16);
        avlTree2.insert(32);
        avlTree2.insert(64);
        avlTree2.insert(24);
        avlTree2.insert(20);
        avlTree2.insert(22);
        avlTree2.insert(21);
        avlTree2.insert(98);

        System.out.println("Initial Tree:");
        System.out.println(avlTree2.toStringByLevel());

        System.out.println("\n");
        System.out.println("Delete 64:");
        avlTree2.delete(64);
        System.out.println(avlTree2.toStringByLevel());

        System.out.println("\n");
        System.out.println("Delete 24:");
        avlTree2.delete(24);
        System.out.println(avlTree2.toStringByLevel());

        System.out.println("\n");
        System.out.println("Delete 20:");
        avlTree2.delete(20);
        System.out.println(avlTree2.toStringByLevel());

        System.out.println("\n");
        System.out.println("Delete 16:");
        avlTree2.delete(16);
        System.out.println(avlTree2.toStringByLevel());

        System.out.println("\n");
        System.out.println("Delete 21:");
        avlTree2.delete(21);
        System.out.println(avlTree2.toStringByLevel());

        // Regression Test 3
        System.out.println("\n");
        System.out.println("Regression Test 3: Tree 3");

        AVLTree280<Integer> avlTree3 = new AVLTree280<>();

        System.out.println("Initial Tree:");
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 25:");
        avlTree3.insert(25);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 15:");
        avlTree3.insert(15);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 35:");
        avlTree3.insert(35);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 45:");
        avlTree3.insert(45);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 10:");
        avlTree3.insert(10);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 5:");
        avlTree3.insert(5);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 20:");
        avlTree3.insert(20);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 30:");
        avlTree3.insert(30);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Delete 15:");
        avlTree3.delete(15);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 40:");
        avlTree3.insert(40);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Delete 45:");
        avlTree3.delete(45);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 50:");
        avlTree3.insert(50);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Delete 5:");
        avlTree3.delete(5);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Insert 55:");
        avlTree3.insert(55);
        System.out.println(avlTree3.toStringByLevel());

        System.out.println("\n");
        System.out.println("Delete 30:");
        avlTree3.delete(30);
        System.out.println(avlTree3.toStringByLevel());
    }
}
