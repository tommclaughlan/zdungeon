package com.dungeon.entities.items;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.dungeon.entities.Item;
import com.dungeon.entities.Weapon;
import com.dungeon.image.Art;
import com.dungeon.level.Level;

public class WeaponItem extends Item {
	
	BufferedImage bi = Art.black;
	private Weapon weap;
	private int ammo = 0;

	public WeaponItem(Level level, double x, double y, Weapon dw, int am) {
		super(level,x,y);
		weap = dw;
		bi = weap.getImage();
		radiusx = (int) (radiusx*(bi.getWidth() / 32.0));
		ammo = am;
	}
	
	
	public void remove() {
		level.getPlayer().getInventory().addWeapon(weap);
		level.getPlayer().addMana(ammo);
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
		g.drawImage(renderImage, (int)(x-radiusx - 3), (int)(y-radiusy - 3), radiusx*2 + 6 , radiusy*2 + 6, null);
		////g.fillRect((int) (x-radiusx), (int) (y-radiusx), 2*radiusx, 2*radiusy);
		
//		Color oldCol = g.getColor();
//		g.setColor(Color.WHITE);
//		g.fillRect((int) (x-radiusx), (int) (y-radiusx), 2*radiusx, 2*radiusy);
//		g.setColor(Color.BLACK);
//		Font font = new Font("Serif", Font.PLAIN, radiusx);
//		g.setFont(font);
//		g.drawString(weap.getName(), (int) (x-radiusx), (int) (y));
//		g.setColor(oldCol);
	}

}
