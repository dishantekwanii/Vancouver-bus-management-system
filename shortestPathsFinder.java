package busoute;

import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class shortestPathsFinder {

    private static Graph graph;

    public class NodeInfo {
        
        double distance;
        int prevStop;
        boolean visited;

        NodeInfo(){
            distance = Double.POSITIVE_INFINITY;
            prevStop = -1;
            visited = false;
        }
            
    }

    public static ArrayList<String> getShortestPath(int startStop, int endStop) {
        graph = new Graph();
        /*
         * Creating graph from stop_times
         */
        String fileName = "inputs/stop_times.txt";

        try {
            File file = new File(fileName);
            Scanner inputStream = new Scanner(file);

            String data = inputStream.nextLine(); // skip the first line as it contains the column names

            data = inputStream.nextLine(); // take the second line
            String[] values = data.split(",");
            String currentTripID = values[0]; // store the first trip id
            String currentStopID = values[3]; // and the stop id

            String nextTripID, nextStopID;

            while (inputStream.hasNextLine()) {
                data = inputStream.nextLine();
                values = data.split(",");
                nextTripID = values[0];
                nextStopID = values[3];

                // add directed edge
                if (currentTripID.equals(nextTripID))
                    graph.addEdge(Integer.parseInt(currentStopID), Integer.parseInt(nextStopID), 1);

                // make next node as current node
                currentTripID = nextTripID;
                currentStopID = nextStopID;
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            graph = null;
        }

        /*
         * Creating graph from transfers
         */
        String transfers_file = "inputs/transfers.txt";

        try {
            File file = new File(transfers_file);
            Scanner inputStream = new Scanner(file);

            String data = inputStream.nextLine(); // skip the first line as it contains the column names
            String[] values;
            String currentStopID;
            String nextStopID;

            while (inputStream.hasNextLine()) {
                data = inputStream.nextLine();
                values = data.split(",");
                currentStopID = values[0];
                nextStopID = values[1];

                // add directed edge with transfer time
                if (Integer.parseInt(values[2]) == 0)
                    graph.addEdge(Integer.parseInt(currentStopID), Integer.parseInt(nextStopID), 2);
                else if (Integer.parseInt(values[2]) == 2)
                    graph.addEdge(Integer.parseInt(currentStopID), Integer.parseInt(nextStopID),
                            Integer.parseInt(values[3]) / 100);
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            graph = null;
        }

        // check if we should bother with dijkstra
        if(graph.adjLists.get(startStop) == null || graph.adjLists.get(endStop) == null){
            return null;
        }

        // Prep work for dijkstra result
        HashMap<Integer, NodeInfo> hm = new HashMap<>();
        // Ugly, but hey....
        shortestPathsFinder spf = new shortestPathsFinder();
        // init hm
        for(Map.Entry<Integer, ArrayList<Integer>> pair : graph.adjLists.entrySet()){
            NodeInfo nodeInfo = spf.new NodeInfo();
            nodeInfo.distance = graph.getDistance(startStop, pair.getKey());
            hm.put(pair.getKey(), nodeInfo);
        }

        // Run dijkstra
        runDijkstraAlgorithm(startStop, startStop, endStop, hm);

        // Format result
        ArrayList<String> result = new ArrayList<String>(0);
        
        
        for(int i = endStop; i!=startStop;){
            if(i == -1) break;
            result.add(String.valueOf(i));
            i = hm.get(i).prevStop;
        }
        result.add(String.valueOf(startStop));
        result.add( String.valueOf(hm.get(endStop).distance));
        Collections.reverse(result);

        
        return result;
    }

    public static double runDijkstraAlgorithm(int startStop, int currentStop, int endStop, HashMap<Integer, NodeInfo> hm) {
        // check if we are done
        // System.out.println("Current stop is: " + currentStop);
    	hm.get(currentStop).visited = true;
        if(currentStop==endStop) return hm.get(endStop).distance;
        
    	int currentNeighbour;
    	double newDistance;

    	// actual dijkstra algorithm
		for(int i = 0; i < graph.adjLists.get(currentStop).size();i++) {
            // for each neighbour of the current stop
			 currentNeighbour = graph.adjLists.get(currentStop).get(i);
             // get the new distance
			 newDistance = hm.get(currentStop).distance + graph.getDistance(currentStop, currentNeighbour);
             // check if new distance is shorter than current distance
			 if(newDistance < hm.get(currentNeighbour).distance) {
                // System.out.println("The new distance is:" + newDistance);
                // update distance table
                hm.get(currentNeighbour).distance = newDistance;
                // indicate which stop we came from
                hm.get(currentNeighbour).prevStop = currentStop;
             }
		}

        // find closest stop to start stop (to take next in dijkstra algo)
        boolean allVisited = true;
        int closestVertex = -1;
        for(Map.Entry<Integer, NodeInfo> pair : hm.entrySet()){
            NodeInfo nodeInfo = pair.getValue();
            if(!nodeInfo.visited) {
        
                // first unvisited vertex we find will become closestVertex, and we will be able to index into arrays with it
                if(closestVertex == -1)
                    closestVertex = pair.getKey();

                // check if current vertex is closer than so called closestVertex
                if(closestVertex != -1 && hm.get(closestVertex).distance>nodeInfo.distance) {
                    closestVertex = pair.getKey();
                }
            }
        }		
        // if closest vertex remains -1, then we have not found an unvisited vertex -> we are done
        allVisited = (closestVertex==-1); 
        if(!allVisited)
            return runDijkstraAlgorithm(startStop,closestVertex,endStop,hm);
        return hm.get(endStop).distance;
	} 
}