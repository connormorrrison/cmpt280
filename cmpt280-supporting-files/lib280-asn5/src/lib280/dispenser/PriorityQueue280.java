package lib280.dispenser;

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.tree.ArrayedBinaryTreeIterator280;
import lib280.tree.IterableArrayedHeap280;

public class PriorityQueue280<I extends Comparable<? super I>> {
	
	// This is the heap that we are restricting.
	// Items in the priority queue get stored in the heap.
	protected IterableArrayedHeap280<I> items;
	
	
	/**
	 * Create a new priorty queue with a given capacity.
	 * @param cap The maximum number of items that can be in the queue.
	 */
	public PriorityQueue280(int cap) {
		items = new IterableArrayedHeap280<I>(cap);
	}
	
	public String toString() {
		return items.toString();	
	}

	// TO DO
	// Add Priority Queue ADT methods (from the specification) here.


	/**
	 * Inserts the specified item into the priority queue
	 * @param item the item to be inserted into the priority queue
	 * @throws ContainerFull280Exception if the priority queue is full
	 */
	public void insert(I item) throws ContainerFull280Exception {
		// TO DO: implement insert
		if (this.isFull()) {
			throw new ContainerFull280Exception("Cannot insert into a full priority queue.");
		}
		this.items.insert(item);
	}

	/**
	 * Returns true if the queue is full
	 * @return true if the queue is full, false otherwise
	 */
	public boolean isFull() {
		// TO DO: implement isFull
		return this.items.count() >= this.items.capacity();
	}

	/**
	 * Returns true if the queue is empty
	 * @return true if the queue is empty, false otherwise
	 */
	public boolean isEmpty() {
		// TO DO: implement isEmpty
		return this.items.count() == 0;
	}

	/**
	 * Returns the number of items in the queue
	 * @return the number of items in the queue
	 */
	public int count() {
		// TO DO: implement count
		return this.items.count();
	}

