package com.dungeon.entities.player;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

import com.dungeon.entities.Item;
import com.dungeon.entities.Weapon;

public class Inventory {
	
	private List<Item> items = new ArrayList<Item>();
	private List<Weapon> weapons = new ArrayList<Weapon>();
	
	private int maxitems = 10;
	private int maxweapons = 6;
	
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
	
	public void drawInventory(Graphics g) {
		
	}

}
