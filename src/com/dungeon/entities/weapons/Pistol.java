package com.dungeon.entities.weapons;

import java.awt.image.BufferedImage;
import java.util.Random;

import com.dungeon.MainComponent;
import com.dungeon.entities.Bullet;
import com.dungeon.entities.Player;
import com.dungeon.entities.Weapon;
import com.dungeon.image.Art;
import com.dungeon.level.Level;

public class Pistol implements Weapon {
	
	private int speed = 18;
	private int str = 2;
	private double crt = 0.05;
	private double acc = 0.02;
	private int shots = 1;
	private int ilvl = 1;

	private int maxstr = 4;
	private int minstr = 1;
	private int maxspeed = 25;
	private int minspeed = 16;
	private double maxcrt = 0.10;
	private double mincrt = 0.005;
	private double maxacc = 0.01;
	private double minacc = 0.06;
	
	BufferedImage img = Art.pistol;
	
	
	public Pistol(int ilvl) {
		this.ilvl = ilvl;
		Random rand = new Random();
		
		if(ilvl==1) {
			maxstr = 2;
			minstr = 1;
			maxspeed = 44;
			minspeed = 18;
			maxcrt = 0.04;
			mincrt = 0.002;
			maxacc = 0.03;
			minacc = 0.08;
		}
		else if(ilvl==2) {
			maxstr = 20;
			minstr = 5;
			maxspeed = 44;
			minspeed = 20;
			maxcrt = 0.05;
			mincrt = 0.005;
			maxacc = 0.02;
			minacc = 0.06;
		}
		else if(ilvl==3) {
			maxstr = 38;
			minstr = 16;
			maxspeed = 44;
			minspeed = 22;
			maxcrt = 0.08;
			mincrt = 0.01;
			maxacc = 0.02;
			minacc = 0.05;
		}
		else if(ilvl==4) {
			maxstr = 75;
			minstr = 28;
			maxspeed = 46;
			minspeed = 32;
			maxcrt = 0.10;
			mincrt = 0.03;
			maxacc = 0.01;
			minacc = 0.04;
		}
		
		str = minstr + ((int) (rand.nextDouble() * (maxstr-minstr)));
		speed = minspeed + ((int) (rand.nextDouble() * (maxspeed-minspeed)));
		crt = mincrt + (rand.nextDouble() * (maxcrt-mincrt));
		acc = maxacc + (rand.nextDouble() * (minacc-maxacc));
	}
	
//	public void fire(Level level, int x, int y, int strength, double crit) {
//		Player player = (Player) level.getPlayer();
//		if(player != null && player.ammo > 0) {
//			double px = player.x;
//			double py = player.y - 4;
//			double tx = (x) - (px);
//			double ty = (y) - (py);
//			
//			System.out.print("Click x,y = "+x+","+y+"\nPlayer x,y = "+px+","+(py+4)+"\n");
//			
//			if(level.viewRadius*(2/MainComponent.SCALE) < px)
//				tx += 2*(px - level.viewRadius*(2/MainComponent.SCALE));
//			if((level.viewRadius*(2/MainComponent.SCALE)*9/16) < py)
//				ty += 2*(py - (level.viewRadius*(2/MainComponent.SCALE)*9/16));
//			if( level.width  - level.viewRadius*(2/MainComponent.SCALE) < px)
//			    tx -= 2*(px - (level.width - level.viewRadius*(2/MainComponent.SCALE)));
//			if( level.height - (level.viewRadius*(2/MainComponent.SCALE)*9/16) < py)
//				ty -= 2*(py - (level.height - (level.viewRadius*(2/MainComponent.SCALE)*9/16)));
//
//			System.out.print("Target x,y = "+tx+","+ty+"\n");
//
//			double theta = Math.atan2(tx, ty);
//			double L = Math.sqrt((Math.pow(ty, 2) + Math.pow(tx,2)));
//			
//			for(int i=0; i<shots; ++i) {
//				double phi = Math.min(rand.nextGaussian(),1.2);
//				double t1x = (L*Math.sin(theta + phi*acc)) + px;
//				double t1y = (L*Math.cos(theta + phi*acc)) + py;
//				level.bullets.add(new Bullet(level, px, py, t1x, t1y, player.velocity, false,  strength+str, Math.min(1.0, crit+crt), speed));
//				player.useAmmo();
//			}
//			level.firing = false;
//		}
//	}
	
	public void fire(Level level, int x, int y, int strength, double crit) {
		Player player = (Player) level.getPlayer();
		if(player != null && player.ammo > 0) {
			
			double px = player.x;
			double py = player.y - 4;
			
			double tx = x - (MainComponent.GAME_WIDTH/2);
			double ty = y - (MainComponent.GAME_HEIGHT/2);
			
			double tX = px - level.viewRadius;
			double tY = py - (level.viewRadius*9/16);

			if(tX < 0)
				tx+=(MainComponent.GAME_WIDTH/2) - px;
			if(tY < 0)
				ty+=(MainComponent.GAME_HEIGHT/2) - py;
			if(tX + 2*level.viewRadius > level.width)
				tx-=(MainComponent.GAME_WIDTH/2) - (level.width - px);
			if(tY + (level.viewRadius*9/8) > level.height)
				ty-=(MainComponent.GAME_HEIGHT/2) - (level.height - py);
			
			double theta = Math.atan2(tx, ty);
			double L = Math.sqrt((Math.pow(ty, 2) + Math.pow(tx,2)));
			
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
		return "Pistol";
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
