package com.dungeon.entities.player;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.dungeon.Keys;
import com.dungeon.entities.Item;
import com.dungeon.entities.Weapon;
import com.dungeon.level.Level;

public class Inventory {
	
	private List<Item> items = new ArrayList<Item>();
	private List<Weapon> weapons = new ArrayList<Weapon>();
	
	private int maxitems = 10;
	private int maxweapons = 6;
	private int selectedweapon = 1;
	private int equippedweapon = 0;
	public boolean changedweapon = false;
	
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
	}

}
