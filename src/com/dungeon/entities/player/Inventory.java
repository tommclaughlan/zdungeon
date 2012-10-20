package com.dungeon.entities.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.dungeon.Keys;
import com.dungeon.entities.Item;
import com.dungeon.entities.Weapon;
import com.dungeon.entities.items.WeaponItem;
import com.dungeon.level.Level;

public class Inventory {
	
	private List<Item> items = new ArrayList<Item>();
	private List<Weapon> weapons = new ArrayList<Weapon>();
	
	private int maxitems = 10;
	private int maxweapons = 6;
	private int selectedweapon = 0;
	private int equippedweapon = 0;
	public boolean changedweapon = true;
	
	private Level level;
	private Keys keys;
	
	public Inventory(Level level, Keys keys) {
		this.level = level;
		this.keys = keys;
	}
	
	public void addItem(Item it) {
		if(items.size() < maxitems)
			items.add(it);
	}	
	
	public void addWeapon(Weapon weap) {
		if(weapons.size() < maxweapons)
			weapons.add(weap);
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public List<Weapon> getWeapons() {
		return weapons;
	}
	
	public int getSelectedWeapon() {
		return selectedweapon;
	}
	
	public Weapon getEquippedWeapon() {
		return weapons.get(equippedweapon);
	}
	
	public void dropWeapon(int i) {
		if(weapons.size() == 1)
			return;
		Weapon dw = weapons.remove(i);
		level.items.add(new WeaponItem(level, level.getPlayer().x, level.getPlayer().y, dw));
		if(equippedweapon >= weapons.size())
			equippedweapon--;
	}
	
	public void tick() {
		if(keys.down.wasReleased()) {
			if(selectedweapon < weapons.size()-1)
				selectedweapon++;
			else
				selectedweapon = 0;
		}
		if(keys.up.wasReleased()){
			if(selectedweapon > 0)
				selectedweapon--;
			else
				selectedweapon = weapons.size() - 1;
		}
		if(keys.select.wasReleased()){
			equippedweapon = selectedweapon;
			changedweapon = true;
		}
		if(keys.drop.wasReleased()){
			dropWeapon(selectedweapon);
			selectedweapon--;
			if(selectedweapon<0)
				selectedweapon=0;
		}
	}

	public void drawInventory(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRoundRect(100, 100, 600, 350, 50, 50);
		
		Font font = new Font("", Font.PLAIN, 20);
		Font selectedfont = new Font("", Font.BOLD, 24);
		Font oldfont = g.getFont();
		
		for(int i=0; i<weapons.size(); ++i) {
			if(i == selectedweapon) {
				g.setFont(font);
				drawWeaponStats(g, weapons.get(i),350, 150, true);
				g.setFont(selectedfont);
			}
			else
				g.setFont(font);
			drawWeaponName(g, weapons.get(i),150, 150+(i*40));
		}
		g.setFont(oldfont);
	}
	
	public void drawWeaponName(Graphics g, Weapon w, int x, int y) {
		String weap = w.getName();
		g.setColor(Color.BLACK);
		g.drawString(weap, x, y);
		g.setColor(Color.MAGENTA);
		g.drawString(weap, x, y-1);
	}

	public void drawWeaponStats(Graphics g, Weapon w, int x, int y, boolean compare) {
		Weapon pw = weapons.get(equippedweapon);
		int str = w.getStrength();
    	String stat = "Strength = "+str;
		g.setColor(Color.BLACK);
		g.drawString(stat, x, y);
		g.setColor(Color.YELLOW);
		if(compare) {
			if(str > pw.getStrength())
				g.setColor(Color.GREEN);
			else if(str < pw.getStrength())
				g.setColor(Color.RED);
		}
		g.drawString(stat, x, y-1);
		y+=20;
    	double crt = w.getCrit();
    	stat = "Crit = "+String.format("%.2f", 100*crt)+"%";
		g.setColor(Color.BLACK);
		g.drawString(stat, x, y);
		g.setColor(Color.YELLOW);
		if(compare) {
			if(crt > pw.getCrit())
				g.setColor(Color.GREEN);
			else if(crt < pw.getCrit())
				g.setColor(Color.RED);
		}
		g.drawString(stat, x, y-1);
		y+=20;
    	double acc = w.getAccuracy();
    	stat = "Accuracy = "+String.format("%.2f", acc);
		g.setColor(Color.BLACK);
		g.drawString(stat, x, y);
		g.setColor(Color.YELLOW);
		if(compare) {
			if(acc < pw.getAccuracy())
				g.setColor(Color.GREEN);
			else if(acc > pw.getAccuracy())
				g.setColor(Color.RED);
		}
		g.drawString(stat, x, y-1);
		y+=20;
    	double speed = w.getSpeed();
    	stat = "Bullet Speed = "+speed;
		g.setColor(Color.BLACK);
		g.drawString(stat, x, y);
		g.setColor(Color.YELLOW);
		if(compare) {
			if(speed > pw.getSpeed())
				g.setColor(Color.GREEN);
			else if(speed < pw.getSpeed())
				g.setColor(Color.RED);
		}
		g.drawString(stat, x, y-1);
		y+=20;
    	double shots = w.getShots();
    	stat = "Bullets per shot = "+shots;
		g.setColor(Color.BLACK);
		g.drawString(stat, x, y);
		g.setColor(Color.YELLOW);
		g.drawString(stat, x, y-1);
	}
	
}
