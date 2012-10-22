package com.dungeon.entities;

import com.dungeon.level.Level;

public class Item extends Entity {

	public Item(Level level, double x, double y) {
		super(level);
		this.x = x;
		this.y = y;
		this.radiusx = 8;
		this.radiusy = 8;
	}
	
}
