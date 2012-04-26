package com.dungeon.screen;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.dungeon.entities.Entity;
import com.dungeon.level.tile.Tile;

public class Screen extends BufferedImage {

	public int w,h;
	private Graphics2D g;
	
	public Screen(int width, int height) {
		super(width, height, BufferedImage.TYPE_INT_ARGB);
		g = this.createGraphics();
	}

	public Graphics2D getGraphics() {
		return g;
	}

	public void draw(Entity entity) {
		entity.draw(g);
	}
	
	public void draw(Tile tile) {
		tile.draw(g);
	}


}
