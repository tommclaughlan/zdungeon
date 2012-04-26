package com.dungeon.level.tile;

import com.dungeon.image.Art;
import com.dungeon.level.Level;

public class FloorTile extends Tile {


	public FloorTile(Level level, int x, int y) {
		super(level, x, y);
		//color = Color.DARK_GRAY;
		bi = Art.floorTest;
		// TODO Auto-generated constructor stub
	}

}
