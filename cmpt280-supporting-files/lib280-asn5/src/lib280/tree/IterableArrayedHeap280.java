package lib280.tree;

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class IterableArrayedHeap280<I extends Comparable<? super I>> extends ArrayedHeap280<I> {

	/**
	 * Create an iterable heap with a given capacity.
	 * @param cap The maximum number of elements that can be in the heap.
	 */
	public IterableArrayedHeap280(int cap) { super(cap); }

	// TO DO
	// Add iterator() and deleteAtPosition() methods here.

	/**
	 * Creates and returns an iterator for the heap
	 * @return An ArrayedBinaryTreeIterator280 for this heap
	 */
	public ArrayedBinaryTreeIterator280<I> iterator() {
		return new ArrayedBinaryTreeIterator280<I>(this);
	}

	/**
	 * Deletes the item at the current position of the iterator
	 * @param iter The iterator pointing to the item to be deleted
	 * @throws ContainerEmpty280Exception If the heap is empty
	 * @throws NoCurrentItem280Exception If the iterator does not point to an existing item
	 */
	public void deleteAtPosition(ArrayedBinaryTreeIterator280<I> iter) throws ContainerEmpty280Exception, NoCurrentItem280Exception {
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot delete an item from an empty heap");
		}
		if (!iter.itemExists()) {
			throw new NoCurrentItem280Exception("The iterator does not point to an existing item");
		}

		I target = iter.item();
		int positionToDelete = -1;
		for (int i = 1; i <= this.count; i++) {
			if (this.items[i].equals(target)) {
				positionToDelete = i;
				break;
			}
		}

		if (positionToDelete == -1) {
			throw new NoCurrentItem280Exception("The item pointed to by the iterator was not found in the heap");
		}

		// If more than one element and we are not deleting the last element
		// copy the last element in the array to the position to be deleted
		if (this.count > 1 && positionToDelete != this.count) {
			this.items[positionToDelete] = this.items[this.count];
		}
		this.count--;

		// If the heap is now empty, nothing more to do
		if (this.count == 0) {
			return;
		}

		int n = positionToDelete;

		// Shift the element downward as needed while the current node has a left child
		while (findLeftChild(n) <= count) {
			int child = findLeftChild(n);
			// For a max-heap, choose the larger child
			if (child + 1 <= count && items[child].compareTo(items[child + 1]) < 0) {
				child++;
			}
			if (items[n].compareTo(items[child]) < 0) {
				I temp = items[n];
				items[n] = items[child];
				items[child] = temp;
				n = child;
			} else {
				break;
			}
		}

		// Shift the element upward as needed to maintain the heap order
		while (n > 1 && items[n / 2].compareTo(items[n]) < 0) {
			I temp = items[n];
			items[n] = items[n / 2];
			items[n / 2] = temp;
			n = n / 2;
		}
	}
}