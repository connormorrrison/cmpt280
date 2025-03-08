import lib280.base.Dispenser280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.tree.ArrayedBinaryTree280;

public class ArrayedHeap280<I extends Comparable<? super I>> extends ArrayedBinaryTree280<I> implements Dispenser280<I> {
  /**
   * Creates a heap with the given capacity
   * @param capacity max number of elements
   */
    @SuppressWarnings("unchecked")
    public ArrayedHeap280(int capacity) {
        super(capacity);
        items = (I[]) new Comparable[this.capacity +1];
    }

    /**
     * Inserts an item into the heap
     * @param item item to be inserted into the data structure
     * @throws ContainerFull280Exception
     */
    public void insert(I item) throws ContainerFull280Exception {
        if (this.isFull()) {
            throw new ContainerFull280Exception("Cannot add item to a tree that is full.");
        }

        // If the cursor is in the after position where the new item is to be
        // inserted, move it over so that the cursor stays in the after position
        if (this.currentNode == this.count + 1) {
            this.currentNode++;
        }

        this.count++;
        this.items[this.count] = item;

        // While e is larger than its parent and is not at the root,
        // swap e with its parent
        int currentPosition = this.count;
        while (currentPosition > 1 && this.items[currentPosition].compareTo(this.items[findParent(currentPosition)]) > 0) {
            // Store the old parent
            I oldParent = this.items[findParent(currentPosition)];
            this.items[findParent(currentPosition)] = this.items[currentPosition];
            this.items[currentPosition] = oldParent;

            // Move up the tree
            currentPosition = findParent(currentPosition);
        }

        // Set current position to the root (which will be the largest element in a max heap)
        this.currentNode = 1;
    }

    /**
     * Removes the largest element from the heap
     * @throws ContainerEmpty280Exception if the heap is empty
     * @throws NoCurrentItem280Exception if there is no current item
     */
    public void deleteItem() throws ContainerEmpty280Exception, NoCurrentItem280Exception {
        // Removes the largest element from the heap H
        if (this.isEmpty()) {
            throw new ContainerEmpty280Exception("Cannot delete item from an empty tree.");
        }

        // Since the largest element in a heap is always at the root...
        // Remove the root from H normally, as in ArrayedBinaryTreeWithCursors280<I>
        // (copy the right-most element in the bottom level, e, into the root,
        // remove the original copy of e.)
        if (this.count > 1) {
            this.items[1] = this.items[this.count];
            this.items[this.count] = null;
        }
        this.count--;

        // While e is smaller than its largest child,
        // swap e with its largest child
        int currentParent = 1;  // Start at the root

        while (findLeftChild(currentParent) <= this.count) {  // While there is at least one child
            int leftChildIndex = findLeftChild(currentParent);
            int rightChildIndex = findRightChild(currentParent);
            int largestChild = leftChildIndex;  // Assume left child is the largest

            // If the right child exists and is greater than the left child, update largestChild
            if (rightChildIndex <= this.count && this.items[rightChildIndex].compareTo(this.items[leftChildIndex]) > 0) {
                largestChild = rightChildIndex;
            }

            // If the current node is already larger than the largest child, stop
            if (this.items[currentParent].compareTo(this.items[largestChild]) >= 0) {
                break;
            }

            // Swap the current node with the largest child
            I temp = this.items[currentParent];
            this.items[currentParent] = this.items[largestChild];
            this.items[largestChild] = temp;

            // Move down to the swapped position
            currentParent = largestChild;
        }
        if (this.count == 0) {
            this.currentNode = 0; // After position
        } else {
            this.currentNode = 1; // Point to the root
        }
    }

    /**
     * Helper for the regression test.  Verifies the heap property for all nodes.
     */
    private boolean hasHeapProperty() {
        for(int i=1; i <= count; i++) {
            if( findRightChild(i) <= count ) {  // if i Has two children...
                // ... and i is smaller than either of them, , then the heap property is violated.
                if( items[i].compareTo(items[findRightChild(i)]) < 0 ) return false;
                if( items[i].compareTo(items[findLeftChild(i)]) < 0 ) return false;
            }
            else if( findLeftChild(i) <= count ) {  // if n has one child...
                // ... and i is smaller than it, then the heap property is violated.
                if( items[i].compareTo(items[findLeftChild(i)]) < 0 ) return false;
            }
            else break;  // Neither child exists.  So we're done.
        }
        return true;
    }

    /**
     * Regression test
     */
    public static void main(String[] args) {

        ArrayedHeap280<Integer> H = new ArrayedHeap280<Integer>(10);

        // Empty heap should have the heap property.
        if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");

        // Insert items 1 through 10, checking after each insertion that
        // the heap property is retained, and that the top of the heap is correctly i.
        for(int i = 1; i <= 10; i++) {
            H.insert(i);
            if(H.item() != i) System.out.println("Expected current item to be " + i + ", got " + H.item());
            if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
        }

        // Remove the elements 10 through 1 from the heap, chekcing
        // after each deletion that the heap property is retained and that
        // the correct item is at the top of the heap.
        for(int i = 10; i >= 1; i--) {
            // Remove the element i.
            H.deleteItem();
            // If we've removed item 1, the heap should be empty.
            if(i==1) {
                if( !H.isEmpty() ) System.out.println("Expected the heap to be empty, but it wasn't.");
            }
            else {
                // Otherwise, the item left at the top of the heap should be equal to i-1.
                if(H.item() != i-1) System.out.println("Expected current item to be " + (i-1) + ", got " + H.item());
                if(!H.hasHeapProperty()) System.out.println("Does not have heap property.");
            }
        }

        System.out.println("Regression Test Complete.");
    }
}