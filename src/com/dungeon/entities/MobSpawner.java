package com.dungeon.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;

import com.dungeon.boundingbox.BoundingBox;
import com.dungeon.entities.items.HealthPotion;
import com.dungeon.entities.items.AmmoPack;
import com.dungeon.entities.items.WeaponItem;
import com.dungeon.entities.weapons.MachineGun;
import com.dungeon.entities.weapons.Pistol;
import com.dungeon.entities.weapons.Shotgun;
import com.dungeon.image.Art;
import com.dungeon.image.ImageProcessing;
import com.dungeon.level.Level;
import com.dungeon.math.Combat;
import com.dungeon.math.Vector;

public class MobSpawner extends Entity {
	
	public int health = 40;
	public int val = 15;
	private int ilvl = 1;
	private boolean flash = false;
	private int flashTime = 0;
	Random rand = new Random();
	private int spawnTime = (int) (500 + rand.nextGaussian()*400);
	BufferedImage bi = Art.spawner;
	private int diff;

	public MobSpawner(Level level, int x, int y, int difficulty) {
		super(level);
		this.x = x;
		this.y = y;

		this.radiusx = 16;
		this.radiusy = 16;
		
		diff = difficulty;
		
		this.health = 100 + 15*diff;
		this.val = 20 + diff*2;
		
		this.ilvl = Math.min((int) Math.sqrt(diff), 4);

	}
	
	public void spawnMob() {
		Mob badGuyPoint = new Mob(level, x, y, diff);
		badGuyPoint.xto = x;
		badGuyPoint.yto = y;
		level.entities.add(badGuyPoint);
	}
	
	public void die() {
		remove();
		level.score+=5;
		level.getPlayer().addExp(val);
		if(rand.nextDouble() > 0.95)
			level.items.add(new WeaponItem(level, x, y, new Shotgun(ilvl), 35));
		else if(rand.nextDouble() > 0.93)
			level.items.add(new WeaponItem(level, x, y, new MachineGun(ilvl), 50));
		else if(rand.nextDouble() > 0.91)
			level.items.add(new WeaponItem(level, x, y, new Pistol(ilvl), 20));
		else if(rand.nextDouble() > 0.75)
			level.items.add(new HealthPotion(level, x, y, diff*18));
		else if(rand.nextDouble() > 0.6)
			level.items.add(new AmmoPack(level, x, y, 50));
				
	}

	public void hurt() {
		health--;
		flash();
	}

	public void flash() {
		flash = true;
		flashTime = 5;
	}
	
	public void tick() {
		
		spawnTime--;
		
		int badGuyCount = 0;
		for(int i = 0; i < level.entities.size(); i++) {
			Entity e = level.entities.get(i);
			if(!e.removed) {
				if(e instanceof Mob)
					badGuyCount++;
			}
		}
		
		if(spawnTime <= 0) {
			if(badGuyCount < level.maxmobs)
				spawnMob();
			spawnTime = Math.abs((int) (500 + rand.nextGaussian()*400));
		}
		
		if(health <= 0)
			die();
		
		if(flash)
			flashTime--;
		if(flashTime <= 0) {
			flash = false;
			colour = Color.RED;
		}
		

		BoundingBox mybb = getBoundingBox();
		List<Bullet> bullets = level.getBullets((int) (this.x - (radiusx+1)), (int) (this.y  - (radiusy+1)), (int) (this.x + (radiusx+1)), (int) (this.y + (radiusy+1)));
		for(int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
				if(bullet.hostile)
					continue;
				if(bullet.removed) {
					break;
				}
				BoundingBox playerbb = bullet.getBoundingBox();
				if(mybb.intersects(playerbb)) {
					bullet.remove();
					hurt(Combat.bulletDamage(bullet.damage, bullet.crit, 0));
				}
		}
	
	}
	
	public void hurt(int damage) {
		health-=damage;
		level.damagetext.add(new DamageText(level, x, y, new Vector(), 20, 8, 1, true, damage, Color.YELLOW));
		flash();
	}

	public void draw(Graphics g) {
//		Color oldCol = g.getColor();
//		g.setColor(Color.GREEN);
		BufferedImage renderImage = new BufferedImage(bi.getWidth(),bi.getHeight(),bi.getType());
		Graphics gi = renderImage.createGraphics();
		gi.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
		if(flash)
			ImageProcessing.recolourImage(renderImage, 50, -255, -255);
//		if(flash)
//			g.setColor(Color.RED);
		g.drawImage(renderImage, (int)(x-radiusx - 3), (int)(y-radiusy - 3), radiusx*2 + 6 , radiusy*2 + 6, null);
//		g.fillRect((int) (x-radiusx), (int) (y-radiusx), 2*radiusx, 2*radiusy);
//		
//		g.setColor(oldCol);
	}
}
