package com.dungeon.level.tile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.dungeon.boundingbox.BoundingBox;
import com.dungeon.boundingbox.BoundingBoxOwner;
import com.dungeon.entities.Entity;
import com.dungeon.image.Art;
import com.dungeon.level.Level;

public class Tile implements BoundingBoxOwner {
	
	public int x,y;
	public int tileWidth;
	BufferedImage bi = Art.black;
	//Color color = Color.DARK_GRAY;
	public boolean noclip = true;
	
	public Tile(Level level, int x, int y) {
		tileWidth = level.getMap().tileSize;
		this.x = x*tileWidth;
		this.y = y*tileWidth;
	}

	@Override
	public void handleCollision(Entity entity, double xa, double ya) {
		
	}

	public BoundingBox getBoundingBox() {
		return new BoundingBox(this, x, y, x + tileWidth, y + tileWidth);
	}

	public void draw(Graphics2D g) {
		//g.setColor(color);
		//g.fillRect(x, y, tileWidth, tileWidth);
		//BufferedImage renderImage = new BufferedImage(bi.getWidth(),bi.getHeight(),bi.getType());
		//Graphics gi = renderImage.createGraphics();
		//gi.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
		g.drawImage(bi, x, y, tileWidth, tileWidth, null);
	}

}
