package com.dungeon.entities.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.dungeon.Keys;
import com.dungeon.entities.Item;
import com.dungeon.entities.Player;
import com.dungeon.entities.Weapon;
import com.dungeon.entities.items.*;
import com.dungeon.level.Level;

public class Inventory {
	
	private List<ConsumableItem> items = new ArrayList<ConsumableItem>();
	private List<Weapon> weapons = new ArrayList<Weapon>();
	
	private int maxitems = 6;
	private int maxweapons = 6;
	private int selectedweapon = 0;
	private int equippedweapon = 0;
	public boolean changedweapon = true;
	
	private int selecteditem = 0;
	
	private enum Tabs {WEAPONS, ITEMS}
	private Tabs invtab = Tabs.WEAPONS;
	
	private Level level;
	private Keys keys;
	
	public Inventory(Level level, Keys keys) {
		this.level = level;
		this.keys = keys;
	}
	
	public void addItem(ConsumableItem it) {
		if(items.size() < maxitems)
			items.add(it);
		else
			level.items.add(new HealthPotion(level, level.getPlayer().x, level.getPlayer().y, ((HealthPotion) it).value));
	}	
	
	public void addWeapon(Weapon weap) {
		if(weapons.size() < maxweapons)
			weapons.add(weap);
		else
			level.items.add(new WeaponItem(level, level.getPlayer().x, level.getPlayer().y, weap, 0));
	}
	
	public List<ConsumableItem> getItems() {
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
		level.items.add(new WeaponItem(level, level.getPlayer().x, level.getPlayer().y, dw, 0));
		if(equippedweapon >= weapons.size() || i < equippedweapon)
			equippedweapon--;
	}
	
	public void dropItem(int i) {
		ConsumableItem dw = items.remove(i);
		if(dw instanceof HealthPotion)
			level.items.add(new HealthPotion(level, level.getPlayer().x, level.getPlayer().y, ((HealthPotion) dw).value));
	}
	
	public void useItem(int i) {
		ConsumableItem dw = items.remove(i);
		dw.use();
	}
	
