package com.dungeon.entities.weapons;

import java.awt.image.BufferedImage;
import java.util.Random;

import com.dungeon.entities.Bullet;
import com.dungeon.entities.Player;
import com.dungeon.entities.Weapon;
import com.dungeon.image.Art;
import com.dungeon.level.Level;

public class Shotgun implements Weapon {
	
	private int speed = 18;
	private int str = 4;
	private double crt = 0.04;
	private double acc = 0.10;
	private int shots = 3;
	private int ilvl = 1;

	private int maxstr = 6;
	private int minstr = 2;
	private int maxspeed = 22;
	private int minspeed = 14;
	private double maxcrt = 0.08;
	private double mincrt = 0.01;
	private double maxacc = 0.06;
	private double minacc = 0.20;
	
	BufferedImage img = Art.shotgun;
	
	public Shotgun(int ilvl) {
		this.ilvl = ilvl;
		Random rand = new Random();
		
		if(ilvl==1) {
			maxstr = 4;
			minstr = 1;
			maxspeed = 18;
			minspeed = 10;
			maxcrt = 0.02;
			mincrt = 0.00;
			maxacc = 0.06;
			minacc = 0.16;
		}
		else if(ilvl==2) {
			maxstr = 25;
			minstr = 3;
			maxspeed = 20;
			minspeed = 12;
			maxcrt = 0.03;
			mincrt = 0.00;
			maxacc = 0.05;
			minacc = 0.14;
		}
		else if(ilvl==3) {
			maxstr = 45;
			minstr = 10;
			maxspeed = 24;
			minspeed = 22;
			maxcrt = 0.05;
			mincrt = 0.00;
			maxacc = 0.04;
			minacc = 0.12;
		}
		else if(ilvl==4) {
			maxstr = 90;
			minstr = 20;
			maxspeed = 26;
			minspeed = 22;
			maxcrt = 0.06;
			mincrt = 0.00;
			maxacc = 0.04;
			minacc = 0.08;
		}
		
		str = minstr + ((int) (rand.nextDouble() * (maxstr-minstr)));
		speed = minspeed + ((int) (rand.nextDouble() * (maxspeed-minspeed)));
		crt = mincrt + (rand.nextDouble() * (maxcrt-mincrt));
		acc = maxacc + (rand.nextDouble() * (minacc-maxacc));
		shots = 2 + rand.nextInt(5);
	}
	
	public void fire(Level level, int x, int y, int strength, double crit) {
		Player player = (Player) level.getPlayer();
		if(player != null && player.ammo > 0) {
			double px = player.x;
			double py = (player.y - 4);
			double tx = (x) - (px);
			double ty = (y) - (py);

			if(level.viewRadius < px)
				tx += 2*(px - level.viewRadius);
			if((level.viewRadius*9/16) < py)
				ty += 2*(py - (level.viewRadius*9/16));
			if( level.width  - level.viewRadius < px)
			    tx -= 2*(px - (level.width - level.viewRadius));
			if( level.height - (level.viewRadius*9/16) < py)
				ty -= 2*(py - (level.height - (level.viewRadius*9/16)));
			
			double theta = Math.atan2(tx-px, ty-py);
			double L = Math.sqrt((Math.pow(ty-py, 2) + Math.pow(tx-px,2)));
			
			for(int i=0; i<shots; ++i) {
				double phi = Math.min(rand.nextGaussian(),1.2);
				double t1x = (L*Math.sin(theta + phi*acc)) + px;
				double t1y = (L*Math.cos(theta + phi*acc)) + py;

				level.bullets.add(new Bullet(level, px, py, t1x, t1y, player.velocity, false,  strength+str, Math.min(1.0, crit+crt), speed));
				player.useAmmo();
			}
			level.firing = false;
		}
	}

	public String getName() {
		return "Shotgun";
	}
	
	public int getStrength() {
		return str;
	}
	
	public double getCrit() {
		return crt;
	}
	
	public double getAccuracy() {
		return acc;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getShots() {
		return shots;
	}
	
	public int getiLvl() {
		return ilvl;
	}

	public BufferedImage getImage() {
		return img;
	}

}
