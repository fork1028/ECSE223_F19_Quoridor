package ca.mcgill.ecse223.quoridor.controller;

import java.util.*;

import ca.mcgill.ecse223.quoridor.model.Direction;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;

class Edge {

	public int node_1;
	public int node_2;

	Edge(int u, int v) {
		this.node_1 = u;
		this.node_2 = v;
	}

	@Override
	public String toString() {
		return "(" + node_1 + ", " + node_2 + ")";
	}
}

public class Graph {
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	private ArrayList<Integer> nodes = new ArrayList<Integer>();

	// copy constructor
	public Graph() {
		// adding all nodes to the graph
		for (int row = 1; row <= 9 ; row++) {
			for (int col = 1; col <= 9; col++) {
				int node_1 = (row - 1) * 9 + col;
				add_node(node_1);
			}
		}

		// adding edges
		for (int row = 1; row <= 8; row++) {
			for (int col = 1; col <= 8; col++) {
				int node_1 = (row - 1) * 9 + col;
				int node_2 = (row - 1) * 9 + col + 1;
				int node_3 = row * 9 + col;
				Edge edge_1 = new Edge(node_1, node_2);
				Edge edge_2 = new Edge(node_1, node_3);
				edges.add(edge_1);
				edges.add(edge_2);

				if (col == 8) {
					int node_border1 = (row - 1) * 9 + col + 1;
					int node_border2 = row * 9 + col + 1;
					Edge edge_border = new Edge(node_border1, node_border2);
					edges.add(edge_border);
				}

				if (row == 8) {
					int node_border1 = row * 9 + col;
					int node_border2 = row * 9 + col + 1;
					Edge edge_border = new Edge(node_border1, node_border2);
					edges.add(edge_border);
				}
			}
		}
	}

	public int wallAdded(int row, int col, Direction direction) {

		if (direction.equals(Direction.Horizontal)) {
			int node_1 = (row - 1) * 9 + col;
			int node_2 = row * 9 + col;
			int node_3 = (row - 1) * 9 + col + 1;
			int node_4 = row * 9 + col + 1;
			Edge edgeToDelete1 = null, edgeToDelete2 = null;

			for (Edge e : edges) {
				if (e.node_1 == node_1 && e.node_2 == node_2) {
					edgeToDelete1 = e;
				}
				if (e.node_1 == node_3 && e.node_2 == node_4) {
					edgeToDelete2 = e;
				}
			}
			edges.remove(edgeToDelete1);
			edges.remove(edgeToDelete2);
		}
		else {
			int node_1 = (row - 1) * 9 + col;
			int node_2 = (row - 1) * 9 + col + 1;
			int node_3 = row * 9 + col;
			int node_4 = row * 9 + col + 1;
			Edge edgeToDelete1 = null, edgeToDelete2 = null;

			for (Edge e : edges) {
				if (e.node_1 == node_1 && e.node_2 == node_2) {
					edgeToDelete1 = e;
				}
				if (e.node_1 == node_3 && e.node_2 == node_4) {
					edgeToDelete2 = e;
				}
			}
			edges.remove(edgeToDelete1);
			edges.remove(edgeToDelete2);
		}

		return dfsSearch();
	}

	// returns 00 if both white and black have no path
	// returns 11 if both white and black have path
	// returns 10 if white has path but black doesn't
	// return 01 if black has path but white doesn't
	private int dfsSearch() {
		// white pawn
		int whitePawnRow = QuoridorController.getCurrentRowForPawn(false);
		int whitePawnCol = QuoridorController.getCurrentColForPawn(false);

		// black pawn
		int blackPawnRow = QuoridorController.getCurrentRowForPawn(true);
		int blackPawnCol = QuoridorController.getCurrentColForPawn(true);

		int whiteCoordinate = (whitePawnRow - 1) * 9 + whitePawnCol;
		int blackCoordinate = (blackPawnRow - 1) * 9 + blackPawnCol;

		boolean whitePath = dfs(whiteCoordinate, true);
		boolean blackPath = dfs(blackCoordinate, false);
		
		if (whitePath == false && blackPath == false)
			return 00;
		
		if (whitePath == true && blackPath == true)
			return 11;
		
		if (whitePath == true && blackPath == false)
			return 10;
		
		//if (whitePath == false && blackPath == true)
		return 01;
	}

	private boolean dfs(int coordinate, boolean isWhitePlayer) {
		// Initially mark all vertices as not visited 
		ArrayList<Boolean> visited = new ArrayList<Boolean>(); 
		for (int i = 0; i <= 82; i++) 
			visited.add(false); 

		// Create a stack for DFS 
		Stack<Integer> stack = new Stack<>(); 

		// Push the current source node 
		stack.push(coordinate); 

		while(stack.empty() == false) { 
			// Pop a vertex from stack and print it 
			coordinate = stack.peek(); 
			stack.pop(); 

			// Stack may contain same vertex twice. So 
			// we need to print the popped item only 
			// if it is not visited. 
			if(visited.get(coordinate) == false)   
				visited.set(coordinate, true); 

			// Get all adjacent vertices of the popped vertex s 
			// If a adjacent has not been visited, then push it 
			// to the stack. 

			Iterator<Edge> itr = edges.iterator();
			
			Edge e = edges.get(coordinate);
			
			while (itr.hasNext()) {
				Edge v = itr.next();
				if (v.node_1 == e.node_1 && v.node_2 == e.node_2)
					break;
			}
			
			while (itr.hasNext()) { 
				Edge v = itr.next();
				if(!visited.get(v.node_1)) 
					stack.push(v.node_1); 
			} 
		}
		
		if (isWhitePlayer) {
			for (int i = 73; i <= 81; i++) {
				if (visited.get(i) == false)
					return false;
			}
		}
		else {
			for (int i = 1; i <= 9; i++) {
				if (visited.get(i) == false)
					return false;
			}
		}

		return true;
	}
	
	public void add_node(int n) {
		if (!nodes.contains(n)) nodes.add(n);
	}

}