	/**
	 * Returns the highest priority item in the queue
	 * @return the highest priority item in the queue
	 * @throws ContainerEmpty280Exception if the queue is empty
	 */
	public I maxItem() throws ContainerEmpty280Exception {
		// TO DO: implement maxItem
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot get max item from an empty priority queue.");
		}
		return items.item();  // The root of the heap is the max item
	}

	/**
	 * Returns the lowest priority item in the queue
	 * @return the lowest priority item in the queue
	 * @throws ContainerEmpty280Exception if the queue is empty
	 */
	public I minItem() throws ContainerEmpty280Exception {
		// TO DO: implement minItem
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot get min item from an empty priority queue.");
		}

		// Use the iterator to find the min item
		ArrayedBinaryTreeIterator280<I> iter = items.iterator();
		iter.goFirst();

		I minItem = iter.item();  // minItem becomes the first item
		while (iter.itemExists()) {
			if (iter.item().compareTo(minItem) < 0) {
				minItem = iter.item();
			}
			iter.goForth();
		}

		return minItem;
	}

	/**
	 * Removes the highest priority item from the queue
	 * @throws ContainerEmpty280Exception if the queue is empty
	 */
	public void deleteMax() throws ContainerEmpty280Exception {
		// TO DO: implement deleteMax
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot delete max item from an empty priority queue.");
		}
		this.items.deleteItem();  // deleteItem() handles the removal of the root/max item
	}

	/**
	 * Removes the lowest priority item from the queue
	 * @throws ContainerEmpty280Exception if the queue is empty
	 */
	public void deleteMin() throws ContainerEmpty280Exception {
		// TO DO: implement deleteMin
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot delete min item from an empty priority queue.");
		}

		// Find the min item using the iterator
		ArrayedBinaryTreeIterator280<I> iter = items.iterator();
		iter.goFirst();

		I minItem = iter.item();  // Initialize minItem to first item
		ArrayedBinaryTreeIterator280<I> minIter = items.iterator();
		minIter.goFirst();

		while (iter.itemExists()) {
			if (iter.item().compareTo(minItem) < 0) {
				minItem = iter.item();
				// Reset minIter to find the position of the new minItem
				minIter = items.iterator();
				minIter.goFirst();
				// Move minIter to the position of the new minItem
				while (!minIter.item().equals(minItem)) {
					minIter.goForth();
				}
			}
			iter.goForth();
		}

		this.items.deleteAtPosition(minIter);
	}

	/**
	 * Removes all items that have the same priority as the highest priority item
	 * @throws ContainerEmpty280Exception if the queue is empty
	 */
	public void deleteAllMax() throws ContainerEmpty280Exception {
		// TO DO: implement deleteAllMax
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot delete max items from an empty priority queue.");
		}

		// Get max item (highest priority)
		I maxItem = maxItem();
		while (!this.isEmpty() && maxItem().compareTo(maxItem) == 0) {
			deleteMax();
		}
	}


	// UNCOMMENT THE REGRESSION TEST WHEN YOU ARE READY

	public static void main(String args[]) {
		class PriorityItem<I> implements Comparable<PriorityItem<I>> {
			I item;
			Double priority;
						
			public PriorityItem(I item, Double priority) {
				super();
				this.item = item;
				this.priority = priority;
			}

			public int compareTo(PriorityItem<I> o) {
				return this.priority.compareTo(o.priority);
			}
			
			public String toString() {
				return this.item + ":" + this.priority;
			}
		}
		
		PriorityQueue280<PriorityItem<String>> Q = new PriorityQueue280<PriorityItem<String>>(5);
		
		// Test isEmpty()
		if( !Q.isEmpty()) 
			System.out.println("Error: Queue is empty, but isEmpty() says it isn't.");
		
		// Test insert() and maxItem()
		Q.insert(new PriorityItem<String>("Sing", 5.0));
		if( Q.maxItem().item.compareTo("Sing") != 0) {
			System.out.println("??Error: Front of queue should be 'Sing' but it's not. It is: " + Q.maxItem().item);
		}
		
		// Test isEmpty() when queue not empty
		if( Q.isEmpty()) 
			System.out.println("Error: Queue is not empty, but isEmpty() says it is.");
		
		// test count()
		if( Q.count() != 1 ) {
			System.out.println("Error: Count should be 1 but it's not.");			
		}

		// test minItem() with one element
		if( Q.minItem().item.compareTo("Sing")!=0) {
			System.out.println("Error: min priority item should be 'Sing' but it's not.");
		}	
		
		// insert more items
		Q.insert(new PriorityItem<String>("Fly", 5.0));
		if( Q.maxItem().item.compareTo("Sing")!=0) System.out.println("Front of queue should be 'Sing' but it's not.");
		Q.insert(new PriorityItem<String>("Dance", 3.0));
		if( Q.maxItem().item.compareTo("Sing")!=0) System.out.println("Front of queue should be 'Sing' but it's not.");
		Q.insert(new PriorityItem<String>("Jump", 7.0));
		if( Q.maxItem().item.compareTo("Jump")!=0) System.out.println("Front of queue should be 'Jump' but it's not.");
		
		if(Q.minItem().item.compareTo("Dance") != 0) System.out.println("minItem() should be 'Dance' but it's not.");
		
		if( Q.count() != 4 ) {
			System.out.println("Error: Count should be 4 but it's not.");			
		}
		
		// Test isFull() when not full
		if( Q.isFull()) 
			System.out.println("Error: Queue is not full, but isFull() says it is.");
		
		Q.insert(new PriorityItem<String>("Eat", 10.0));
		if( Q.maxItem().item.compareTo("Eat")!=0) System.out.println("Front of queue should be 'Eat' but it's not.");

		if( !Q.isFull()) 
			System.out.println("Error: Queue is full, but isFull() says it isn't.");

		// Test insertion on full queue
		try {
			Q.insert(new PriorityItem<String>("Sleep", 15.0));
			System.out.println("Expected ContainerFull280Exception inserting to full queue but got none.");
		}
		catch(ContainerFull280Exception e) {
			// Expected exception
		}
		catch(Exception e) {
			System.out.println("Expected ContainerFull280Exception inserting to full queue but got a different exception.");
			e.printStackTrace();
		}
		
		// test deleteMin
		Q.deleteMin();
		if(Q.minItem().item.compareTo("Sing") != 0) System.out.println("Min item should be 'Sing', but it isn't.");
		
		Q.insert(new PriorityItem<String>("Dig", 1.0));
		if(Q.minItem().item.compareTo("Dig") != 0) System.out.println("minItem() should be 'Dig' but it's not.");

		// Test deleteMax
		Q.deleteMax();
		if( Q.maxItem().item.compareTo("Jump")!=0) System.out.println("Front of queue should be 'Jump' but it's not.");

		Q.deleteMax();
		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Front of queue should be 'Fly' but it's not.");

		if(Q.minItem().item.compareTo("Dig") != 0) System.out.println("minItem() should be 'Dig' but it's not.");

		Q.deleteMin();
		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Front of queue should be 'Fly' but it's not.");

		Q.insert(new PriorityItem<String>("Scream", 2.0));
		Q.insert(new PriorityItem<String>("Run", 2.0));

		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Front of queue should be 'Fly' but it's not.");
		
		// test deleteAllMax()
		Q.deleteAllMax();
		if( Q.maxItem().item.compareTo("Scream")!=0) System.out.println("Front of queue should be 'Scream' but it's not.");
		if( Q.minItem().item.compareTo("Scream") != 0) System.out.println("minItem() should be 'Scream' but it's not.");
		Q.deleteAllMax();

		// Queue should now be empty again.
		if( !Q.isEmpty()) 
			System.out.println("Error: Queue is empty, but isEmpty() says it isn't.");

		System.out.println("Regression test complete.");
	}


	//*/
}
