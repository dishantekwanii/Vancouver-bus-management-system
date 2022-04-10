package busoute;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Graph {

	// The adjacency list of each vertex, stored into one array
	HashMap<Integer,ArrayList<Integer>> adjLists;
	
	// Same idea with the distances
	HashMap<Integer,ArrayList<Double>> dists;
	
	@SuppressWarnings("unchecked")
	Graph(){
		
		adjLists = new HashMap<>();
		dists = new HashMap<>();

		// populate maps
		try {
			File file = new File("inputs/stops.txt");
        	Scanner inputStream = new Scanner(file);
        	String data = inputStream.nextLine(); // skip the first line as it contains the column names
			while(inputStream.hasNextLine()){
				data = inputStream.nextLine(); // take the second line
				String[] values = data.split(",");
				int id =  Integer.parseInt(values[0]);
				adjLists.put(id, new ArrayList<Integer>());
				dists.put(id, new ArrayList<Double>());
				
			}
			inputStream.close();
		} catch (FileNotFoundException e) {
			System.out.println("File was not found in 'shortestPaths.java'");
		}
	}
	/**
	 * 
	 * @param vA - vertex A
	 * @param vB - vertex B
	 * @param distance - length of edge AB (so distance between them)
	 */
	public void addEdge(int vA, int vB, double distance) {
		
		// update adjacency list
		ArrayList<Integer> adj = adjLists.get(vA);
		adj.add(vB);
		adjLists.put(vA, adj);

		// record distance
		ArrayList<Double> dist = dists.get(vA);
		dist.add(distance);
		dists.put(vA, dist);
	}
	 
	/** This implentation will only work if there are no negative edges
	 * 
	 * @param vA - vertex A
	 * @param vB - vertex B
	 * @return double - distance between them:
	 * 						- 0 if A==B
	 * 						- infinity, if B is not adjacent to A (so distance is unknown) 
	 * 						- the distance if B is adjacent to A  (so distance is known)
	 */
	public double getDistance(int vA, int vB) {
		if(vA == vB) return 0;

		int vIndex = -1;
		ArrayList<Integer> adjList = adjLists.get(vA);
		for(int i = 0; i<adjList.size(); i++){
			if(adjList.get(i)==vB) vIndex =  i;
		}
		if(vIndex>=0) return dists.get(vA).get(vIndex);
		
		return Double.POSITIVE_INFINITY;
	}
}
