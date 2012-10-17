package com.dungeon.entities.player;

import java.util.ArrayList;
import java.util.List;

import com.dungeon.entities.Item;
import com.dungeon.entities.Weapon;

public class Inventory {
	
	private List<Item> items = new ArrayList<Item>();
	private List<Weapon> weapons = new ArrayList<Weapon>();
	
	public void addItem(Item it) {
		items.add(it);
	}	
	
	public void addWeapon(Weapon weap) {
		weapons.add(weap);
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public List<Weapon> getWeapons() {
		return weapons;
	}

}
