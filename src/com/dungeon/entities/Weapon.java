package com.dungeon.entities;

import java.util.Random;

import com.dungeon.level.Level;

public interface Weapon {
	
	int speed = 0;
	double crit = 0;
	int strength = 0;
	double acc = 0;
	
	Random rand = new Random();
	
	void fire(Level level, int x, int y, int strength, double crit);

}
