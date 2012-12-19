package com.dungeon.entities;

import com.dungeon.entities.items.AmmoPack;
import com.dungeon.entities.items.WeaponItem;
import com.dungeon.entities.weapons.MachineGun;
import com.dungeon.entities.weapons.Pistol;
import com.dungeon.entities.weapons.Shotgun;
import com.dungeon.level.Level;
import com.dungeon.math.Vector;

public class Exploder extends Mob {

	public Exploder(Level level, double x, double y, int difficulty) {
		super(level, x, y, difficulty);
	}

	public void tick() {
		Player player = (Player) level.getPlayer();
		Vector metoplayer = new Vector(x,y,player.x,player.y);
		if(metoplayer.length() < sightRadius / 8)
			die();
		
		super.tick();
		
	}

	public void die() {
		remove();
		level.score++;
		level.getPlayer().addExp(val);
		spray(new Vector(), 250, 200, 3);
		
		for(int i=0; i<60; ++i) {
			level.enemyBullet(this, (int)(100*rand.nextGaussian()+x), (int)(100*rand.nextGaussian()+y), strength, crit);
		}
		
		if(rand.nextDouble() > 0.96)
			level.items.add(new WeaponItem(level, x, y, new Shotgun(ilvl), 35));
		else if(rand.nextDouble() > 0.94)
			level.items.add(new WeaponItem(level, x, y, new MachineGun(ilvl), 50));
		else if(rand.nextDouble() > 0.92)
			level.items.add(new WeaponItem(level, x, y, new Pistol(ilvl), 20));
		else if(rand.nextDouble() > 0.8)
			level.items.add(new AmmoPack(level, x, y, 25));
	}

}