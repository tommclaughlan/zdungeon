package com.dungeon.math;

import java.util.ArrayList;
import java.util.List;

public class Node {

	public int x,y;
	public Node parent;
	public int h,g,cost;
	public List<Node> neighbours = new ArrayList<Node>();
	
	public Node(int x, int y, Node parent) {
		this.x = x;
		this.y = y;
		this.parent = parent;
		h = 0;
		g = 0;
		cost = 0;
	}
	
	public Node getParent() {
		return parent;
	}

	public void setHScore(int h) {
		this.h = h;
	}
	
	public void setGScore(int g) {
		this.g = g;
	}
	
	public int getFScore() {
		return g+h;
	}
	
	public String printNode() {
		return "("+x+", "+y+")";
	}
	
	public void fillNeighbours(List<Node> nodemap) {
		
		for(Node n : nodemap) {
			if(n.x > this.x + 1)
				continue;
			if(n.x < this.x - 1)
				continue;
			if(n.y > this.y + 1)
				continue;
			if(n.y < this.y - 1)
				continue;
			if(n.y == this.y && n.x == this.x)
				continue;
			
			neighbours.add(n);
		}
	}
	
	public int distance(Node target) {
		// returns distance using manhattan method
		return (Math.abs(this.x - target.x) + Math.abs(this.y - target.y));
	}
	
	
	
}
