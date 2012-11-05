package com.dungeon.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import com.dungeon.boundingbox.BoundingBox;
import com.dungeon.boundingbox.BoundingBoxOwner;
import com.dungeon.level.Level;
import com.dungeon.level.tile.FloorTile;
import com.dungeon.level.tile.Tile;
import com.dungeon.math.Vector;

public class Entity implements BoundingBoxOwner{
	public Level level;
    public double x, y;
    public double xto, yto;
    public int radiusx, radiusy;
    public Color colour;
    public boolean removed = false;

	public Vector velocity;
    
    public Entity(Level level) {
    	this.level = level;
    }
    
    public Color getColour() {
    	return this.colour;
    }
    
	public double getX() {
		return this.x;
	}	
	
	public double getY() {
		return this.y;
	}
	
	public void tick() {
	}
	
	public void remove() {
		removed = true;
	}

    public BoundingBox getBoundingBox() {
        return new BoundingBox(this, x - radiusx, y - radiusy, x + radiusx, y + radiusy);
    }
    
    public BoundingBox getToXBoundingBox() {
        return new BoundingBox(this, xto - radiusx, y - radiusy, xto + radiusx, y + radiusy);
    }
    
    public BoundingBox getToYBoundingBox() {
        return new BoundingBox(this, x - radiusx, yto - radiusy, x + radiusx, yto + radiusy);
    }

    public BoundingBox[] getBBS() {

    	BoundingBox[] bbs = new BoundingBox[level.entities.size()];
    	int i = 0;
    	for(Entity e : level.entities) {
    		bbs[i] = e.getBoundingBox();
    		i++;
    	}
    	return bbs;
    }
    
	public void hurt() {
	}
	
	public boolean canMoveX() {
		// TODO: these need to be arranged in x and y coordinates so can easily grab the nearest tiles
		// then check if we collide or not. looping over all tiles is just too inefficient!
		List<Tile> tiles = level.getTiles((int) (xto/level.getMap().tileSize) - 2, (int) (y/level.getMap().tileSize) - 2, (int) (xto/level.getMap().tileSize) + 2, (int) (y/level.getMap().tileSize) + 2);
		for(int i=0; i < tiles.size(); i++) {
			if(tiles.get(i) instanceof FloorTile)
				continue;
			if(getToXBoundingBox().intersects(tiles.get(i).getBoundingBox()))
				return false;	
		}
		return true;
	}
	
	public boolean canMoveY() {
		List<Tile> tiles = level.getTiles((int) (x/level.getMap().tileSize) - 2, (int) (yto/level.getMap().tileSize) - 2, (int) (x/level.getMap().tileSize) + 2, (int) (yto/level.getMap().tileSize) + 2);
		for(int i=0; i < tiles.size(); i++) {
			if(tiles.get(i) instanceof FloorTile)
				continue;
			if(getToYBoundingBox().intersects(tiles.get(i).getBoundingBox()))
				return false;	
		}
		return true;
	}

	public void moveX() {
		x=xto;
		xto = 0;
	}
	
	public void moveY() {
		y=yto;
		yto = 0;
	}

	public void handleCollision(Entity entity, double xa, double ya) {
		
	}

	public void draw(Graphics g) {
	}

}
