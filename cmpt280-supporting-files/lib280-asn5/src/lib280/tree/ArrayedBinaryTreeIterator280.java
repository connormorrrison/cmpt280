package lib280.tree;

import lib280.base.LinearIterator280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.NoCurrentItem280Exception;

public class ArrayedBinaryTreeIterator280<I> extends ArrayedBinaryTreePosition280 implements LinearIterator280<I> {

	// This is a reference to the tree that created this iterator object.
	ArrayedBinaryTree280<I> tree;

	// An integer that represents the cursor position is inherited from
	// ArrayedBinaryTreePosition280.

	/**
	 * Create a new iterator from a given heap.
	 *
	 * @param t The heap for which to create a new iterator.
	 */
	public ArrayedBinaryTreeIterator280(ArrayedBinaryTree280<I> t) {
		super(t.currentNode);
		this.tree = t;
	}

	/**
	 * Returns true if the iterator is positioned before the first element
	 * @return true if the iterator is before the first element, false otherwise
	 */
	@Override
	public boolean before() {
		return this.currentNode < 1;
	}

	/**
	 * Returns true if the iterator is positioned after the last element
	 * @return true if the iterator is after the last element, false otherwise
	 */
	@Override
	public boolean after() {
		return this.currentNode > this.tree.count();
	}

	/**
	 * Advances the iterator to the next element
	 * @throws AfterTheEnd280Exception if the iterator is already after the last element
	 */
	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if (this.after()) {
			throw new AfterTheEnd280Exception("Cannot go forth, already after the end.");
		}
		this.currentNode++;
	}

	/**
	 * Positions the iterator at the first element in the tree
	 * @throws ContainerEmpty280Exception if the tree is empty
	 */
	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		if (this.tree.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot go first, already empty tree.");
		}
		this.currentNode = 1;
	}

	/**
	 * Positions the iterator before the first element
	 */
	@Override
	public void goBefore() {
		this.currentNode = 0;
	}

	/**
	 * Positions the iterator after the last element
	 */
	@Override
	public void goAfter() {
		this.currentNode = this.tree.count() + 1;
	}

	/**
	 * Returns the current item pointed to by the iterator
	 * @return the current item
	 * @throws NoCurrentItem280Exception if there is no current item
	 */
	@Override
	public I item() throws NoCurrentItem280Exception {
		if (!this.itemExists()) {
			throw new NoCurrentItem280Exception("There is no current item.");
		}
		return this.tree.items[this.currentNode];
	}

	/**
	 * Checks if the iterator currently points to a valid item
	 * @return true if there is a current item, false otherwise
	 */
	@Override
	public boolean itemExists() {
		return this.tree.count() > 0 && (this.currentNode >= 1 && this.currentNode <= this.tree.count());
	}

//	// Main method for regression testing
//	public static void main(String[] args) {
//		boolean failed = false;
//
//		class ConcreteArrayedBinaryTree280 extends ArrayedBinaryTree280<Integer> {
//			public ConcreteArrayedBinaryTree280(int cap) {
//				super(cap);
//				items = new Integer[cap + 1];
//			}
//		}
//
//		// Create a tree with capacity 10
//		ConcreteArrayedBinaryTree280 tree = new ConcreteArrayedBinaryTree280(10);
//
//		// Populate the tree
//		tree.count = 5;
//		tree.items[1] = 100;
//		tree.items[2] = 200;
//		tree.items[3] = 300;
//		tree.items[4] = 400;
//		tree.items[5] = 500;
//
//		ArrayedBinaryTreeIterator280<Integer> iter = new ArrayedBinaryTreeIterator280<>(tree);
//
//		// Test initial state: should be before the first element and no item exists
//		if (!iter.before()) {
//			System.out.println("FAIL: Expected iterator to be before first element initially.");
//			failed = true;
//		}
//		if (iter.itemExists()) {
//			System.out.println("FAIL: Expected itemExists() to be false initially.");
//			failed = true;
//		}
//
//		// Test goFirst()
//		try {
//			iter.goFirst();
//			if (iter.currentNode != 1) {
//				System.out.println("FAIL: After goFirst(), expected currentNode to be 1.");
//				failed = true;
//			}
//			if (iter.before()) {
//				System.out.println("FAIL: After goFirst(), before() should be false.");
//				failed = true;
//			}
//			if (iter.after()) {
//				System.out.println("FAIL: After goFirst(), after() should be false.");
//				failed = true;
//			}
//			if (iter.item() != 100) {
//				System.out.println("FAIL: After goFirst(), expected item() to return 100.");
//				failed = true;
//			}
//		} catch (ContainerEmpty280Exception | NoCurrentItem280Exception e) {
//			System.out.println("FAIL: Unexpected exception in goFirst(): " + e.getMessage());
//			failed = true;
//		}
//
//		// Test goForth()
//		try {
//			iter.goForth();
//			if (iter.currentNode != 2) {
//				System.out.println("FAIL: After goForth(), expected currentNode to be 2.");
//				failed = true;
//			}
//			if (iter.item() != 200) {
//				System.out.println("FAIL: After goForth(), expected item() to return 200.");
//				failed = true;
//			}
//		} catch (AfterTheEnd280Exception | NoCurrentItem280Exception e) {
//			System.out.println("FAIL: Unexpected exception in goForth(): " + e.getMessage());
//			failed = true;
//		}
//
//		// Test goAfter()
//		iter.goAfter();
//		if (iter.currentNode != tree.count + 1) {
//			System.out.println("FAIL: After goAfter(), expected currentNode to be count+1 (" + (tree.count+1) + ").");
//			failed = true;
//		}
//		if (!iter.after()) {
//			System.out.println("FAIL: After goAfter(), after() should be true.");
//			failed = true;
//		}
//		try {
//			iter.item();
//			System.out.println("FAIL: Expected exception calling item() when after the last element.");
//			failed = true;
//		} catch (NoCurrentItem280Exception e) {
//			// Expected
//		}
//
//		// Test goBefore()
//		iter.goBefore();
//		if (iter.currentNode != 0) {
//			System.out.println("FAIL: After goBefore(), expected currentNode to be 0.");
//			failed = true;
//		}
//		if (!iter.before()) {
//			System.out.println("FAIL: After goBefore(), before() should be true.");
//			failed = true;
//		}
//		try {
//			iter.item();
//			System.out.println("FAIL: Expected exception calling item() when before the first element.");
//			failed = true;
//		} catch (NoCurrentItem280Exception e) {
//			// Expected
//		}
//
//		// Test exception on goForth() when already after
//		try {
//			// First, reset iterator to first element
//			iter.goFirst();
//			// Go through all valid items.
//			for (int i = 1; i <= tree.count; i++) {
//				iter.goForth();
//			}
//			// At this point, currentNode should be count+1
//			if (iter.currentNode != tree.count + 1) {
//				System.out.println("FAIL: Expected currentNode to be " + (tree.count + 1) + " after iterating through all items.");
//				failed = true;
//			}
//			// One more goForth() should throw an exception
//			try {
//				iter.goForth();
//				System.out.println("FAIL: Expected exception on goForth() when already after the last element.");
//				failed = true;
//			} catch (AfterTheEnd280Exception e) {
//				// Expected exception
//			}
//		} catch (Exception e) {
//			System.out.println("FAIL: Unexpected exception when testing goForth() after last element: " + e.getMessage());
//			failed = true;
//		}
//
//		if (!failed) {
//			System.out.println("All tests passed.");
//		}
//	}

}
