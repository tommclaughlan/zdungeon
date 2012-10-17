package com.dungeon.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Random;

import com.dungeon.level.Level;
import com.dungeon.math.Vector;

public class Bullet extends Entity {

	public double targetx;
	public double targety;
	public double startx;
	public double starty;
	public int damage;
	public double crit;
	public int lifetime;
	Vector vec;
	double distance = 0;
	protected Random rand = new Random();
	private int startradiusx = 5, startradiusy = 5;
	public boolean hostile = false;
	
	public Bullet(Level level, double x, double y, double targetx, double targety, Vector velocity, boolean hostile, int damage, double crit, int speed) {
		super(level);
		this.x = x;
		this.y = y;
		this.startx = x;
		this.starty = y;
		this.targetx = targetx;
		this.targety = targety;
		this.damage = damage;
		this.crit = crit;
		this.vec = new Vector(x, y, targetx, targety, 1.0); // force unit vector
		vec.extend(speed);
		lifetime = (int) (20+(rand.nextGaussian()*10));
		this.colour = Color.WHITE;
		this.radiusx = startradiusx;
		this.radiusy = startradiusy;
		this.velocity = new Vector(velocity);
		this.hostile = hostile;
		if(hostile) {
			lifetime = (int) (20+(rand.nextGaussian()*8));
			vec.extend(speed);
			vec.x += rand.nextGaussian();
			vec.y += rand.nextGaussian();
			this.colour = new Color(rand.nextInt(20), rand.nextInt(135-100)+100, rand.nextInt(20));
		}
	}
	
	public void tick() {
		if(!removed) {
			lifetime--;

			radiusx = (int) (startradiusx*(0.5+rand.nextDouble()/2));
			radiusy = (int) (startradiusy*(0.5+rand.nextDouble()/2));
			
			int wigglex = 0;
			int wiggley = 0;
			
			if(lifetime % 1 == 0) {
				wigglex = (int) (Math.cos((rand.nextDouble()*2*Math.PI))*2);
				wiggley = (int) (Math.sin((rand.nextDouble()*2*Math.PI))*2);
			}
			
			xto = x + (int) (vec.x + velocity.x) + wigglex;
			yto = y + (int) (vec.y + velocity.y) + wiggley;

			if(!canMoveX() || !canMoveY())
				die();
			
			if(canMoveX())
				moveX();
			if(canMoveY())
				moveY();
			if(lifetime <= 0)
				removed = true;

		}
	}
	
	private void die() {
		remove();
		spray(new Vector(), 10, 10, 1);
	}

	private void spray(Vector velocity, int num, int life, int rad) {
		for(int i=0; i < num; i++) {
			level.particles.add(new Particle(level, x, y-8, velocity, life, rad, hostile ? 3 : 2, true));
		}
	}

	public void draw(Graphics g) {
		g.setColor(this.colour);
		if(hostile) {
			g.fillOval((int)(this.x - radiusx), (int)(this.y - radiusy), radiusx*2, radiusy*2);
		}
		if(!hostile) {
			Font font = new Font("Serif", Font.ITALIC, Math.max(3*radiusx,3*radiusy));
			g.setFont(font);
			g.fillOval((int)(this.x - radiusx), (int)(this.y - radiusy), radiusx, radiusy);
		}
	}
	

}
