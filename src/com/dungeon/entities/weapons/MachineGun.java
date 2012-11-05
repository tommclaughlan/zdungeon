package com.dungeon.entities.weapons;

import java.awt.image.BufferedImage;
import java.util.Random;

import com.dungeon.MainComponent;
import com.dungeon.entities.Bullet;
import com.dungeon.entities.Player;
import com.dungeon.entities.Weapon;
import com.dungeon.image.Art;
import com.dungeon.level.Level;

public class MachineGun implements Weapon {
	
	private int speed = 12;
	private int str = 3;
	private double crt = 0.050;
	private double acc = 0.01;
	private int shots = 1;
	private int ilvl = 1;

	private int maxstr = 6;
	private int minstr = 2;
	private int maxspeed = 28;
	private int minspeed = 20;
	private double maxcrt = 0.06;
	private double mincrt = 0.001;
	private double maxacc = 0.01;
	private double minacc = 0.08;
	
	BufferedImage img = Art.machinegun;
	
	public MachineGun(int ilvl) {
		this.ilvl = ilvl;
		Random rand = new Random();
		
		if(ilvl==1) {
			maxstr = 3;
			minstr = 1;
			maxspeed = 42;
			minspeed = 16;
			maxcrt = 0.03;
			mincrt = 0.001;
			maxacc = 0.04;
			minacc = 0.10;
		}
		else if(ilvl==2) {
			maxstr = 22;
			minstr = 6;
			maxspeed = 44;
			minspeed = 18;
			maxcrt = 0.04;
			mincrt = 0.005;
			maxacc = 0.03;
			minacc = 0.08;
		}
		else if(ilvl==3) {
			maxstr = 42;
			minstr = 17;
			maxspeed = 45;
			minspeed = 20;
			maxcrt = 0.05;
			mincrt = 0.01;
			maxacc = 0.02;
			minacc = 0.07;
		}
		else if(ilvl==4) {
			maxstr = 80;
			minstr = 30;
			maxspeed = 46;
			minspeed = 32;
			maxcrt = 0.08;
			mincrt = 0.03;
			maxacc = 0.01;
			minacc = 0.05;
		}
		
		
		str = minstr + ((int) (rand.nextDouble() * (maxstr-minstr)));
		speed = minspeed + ((int) (rand.nextDouble() * (maxspeed-minspeed)));
		crt = mincrt + (rand.nextDouble() * (maxcrt-mincrt));
		acc = maxacc + (rand.nextDouble() * (minacc-maxacc));
	}
	
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
		}
	}
	
	public String getName() {
		return "Machine Gun";
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
