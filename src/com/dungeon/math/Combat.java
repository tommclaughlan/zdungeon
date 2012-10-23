package com.dungeon.math;

import java.util.Random;

public class Combat {
	
	static Random rand = new Random();
	
	static public int bulletDamage(int attStr, double attCrit, int defDef) {
		
		int damage = 0;
		
		int hit = (int) (Math.ceil(rand.nextDouble() * attStr) - (Math.ceil(rand.nextDouble() * 0.75 * defDef)));
		
		if (hit <= 0) {
			if(defDef>0)
				return 0;
			else
				return 1;
		}
		
		boolean crit = rand.nextDouble() < attCrit;
		
		if (crit) {
			damage = 1 + (int) (rand.nextDouble() * attStr * 2);
		}
		else {
			damage = 1 + (int) (rand.nextDouble() * attStr);
		}
		
		return damage;
		
	}

}
