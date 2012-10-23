package com.dungeon.entities.items;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dungeon.image.Art;
import com.dungeon.level.Level;

public class HealthPotion extends ConsumableItem {
	
	BufferedImage bi = Art.fullhealth;

	public HealthPotion(Level level, double x, double y, int value) {
		super(level,x,y,value);
		this.value = value;
	}
	
	public void remove() {
		level.getPlayer().getInventory().addItem(this);
		removed = true;
	}
	
	public void draw(Graphics g) {
		BufferedImage renderImage = new BufferedImage(bi.getWidth(),bi.getHeight(),bi.getType());
		Graphics gi = renderImage.createGraphics();
		gi.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
		g.drawImage(renderImage, (int)(x-radiusx - 3), (int)(y-radiusy - 3), radiusx*2 + 6 , radiusy*2 + 6, null);
		
	}

	public void use() {
		level.getPlayer().addHealth(value);
		removed = true;
	}

	public String getName() {
		return "Health Potion";
	}

	public String getValue() {
		return "Restores "+value+" health";
	}

}
