package com.dungeon.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dungeon.math.Node;

public class Map {
	
	public int width, height;
	protected int[][] tiles;
	public int tileSize = 32;
	public int maxCorridorsPerRoom = 2;
	public List<Node> nodes = new ArrayList<Node>();
	public List<Room> rooms = new ArrayList<Room>();
	public List<Crawler> crawlers = new ArrayList<Crawler>();
	public List<Point> mobspawners = new ArrayList<Point>();
	
	Random rand = new Random();
	
	public Map(int width, int height) {
		this.width = width;
		this.height = height;
		tiles = new int[width][height];
	}

	public void generate() {
		
		for(int x=0; x < width; x++) {
			for(int y=0; y < height; y++) {
				tiles[x][y] =1;
			}
		}

		for(int x=0; x < width; x++) {
			tiles[x][0] = 1;
			tiles[x][height-1] = 1;
		}
		for(int y=0; y < height; y++) {
			tiles[0][y] = 1;
			tiles[width-1][y] = 1;
		}

		int numRooms = width / 10;
		rooms.clear();
		crawlers.clear();
		nodes.clear();
		mobspawners.clear();
		rooms.add(new Room(1,1,6,6,10));
		
		while(rooms.size() < numRooms) {
			int roomSizeX = ((rand.nextInt(10)+2) * 2) + 1;
			int roomSizeY = ((rand.nextInt(10)+2) * 2) + 1;
			int roomX = rand.nextInt(width - roomSizeX -1) + 1;
			int roomY = rand.nextInt(height - roomSizeY -1) + 1;
			
			Room room = new Room(roomX, roomY, roomX+roomSizeX, roomY+roomSizeY, -2);//rand.nextInt(5)+1);
			rooms.add(room);
			
			boolean overlapping = false;
			
			for(Room r : rooms) {
				if(room == r)
					continue;
				if(room.overlaps(r)) {
					overlapping = true;
				}
			}
			if(overlapping) {
				rooms.remove(room);
			}
		}
		
		for(Room r : rooms) {
			r.createRoom(this);
			int corridorsperroom = rand.nextInt(maxCorridorsPerRoom) + 1;
			for(int i=0; i < corridorsperroom; i++) {
				Point start = r.randomStart();
				Point target = r.randomTarget(rooms);
				crawlers.add(new Crawler(start.x, start.y, width+height, this, target.x, target.y));
			}
			int mobspawnerNum = rand.nextInt(3) + 1;
			if(r.x0 == 1 && r.y0 ==1)
				continue;
			mobspawners.addAll(r.addMobs(mobspawnerNum));
		}
		if(rand.nextDouble() > 0.7)
			crawlers.add(new Crawler(1,1,rand.nextInt(500)+250,this,0,0));
		
		for(Crawler c : crawlers) {
			while(c.life > 0)
				c.crawl();
		}
		
		/*
		Crawler crawler = new Crawler(1,1,2000,this);
		while(crawler.life > 0)
			crawler.crawl();
		*/
		
		makeNodes();
		
	}
	
	public int getSquare(int x, int y) {
		return tiles[x][y];
	}
	
	public boolean[][] getWalkableMap() {
		boolean[][] result = new boolean[width][height];
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < width; y++) {
				if(tiles[x][y] != 0)
					result[x][y] = false;
				else
					result[x][y] = true;
			}
		}
		return result;
	}
	
	public void makeNodes() {
		//Fill list of blank nodes to give us the map
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				if(tiles[x][y] != 0)
					continue;
				nodes.add(new Node(x,y,null));
			}
		}
		for(Node n : nodes)
			n.fillNeighbours(nodes);
	}

}
