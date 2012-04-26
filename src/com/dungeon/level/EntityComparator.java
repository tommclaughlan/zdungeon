package com.dungeon.level;

import java.util.Comparator;

import com.dungeon.entities.Entity;

public class EntityComparator implements Comparator<Entity> {

	@Override
	public int compare(Entity o0, Entity o1) {
		if(o0.y < o1.y) return -1;
		if(o0.x < o1.x) return -1;
		if(o0.y > o1.y) return 1;
		if(o0.x > o1.x) return 1;
		return 0;
	}

}
