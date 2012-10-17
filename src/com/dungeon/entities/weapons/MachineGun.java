package com.dungeon.entities.weapons;

import com.dungeon.entities.Bullet;
import com.dungeon.entities.Player;
import com.dungeon.entities.Weapon;
import com.dungeon.level.Level;

public class MachineGun implements Weapon {
	
	private int speed = 12;
	private int str = 2;
	private double crt = 0.2;
	private double acc = 0.01;
	
	public void fire(Level level, int x, int y, int strength, double crit) {
		Player player = (Player) level.getPlayer();
		if(player != null && player.ammo > 0) {
			double px = player.x;
			double py = player.y - 4;
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
			
			double theta = Math.atan2(tx, ty);
			double L = Math.sqrt((Math.pow(ty, 2) + Math.pow(tx,2)));
			
			for(int i=0; i<1; ++i) {
				double phi = Math.min(rand.nextGaussian(),1.2);
				double t1x = (L*Math.sin(theta + phi*acc));
				double t1y = (L*Math.cos(theta + phi*acc));
				level.bullets.add(new Bullet(level, px, py, t1x, t1y, player.velocity, false,  strength+str, Math.min(1.0, crit+crt), speed));
			}
			player.useAmmo();
		}
	}

}
