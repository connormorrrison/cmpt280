import com.opencsv.CSVReader;
import lib280.base.Pair280;
import lib280.hashtable.KeyedChainedHashTable280;
import lib280.tree.OrderedSimpleTree280;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

// This project uses a JAR called opencsv which is a library for reading and
// writing CSV (comma-separated value) files.
// 
// You don't need to do this for this project, because it's already done, but
// if you want to use opencsv in other projects on your own, here's the process:
//
// 1. Download opencsv-3.1.jar from http://sourceforge.net/projects/opencsv/
// 2. Drag opencsv-3.1.jar into your project.
// 3. Right-click on the project in the package explorer, select "Properties" (at bottom of popup menu)
// 4. Choose the "Libraries" tab
// 5. Click "Add JARs"
// 6. Select the opencsv-3.1.jar from within your project from the list.
// 7. At the top of your .java file add the following imports:
//        import java.io.FileReader;
//        import com.opencsv.CSVReader;
//
// Reference documentation for opencsv is here:  
// http://opencsv.sourceforge.net/apidocs/overview-summary.html


public class QuestLog extends KeyedChainedHashTable280<String, QuestLogEntry> {

	public QuestLog() {
		super();
	}
	
	/**
	 * Obtain an array of the keys (quest names) from the quest log.  There is 
	 * no expectation of any particular ordering of the keys.
	 * 
	 * @return The array of keys (quest names) from the quest log.
	 */
	public String[] keys() {
		// TO DO Implement this method.
		ArrayList<String> keysList = new ArrayList<>();

		// Iterate
		for (int i = 0; i < this.hashArray.length; i++) {
			if (this.hashArray[i] != null) {  // Null check
				this.hashArray[i].goFirst();

				// Traverse linked list at this hash position
				while (this.hashArray[i].itemExists()) {
					keysList.add(this.hashArray[i].item().key());
					this.hashArray[i].goForth();
				}
			}
		}

		// Converts to array
		return keysList.toArray(new String[0]);
	}
	
	/**
	 * Format the quest log as a string which displays the quests in the log in 
	 * alphabetical order by name.
	 * 
	 * @return A nicely formatted quest log.
	 */
	public String toString() {
		// TO DO Implement this method.
		// Fetch keys and sort them
		String[] keys = this.keys();
		Arrays.sort(keys);  // Alphabetically sort keys

		StringBuffer sb = new StringBuffer();

		// Append each quest log entry's details to the sb
		for (String key : keys) {
			QuestLogEntry entry = this.obtain(key);
			sb.append(entry.toString()).append("\n");
		}

		// Return formatted string
		return sb.toString();
		
		// Remove this following line when you're ready.  It's just to prevent compiler errors.
		//return "QuestLog.toString() not implemented yet!\n";
	}
	
