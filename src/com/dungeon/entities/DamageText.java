package com.dungeon.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.dungeon.level.Level;
import com.dungeon.math.Vector;

public class DamageText extends Particle {

	private String damage;
	private Color color;
	public DamageText(Level level, double x, double y, Vector direction, int lifetime, int radius, int type, boolean gravity, int damage, Color color) {
		super(level, x, y, direction, lifetime, radius, type, gravity);
		this.damage = String.valueOf(damage);
		this.color = color;
		this.radiusx = this.radiusy = (int) (2 + Math.ceil(Math.sqrt(damage/3.0)));
	}
	
	public DamageText(Level level, double x, double y, Vector direction,
			int lifetime, int radius, int type, boolean gravity, String damage,
			Color color) {
		super(level, x, y, direction, lifetime, radius, type, gravity);
		this.damage = damage;
		this.color = color;
		this.radiusx = this.radiusy = 4;
	}

	public void draw(Graphics g) {
		g.setColor(color);
		Font font = new Font("Serif", Font.BOLD, Math.max(3*radiusx,3*radiusy));
		g.setFont(font);
		String letter = damage;
		g.drawString(letter, (int)x - radiusx, (int)y + radiusy);
	}

}
