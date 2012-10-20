package com.dungeon.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;


import com.dungeon.Keys;
import com.dungeon.boundingbox.BoundingBox;
import com.dungeon.entities.player.Inventory;
import com.dungeon.entities.weapons.*;
import com.dungeon.image.Art;
import com.dungeon.image.ImageProcessing;
import com.dungeon.level.Level;
import com.dungeon.math.Combat;
import com.dungeon.math.Vector;

public class Player extends Entity {

	BufferedImage[][] bi = Art.playerImages;
	Color colour;
	Keys keys;
	public double speed;
	private boolean flash = false;
	private int flashTime = 0;
	public int health;
	public int ammo;
	
	public int maxhealth = 30;
	public int maxammo = 500;
	
	public int strength;
	public int defense;
	public double crit;
	Random rand = new Random();
	public int fireRate = 6;
	private int walkTime;
	
	public boolean changedweapon = true;
	private int weaponInfoTimer = 120;

	private Weapon weapon;
	
	private Inventory inventory;
	
	public Player(Level level, Keys keys, int x, int y) {
		super(level);
		this.x = x;
		this.y = y;
		this.keys = keys;
		this.inventory = new Inventory(level,keys);
		this.radiusx = 9;
		this.radiusy = 10;
		colour = Color.YELLOW;
		speed = 2.5;
		health = maxhealth;
		ammo = maxammo;
		
		strength = 4;
		defense = 2;
		crit = 0.2;
		
		walkTime = 0;
		velocity = new Vector();

		inventory.addWeapon(new Pistol());
		inventory.addWeapon(new Shotgun());
		inventory.addWeapon(new MachineGun());
		weapon = inventory.getEquippedWeapon();
	}
	
	public void move(int x, int y) {
		this.x = x;
		this.y = y;	
	}
	
	public void tick() {
		//if(ammo < maxammo && rand.nextDouble() > 0.93)
		//	ammo++;
		if(changedweapon)
			weaponInfoTimer--;
		if(weaponInfoTimer <= 0) {
			inventory.changedweapon=false;
			weaponInfoTimer = 120;
		}
		
		BoundingBox mybb = getBoundingBox();
		for(int i = 0; i < level.getBullets().size(); i++) {
			Bullet bullet = level.getBullets().get(i);
				if(bullet.removed || !bullet.hostile) {
					continue;
				}
				BoundingBox playerbb = bullet.getBoundingBox();
				if(mybb.intersects(playerbb) && rand.nextDouble() > 0.5) {
					bullet.remove();
					hurt(Combat.bulletDamage(bullet.damage, bullet.crit, this.defense));
				}
		}
		for(int i = 0; i < level.getItems().size(); i++) {
			Item item = level.getItems().get(i);
			if(item.removed) {
				break;
			}
			BoundingBox playerbb = item.getBoundingBox();
			if(mybb.intersects(playerbb)) {
				item.remove();
			}
		}

		if(health < maxhealth && rand.nextDouble() > 0.999)
			health++;
		
		if(flash)
			flashTime--;
		if(flashTime <= 0)
			flash = false;
		
		velocity.x = 0;
		velocity.y = 0;
		
		if(keys.down.isDown && y+radiusy <= level.height)
			velocity.y+=speed;
		if(keys.up.isDown && y-radiusy >= 0)
			velocity.y+=-speed;
		if(keys.left.isDown && x-radiusx >= 0)
			velocity.x+=-speed;
		if(keys.right.isDown && x+radiusx <= level.width)
			velocity.x+=speed;
		
		if(velocity.x != 0 && velocity.y != 0)
			velocity.extend(speed);
		
		xto = x+velocity.x;
		yto = y+velocity.y;


		if(keys.down.isDown || keys.up.isDown || keys.left.isDown || keys.right.isDown)
			walkTime++;
		
		if(canMoveX()){
			moveX();
		}
		if(canMoveY()){
			moveY();
		}
		
		if(inventory.changedweapon) {
			weapon=inventory.getEquippedWeapon();
		}
		
	}

	public void draw(Graphics g) {

        int frame = (walkTime / 6 % 6 + 6) % 6;
        
		BufferedImage renderImage = new BufferedImage(bi[frame][0].getWidth(),bi[frame][0].getHeight(),bi[frame][0].getType());
		Graphics gi = renderImage.createGraphics();
		gi.drawImage(bi[frame][0],0,0,bi[frame][0].getWidth(),bi[frame][0].getHeight(),null);
		if(flash)
			ImageProcessing.recolourImage(renderImage, 50, -255, -255);
		g.drawImage(renderImage, (int)(x-radiusx - 7), (int)(y-radiusy - 20), radiusx*2+14  , radiusy*2+12, null);
	}
	
	public void hurt(int damage) {
		if(damage > 0) {
			health-=damage;
			level.damagetext.add(new DamageText(level, x, y, new Vector(), 20, 8, 1, true, damage, Color.RED));
			flash();
		}
		else {
			level.damagetext.add(new DamageText(level, x, y, new Vector(), 20, 8, 1, true, "miss", Color.GREEN));
		}
	}

	public void addHealth(int value) {
		health+=value;
		if(health > maxhealth)
			health = maxhealth;
	}
	
	public void useAmmo() {
		ammo--;
	}

	public void addMana(int value) {
		ammo+=value;
		if(ammo > maxammo)
			ammo = maxammo;
	}

	public Weapon getWeapon() {
		return weapon;
	}
	
	public void bouncePlayer(Vector metoplayer, int bounceTime) {
		if(metoplayer.x < 0 && x > 0 + radiusx)
			xto=x-bounceTime/2;
		if(metoplayer.x > 0 && x < level.width - radiusx)
			xto=x+bounceTime/2;
		if(metoplayer.y < 0 && y > 0 + radiusy)
			yto=y-bounceTime/2;
		if(metoplayer.y > 0 && y < level.height - radiusy)
			yto=y+bounceTime/2;

		if(canMoveX())
			moveX();
		if(canMoveY())
			moveY();
	}

	public void flash() {
		flash = true;
		flashTime = 5;
	}

	public Inventory getInventory() {
		return inventory;
	}

}
