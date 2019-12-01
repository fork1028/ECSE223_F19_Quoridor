package ca.mcgill.ecse223.quoridor.controller;

import java.io.*;
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

	public boolean wallAdded(int row, int col, Direction direction) {

		if (direction.equals(Direction.Horizontal)) {
			int node_1 = (row - 1) * 9 + col;
			int node_2 = row * 9 + col;
			int node_3 = (row - 1) * 9 + col + 1;
			int node_4 = row * 9 + col + 1;
			for (Edge e : edges) {
				if (e.node_1 == node_1 && e.node_2 == node_2) {
					edges.remove(e);
				}
				if (e.node_1 == node_3 && e.node_2 == node_4) {
					edges.remove(e);
				}
			}

		}
		else {
			int node_1 = (row - 1) * 9 + col;
			int node_2 = (row - 1) * 9 + col + 1;
			int node_3 = row * 9 + col;
			int node_4 = row * 9 + col + 1;
			for (Edge e : edges) {
				if (e.node_1 == node_1 && e.node_2 == node_2) {
					edges.remove(e);
				}
				if (e.node_1 == node_3 && e.node_2 == node_4) {
					edges.remove(e);
				}
			}
		}

		return dfsSearch();
	}

	private boolean dfsSearch() {
		// white pawn
		int whitePawnRow = QuoridorController.getCurrentRowForPawn(false);
		int whitePawnCol = QuoridorController.getCurrentColForPawn(false);
		
		// black pawn
		int blackPawnRow = QuoridorController.getCurrentRowForPawn(true);
		int blackPawnCol = QuoridorController.getCurrentColForPawn(true);

		int whiteCoordinate = (whitePawnRow - 1) * 9 + whitePawnCol;
		int blackCoordinate = (blackPawnRow - 1) * 9 + blackPawnCol;


		return true;
	}

	public void add_node(int n) {
		if (!nodes.contains(n)) nodes.add(n);
	}

}
