package com.dungeon.boundingbox;

import com.dungeon.entities.Entity;

public interface BoundingBoxOwner {

	void handleCollision(Entity entity, double xa, double ya);
}
