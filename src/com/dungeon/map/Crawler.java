package com.dungeon.map;

import java.util.Random;

public class Crawler {

	int startx, starty;
	int targetx, targety;
	int x, y;
	int life;
	int facing;
	Map map;
	Random rand = new Random();
	
	public Crawler(int x, int y, int life, Map map, int targetx, int targety) {
		this.startx = x;
		this.starty = y;
		this.targetx = targetx;
		this.targety = targety;
		this.life = life;
		this.x = startx;
		this.y = starty;
		this.map = map;
		facing = rand.nextInt(4);
	}
	
	public void crawl() {
		if(targetx == 0 && targety == 0) {
			if(rand.nextDouble() > 0.95)
				facing = rand.nextInt(4);

			if(facing == 0 && y > 1) { // north
				y-=1;
			}
			if(facing == 1 && x < map.width - 2) { // east
				x+=1;
			}
			if(facing == 2 && y < map.height - 2) { // south
				y+=1;
			}
			if(facing == 3 && x > 1) { // west
				x-=1;
			}

		}
		else {
			if(facing==0 || facing==2) {
				if(targety < y && y > 1)
					y-=1;
				if(targety > y && y < map.height - 2)
					y+=1;
			}if(facing==1 || facing==3) {
				if(targetx < x && x > 1)
					x-=1;
				if(targetx > x && x < map.width - 2)
					x+=1;
			}
			if(rand.nextDouble() > 0.7)
				facing = rand.nextInt(4);
		}
		
		if(map.tiles[x][y] != 0) {
			map.tiles[x][y] = 0;
		}

		life--;

		
	}
	
}
