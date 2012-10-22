package com.dungeon.entities.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dungeon.entities.Item;
import com.dungeon.image.Art;
import com.dungeon.level.Level;

public class AmmoPack extends Item {
	
	public int value;
	BufferedImage bi = Art.ammo;

	public AmmoPack(Level level, double x, double y, int value) {
		super(level,x,y);
		this.value = value;
	}
	
	public void remove() {
		level.getPlayer().addAmmo(value);
		removed = true;
	}
	
	public void draw(Graphics g) {
		////Color oldCol = g.getColor();
		////g.setColor(Color.BLUE);
		//if(value < 10)
		//	bi = Art.lessermana;
		BufferedImage renderImage = new BufferedImage(bi.getWidth(),bi.getHeight(),bi.getType());
		Graphics gi = renderImage.createGraphics();
		gi.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
		g.drawImage(renderImage, (int)(x-radiusx - 3), (int)(y-radiusy - 3), 2*radiusx, 2*radiusy, null);
		////g.fillRect((int) (x-radiusx), (int) (y-radiusx), 2*radiusx, 2*radiusy);
		
//		Color oldCol = g.getColor();
//		g.setColor(Color.WHITE);
//		g.fillRect((int) (x-radiusx), (int) (y-radiusx), 2*radiusx, 2*radiusy);
//		g.setColor(Color.BLACK);
//		Font font = new Font("Serif", Font.PLAIN, radiusx/2);
//		g.setFont(font);
//		g.drawString("AMMO", (int) (x-radiusx), (int) (y));
//		g.setColor(oldCol);
	}
}
