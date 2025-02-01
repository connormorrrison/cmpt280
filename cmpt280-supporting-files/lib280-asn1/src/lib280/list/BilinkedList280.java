package lib280.list;


import lib280.base.BilinearIterator280;
import lib280.base.CursorPosition280;
import lib280.base.Pair280;
import lib280.exception.*;

/**	This list class incorporates the functions of an iterated 
	dictionary such as has, obtain, search, goFirst, goForth, 
	deleteItem, etc.  It also has the capabilities to iterate backwards 
	in the list, goLast and goBack. */
public class BilinkedList280<I> extends LinkedList280<I> implements BilinearIterator280<I>
{
	/* 	Note that because firstRemainder() and remainder() should not cut links of the original list,
		the previous node reference of firstNode is not always correct.
		Also, the instance variable prev is generally kept up to date, but may not always be correct.  
		Use previousNode() instead! */

	/**	Construct an empty list.
		Analysis: Time = O(1) */
	public BilinkedList280()
	{
		super();
	}

	/**
	 * Create a BilinkedNode280 this Bilinked list.  This routine should be
	 * overridden for classes that extend this class that need a specialized node.
	 * @param item - element to store in the new node
	 * @return a new node containing item
	 */
	protected BilinkedNode280<I> createNewNode(I item)
	{
		// TO DO
		return new BilinkedNode280<I>(item);
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insertFirst(I x) 
	{
		// TO DO
		// Create new node
		BilinkedNode280<I> newNode = createNewNode(x);

		// Case: the list is empty
		if (this.isEmpty()) {
			// Update next and prev pointers
			newNode.setNextNode(null);
			newNode.setPreviousNode(null);

			// Update head and tail references
			this.head = newNode;
			this.tail = newNode;
		} else {
			// Case: the list has one or more elements
			// Update next and prev pointers
			newNode.setNextNode(this.head);
			newNode.setPreviousNode(null);

			// Update head's prev pointer
			((BilinkedNode280<I>)this.head).setPreviousNode(newNode);

			// Update cursor position
			// If cursor is currently at the old head, adjust prev cursor pointer to the new head
			if (this.position == this.head) {
				prevPosition = newNode;
			}

			// The new node becomes the head
			this.head = newNode;
		}
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insert(I x) 
	{
		this.insertFirst(x);
	}

	/**
	 * Insert an item before the current position.
	 * @param x - The item to be inserted.
	 */
	public void insertBefore(I x) throws InvalidState280Exception {
		if( this.before() ) throw new InvalidState280Exception("Cannot insertBefore() when the cursor is already before the first element.");
		
		// If the item goes at the beginning or the end, handle those special cases.
		if( this.head == position ) {
			insertFirst(x);  // special case - inserting before first element
		}
		else if( this.after() ) {
			insertLast(x);   // special case - inserting at the end
		}
		else {
			// Otherwise, insert the node between the current position and the previous position.
			BilinkedNode280<I> newNode = createNewNode(x);
			newNode.setNextNode(position);
			newNode.setPreviousNode((BilinkedNode280<I>)this.prevPosition);
			prevPosition.setNextNode(newNode);
			((BilinkedNode280<I>)this.position).setPreviousNode(newNode);
			
			// since position didn't change, but we changed it's predecessor, prevPosition needs to be updated to be the new previous node.
			prevPosition = newNode;			
		}
	}
	
	
	/**	Insert x before the current position and make it current item. <br>
		Analysis: Time = O(1)
		@param x item to be inserted before the current position */
	public void insertPriorGo(I x) 
	{
		this.insertBefore(x);
		this.goBack();
	}

	/**	Insert x after the current item. <br>
		Analysis: Time = O(1) 
		@param x item to be inserted after the current position */
	public void insertNext(I x) 
	{
		if (isEmpty() || before())
			insertFirst(x); 
		else if (this.position==lastNode())
			insertLast(x); 
		else if (after()) // if after then have to deal with previous node  
		{
			insertLast(x); 
			this.position = this.prevPosition.nextNode();
		}
		else // in the list, so create a node and set the pointers to the new node 
		{
			BilinkedNode280<I> temp = createNewNode(x);
			temp.setNextNode(this.position.nextNode());
			temp.setPreviousNode((BilinkedNode280<I>)this.position);
			((BilinkedNode280<I>) this.position.nextNode()).setPreviousNode(temp);
			this.position.setNextNode(temp);
		}
	}

	/**
	 * Insert a new element at the end of the list
	 * @param x item to be inserted at the end of the list 
	 */
	public void insertLast(I x) 
	{
		// TO DO
		// Create a new node
		BilinkedNode280<I> newNode = createNewNode(x);

		// Case: the list is empty
		if (this.isEmpty()) {
			this.insertFirst(x);
		} else {
			// Case: the list has one or more elements
			// Update next and prev pointers
			newNode.setNextNode(null);
			newNode.setPreviousNode((BilinkedNode280<I>) this.tail);

			// Update tail
			this.tail.setNextNode(newNode);
			this.tail = newNode;

			// Update cursor position
			if (this.after()) {
				this.prevPosition = this.tail;
			}
		}
	}

	/**
	 * Delete the item at which the cursor is positioned
	 * @precond itemExists() must be true (the cursor must be positioned at some element)
	 */
	public void deleteItem() throws NoCurrentItem280Exception
	{
		// TO DO
		// Case: the list is empty
		if (!this.itemExists()) {
			throw new NoCurrentItem280Exception("No current item to delete.");
		}

		// Case: the list has one element
		if (this.position == this.head) {
			this.deleteFirst();
		} else {
			// Case: the list has more than one element
			BilinkedNode280<I> previousNode = ((BilinkedNode280<I>) this.position).previousNode;

			previousNode.setNextNode(this.position.nextNode());  // Relink

			// If there's a node after the current, update its prev pointer
			if (this.position.nextNode() != null) {
				((BilinkedNode280<I>) this.position.nextNode()).setPreviousNode(previousNode);
			}

			// If removed node is tail, update tail pointer
			if (this.position == this.tail) {
				this.tail = this.prevPosition;
			}

			// Adjust cursor and prev pointer
			this.position = this.position.nextNode();
			this.prevPosition = previousNode;
		}
	}

	
	@Override
	public void delete(I x) throws ItemNotFound280Exception {
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete from an empty list.");

		// Save cursor position
		LinkedIterator280<I> savePos = this.currentPosition();
		
		// Find the item to be deleted.
		search(x);
		if( !this.itemExists() ) throw new ItemNotFound280Exception("Item to be deleted wasn't in the list.");

		// If we are about to delete the item that the cursor was pointing at,
		// advance the cursor in the saved position, but leave the predecessor where
		// it is because it will remain the predecessor.
		if( this.position == savePos.cur ) savePos.cur = savePos.cur.nextNode();
		
		// If we are about to delete the predecessor to the cursor, the predecessor 
		// must be moved back one item.
		if( this.position == savePos.prev ) {
			
			// If savePos.prev is the first node, then the first node is being deleted
			// and savePos.prev has to be null.
			if( savePos.prev == this.head ) savePos.prev = null;
			else {
				// Otherwise, Find the node preceding savePos.prev
				LinkedNode280<I> tmp = this.head;
				while(tmp.nextNode() != savePos.prev) tmp = tmp.nextNode();
				
				// Update the cursor position to be restored.
				savePos.prev = tmp;
			}
		}
				
		// Unlink the node to be deleted.
		if( this.prevPosition != null)
			// Set previous node to point to next node.
			// Only do this if the node we are deleting is not the first one.
			this.prevPosition.setNextNode(this.position.nextNode());
		
		if( this.position.nextNode() != null )
			// Set next node to point to previous node 
			// But only do this if we are not deleting the last node.
			((BilinkedNode280<I>)this.position.nextNode()).setPreviousNode(((BilinkedNode280<I>)this.position).previousNode());
		
		// If we deleted the first or last node (or both, in the case
		// that the list only contained one element), update head/tail.
		if( this.position == this.head ) this.head = this.head.nextNode();
		if( this.position == this.tail ) this.tail = this.prevPosition;
		
		// Clean up references in the node being deleted.
		this.position.setNextNode(null);
		((BilinkedNode280<I>)this.position).setPreviousNode(null);
		
		// Restore the old, possibly modified cursor.
		this.goPosition(savePos);
		
	}
	/**
	 * Remove the first item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteFirst() throws ContainerEmpty280Exception
	{
		// TO DO
		// Case: list is empty
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot delete from an empty list.");
		}

		// Save reference to the current head
		BilinkedNode280<I> oldHead = (BilinkedNode280<I>) this.head;

		// Case: list has one element.
		if (this.head == this.tail) {
			this.head = null;
			this.tail = null;
			this.position = null;
			this.prevPosition = null;
		} else {
			// Case: list has more than one element
			// Move the head pointer to the next node
			this.head = this.head.nextNode();
			((BilinkedNode280<I>) this.head).setPreviousNode(null);

			// If the cursor was on the old head, update it to the new head
			if (this.position == oldHead) {
				this.position = this.head;
				this.prevPosition = null;
			}
		}
	}

	/**
	 * Remove the last item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteLast() throws ContainerEmpty280Exception
	{
		// TO DO
		// Case: list is empty
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot delete from an empty list.");
		}

		// Save reference to the old tail
		BilinkedNode280<I> oldTail = (BilinkedNode280<I>) this.tail;

		// Case: list has one element
		if (this.head == this.tail) {
			this.deleteFirst();
		} else {
			// Case: list has more than one element

			// Adjust cursor positions if necessary
			if (this.prevPosition == oldTail) {
				this.prevPosition = oldTail.previousNode();
			}
			// If the cursor is directly on the old tail, move both position and prev back
			else if (this.position == oldTail) {
				this.prevPosition = oldTail.previousNode();
				this.position = oldTail.previousNode();
			}

			// Update the tail to the node before the old tail
			this.tail = oldTail.previousNode();

			// Update the new tail's next pointer
			if (this.tail != null) {
				this.tail.setNextNode(null);
			}
		}
	}

	
	/**
	 * Move the cursor to the last item in the list.
	 * @precond The list is not empty.
	 */
	public void goLast() throws ContainerEmpty280Exception
	{
		// TO DO
		// Case: list is empty
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot go last on an empty list.");
		}

		// Case: list has one or more elements
		this.position = this.tail;
		this.prevPosition = ((BilinkedNode280<I>) this.tail).previousNode();
	}
  
	/**	Move back one item in the list. 
		Analysis: Time = O(1)
		@precond !before() 
	 */
	public void goBack() throws BeforeTheStart280Exception
	{
		// TO DO
		// Case: cursor is before the list
		if (this.before()) {
			throw new BeforeTheStart280Exception("Before the start of a list.");
		}

		// If after, use goLast()
		if (this.after()) {
			this.goLast();
		} else {
			// Case: we're at a valid item
			this.position = this.prevPosition;

			// If we are before, set prev to null
			if (this.position == null) {
				this.prevPosition = null;
			} else {
				// We are not before, set prev to the node before it
				this.prevPosition = ((BilinkedNode280<I>) this.position).previousNode();
			}
		}
	}

	/**	Iterator for list initialized to first item. 
		Analysis: Time = O(1) 
	*/
	public BilinkedIterator280<I> iterator()
	{
		return new BilinkedIterator280<I>(this);
	}

	/**	Go to the position in the list specified by c. <br>
		Analysis: Time = O(1) 
		@param c position to which to go */
	@SuppressWarnings("unchecked")
	public void goPosition(CursorPosition280 c)
	{
		if (!(c instanceof BilinkedIterator280))
			throw new InvalidArgument280Exception("The cursor position parameter" 
					    + " must be a BilinkedIterator280<I>");
		BilinkedIterator280<I> lc = (BilinkedIterator280<I>) c;
		this.position = lc.cur;
		this.prevPosition = lc.prev;
	}

	/**	The current position in this list. 
		Analysis: Time = O(1) */
	public BilinkedIterator280<I> currentPosition()
	{
		return  new BilinkedIterator280<I>(this, this.prevPosition, this.position);
	}

	
  
	/**	A shallow clone of this object. 
		Analysis: Time = O(1) */
	public BilinkedList280<I> clone() throws CloneNotSupportedException
	{
		return (BilinkedList280<I>) super.clone();
	}


	/* Regression test. */
	public static void main(String[] args) {
		// Regression tests for 'createNewNode()'
		// Test 1: Create a new node with 10 and check its item
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			BilinkedNode280<Integer> node = list.createNewNode(10);
			if (node.item != 10) {
				System.out.println("ERROR: createNewNode() Test 1: node.item is not 10.");
			}
		}

		// Test 2: Create a new node with 20 and check its item
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			BilinkedNode280<Integer> node = list.createNewNode(20);
			if (node.item != 20) {
				System.out.println("ERROR: createNewNode() Test 2: node.item is not 20.");
			}
		}

