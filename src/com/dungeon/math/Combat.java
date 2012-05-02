package com.dungeon.math;

import java.util.Random;

public class Combat {
	
	static Random rand = new Random();
	
	static public int bulletDamage(int attStr, double attCrit, int defDef) {
		
		int damage = 0;
		
		int hit = (int) (Math.ceil(rand.nextDouble() * attStr) - (Math.ceil(rand.nextDouble() * 0.8 * defDef)));
		
		if (hit < 0)
			return 0;
		
		boolean crit = rand.nextDouble() < attCrit;
		
		if (crit) {
			damage = (int) (rand.nextDouble() * attStr * 2);
		}
		else {
			damage = (int) (rand.nextDouble() * attStr);
		}
		
		return damage;
		
	}

}
