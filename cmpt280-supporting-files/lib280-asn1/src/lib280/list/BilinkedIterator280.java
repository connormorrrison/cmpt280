package lib280.list;


import lib280.base.BilinearIterator280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.BeforeTheStart280Exception;
import lib280.exception.ContainerEmpty280Exception;

/**	A LinkedIterator which has functions to move forward and back, 
	and to the first and last items of the list.  It keeps track of 
	the current item, and also has functions to determine if it is 
	before the start or after the end of the list. */
public class BilinkedIterator280<I> extends LinkedIterator280<I> implements BilinearIterator280<I>
{

	/**	Constructor creates a new iterator for list 'list'. <br>
		Analysis : Time = O(1) 
		@param list list to be iterated */
	public BilinkedIterator280(BilinkedList280<I> list)
	{
		super(list);
	}

	/**	Create a new iterator at a specific position in the newList. <br>
		Analysis : Time = O(1)
		@param newList list to be iterated
		@param initialPrev the previous node for the initial position
		@param initialCur the current node for the initial position */
	public BilinkedIterator280(BilinkedList280<I> newList, 
			LinkedNode280<I> initialPrev, LinkedNode280<I> initialCur)
	{
		super(newList, initialPrev, initialCur);
	}
    
	/**
	 * Move the cursor to the last element in the list.
	 * @precond The list is not empty.
	 */
	public void  goLast() throws ContainerEmpty280Exception
	{
		// TO DO
		// Case: the list is empty
		if (this.list.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot go last because the list is empty.");
		}

		// Move cursor to the last node before processing other test cases
		this.cur = list.lastNode();

		// Case: the list has one node
		if (this.cur == null) {
			this.prev = null;
		} else {
			this.prev = ((BilinkedNode280<I>) this.cur).previousNode();
		}
	}

	/**
	 * Move the cursor one element closer to the beginning of the list
	 * @precond !before() - the cursor cannot already be before the first element.
	 */
	public void goBack() throws BeforeTheStart280Exception
	{
		// TO DO
		// Case: before the first element
		if (this.list.before()) {
			throw new BeforeTheStart280Exception("Cannot go back because the list is empty.");
		}

		// Case: after the last element
		if (this.list.after()) {
			goLast();
		} else {
			// Case: somewhere in the list
			this.cur = ((BilinkedNode280<I>) this.cur).previousNode();
			if (this.cur!=null) {
				this.prev = ((BilinkedNode280<I>) this.cur).previousNode();
			}
		}

	 }

	/**	A shallow clone of this object. <br> 
	Analysis: Time = O(1) */
	public BilinkedIterator280<I> clone()
	{
		return (BilinkedIterator280<I>) super.clone();
	}


} 
