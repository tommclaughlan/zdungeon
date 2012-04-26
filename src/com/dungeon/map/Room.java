package com.dungeon.map;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {
	
	public int x0,y0,x1,y1;
	public int border;
	Random rand = new Random();
	
	public Room(int x0, int y0, int x1, int y1, int border) {
		this.x0 = x0;
		this.y0 = y0;
		this.x1 = x1;
		this.y1 = y1;
		this.border = border;
	}
	
	
	public boolean overlaps(Room room) {
		if(room.x0 < x1 + border && room.y0 < y1 + border && room.x1 > x0 - border && room.y1 > y0 - border)
			return true;
		
		return false;
	}
	
	public void createRoom(Map map) {
		for(int x=x0; x < x1; x++) {
			for(int y=y0; y < y1; y++) {
				map.tiles[x][y] = 0;
			}
		}
	}
	
	public Point randomStart() {
		int x = rand.nextInt(x1-x0) + x0;
		int y = rand.nextInt(y1-y0) + y0;
		
		return new Point(x,y);
	}	
	
	public Point randomTarget(List<Room> rooms) {
		Room room = rooms.get(rand.nextInt(rooms.size()));
		while(room.x0 == this.x0 && room.y0 == y0) {
			room = rooms.get(rand.nextInt(rooms.size()));
		}
		int x = rand.nextInt(room.x1-room.x0) + room.x0;
		int y = rand.nextInt(room.y1-room.y0) + room.y0;
		
		return new Point(x,y);
	}
	
	public List<Point> addMobs(int num) {
		List<Point> spawners = new ArrayList<Point>();
		for(int i=0; i<num; i++) {
			int msx = rand.nextInt((x1)-(x0)-2) + (x0+1);
			int msy = rand.nextInt((y1)-(y0)-2) + (y0+1);
			Point p = new Point(msx,msy);
			if(spawners.contains(p)){
				i--;
				continue;
			}
			spawners.add(p);
		}
		return spawners;
	}

}
