import lib280.graph.Edge280;
import lib280.graph.GraphMatrixRep280;
import lib280.list.LinkedList280;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class QuestProgression {
	
	// File format for quest data:
	// First line: Number of quests N
	// Next N lines consist of the following items, separated by commas:
	//     quest ID, quest name, quest area, quest XP
	//     (Quest ID's must be between 1 and N, but the line for each quest IDs may appear in any order).
	// Remaining lines consist of a comma separated pair of id's i and j where i and j are quest IDs indicating
	// that quest i must be done before quest j (i.e. that (i,j) is an edge in the quest graph).
	
	/**
	 * Read the quest data from a text file and build a graph of quest prerequisites.
	 * @param filename Filename from which to read quest data.
	 * @return A graph representing quest prerequisites.  If quest with id i must be done before a quest with id j, then there is an edge in the graph from vertex i to vertex j.
	 */
	public static GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> readQuestFile(String filename) {
		Scanner infile;
		
		// Attempt to open the input filename.
		try {
			infile = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			System.out.println("Error: Unable to open" + filename);
			e.printStackTrace();
			return null;
		}
		
		// Set the delimiters for parsing to commas, and vertical whitespace.
		infile.useDelimiter("[,\\v]");

		// Read the number of quests for which there is data.
		int numQuests = infile.nextInt();
		
		// read the quest data for each quest.
		LinkedList280<Quest> questList = new LinkedList280<Quest>();
		for(int i=0; i < numQuests; i++) {
			int qId = infile.nextInt();
			String qName = infile.next();
			String qArea = infile.next();
			int qXp = infile.nextInt();		
			questList.insertLast(new Quest(qId, qName, qArea, qXp));
		}
	
		// Make a graph with the vertices we created from the quest data.
		GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> questGraph = 
				new GraphMatrixRep280<QuestVertex, Edge280<QuestVertex>> (numQuests, true, "QuestVertex", "lib280.graph.Edge280");
		
		// Add enough vertices for all of our quests.
		questGraph.ensureVertices(numQuests);
		
		// Store each quest in a different vertex.  The quest with id i gets stored vertex i.
		questList.goFirst();
		while(questList.itemExists()) {
			questGraph.vertex(questList.item().id()).setQuest(questList.item());
			questList.goForth();
		}
		
		// Continue reading the input file for the quest prerequisite informaion and add an edge to the graph
		// for each prerequisite.
		while(infile.hasNext()) {
			questGraph.addEdge(infile.nextInt(), infile.nextInt());
		}
				
		infile.close();
		
		return questGraph;
	}
	

	/**
	 * Test whether vertex v has incoming edges or not
	 * @param G A graph.
	 * @param v The integer identifier of a node in G (corresponds to quest ID)
	 * @return Returns true if v has no incoming edges.  False otherwise.
	 */
	public static boolean hasNoIncomingEdges(GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> G, int v) {
		
		// TO DO Write this method
		for (int i = 1; i <= G.numVertices(); i++) {
			// Look for edges coming from other vertices to v
			if (G.isAdjacent(i, v)) {
				// Found an incoming edge, so return false
				return false;
			}
		}

		// Else, no incoming edges were found
		return true;
	}
	
	
	/**
	 * Perform a topological sort of the quests in the quest prerequisite graph G, with priority given
	 * to the highest experience value among the available quests.
	 * @param G The graph on which to perform a topological sort.
	 * @return A list of quests that is the result of the topological sort, that is, the order in which the quests should be done if we always pick the available quest with the largest XP reward first.
	 */
	public static LinkedList280<Quest> questProgression(GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> G) {
	
		// TO DO Write this method
		LinkedList280<Quest> L = new LinkedList280<Quest>();
		LinkedList280<Quest> availableQuests = new LinkedList280<Quest>();

		// Find all quests that have no prerequisites
		for (int i = 1; i <= G.numVertices(); i++) {
			if (hasNoIncomingEdges(G, i)) {
				availableQuests.insertLast(G.vertex(i).quest());
			}
		}

		// Process quests until none remain
		while (!availableQuests.isEmpty()) {
			// Find quest with highest XP from available options
			availableQuests.goFirst();
			Quest highestXpQuest = availableQuests.item();

			availableQuests.goForth();
			while (availableQuests.itemExists()) {
				if (availableQuests.item().experienceValue() > highestXpQuest.experienceValue()) {
					highestXpQuest = availableQuests.item();
				}
				availableQuests.goForth();
			}

			// Remove the selected quest from available list
			availableQuests.goFirst();
			while (availableQuests.itemExists()) {
				if (availableQuests.item() == highestXpQuest) {
					availableQuests.deleteItem();
					break;
				}
				availableQuests.goForth();
			}

			// Add to result list
			L.insertLast(highestXpQuest);

			// Update graph and check for newly available quests
			for (int i = 1; i <= G.numVertices(); i++) {
				if (G.isAdjacent(highestXpQuest.id(), i)) {
					// Remove the dependency edge
					G.eSearch(G.vertex(highestXpQuest.id()), G.vertex(i));
					if (G.eItemExists()) {
						G.deleteEItem();
					}

					// If quest now has no prerequisites, make it available
					if (hasNoIncomingEdges(G, i)) {
						availableQuests.insertLast(G.vertex(i).quest()); // If 'i' now has no incoming edges, add it to the available quests
					}
				}
			}
		}

		// Check for cycles in the graph
		for (int i = 1; i <= G.numVertices(); i++) {
			if (!hasNoIncomingEdges(G, i)) {
				throw new RuntimeException("The graph has at least one cycle.");
			}
		}

		// Return ordered quest list
		return L;

	}
	
	public static void main(String args[]) {
		// Read the quest data and construct the graph.
		
		// If you get an error reading the file here and you're using Eclipse, 
		// remove the 'QuestPrerequisites-Template/' portion of the filename.
		GraphMatrixRep280<QuestVertex,Edge280<QuestVertex>> questGraph = readQuestFile("../cmpt280-supporting-files/QuestPrerequisites-Template/quests16.txt");
		
		// Perform a topological sort on the graph.
		LinkedList280<Quest> questListForMaxXp = questProgression(questGraph);
		
		// Display the quests to be completed in the order determined by the topologial sort.
		questListForMaxXp.goFirst();
		while(questListForMaxXp.itemExists()) {
			System.out.println(questListForMaxXp.item());
			questListForMaxXp.goForth();
		}
				
	}
}
