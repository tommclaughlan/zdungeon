package com.dungeon.entities.items;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dungeon.entities.Item;
import com.dungeon.image.Art;
import com.dungeon.level.Level;

public abstract class ConsumableItem extends Item {
	
	BufferedImage bi = Art.black;
	public int value;

	public ConsumableItem(Level level, double x, double y, int val) {
		super(level,x,y);
		value = val;
	}
	
	public void remove() {
		level.getPlayer().getInventory().addItem(this);
		removed = true;
	}
	
	public abstract void use();
	
	public abstract String getName();
	
	public abstract String getValue();
	
	public abstract void draw(Graphics g);

}
