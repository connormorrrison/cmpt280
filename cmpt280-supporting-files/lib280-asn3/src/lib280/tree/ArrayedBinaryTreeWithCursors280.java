package lib280.tree;

import lib280.base.Cursor280;
import lib280.base.CursorPosition280;
import lib280.dictionary.Dict280;
import lib280.exception.*;

public class ArrayedBinaryTreeWithCursors280<I> extends
		ArrayedBinaryTree280<I> implements Dict280<I>, Cursor280<I> {

	protected boolean searchesRestart;

	public ArrayedBinaryTreeWithCursors280(int cap) {
		super(cap);
		searchesRestart = true;
	}

	@Override
	public I obtain(I y) throws ItemNotFound280Exception {
		CursorPosition280 saved = this.currentPosition();
		this.goFirst();
		while(this.itemExists()) {
			if( membershipEquals(this.item(), y)) {
				I found = this.item();
				this.goPosition(saved);
				return found;
			}
			this.goForth();
		}
		this.goPosition(saved);
		throw new ItemNotFound280Exception("The given item could not be found.");
	}

	@Override
	public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception {
		// TO DO - Implement this method - make sure you understand the arrayed-binary-
		//        tree insertion algorithm before you do.
		// Case 1: the tree is full
		if (this.isFull()) {
			throw new ContainerFull280Exception("The container is full.");
		}

		// Case 2: the tree has available capacity
		this.count += 1;
		this.items[this.count] = x;
		// Adjust cursor position
		if (this.count == 1) {
			// Case 2a: the list has only 1 element, move cursor to the first item
			this.currentNode = 1;
		} else if (this.currentNode == this.count - 1) {
			// Case 2b: the cursor is on the second last element, move it to the last
			this.currentNode = this.count;
		}

		// Note:  because this method is defined in the interface where it appears 
		//        to be *capable* of throwing a DuplicateItems280Exception it must be
		//        similarly defined here.  However, this does not mean
		//        that you *must* throw this exception or that you *must* forbid 
		//        duplicate items in the tree.
		//        The unit test does not attempt to insert duplicate items.
	}

	@Override
	public void deleteItem() throws NoCurrentItem280Exception {
		// TO DO - Implement this method - make sure you understand the arrayed-binary-
		//        tree deletion algorithm before you do.
		// Case 1: no current item to delete
		if (!this.itemExists()) {
			throw new NoCurrentItem280Exception("No current item.");
		}

		// Case 2: if the cursor is on the last element
		if (this.currentNode == this.count) {
			this.items[this.currentNode] = null;
			this.count--;
			if (this.count == 0) {
				// Case 2a: tree becomes empty, set cursor to "before"
				this.currentNode = 0;
			} else {
				// Case 2b: move cursor to new last element
				this.currentNode = this.count;
			}
		} else {
			// Case 3: deleting an internal node
			// Replace the current item with the last item
			this.items[this.currentNode] = this.items[this.count];
			this.items[this.count] = null;
			this.count--;
			// Case 3a: cursor remains at the same index
		}

		// Note: The expected behaviour is that after the deletion, the cursor is to 
	    //       remain on the same array index,
		//       i.e. the cursor remains at the item that becomes the deleted item's
		//       replacement, copied from the end of the array.
		// 
		//       Don't forget to handle the special case that arises when the
		//       cursor is on the last node in the array (bottom-right-most node in the tree)!
		//       You can infer the expected behaviour in this case from the test commented
		//       with "Remove the last item".
	}

	@Override
	public void delete(I x) throws ItemNotFound280Exception {
		// TO DO - Implement this method - make sure you understand the arrayed-binary-
		//        tree deletion algorithm before you do.  As a postcondition, this method
		//        should leave the cursor where it was originally positioned at the
		//        start of the method.
		CursorPosition280 originalPosition = this.currentPosition();

		// Case 1: search for x
		this.goBefore();
		this.search(x);
		if (!this.itemExists()) {
			// Case 1a: item not found
			this.goPosition(originalPosition);  // If not found, restore the old cursor and throw exception
			throw new ItemNotFound280Exception("Item not found.");
		}
		// Case 1b: if x is found, delete it
		this.deleteItem();

		// Case 2: if tree is now empty, move cursor before
		if (this.isEmpty()) {
			this.goBefore();
		} else {
			// Case 3: check if original cursor is still valid
			int originalIndex = ((ArrayedBinaryTreePosition280) originalPosition).currentNode;
			if (originalIndex <= this.count) {
				// Case 3a: cursor is still valid, restore it
				this.goPosition(originalPosition);
			} else {
				// Case 3b: cursor was at the last item, move to new last
				this.currentNode = this.count;
			}
		}
	}

	@Override
	public boolean has(I y) {
		// TO DO - Implement this method
		CursorPosition280 originalPosition = this.currentPosition();

		// Case 1: search for x from the beginning
		this.goBefore();
		this.search(y);
		boolean result = this.itemExists();
		this.goPosition(originalPosition);
		return result;
	}

	@Override
	public boolean membershipEquals(I x, I y) {
	    return x.equals(y);
	}

	@Override
	public void search(I x) {
		// TO DO - Implement this method
		// Case 1: restart search if required or if cursor is before first element
		if (this.searchesRestart || this.before()) {
			try {
				this.goFirst();
			} catch (ContainerEmpty280Exception e) {
				return;
			}
		} else if (this.after()) {
			// Case 2: cursor is already after the last element, nothing to do
			return;
		} else {
			// Case 3: cursor is in the middle of the tree
			if (membershipEquals(x, this.item())) {
				try {
					this.goForth();
				} catch (AfterTheEnd280Exception e) {
					// Case 3a: reached end, stop search
					return;
				}
			}
		}

		// Case 4: traverse until x is found or end is reached
		while (this.itemExists()) {
			if (membershipEquals(x, this.item())) {
				// Case 4a: found x, stop search
				return;
			}
			try {
				this.goForth();
			} catch (AfterTheEnd280Exception e) {
				// Case 4b: reached end, exit loop
				break;
			}
		}
	}


	@Override
	public boolean before() {
		// TO DO - Implement this method
        // Case 1: the tree is empty
		if (this.isEmpty()) {
			return true;
		} else {
			// Case 2: check if cursor is before the first element
			return this.currentNode == 0;
		}
	}

	@Override
	public boolean after() {
		// TO DO - Implement this method
		// Case 1: the tree is empty
		if (this.isEmpty()) {
			return true;
		} else {
			// Case 2: check if cursor is after the last element
			return this.currentNode == this.count + 1;
		}
	}

	@Override
	public void goForth() throws AfterTheEnd280Exception {
		// TO DO - Implement this method
		// Case 1: the cursor is after the end
		if (this.currentNode > this.count) {
			throw new AfterTheEnd280Exception("The cursor is after the end.");
		} else {
			// Case 2: move cursor forward
			this.currentNode += 1;
		}
	}

	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		// TO DO - Implement this method
		// Case 1: the tree is empty
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("The tree is empty.");
		} else {
			// Case 2: move the cursor to the first element
			this.currentNode = 1;
		}
	}

	@Override
	public void goBefore() {
		// TO DO - Implement this method
		this.currentNode = 0;
	}

	@Override
	public void goAfter() {
		// TO DO - Implement this method
		this.currentNode = this.count + 1;
	}

	@Override
	public void restartSearches() {
		this.searchesRestart = true;
	}

	@Override
	public void resumeSearches() {
		this.searchesRestart = false;
	}

    @Override
	public CursorPosition280 currentPosition() {
		return new ArrayedBinaryTreePosition280(this.currentNode);
	}

	@Override
	public void goPosition(CursorPosition280 c) {
		if (!(c instanceof ArrayedBinaryTreePosition280))
			throw new InvalidArgument280Exception("The cursor position parameter"
					    + " must be a ArrayedBinaryTreePosition280<I>");

		this.currentNode = ((ArrayedBinaryTreePosition280)c).currentNode;
	}

	/**
	 * Move the cursor to the parent of the current node.
	 * @precond Current node is not the root.
	 * @throws InvalidState280Exception when the cursor is on the root already.
	 */
	public void parent() throws InvalidState280Exception {
        // TO DO - Implement this method
		// Case 1: cursor is at the root
		if (this.currentNode == 1) {
			throw new InvalidState280Exception("Cannot move the cursor to the parent of the current node.");
		}
		// Case 2: move cursor to parent node
		this.currentNode = this.findParent(this.currentNode);
	}

	/**
	 * Move the cursor to the left child of the current node.
	 *
	 * @precond The tree must not be empty and the current node must have a left child.
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @throws InvalidState280Exception if the current node has no left child.
	 */
	public void goLeftChild()  throws InvalidState280Exception, ContainerEmpty280Exception {
        // TO DO - Implement this method
		// Case: the tree is empty
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("The tree is empty.");
		}

		int leftChild = this.findLeftChild(this.currentNode);
		// Case: no left child
		if (leftChild > this.count) {
			throw new InvalidState280Exception("Cannot move the cursor to the left child of the current node.");
		} else {
			// Case 3: move cursor to left child
			this.currentNode = leftChild;
		}
	}

	/**
	 * Move the cursor to the right child of the current node.
	 *
	 * @precond The tree must not be empty and the current node must have a right child.
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @throws InvalidState280Exception if the current item has no right child.
	 */
	public void goRightChild() throws InvalidState280Exception, ContainerEmpty280Exception {
        // TO DO - Implement this method
		// Case: the tree is empty
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("The tree is empty.");
		}

		int rightChild = findRightChild(this.currentNode);
		// Case: no right child
		if (rightChild > this.count) {
			throw new InvalidState280Exception("The current node has no right child.");
		} else {
			// Case 3: move cursor to right child
			this.currentNode = rightChild;
		}
	}

	/**
	 * Move the cursor to the sibling of the current node.
	 *
	 * @precond The current node must have a sibling.  The tree must not be empty.
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 * @throws InvalidState280Exception if the current item has no sibling.
	 */
	public void goSibling() throws InvalidState280Exception, ContainerEmpty280Exception {
        // TO DO - Implement this method
		// Case: the tree is empty
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot go to sibling in an empty tree.");
		}

		int sibling;
		if (this.currentNode % 2 == 0) {
			// Case 2: current node is left child, move to right sibling
			sibling = this.currentNode + 1;  // left -> right
		}
		else {
			// Case 3: current node is right child, move to left sibling
			sibling = this.currentNode - 1;  // right -> left
		}

		// Case 4: check if sibling is valid and shares the same parent
		if (sibling < 1 || sibling > this.count || this.findParent(sibling) != this.findParent(this.currentNode)) {
			throw new InvalidState280Exception("No valid sibling exists.");
		}
		// Case 5: move cursor to sibling
		this.currentNode = sibling;
	}

	/**
	 * Move the cursor to the root of the tree.
	 *
	 * @precond The tree must not be empty.
	 * @throws ContainerEmpty280Exception if the tree is empty.
	 */
	public void root() throws ContainerEmpty280Exception {
        // TO DO - Implement this method
		// Case: tree is empty
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("The tree is empty.");
		} else {
			// Case: move cursor to root
			this.currentNode = 1;
		}
	}


	public static void main(String[] args) {
		ArrayedBinaryTreeWithCursors280<Integer> T = new ArrayedBinaryTreeWithCursors280<Integer>(10);

		// IsEmpty on empty tree.
		if(!T.isEmpty()) System.out.println("Test of isEmpty() on empty tree failed.");

		// test goAfter() when the tree is empty.
		T.goAfter();
		if(!T.before()) System.out.println("Cursor should be before in an empty tree, but it isn't.");
		if(!T.after()) System.out.println("Cursor should be after() in an empty tree but it isn't.");


		// Test root() on empty tree.
		Exception x = null;
		try {
			T.root();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to root of empty tree.  Got none.");
		}

		// test goFirst() on empty tree
		x = null;
		try {
			T.goFirst();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to first elelement of empty tree.  Got none.");
		}



		// Test goLeftChild() on empty tree.
		x = null;
		try {
			T.goLeftChild();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to left child in empty tree.  Got none.");
		}

		// Test goLeftChild() on empty tree.
		x = null;
		try {
			T.goRightChild();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to right child in empty tree.  Got none.");
		}


		// Check itemExists on empty tree
		if(T.itemExists() ) System.out.println("itemExists() returned true on an empty tree.");

		// Insert on empty tree.
		T.insert(1);

		// Check ItemExists on tree with one element.
		T.root();
		if(!T.itemExists() ) System.out.println("itemExists() returned false on a tree with one element with cursor at the root.");

		// isEmpty on tree with 1 element.
		if(T.isEmpty()) System.out.println("Test of isEmpty() on non-empty tree failed.");

		// Insert on tree with 1 element
		T.insert(2);

		// Insert some more elements
		for(int i=3; i <= 10; i++) T.insert(i);

		if(T.count() != 10  ) System.out.println("Expected tree count to be 10, got "+ T.count());


		// Test for isFull on a full tree.
		if(!T.isFull()) System.out.println("Test of isFull() on a full tree failed.");

		// Test insert on a full tree
		x = null;
		try {
			T.insert(11);
		}
		catch(ContainerFull280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception inserting into a full tree.  Got none.");
		}


		// Explicitly test search():

		// Search for item at root:
		T.search(1);
		if( !T.itemExists() )
			System.out.println("Error: search for item 1 failed when 1 is in the tree.");
		if( T.item() != 1)
			System.out.println("Error: search for item 1 did not result in cursor being at item 1.");


		// Search for item in middle
		T.search(5);
		if( !T.itemExists() )
			System.out.println("Error: search for item 5 failed when 5 is in the tree.");
		if( T.item() != 5)
			System.out.println("Error: search for item 5 did not result in cursor being at item 5.");

		// Search for item at end of array
		T.search(10);
		if( !T.itemExists() )
			System.out.println("Error: search for item 10 failed when 10 is in the tree.");
		if( T.item() != 10)
			System.out.println("Error: search for item 10 did not result in cursor being at item 10.");


		// Test positioning methods

		// Test root()
		T.root();
		if( T.item() != 1 ) System.out.println("Expected item at root to be 1, got " + T.item());

		T.goLeftChild();

		if( T.item() != 2 ) System.out.println("Expected current item to be 2, got " + T.item());

		T.goRightChild();
		if( T.item() != 5 ) System.out.println("Expected current item to be 5, got " + T.item());


		T.goLeftChild();
		if( T.item() != 10 ) System.out.println("Expected current item to be 10,  got " + T.item());

		// Current node now has no children.
		x = null;
		try {
			T.goLeftChild();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to left child of a leaf.  Got none.");
		}

		x = null;
		try {
			T.goRightChild();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to right child of a leaf.  Got none.");
		}

		// Remove the last item ( a leaf)
		T.deleteItem();
		if( T.item() != 9 ) System.out.println("Expected current item to be 9, got " + T.item());

		T.parent();


		// Remove a node with 2 children.  The right child 9 gets promoted.
		T.deleteItem();
		if( T.item() != 9 ) System.out.println("Expected current item to be 9, got " + T.item());


		// Remove a node with 1 child.  The left child 8 gets promoted.
		T.deleteItem();
		if( T.item() != 8 ) System.out.println("Expected current item to be 8, got " + T.item());

		// Remove the root successively.  There are 7 items left.
		T.root();
		T.deleteItem();
		if( T.item() != 7 ) System.out.println("Expected root to be 7, got " + T.item());

		T.deleteItem();
		if( T.item() != 6 ) System.out.println("Expected root to be 6, got " + T.item());

		T.deleteItem();
		if( T.item() != 5 ) System.out.println("Expected root to be 5, got " + T.item());

		T.deleteItem();
		if( T.item() != 8 ) System.out.println("Expected root to be 8, got " + T.item());

		T.deleteItem();
		if( T.item() != 3 ) System.out.println("Expected root to be 3, got " + T.item());

		T.deleteItem();
		if( T.item() != 2 ) System.out.println("Expected root to be 2, got " + T.item());

		// Tree has one item.  Try parent() on one item.
		x = null;
		try {
			T.parent();
		}
		catch( InvalidState280Exception e ) {
			x = e;
		}
		finally {
			if( x == null) System.out.println("Expected exception moving to parent of root.  Got none.");
		}


		// Try to go to the sibling
		x = null;
		try {
			T.goSibling();
		}
		catch(InvalidState280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling when at the root.  Got none.");
		}



		T.deleteItem();


		// Tree should now be empty
		if(!T.isEmpty()) System.out.println("Expected empty tree.  isEmpty() returned false.");

		if(T.capacity() != 10) System.out.println("Expected capacity to be 10, got "+ T.capacity());

		if(T.count() != 0  ) System.out.println("Expected tree count to be 0, got "+ T.count());

		// Remove from empty tree.
		x = null;
		try {
			T.deleteItem();
		}
		catch(NoCurrentItem280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception deleting from an empty tree.  Got none.");
		}



		// Try to go to the sibling
		x = null;
		try {
			T.goSibling();
		}
		catch(ContainerEmpty280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling in empty tree tree.  Got none.");
		}


		T.insert(1);
		T.root();

		// Try to go to the sibling when there is no child.
		x = null;
		try {
			T.goSibling();
		}
		catch(InvalidState280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception moving to sibling of node with no sibling.  Got none.");
		}

		T.goBefore();
		if(!T.before()) System.out.println("Error: Should be in 'before' position, but before() reports otherwise.");
		if(T.after()) System.out.println("Error: T.after() reports cursor in the after position when it should not be.");

		T.goForth();
		if(T.before()) System.out.println("Error: T.before() reports cursor in the before position when it should not be.");
		if(T.after()) System.out.println("Error: T.after() reports cursor in the after position when it should not be.");

		T.goForth();
		if(!T.after()) System.out.println("Error: Should be in 'after' position, but after() reports otherwise.");
		if(T.before()) System.out.println("Error: T.before() reports cursor in the before position when it should not be.");

		x=null;
		try {
			T.goForth();
		}
		catch(AfterTheEnd280Exception e) {
			x = e;
		}
		finally {
			if( x == null ) System.out.println("Expected exception advancing cursor when already after the end.  Got none.");
		}


		int y=-1;
		T.goBefore();
		try {
			y =  T.obtain(1);
		}
		catch( ItemNotFound280Exception e ) {
			System.out.println("Error: Unexpected exception occured when attempting T.obtain(1).");
		}
		finally {
			if(y != 1 ) System.out.println("Obtained item should be 1 but it isn't.");
			if(!T.before()) System.out.println("Error: cursor should still be in the before() position after T.obtain(1), but it isn't.");
		}

		if(!T.has(1)) System.out.println("Error: Tree has element 1, but T.has(1) reports that it does not.");


		T.insert(2);
		T.insert(3);
		T.insert(4);
		T.insert(5);
		T.insert(6);
		T.insert(7);

		// Test goSibling()
		try {
			T.goFirst();
			T.goLeftChild();
			T.goSibling();
		}
		catch (Exception e) {
			System.out.println("Error: unexpected exception attempting to move cursor to left child of root.");
		}
		finally {
			if(T.item() != 3) {
				System.out.println("Error: Cursor should be on 3 (sibling of 2) but it is not.");
			}
		}

		try {
			T.goSibling();
		}
		catch (Exception e) {
			System.out.println("Error: unexpected exception attempting to move cursor to left child of root.");
		}
		finally {
			if(T.item() != 2) {
				System.out.println("Error: Cursor should be on 2 (sibling of 3) but it is not.");
			}
		}

		// Explicitly test delete()

		// Test deleting root:
		T.delete(1);
		T.root();
		if( T.item() != 7) {
			System.out.println("Error: Tree state after deletion of root is incorrect.");
		}
		T.goPosition(new ArrayedBinaryTreePosition280(T.count()));
		if( T.item() != 6) {
			System.out.println("Error: Tree state after deletion of root is incorrect.");
		}

		// Test deleting leaf node.
		T.delete(5);
		T.goPosition(new ArrayedBinaryTreePosition280(T.count()));
		if( T.item() != 6) {
			System.out.println("Error: Tree state after deletion of 5 is incorrect.");
		}

		// Test deleting internal node.
		T.delete( 2);
		T.goPosition(new ArrayedBinaryTreePosition280(2));
		if( T.item() != 6 ) {
			System.out.println("Error: Tree state after deletion of 2 is incorrect.");
		}
		T.goPosition(new ArrayedBinaryTreePosition280(T.count()));
		if( T.item() != 4) {
			System.out.println("Error: Tree state after deletion of 2 is incorrect.");
		}


		if(T.count() != 4) {
			System.out.println("There should be 4 items in the tree now, but T.count() says otherwise.");
		}

// Test whether search() respects the "searchesRestart" variable
		T.insert(9);
		T.insert(3);
		T.insert(6);
		T.insert(4);
		T.insert(4);

		T.restartSearches();
		// This search should succeed
		T.search(9);
		if(!T.itemExists()) System.out.println("Error: Initial search for 9 failed, but should have succeeded.");
		// And so should this one because the search should restart (there is only one 9 in the tree)
		T.search(9);
		if(!T.itemExists()) System.out.println("Error: Restarted search for 9 failed, but should have succeeded.");

		T.resumeSearches();
		T.goBefore();
		// This search should succeed and find the only occurrence of 9.
		T.search(9);
		if(!T.itemExists()) System.out.println("Error: Search for 9 failed, but should have succeeded.");
		// This search should fail because there are no more occurrences of 9.
		T.search(9);
		if(T.itemExists()) System.out.println("Error: Search for second 9 succeeded when it should have failed.  Is search() not respecting the 'searchesContinue' variable?");

		T.goBefore();  // Start a new search from the beginning.
		// This search should succeed and find the first occurrence of 6.
		T.search(6);
		if(!T.itemExists()) System.out.println("Error: Search for first 6 failed, but should have succeeded.");
		// This search should succeed and find the second occurrence of 6.
		T.search(6);
		if(!T.itemExists()) System.out.println("Error: Search for second 6 failed, but should have succeeded.");
		// This search should fail because there are no more occurrences of 6.
		// This search should succeed and find the first occurrence of 6.
		T.search(6);
		if(T.itemExists()) System.out.println("Error: Search for third 6 succeeded when it should have failed.");

		T.goBefore();  // Start a new search from the beginning.
		// Find all three occurrences of 4.
		T.search(4);
		if(!T.itemExists()) System.out.println("Error: Search for first 4 failed, but should have succeeded.");
		// This search should succeed and find the second occurrence of 6.
		T.search(4);
		if(!T.itemExists()) System.out.println("Error: Search for second 4 failed, but should have succeeded.");
		// This search should succeed and find the third occurrence of 4.
		T.search(4);
		if(!T.itemExists()) System.out.println("Error: Search for third 4 failed, but should have succeeded.");
		// This search should fail because there are no more occurrences of 4.
		T.search(4);
		if(T.itemExists()) System.out.println("Error: Search for fourth 4 succeeded when it should have failed.");

		System.out.println("Regression test complete.");	}

}