		// Test 3: Check that a new node's next pointer is null
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			BilinkedNode280<Integer> node = list.createNewNode(30);
			if (node.nextNode() != null) {
				System.out.println("ERROR: createNewNode() Test 3: node.nextNode is not null.");
			}
		}

		// Test 4: Check that a new node's previous pointer is null
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			BilinkedNode280<Integer> node = list.createNewNode(40);
			if (node.previousNode != null) {
				System.out.println("ERROR: createNewNode() Test 4: node.previousNode is not null.");
			}
		}

		// Test 5: Ensure that createNewNode() does not modify the list's head or tail
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			BilinkedNode280<Integer> node = list.createNewNode(50);
			if (list.head != null || list.tail != null) {
				System.out.println("ERROR: createNewNode() Test 5: list.head or list.tail was modified.");
			}
		}

		// Regression tests for 'insertFirst()'
		// Test 1: Inserting into an empty list should set both head and tail
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertFirst(100);
			if (list.head == null || list.tail == null || list.head.item != 100 || list.tail.item != 100) {
				System.out.println("ERROR: insertFirst() Test 1: Inserting into an empty list did not set head and tail correctly.");
			}
		}

		// Test 2: Inserting into a non-empty list should update head
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertFirst(100);
			list.insertFirst(200);
			if (list.head.item != 200) {
				System.out.println("ERROR: insertFirst() Test 2: head.item is not 200 after inserting at beginning.");
			}
			// Check that the second node is set to 100
			if (((BilinkedNode280<Integer>) list.head).nextNode().item != 100) {
				System.out.println("ERROR: insertFirst() Test 2: second node's item is not 100.");
			}
		}

		// Test 3: New head's previous pointer should be null
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertFirst(300);
			if (((BilinkedNode280<Integer>)list.head).previousNode != null) {
				System.out.println("ERROR: insertFirst() Test 3: head.previousNode is not null.");
			}
		}

		// Test 4: The old head’s previous pointer should point to the new head
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertFirst(400);
			list.insertFirst(500);
			if (((BilinkedNode280<Integer>) list.head.nextNode()).previousNode != list.head) {
				System.out.println("ERROR: insertFirst() Test 4: old head's previousNode does not point to the new head.");
			}
		}

		// Test 5: The insert() method (which calls insertFirst()) should work correctly
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insert(600);
			if (list.head.item != 600) {
				System.out.println("ERROR: insertFirst() Test 5: insert() did not insert at the beginning correctly.");
			}
		}


		// Regression tests for 'insertLast()'
		// Test 1: Inserting into an empty list using insertLast()
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(700);
			if (list.head == null || list.tail == null || list.head.item != 700 || list.tail.item != 700) {
				System.out.println("ERROR: insertLast() Test 1: Inserting into an empty list did not set head and tail correctly.");
			}
		}

		// Test 2: Inserting into a non-empty list should update tail
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertFirst(800);
			list.insertLast(900);
			if (list.head.item != 800) {
				System.out.println("ERROR: insertLast() Test 2: head.item is not 800.");
			}
			if (list.tail.item != 900) {
				System.out.println("ERROR: insertLast() Test 2: tail.item is not 900.");
			}
			if (((BilinkedNode280<Integer>)list.tail).previousNode.item != 800) {
				System.out.println("ERROR: insertLast() Test 2: tail.previousNode.item is not 800.");
			}
		}

		// Test 3: Inserting multiple elements using insertLast() should preserve order
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(10);
			list.insertLast(20);
			list.insertLast(30);
			BilinkedNode280<Integer> current = (BilinkedNode280<Integer>) list.head;
			if (current.item != 10) {
				System.out.println("ERROR: insertLast() Test 3: first element is not 10.");
			}
			current = (BilinkedNode280<Integer>) current.nextNode();
			if (current.item != 20) {
				System.out.println("ERROR: insertLast() Test 3: second element is not 20.");
			}
			current = (BilinkedNode280<Integer>) current.nextNode();
			if (current.item != 30) {
				System.out.println("ERROR: insertLast() Test 3: third element is not 30.");
			}
		}

		// Test 4: After insertLast(), tail.next should be null
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(40);
			list.insertLast(50);
			if (list.tail.nextNode() != null) {
				System.out.println("ERROR: insertLast() Test 4: tail.nextNode is not null.");
			}
		}

		// Test 5: After multiple insertLast() calls, tail.previous should be set
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(60);
			list.insertLast(70);
			if (((BilinkedNode280<Integer>)list.tail).previousNode == null) {
				System.out.println("ERROR: insertLast() Test 5: tail.previousNode is null when it should not be.");
			}
		}


		// Regression tests for 'deleteItem()'
		// Test 1: Delete the first item from a two-element list
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(1000);
			list.insertLast(1100);
			// Set the cursor to the first element
			list.position = list.head;
			list.prevPosition = null;
			try {
				list.deleteItem();
				if (list.head.item != 1100) {
					System.out.println("ERROR: deleteItem() Test 1: head.item is not 1100 after deleting first item.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: deleteItem() Test 1: Exception thrown: " + e);
			}
		}

		// Test 2: Delete the middle item from a three-element list
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(1200);
			list.insertLast(1300);
			list.insertLast(1400);
			// Set the cursor to middle of the list
			list.position = list.head.nextNode();
			list.prevPosition = list.head;
			try {
				list.deleteItem();
				// Now the list should be 1200 -> 1400
				if (list.head.item != 1200 || list.head.nextNode().item != 1400) {
					System.out.println("ERROR: deleteItem() Test 2: List structure incorrect after deleting middle item.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: deleteItem() Test 2: Exception thrown: " + e);
			}
		}

		// Test 3: Delete the last item
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(1500);
			list.insertLast(1600);
			// Set the cursor to the last element
			list.position = list.tail;
			list.prevPosition = list.head;
			try {
				list.deleteItem();
				if (list.tail.item != 1500) {
					System.out.println("ERROR: deleteItem() Test 3: tail.item is not 1500 after deleting last item.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: deleteItem() Test 3: Exception thrown: " + e);
			}
		}

		// Test 4: Delete the only item in the list
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(1700);
			list.position = list.head;
			list.prevPosition = null;
			try {
				list.deleteItem();
				if (list.head != null || list.tail != null) {
					System.out.println("ERROR: deleteItem() Test 4: List is not empty after deleting the only item.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: deleteItem() Test 4: Exception thrown: " + e);
			}
		}

		// Test 5: Attempt to delete when no current item exists should throw an exception
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			try {
				list.deleteItem();
				System.out.println("ERROR: deleteItem() Test 5: No exception thrown when deleting with no current item.");
			} catch(NoCurrentItem280Exception e) {
				// Expected
			} catch(Exception e) {
				System.out.println("ERROR: deleteItem() Test 5: Wrong exception thrown: " + e);
			}
		}


		// Regression tests for 'deleteFirst()'
		// Test 1: Delete first on a list with one element
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(1800);
			try {
				list.deleteFirst();
				if (list.head != null || list.tail != null) {
					System.out.println("ERROR: deleteFirst() Test 1: List is not empty after deleting the only element.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: deleteFirst() Test 1: Exception thrown: " + e);
			}
		}

		// Test 2: Delete first on a list with multiple elements
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(1900);
			list.insertLast(2000);
			try {
				list.deleteFirst();
				if (list.head.item != 2000) {
					System.out.println("ERROR: deleteFirst() Test 2: head.item is not 2000 after deleting first element.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: deleteFirst() Test 2: Exception thrown: " + e);
			}
		}

		// Test 3: After deleteFirst(), the new head's previous pointer should be null
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(2100);
			list.insertLast(2200);
			try {
				list.deleteFirst();
				if (((BilinkedNode280<Integer>)list.head).previousNode != null) {
					System.out.println("ERROR: deleteFirst() Test 3: new head's previousNode is not null.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: deleteFirst() Test 3: Exception thrown: " + e);
			}
		}

		// Test 4: Delete first when the cursor is set to head
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(2300);
			list.insertLast(2400);
			list.position = list.head;
			list.prevPosition = null;
			try {
				list.deleteFirst();
				if (list.head.item != 2400) {
					System.out.println("ERROR: deleteFirst() Test 4: head.item is not 2400 after deletion.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: deleteFirst() Test 4: Exception thrown: " + e);
			}
		}

		// Test 5: Attempting deleteFirst() on an empty list should throw an exception
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			try {
				list.deleteFirst();
				System.out.println("ERROR: deleteFirst() Test 5: No exception thrown on empty list.");
			} catch(ContainerEmpty280Exception e) {
				// Expected
			} catch(Exception e) {
				System.out.println("ERROR: deleteFirst() Test 5: Wrong exception thrown: " + e);
			}
		}

		// Regression tests for 'deleteLast()'
		// Test 1: Delete last on a list with one element
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(2500);
			try {
				list.deleteLast();
				if (list.head != null || list.tail != null) {
					System.out.println("ERROR: deleteLast() Test 1: List is not empty after deleting the only element.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: deleteLast() Test 1: Exception thrown: " + e);
			}
		}

		// Test 2: Delete last on a list with multiple elements
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(2600);
			list.insertLast(2700);
			try {
				list.deleteLast();
				if (list.tail.item != 2600) {
					System.out.println("ERROR: deleteLast() Test 2: tail.item is not 2600 after deletion.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: deleteLast() Test 2: Exception thrown: " + e);
			}
		}

		// Test 3: After deleteLast(), tail.next should be null
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(2800);
			list.insertLast(2900);
			try {
				list.deleteLast();
				if (list.tail.nextNode() != null) {
					System.out.println("ERROR: deleteLast() Test 3: tail.nextNode is not null after deletion.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: deleteLast() Test 3: Exception thrown: " + e);
			}
		}

		// Test 4: Delete last when the cursor is on the tail
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(3000);
			list.insertLast(3100);
			list.position = list.tail;
			list.prevPosition = list.head;
			try {
				list.deleteLast();
				if (list.tail.item != 3000) {
					System.out.println("ERROR: deleteLast() Test 4: tail.item is not 3000 after deletion.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: deleteLast() Test 4: Exception thrown: " + e);
			}
		}

		// Test 5: Attempting deleteLast() on an empty list should throw an exception
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			try {
				list.deleteLast();
				System.out.println("ERROR: deleteLast() Test 5: No exception thrown on empty list.");
			} catch(ContainerEmpty280Exception e) {
				// Expected
			} catch(Exception e) {
				System.out.println("ERROR: deleteLast() Test 5: Wrong exception thrown: " + e);
			}
		}


		// Regression tests for 'goLast()'
		// Test 1: goLast() on a list with one element
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(3200);
			try {
				list.goLast();
				if (list.position.item != 3200) {
					System.out.println("ERROR: goLast() Test 1: position.item is not 3200.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: goLast() Test 1: Exception thrown: " + e);
			}
		}

		// Test 2: goLast() on a list with multiple elements
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(3300);
			list.insertLast(3400);
			try {
				list.goLast();
				if (list.position.item != 3400) {
					System.out.println("ERROR: goLast() Test 2: position.item is not 3400.");
				}
				if (((BilinkedNode280<Integer>)list.position).previousNode.item != 3300) {
					System.out.println("ERROR: goLast() Test 2: position.previousNode.item is not 3300.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: goLast() Test 2: Exception thrown: " + e);
			}
		}

		// Test 3: goLast() should not modify the head
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(3500);
			list.insertLast(3600);
			BilinkedNode280<Integer> originalHead = (BilinkedNode280<Integer>) list.head;
			try {
				list.goLast();
				if (list.head != originalHead) {
					System.out.println("ERROR: goLast() Test 3: head was modified.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: goLast() Test 3: Exception thrown: " + e);
			}
		}

		// Test 4: Calling goLast() on an empty list should throw an exception
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			try {
				list.goLast();
				System.out.println("ERROR: goLast() Test 4: No exception thrown on empty list.");
			} catch(ContainerEmpty280Exception e) {
				// Expected
			} catch(Exception e) {
				System.out.println("ERROR: goLast() Test 4: Wrong exception thrown: " + e);
			}
		}

		// Test 5: After goLast(), prevPosition should point to the node immediately before tail
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(3700);
			list.insertLast(3800);
			try {
				list.goLast();
				if (list.prevPosition.item != 3700) {
					System.out.println("ERROR: goLast() Test 5: prevPosition.item is not 3700.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: goLast() Test 5: Exception thrown: " + e);
			}
		}


		// Regression tests for 'goBack()'
		// Test 1: goBack() from the tail should move to the previous element
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(3900);
			list.insertLast(4000);
			try {
				list.goLast();
				list.goBack();
				if (list.position.item != 3900) {
					System.out.println("ERROR: goBack() Test 1: position.item is not 3900 after goBack.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: goBack() Test 1: Exception thrown: " + e);
			}
		}

		// Test 2: goBack() from a middle position
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(4100);
			list.insertLast(4200);
			list.insertLast(4300);
			// Set cursor to second element
			list.position = list.head.nextNode();
			list.prevPosition = list.head;
			try {
				list.goBack();
				if (list.position.item != list.head.item) {
					System.out.println("ERROR: goBack() Test 2: position.item is not equal to head.item after goBack.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: goBack() Test 2: Exception thrown: " + e);
			}
		}

		// Test 3: Calling goBack() from the first element should move the cursor before the list
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(4400);
			// Initially, the cursor is at the only element (the head)
			list.position = list.head;
			list.prevPosition = null;
			try {
				list.goBack();
				// After calling goBack(), the cursor should move to a "before" state (position == null)
				if (list.position != null) {
					System.out.println("ERROR: goBack() Test 3: Expected cursor to be before the list (position == null) but got " + list.position.item);
				}
				if (list.prevPosition != null) {
					System.out.println("ERROR: goBack() Test 3: Expected prevPosition to be null after moving before the list.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: goBack() Test 3: Unexpected exception thrown: " + e);
			}
		}

		// Test 4: Simulate an 'after' state and call goBack() so that the cursor moves to the tail
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(4500);
			list.insertLast(4600);
			// Simulate an 'after' state: position == null, prevPosition == tail
			list.position = null;
			list.prevPosition = list.tail;
			try {
				list.goBack();
				if (list.position == null || list.position.item != list.tail.item) {
					System.out.println("ERROR: goBack() Test 4: position.item is not equal to tail.item after goBack from after state.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: goBack() Test 4: Exception thrown: " + e);
			}
		}

		// Test 5: After goBack(), prevPosition should be updated correctly
		{
			BilinkedList280<Integer> list = new BilinkedList280<>();
			list.insertLast(4700);
			list.insertLast(4800);
			list.insertLast(4900);
			// Set cursor to the tail
			list.position = list.tail;
			// Set prevPosition to the second node
			list.prevPosition = list.head.nextNode();
			try {
				list.goBack();
				// After goBack(), position should be the second element and prevPosition should be the first
				if (list.position.item != 4800 || list.prevPosition.item != 4700) {
					System.out.println("ERROR: goBack() Test 5: position or prevPosition not updated correctly.");
				}
			} catch(Exception e) {
				System.out.println("ERROR: goBack() Test 5: Exception thrown: " + e);
			}
		}
	}
}