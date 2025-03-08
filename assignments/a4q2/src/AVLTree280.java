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
     * Recursively inserts unique data into the subtree
     * @param node subtree root for insertion
     * @param data value to insert
     * @return updated subtree root
     */
    private AVLNode280<I> insertRecursive(AVLNode280<I> node, I data) {
        // If node is null, create a new node
        if (node == null) {
            return new AVLNode280<>(data);
        }

        // Insert into left subtree if data is smaller
        if (data.compareTo(node.data) < 0) {
            node.left = insertRecursive(node.left, data);
        }
       // Insert into right subtree if data is greater
        else if (data.compareTo(node.data) > 0) {
            node.right = insertRecursive(node.right, data);
        }
        // Do not insert duplicates if data is equal
        else {
            return node;
        }

        // Restore AVL balance after insertion
        return restoreAVLProperty(node);
    }

    /**
     * Deletes the node containing data from the AVL tree
     * @param data the item to be deleted
     */
    public void delete(I data) {
        root = deleteRecursive(root, data);
    }

    /**
     * Recursively deletes data from the subtree
     * @param node subtree root for deletion
     * @param data value to delete
     * @return updated subtree root
     */
    private AVLNode280<I> deleteRecursive(AVLNode280<I> node, I data) {
        // If the node is null, data is not in the tree
        if (node == null) {
            return node;
        }

        // Traverse left if data is smaller
        if (data.compareTo(node.data) < 0) {
            node.left = deleteRecursive(node.left, data);
        }
        // Traverse right if data is greater
        else if (data.compareTo(node.data) > 0) {
            node.right = deleteRecursive(node.right, data);
        }
        // Found the node to delete
        else {
            // Node with only one child or no child
            if (node.left == null || node.right == null) {
                AVLNode280<I> temp = (node.left != null) ? node.left : node.right;
                // No child case
                if (temp == null) {
                    node = null;
                }
                // One child case
                else {
                    node = temp;
                }
            }
            // Node with two children: get the inorder successor (smallest in right subtree)
            else {
                AVLNode280<I> temp = minValueNode(node.right);
                node.data = temp.data;
                node.right = deleteRecursive(node.right, temp.data);
            }
        }

        // If node was deleted (or is null now), nothing to re-balance
        if (node == null) {
            return node;
        }

        // Restore AVL balance after deletion
        return restoreAVLProperty(node);
    }

    /**
     * Searches for data in the AVL tree
     * @param data the item to search for
     * @return true if found, false otherwise
     */
    public boolean search(I data) {
        return searchRecursive(root, data) != null;
    }

    /**
     * Recursively searches for data in the subtree
     * @param node current subtree root
     * @param data data value to search for
     * @return node containing data if found, otherwise null
     */
    private AVLNode280<I> searchRecursive(AVLNode280<I> node, I data) {
        // If node is null, data is not in the tree
        if (node == null) {
            return null;
        }
        // If data matches node's value, return node
        if (data.compareTo(node.data) == 0) {
            return node;
        }
        // If data is smaller, search left subtree
        else if (data.compareTo(node.data) < 0) {
            return searchRecursive(node.left, data);
        }
        // If data is greater, search right subtree
        else {
            return searchRecursive(node.right, data);
        }
    }

    /**
     * Finds the node with the smallest value in the subtree
     * @param node subtree root to search
     * @return node with the smallest value
     */
    private AVLNode280<I> minValueNode(AVLNode280<I> node) {
        AVLNode280<I> current = node;
        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    /**
     * Helper method for left rotation
     * @param node root of the subtree to rotate
     * @return new root after rotation
     */
    private AVLNode280<I> rotateLeft(AVLNode280<I> node) {
        AVLNode280<I> newRoot = node.right;
        node.right = newRoot.left;
        newRoot.left = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    /**
     * Helper method for right rotation
     * @param node root of the subtree to rotate
     * @return new root after rotation
     */
    private AVLNode280<I> rotateRight(AVLNode280<I> node) {
        AVLNode280<I> newRoot = node.left;
        node.left = newRoot.right;
        newRoot.right = node;
        updateHeight(node);
        updateHeight(newRoot);
        return newRoot;
    }

    /**
     * Helper method for left-right rotation
     * @param node root of the subtree to rotate
     * @return new root after rotation
     */
    private AVLNode280<I> rotateLeftRight(AVLNode280<I> node) {
        node.left = rotateLeft(node.left);
        return rotateRight(node);
    }

    /**
     * Helper method for right-left rotation
     * @param node root of the subtree to rotate
     * @return new root after rotation
     */
    private AVLNode280<I> rotateRightLeft(AVLNode280<I> node) {
        node.right = rotateRight(node.right);
        return rotateLeft(node);
    }

    /**
     * Recalculates leftHeight and rightHeight, checks balance,
     * and performs the appropriate rotation(s)
     */
    private AVLNode280<I> restoreAVLProperty(AVLNode280<I> node) {
        // If node is null
        if (node == null) {
            return null;
        }

        // First update the heights from children
        updateHeight(node);

        // Calculate balance factor
        int balance = node.getLeftHeight() - node.getRightHeight();

        // Left heavy
        if (balance > 1) {
            // Left subtree is left-heavy or balanced, do right rotation
            if (node.left != null && node.left.getLeftHeight() >= node.left.getRightHeight()) {
                node = rotateRight(node);
            }
            // Otherwise, do left-right rotation
            else {
                node = rotateLeftRight(node);
            }
        }
        // Right heavy
        else if (balance < -1) {
            // Right subtree is right-heavy or balanced, do left rotation
            if (node.right != null && node.right.getRightHeight() >= node.right.getLeftHeight()) {
                node = rotateLeft(node);
            }
            // Otherwise, do right-left rotation
            else {
                node = rotateRightLeft(node);
            }
        }

        return node;
    }

    /**
     * Updates the height of the given node based on its children's heights
     * @param node node whose height needs to be updated
     */
    private void updateHeight(AVLNode280<I> node) {
        // Check if node is null
        if (node == null) {
            return;
        }
        // Update left child's height if exists
        if (node.left != null) {
            node.setLeftHeight(node.left.getHeight());
        }
        // Set left height to 0 if left child is absent
        else {
            node.setLeftHeight(0);
        }

        // Update right child's height if exists
        if (node.right != null) {
            node.setRightHeight(node.right.getHeight());
        }
        // Set right height to 0 if right child is absent
        else {
            node.setRightHeight(0);
        }
    }

    /**
     * Checks if the tree is empty
     * @return true if the tree is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Retrieves the data stored in the root node
     * @return the value of the root node
     * @throws ContainerEmpty280Exception if the tree is empty
     */
    @Override
    public I rootItem() {
        if (isEmpty())
            throw new ContainerEmpty280Exception("Cannot access the root of an empty tree");
        return root.data;
    }

    /**
     * Returns the left subtree of the root as a new AVL tree
     * @return left subtree of the root
     * @throws ContainerEmpty280Exception if the tree is empty
     */
    @Override
    public AVLTree280<I> rootLeftSubtree() {
        // If tree is empty
        if (isEmpty()) {
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty tree");
        }
        AVLTree280<I> leftTree = new AVLTree280<>();
        leftTree.root = root.left;
        return leftTree;
    }

    /**
     * Returns the right subtree of the root as a new AVL tree
     * @return right subtree of the root
     * @throws ContainerEmpty280Exception if the tree is empty
     */
    @Override
    public AVLTree280<I> rootRightSubtree() {
        // If tree is empty
        if (isEmpty()) {
            throw new ContainerEmpty280Exception("Cannot return a subtree of an empty tree");
        }
        AVLTree280<I> rightTree = new AVLTree280<>();
        rightTree.root = root.right;
        return rightTree;
    }

    /**
     * Removes all elements from the AVL tree, making it empty
     */
    @Override
    public void clear() {
        root = null;
    }

    /**
     * Form a string representation that includes level numbers
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
     * String representation of the tree, level by level
     * Analysis: Time = O(n), where n = number of items in the tree
     */
    public String toStringByLevel() {
        return toStringByLevel(1);
    }

    /**
     * Main method for regression testing
     */
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

        // Regression Test 4: Search Test
        System.out.println("\n");
        System.out.println("Regression Test 4: Search Test");

        AVLTree280<Integer> avlTree4 = new AVLTree280<>();
        avlTree4.insert(10);
        avlTree4.insert(20);
        avlTree4.insert(30);
        avlTree4.insert(40);
        avlTree4.insert(50);

        System.out.println("Initial Tree:");
        System.out.println(avlTree4.toStringByLevel());

        System.out.println("\n");
        System.out.print("Search 30: ");
        if (avlTree4.search(30)) {
            System.out.println("Found");
        } else {
            System.out.println("Not Found");
        }
        System.out.print("Search 15: ");
        if (avlTree4.search(15)) {
            System.out.println("Found");
        } else {
            System.out.println("Not Found");
        }
        System.out.print("Search 50: ");
        if (avlTree4.search(50)) {
            System.out.println("Found");
        } else {
            System.out.println("Not Found");
        }
        System.out.print("Search 5: ");
        if (avlTree4.search(5)) {
            System.out.println("Found");
        } else {
            System.out.println("Not Found");
        }

        System.out.println("\n");
        System.out.println("Delete 30:");
        avlTree4.delete(30);
        System.out.println(avlTree4.toStringByLevel());

        System.out.println("\n");
        System.out.print("Search 30 after deletion: ");
        if (avlTree4.search(30)) {
            System.out.println("Found");
        } else {
            System.out.println("Not Found");
        }
    }
}
