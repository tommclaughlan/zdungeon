package com.dungeon.entities.items;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dungeon.entities.Item;
import com.dungeon.image.Art;
import com.dungeon.level.Level;

public class ManaPotion extends Item {
	
	public int value;
	BufferedImage bi = Art.fullmana;

	public ManaPotion(Level level, double x, double y, int value) {
		super(level,x,y);
		this.value = value;
	}
	
	
	public void remove() {
		level.getPlayer().addMana(value);
		removed = true;
	}
	
	public void draw(Graphics g) {
		//Color oldCol = g.getColor();
		//g.setColor(Color.BLUE);
		if(value < 10)
			bi = Art.lessermana;
		BufferedImage renderImage = new BufferedImage(bi.getWidth(),bi.getHeight(),bi.getType());
		Graphics gi = renderImage.createGraphics();
		gi.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
		g.drawImage(renderImage, (int)(x-radiusx - 3), (int)(y-radiusy - 3), radiusx*2 + 6 , radiusy*2 + 6, null);
		//g.fillRect((int) (x-radiusx), (int) (y-radiusx), 2*radiusx, 2*radiusy);
		
		//g.setColor(oldCol);
	}

}
