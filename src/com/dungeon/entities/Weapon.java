package com.dungeon.entities;

import java.awt.image.BufferedImage;
import java.util.Random;

import com.dungeon.image.Art;
import com.dungeon.level.Level;

public interface Weapon {
	
	int speed = 0;
	double crt = 0;
	int str = 0;
	double acc = 0;
	int lvl = 0;
	
	BufferedImage img = Art.black;
	
	Random rand = new Random();
	
	void fire(Level level, int x, int y, int strength, double crit);

	String getName();

	int getStrength();
	
	double getCrit();
	
	double getAccuracy();

	int getSpeed();
	
	int getShots();
	
	int getiLvl();
	
	BufferedImage getImage();

}
