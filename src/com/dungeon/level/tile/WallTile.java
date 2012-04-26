package com.dungeon.level.tile;

import java.awt.Graphics2D;

import com.dungeon.image.Art;
import com.dungeon.level.Level;

public class WallTile extends Tile {

	public WallTile(Level level, int x, int y) {
		super(level, x, y);
		bi = Art.wallTest;
		//color = Color.BLACK;
		noclip = false;
	}

	public void draw(Graphics2D g) {
		//BufferedImage renderImage = new BufferedImage(bi.getWidth(),bi.getHeight(),bi.getType());
		//Graphics gi = renderImage.createGraphics();
		//gi.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
		g.drawImage(bi, x-2, y - 16, tileWidth + 4, tileWidth + 17, null);
	}

}
