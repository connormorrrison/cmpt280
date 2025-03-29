import lib280.base.NDPoint280;
import java.util.ArrayList;
import java.util.List;

/**
 * KDTree280 data structure organizes points in k-dimensional space
 */
public class KDTree280 {
    private KDNode280 root;
    private int k;  // dimensionality of the tree

    /**
     * Constructs a KDTree280 from an array of points
     * @param pointArray an array of NDPoint280 objects representing the points to be stored in the KD-Tree
     * @throws IllegalArgumentException if the pointArray is null or empty
     */
    public KDTree280(NDPoint280[] pointArray) {
        if (pointArray == null || pointArray.length == 0) {
            throw new IllegalArgumentException("Points array cannot be null or empty.");
        }
        this.k = pointArray[0].dim();  // First point dimensionality
        this.root = kdtree(pointArray, 0, pointArray.length - 1, 0);
    }

    /**
     * Finds the j-th smallest element in a given dimension d within a sublist of an array
     * @param list the array of NDPoint280 objects
     * @param left the left boundary index of the sublist
     * @param right the right boundary index of the sublist
     * @param d the dimension in which to compare the points
     * @param j the index of the smallest element to find within the sublist
     */
    private void jSmallest(NDPoint280[] list, int left, int right, int d, int j) {
        if (right > left) {
            int pivotIndex = partition(list, left, right, d);

            if (j < pivotIndex) {
                jSmallest(list, left, pivotIndex - 1, d, j);
            } else if (j > pivotIndex) {
                jSmallest(list, pivotIndex + 1, right, d, j);
            }
            // If j == pivotIndex, we've found the j-th smallest element, so we're done
        }
    }

    /**
     * Partitions a sublist of an array around a pivot
     * @param list the array of NDPoint280 objects
     * @param left the left boundary index of the sublist
     * @param right the right boundary index of the sublist
     * @param d the dimension in which to compare the points
     * @return index of the pivot after partitioning
     */
    private int partition(NDPoint280[] list, int left, int right, int d) {
        NDPoint280 pivot = list[right];  // Rightmost element as pivot
        int i = left - 1;  // Index of smaller element

        for (int j = left; j < right; j++) {
            // If current element is smaller than or equal to pivot
            if (list[j].compareByDim(d, pivot) < 0 || (list[j].compareByDim(d, pivot) == 0 && list[j].compareTo(pivot) < 0)) {
                i++;
                swap(list, i, j);
            }
        }
        swap(list, i + 1, right);
        return i + 1;
    }

    /**
     * Swaps two elements in an array
     * @param list the array of NDPoint280 objects
     * @param i the index of the first element
     * @param j the index of the second element
     */
    private void swap(NDPoint280[] list, int i, int j) {
        NDPoint280 temp = list[i];
        list[i] = list[j];
        list[j] = temp;
    }

    /**
     * Recursively builds the KD-Tree from a sublist of an array of points
     * @param pointArray the array of NDPoint280 objects
     * @param left the left boundary
     * @param right the right boundary
     * @param depth the current depth
     * @return root node of the subtree
     */
    private KDNode280 kdtree(NDPoint280[] pointArray, int left, int right, int depth) {
        if (left > right) {
            return null;
        }
        int d = depth % k;
        int medianOffset = (left + right) / 2;

        jSmallest(pointArray, left, right, d, medianOffset);

        KDNode280 node = new KDNode280(pointArray[medianOffset]);

        // Build left and right subtrees
        node.setLeftChild(kdtree(pointArray, left, medianOffset - 1, depth + 1));
        node.setRightChild(kdtree(pointArray, medianOffset + 1, right, depth + 1));
        return node;
    }

    /**
     * Performs a range search in the KD-Tree, finding all points within the given range
     * @param lower the lower bounds of the range
     * @param upper the upper bounds of the range
     * @return list of points within the specified range
     */
    public List<NDPoint280> rangeSearch(NDPoint280 lower, NDPoint280 upper) {
        List<NDPoint280> result = new ArrayList<>();
        rangeSearchHelper(root, lower, upper, 0, result);
        return result;
    }

    /**
     * Helper function for performing a range search
     * @param node the current node being examined
     * @param lower the lower bounds of the range
     * @param upper the upper bounds of the range
     * @param depth the current depth in the KD-Tree
     * @param result the list to store points that are within the specified range
     */
    private void rangeSearchHelper(KDNode280 node, NDPoint280 lower, NDPoint280 upper, int depth, List<NDPoint280> result) {
        if (node == null) {
            return;
        }

        int d = depth % k;

        // Check if the current point is within the range
        boolean inRange = true;
        for (int i = 0; i < k; i++) {
            if (node.getItem().idx(i) < lower.idx(i) || node.getItem().idx(i) > upper.idx(i)) {
                inRange = false;
                break;
            }
        }
        if (inRange) {
            result.add(node.getItem());
        }

        // Search left to see if it contains points in range
        if (lower.idx(d) <= node.getItem().idx(d)) {
            rangeSearchHelper(node.getLeftChild(), lower, upper, depth + 1, result);
        }

        // Search right to see if it contains points in range
        if (upper.idx(d) >= node.getItem().idx(d)) {
            rangeSearchHelper(node.getRightChild(), lower, upper, depth + 1, result);
        }
    }

