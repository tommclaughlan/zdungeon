package com.dungeon.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import com.dungeon.level.Level;
import com.dungeon.math.Vector;

public class Particle extends Entity {

	int lifetime;
	Random rand = new Random();
	private boolean gravity;
	private int startlife;
	
	public Particle(Level level, double x, double y, Vector direction, int lifetime, int radius, int type, boolean gravity) {
		super(level);
		this.x = x;
		this.y = y;
		velocity = new Vector(direction);
		this.lifetime = (int) (lifetime + rand.nextGaussian()*lifetime/4);
		if(type == 1) // blood
			this.colour = new Color(rand.nextInt(100-70)+70, rand.nextInt(10), rand.nextInt(10));
		if(type == 2) // bullet spray
			this.colour = new Color(rand.nextInt(255-200)+200, rand.nextInt(255-200)+200, rand.nextInt(255-200)+200);
		if(type == 3) // vom
			this.colour = new Color(rand.nextInt(10), rand.nextInt(135-100)+100, rand.nextInt(10));
		this.radiusx = this.radiusy = (int) Math.ceil(rand.nextDouble()*radius);
		velocity.x += rand.nextGaussian();
		velocity.y += rand.nextGaussian();
		this.gravity = gravity;
		startlife = lifetime;
	}
	
	public void tick() {
		if(lifetime <= 0)
			remove();
		
		x+=velocity.x;
		y+=velocity.y;
		if(gravity)
			y+=2 * (1 - (lifetime / (double)startlife));
		
		velocity.extend(velocity.length()*0.9);
		
		lifetime--;
		
	}
	

	public void draw(Graphics g) {
		g.setColor(this.colour);
		g.fillRoundRect((int)(this.x - radiusx), (int)(this.y - radiusy), radiusx*2, radiusy*2, 6, 6);
		//g.fillOval((int)(this.x - radiusx), (int)(this.y - radiusy), radiusx*2, radiusy*2);
	}

}