	public void tick() {
		
		if(invtab == Tabs.WEAPONS) {
			if(keys.right.wasReleased())
				invtab=Tabs.ITEMS;
			if(keys.left.wasReleased())
				invtab=Tabs.ITEMS;
			
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
		else if(invtab == Tabs.ITEMS){
			if(keys.right.wasReleased())
				invtab=Tabs.WEAPONS;
			if(keys.left.wasReleased())
				invtab=Tabs.WEAPONS;
			
			if(keys.down.wasReleased()) {
				if(selecteditem < items.size()-1)
					selecteditem++;
				else
					selecteditem = 0;
			}
			if(keys.up.wasReleased()){
				if(selecteditem > 0)
					selecteditem--;
				else
					selecteditem = weapons.size() - 1;
			}
			if(keys.select.wasReleased()){
				if(selecteditem >= items.size() || selecteditem < 0)
					return;
				useItem(selecteditem);
			}
			if(keys.drop.wasReleased()){
				dropItem(selecteditem);
				selecteditem--;
				if(selecteditem<0)
					selecteditem=0;
			}
		}
	}

	public void drawInventory(Graphics g) {
		g.setColor(Color.DARK_GRAY);
		g.fillRoundRect(100, 100, 600, 350, 50, 50);
		
		Font font = new Font("", Font.PLAIN, 20);
		Font selectedfont = new Font("", Font.BOLD, 24);
		Font oldfont = g.getFont();
		
		if(invtab == Tabs.WEAPONS){
			g.setFont(selectedfont);
			g.setColor(Color.WHITE);
			g.drawString("Weapons", 150, 150);
			for(int i=0; i<weapons.size(); ++i) {
				if(i == selectedweapon) {
					g.setFont(selectedfont);
				}
				else {
					g.setFont(font);
				}
				drawWeaponName(g, weapons.get(i),150, 200+(i*40));

			}

			g.setFont(font);
			if(selectedweapon == equippedweapon)
				drawWeaponStats(g, weapons.get(selectedweapon),350, 200, false);
			else
				drawWeaponStats(g, weapons.get(selectedweapon),350, 200, true);
		}
		else if(invtab == Tabs.ITEMS){
			g.setFont(selectedfont);
			g.setColor(Color.WHITE);
			g.drawString("Items", 150, 150);
			for(int i=0; i<items.size(); ++i) {
				if(i == selecteditem) {
					g.setFont(font);
					drawItemStats(g, items.get(i),350, 200);
					g.setFont(selectedfont);
				}
				else
					g.setFont(font);
				drawItemName(g, items.get(i),150, 200+(i*40));
			}
		}
		g.setFont(oldfont);
	}
	
	public void drawPlayerStats(Graphics g) {
		
		g.setColor(Color.DARK_GRAY);
		g.fillRoundRect(725, 200, 250, 225, 50, 50);
		
		Player p = level.getPlayer();
		Weapon w = weapons.get(equippedweapon);
		Font font = new Font("", Font.PLAIN, 20);
		g.setFont(font);
		g.setColor(Color.YELLOW);
		g.drawString("Player Level "+p.lvl, 750, 250);
		g.drawString("Strength "+(p.strength+w.getStrength()), 750, 275);
		g.drawString("Defense "+p.defense, 750, 300);
		g.drawString("Crit chance "+String.format("%.2f",(100*Math.min(p.crit+w.getCrit(),1.0)))+"%", 750, 325);
		g.drawString("Max Health "+p.maxhealth, 750, 350);
		g.drawString("Max Ammo "+p.maxammo, 750, 375);
		g.drawString("Exp "+p.exp+"/"+p.expperlvl, 750, 400);
	}
	
	public void drawItemName(Graphics g, ConsumableItem i, int x, int y) {
		String name = i.getName();
		g.setColor(Color.BLACK);
		g.drawString(name, x, y);
		g.setColor(Color.MAGENTA);
		g.drawString(name, x, y-1);
	}	
	
	public void drawItemStats(Graphics g, ConsumableItem i, int x, int y) {
		String name = i.getValue();
		g.setColor(Color.BLACK);
		g.drawString(name, x, y);
		g.setColor(Color.YELLOW);
		g.drawString(name, x, y-1);
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
		g.drawString(stat, x, y-1);
		if(compare) {
			if(str > pw.getStrength())
				g.setColor(Color.GREEN);
			else if(str < pw.getStrength())
				g.setColor(Color.RED);
			g.drawString(String.valueOf(str - pw.getStrength()), x + 250, y);
		}
		y+=20;
    		double crt = w.getCrit();
    		stat = "Crit = "+String.format("%.2f", 100*crt)+"%";
		g.setColor(Color.BLACK);
		g.drawString(stat, x, y);
		g.setColor(Color.YELLOW);
		g.drawString(stat, x, y-1);
		if(compare) {
			if(crt > pw.getCrit())
				g.setColor(Color.GREEN);
			else if(crt < pw.getCrit())
				g.setColor(Color.RED);
			g.drawString(String.format("%.2f",100* (crt - pw.getCrit()))+"%", x + 250, y);
		}
		y+=20;
	    	double acc = w.getAccuracy();
	    	stat = "Accuracy = "+String.format("%.2f", acc);
		g.setColor(Color.BLACK);
		g.drawString(stat, x, y);
		g.setColor(Color.YELLOW);
		g.drawString(stat, x, y-1);
		if(compare) {
			if(acc < pw.getAccuracy())
				g.setColor(Color.GREEN);
			else if(acc > pw.getAccuracy())
				g.setColor(Color.RED);
			g.drawString(String.format("%.2f",pw.getAccuracy()-acc), x + 250, y);
		}
		y+=20;
	    	double speed = w.getSpeed();
	    	stat = "Bullet Speed = "+speed;
		g.setColor(Color.BLACK);
		g.drawString(stat, x, y);
		g.setColor(Color.YELLOW);
		g.drawString(stat, x, y-1);
		if(compare) {
			if(speed > pw.getSpeed())
				g.setColor(Color.GREEN);
			else if(speed < pw.getSpeed())
				g.setColor(Color.RED);
			g.drawString(String.valueOf(speed - pw.getSpeed()), x + 250, y);
		}
		y+=20;
	    	double shots = w.getShots();
	    	stat = "Bullets per shot = "+shots;
		g.setColor(Color.BLACK);
		g.drawString(stat, x, y);
		g.setColor(Color.YELLOW);
		g.drawString(stat, x, y-1);
	}

	public void newLevel(Level level2) {
		level = level2;
	}
	
}