	/**
	 * Obtain the quest with name k, while simultaneously returning the number of
	 * items examined while searching for the quest.
	 * @param k Name of the quest to obtain.
	 * @return A pair in which the first item is the QuestLogEntry for the quest named k, and the
	 *         second item is the number of items examined during the search for the quest named k.
	 *         Note: if no quest named k is found, then the first item of the pair should be null.
	 */
	public Pair280<QuestLogEntry, Integer> obtainWithCount(String k) {
		// TO DO Implement this method.
		
		// Write a method that returns a Pair280 which contains the quest log entry with name k, 
		// and the number QuestLogEntry objects that were examined in the process.  You need to write
		// this method from scratch without using any of the superclass methods (mostly because
		// the superclass methods won't be terribly useful unless you can modify them, which you
		// aren't allowed to do!).  Except for hashPos().  You're allowed to use hashPos()!
		// Gets hash position
		int hashPosition = hashPos(k);

		int count = 0;
		this.hashArray[hashPosition].goFirst();
		// Iterate over chain at the hash position
		while (this.hashArray[hashPosition].itemExists()) {
			count++;

			if (this.hashArray[hashPosition].item().key().equals(k)) {
				// If match found return the pair with the item and count
				return new Pair280<>(this.hashArray[hashPosition].item(), count);
			}

			this.hashArray[hashPosition].goForth();
		}

		// If the item was not found
		return new Pair280<>(null, count);
	
		//return null;  // Remove this line when you're ready.  It's just to prevent compiler errors.
	}
	
	
	public static void main(String args[])  {
		// Make a new Quest Log
		QuestLog hashQuestLog = new QuestLog();
		
		// Make a new ordered binary lib280.tree.
		OrderedSimpleTree280<QuestLogEntry> treeQuestLog =
				new OrderedSimpleTree280<QuestLogEntry>();
		
		
		// Read the quest data from a CSV (comma-separated value) file.
		// To change the file read in, edit the argument to the FileReader constructor.
		CSVReader inFile;
		try {
			//input filename on the next line - path must be relative to the working directory reported above.
			inFile = new CSVReader(new FileReader("../cmpt280-supporting-files/QuestLog-Template/quests100000.csv"));
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found.");
			return;
		}
		
		String[] nextQuest;
		try {
			// Read a row of data from the CSV file
			while ((nextQuest = inFile.readNext()) != null) {
				// If the read succeeded, nextQuest is an array of strings containing the data from
				// each field in a row of the CSV file.  The first field is the quest name,
				// the second field is the quest region, and the next two are the recommended
				// minimum and maximum level, which we convert to integers before passing them to the
				// constructor of a QuestLogEntry object.
				QuestLogEntry newEntry = new QuestLogEntry(nextQuest[0], nextQuest[1], 
						Integer.parseInt(nextQuest[2]), Integer.parseInt(nextQuest[3]));
				// Insert the new quest log entry into the quest log.
				hashQuestLog.insert(newEntry);
				treeQuestLog.insert(newEntry);
			}
		} catch (IOException e) {
			System.out.println("Something bad happened while reading the quest information.");
			e.printStackTrace();
		}
		
		// Print out the hashed quest log's quests in alphabetical order.
		// COMMENT THIS OUT when you're testing the file with 100,000 quests.  It takes way too long.
		System.out.println(hashQuestLog);
		
		// Print out the lib280.tree quest log's quests in alphabetical order.
		// COMMENT THIS OUT when you're testing the file with 100,000 quests.  It takes way too long.
	    System.out.println(treeQuestLog.toStringInorder());
		

		// TO DO Determine the average number of elements examined during access for hashed quest log.
	    // (call hashQuestLog.obtainWithCount() for each quest in the log and find average # of access)
		int totalExaminationsHash = 0;
		int questCountHash = hashQuestLog.count();
		for (String questName : hashQuestLog.keys()) {
			Pair280<QuestLogEntry, Integer> result = hashQuestLog.obtainWithCount(questName);
			totalExaminationsHash += result.secondItem();
		}

		double averageHash = (double) totalExaminationsHash / questCountHash;
		System.out.printf("Avg. # of items examined per query in the hashed quest log with %d entries: %.2f%n",
				questCountHash, averageHash);
		
		
		// TO DO Determine the average number of elements examined during access for lib280.tree quest log.
	    // (call treeQuestLog.searchCount() for each quest in the log and find average # of access)
		int totalExaminationsTree = 0;
		int questCountTree = hashQuestLog.count(); // Use the count from the hash table
		// Iterate through all quests in the hash table
		for (String questName : hashQuestLog.keys()) {
			// Create a dummy QuestLogEntry for searching
			QuestLogEntry dummyEntry = new QuestLogEntry(questName, "", 0, 0);

			// Perform the search and add the count to the total
			totalExaminationsTree += treeQuestLog.searchCount(dummyEntry);
		}

		double averageTree = (double) totalExaminationsTree / questCountTree;

		System.out.printf("Avg. # of items examined per query in the tree-based quest log with %d entries: %.2f%n",
				questCountTree, averageTree);
		
	}
	
	
}