    /**
     * Helper method for printing of the tree
     * @param root Root of the current subtree
     * @param i Current level of the tree
     * @return A string representation of the tree
     */
    protected String toStringByLevel(KDNode280 root, int i) {
        StringBuffer blanks = new StringBuffer((i - 1) * 5);
        for (int j = 0; j < i - 1; j++)
            blanks.append("     ");

        String result = "";

        if (root == null)
            return result;

        if (root.getRightChild() != null)
            result += toStringByLevel(root.getRightChild(), i+1);

        result += "\n" + blanks + i + ":" + root.getItem();

        if (root.getLeftChild() != null)
            result += toStringByLevel(root.getLeftChild(), i+1);

        return result;
    }

    /**
     * String representation of the tree, level by level
     */
    @Override
    public String toString() {
        return toStringByLevel();
    }

    /**
     * String representation of the tree, level by level
     * @return string representation of the tree, level by level
     */
    public String toStringByLevel() {
        if (this.root == null) return "Empty Tree";
        else return toStringByLevel(this.root, 1);
    }

    /**
     * Main class for regression testing
     */
    public static void main(String[] args) {
        // Test with k=2 (2D points)
        System.out.println("Input 2D points:");
        NDPoint280[] points2D = {
                new NDPoint280(new double[]{6.0, 3.0}),
                new NDPoint280(new double[]{8.0, 9.0}),
                new NDPoint280(new double[]{12.0, 2.0}),
                new NDPoint280(new double[]{3.0, 5.0}),
                new NDPoint280(new double[]{1.0, 11.0}),
                new NDPoint280(new double[]{4.0, 8.0}),
                new NDPoint280(new double[]{2.0, 6.0})
        };

        // Print each point
        for (NDPoint280 p : points2D) {
            System.out.println(p);
        }

        // Build and display the 2D tree
        KDTree280 tree2D = new KDTree280(points2D);
        System.out.println("\nThe 2D lib280.tree built from these points is:");
        System.out.println(tree2D);

        // Test with k=4 (4D points) - using a different k value than the example
        System.out.println("\nInput 4D points:");
        NDPoint280[] points4D = {
                new NDPoint280(new double[]{2.0, 11.0, 1.0, 7.5}),
                new NDPoint280(new double[]{17.0, 2.0, 3.0, 9.8}),
                new NDPoint280(new double[]{3.0, 12.0, 15.0, 2.1}),
                new NDPoint280(new double[]{8.0, 4.0, 4.0, 5.2}),
                new NDPoint280(new double[]{4.0, 6.0, 6.0, 8.3}),
                new NDPoint280(new double[]{15.0, 5.0, 5.0, 1.4}),
                new NDPoint280(new double[]{5.0, 7.0, 2.0, 6.9}),
                new NDPoint280(new double[]{6.0, 6.0, 16.0, 3.7})
        };

        // Print each point
        for (NDPoint280 p : points4D) {
            System.out.println(p);
        }

        // Build and display the 4D tree
        KDTree280 tree4D = new KDTree280(points4D);
        System.out.println(tree4D);

        // Perform range searches on the 4D tree
        // Range search 1
        NDPoint280 lower1 = new NDPoint280(new double[]{0.0, 1.0, 0.0, 0.0});
        NDPoint280 upper1 = new NDPoint280(new double[]{5.0, 7.0, 3.0, 8.0});
        System.out.println("\nLooking for points between " + lower1 + " and " + upper1 + ".");
        System.out.println("Found:");
        List<NDPoint280> result1 = tree4D.rangeSearch(lower1, upper1);
        for (NDPoint280 p : result1) {
            System.out.println(p);
        }

        // Range search 2
        NDPoint280 lower2 = new NDPoint280(new double[]{0.0, 1.0, 0.0, 0.0});
        NDPoint280 upper2 = new NDPoint280(new double[]{9.0, 8.0, 5.0, 10.0});
        System.out.println("\nLooking for points between " + lower2 + " and " + upper2 + ".");
        System.out.println("Found:");
        List<NDPoint280> result2 = tree4D.rangeSearch(lower2, upper2);
        for (NDPoint280 p : result2) {
            System.out.println(p);
        }

        // Range search 3
        NDPoint280 lower3 = new NDPoint280(new double[]{0.0, 1.0, 0.0, 0.0});
        NDPoint280 upper3 = new NDPoint280(new double[]{18.0, 13.0, 17.0, 10.0});
        System.out.println("\nLooking for points between " + lower3 + " and " + upper3 + ".");
        System.out.println("Found:");
        List<NDPoint280> result3 = tree4D.rangeSearch(lower3, upper3);
        for (NDPoint280 p : result3) {
            System.out.println(p);
        }
    }
}
