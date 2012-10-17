package com.dungeon.level;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

import com.dungeon.Keys;
import com.dungeon.MainComponent;
import com.dungeon.entities.Bullet;
import com.dungeon.entities.DamageText;
import com.dungeon.entities.Entity;
import com.dungeon.entities.Item;
import com.dungeon.entities.MobSpawner;
import com.dungeon.entities.Particle;
import com.dungeon.entities.Player;
import com.dungeon.level.tile.*;
import com.dungeon.map.Map;
import com.dungeon.screen.Screen;

public class Level {
	
	Random rand = new Random();

	public int playerhealth;
	public int playermana;
	public Bullet bullet;
	public boolean gameOver = false;
	public int viewRadius = MainComponent.GAME_WIDTH / 2;

	public List<Entity> entities = new ArrayList<Entity>();
	public List<Particle> stains = new ArrayList<Particle>();
	public List<Particle> particles = new ArrayList<Particle>();
	public List<Bullet> bullets = new ArrayList<Bullet>();
	public List<DamageText> damagetext = new ArrayList<DamageText>();
	public List<Item> items = new ArrayList<Item>();
	public Tile[][] tiles;
	
	public int width, height;
	
	public int score;
	
	private int ticks;
	
	public boolean firing = false;
	private int fireTx, fireTy;
	
	private Map map;

	public Level(Map map) {

		this.map = map;
		
		this.width = map.width*map.tileSize;
		this.height = map.height*map.tileSize;
		
		tiles = new Tile[map.width][map.height];
		
		score = 0;
		
		playerhealth = 0;
		playermana = 0;
		
		
		ticks = 0;
	}

	public void renderMap() {
		for(int x = 0; x < map.width; x++) {
			for(int y = 0; y < map.height; y++) {
				if(map.getSquare(x, y) == 0)
					tiles[x][y] = new FloorTile(this, x, y);
				if(map.getSquare(x, y) == 1)
					tiles[x][y] = new WallTile(this, x, y);
			}
		}
	}
	
	public Map getMap() {
		return map;
	}
	
	public void addMobSpawners() {
		for(int i=0; i < map.mobspawners.size(); i++) {
			entities.add(new MobSpawner(this, map.mobspawners.get(i).x*32, map.mobspawners.get(i).y*32));
		}
	}
	
	/*public void addBadGuys(int numBadGuys) {
		badGuyMax = numBadGuys;
		
		for(int i=0; i < badGuyMax; i++) {
			int newX = rand.nextInt(width);
			int newY = rand.nextInt(height);
			if(map.getSquare(newX/32, newY/32) != 0)
				continue;
			if(getPlayer() != null) {
				if(Math.abs(newX - getPlayer().x) < viewRadius*2 && Math.abs(newY - getPlayer().y) < viewRadius*2)
					continue;
			}
			Mob badGuyPoint = new Mob(this, newX, newY);
			badGuyPoint.xto = newX;
			badGuyPoint.yto = newY;
			if(!badGuyPoint.canMoveX() || !badGuyPoint.canMoveY())
				continue;
			entities.add(badGuyPoint);
		}
	}*/

	public void tick() {
		//long beforeStain = System.nanoTime();
		
//		Iterator<Particle> s = stains.iterator();
//		while(s.hasNext()) {
//			Particle tmp = s.next();
//			if(tmp.removed)
//				s.remove();
//			else
//				tmp.tick();
//		}
//
//		Iterator<Bullet> b = bullets.iterator();
//		while(b.hasNext()) {
//			Bullet tmp = b.next();
//			if(tmp.removed)
//				b.remove();
//			else
//				tmp.tick();
//		}
//		
//		Iterator<Particle> p = particles.iterator();
//		while(p.hasNext()) {
//			Particle tmp = p.next();
//			if(tmp.removed)
//				p.remove();
//			else
//				tmp.tick();
//		}
		
		for(int i = 0; i < stains.size(); i++) {
			Particle p = stains.get(i);
			if(!p.removed) {
				p.tick();
			}
			if(p.removed) {
				stains.remove(i--);
			}
		}
		
		for(int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if(!b.removed)
				b.tick();
			if(b.removed) {
				bullets.remove(i--);
			}
		}
		
		for(int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			if(!p.removed) {
				p.tick();
			}
			if(p.removed) {
				particles.remove(i--);
			}
		}
		
		
		
		if(getPlayer() != null) {
			Player player = ((Player) getPlayer());
			playerhealth = player.health;
			playermana = player.ammo;
			if(firing && ticks % Math.max(1,(10-player.fireRate)) == 0) {
				//newBullet(fireTx, fireTy, player.strength, player.crit);
				player.getWeapon().fire(this, fireTx, fireTy, player.strength, player.crit);
			}
			if(player.health <= 0){
				gameOver();
				return;
			}
		}
		else {
			playerhealth = 0;
			playermana = 0;
		}
		

//		Iterator<Entity> e = entities.iterator();
//		while(e.hasNext()) {
//			Entity tmp = e.next();
//			if(tmp.removed)
//				e.remove();
//			else
//				tmp.tick();
//		}
//		
//		Iterator<DamageText> dt = damagetext.iterator();
//		while(dt.hasNext()) {
//			DamageText tmp = dt.next();
//			if(tmp.removed)
//				dt.remove();
//			else
//				tmp.tick();
//		}		
//		
//		Iterator<Item> i = items.iterator();
//		while(i.hasNext()) {
//			Item tmp = i.next();
//			if(tmp.removed)
//				i.remove();
//			else
//				tmp.tick();
//		}
		
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if(!e.removed) {
				e.tick();
			}
			if(e.removed) {
				entities.remove(i--);
			}
		}

		
		for(int i = 0; i < damagetext.size(); i++) {
			DamageText b = damagetext.get(i);
			if(!b.removed)
				b.tick();
			if(b.removed) {
				damagetext.remove(i--);
			}
		}

		
		for(int i = 0; i < items.size(); i++) {
			Item it = items.get(i);
			if(!it.removed)
				it.tick();
			if(it.removed) {
				items.remove(i--);
			}
		}
		//long afterBullet = System.nanoTime();

		//System.out.println("TotalTime = "+(afterBullet-beforeStain));
		
		
		ticks++;
	}
	
	public void addPlayer(int x, int y, Keys keys) {
		entities.add(new Player(this, keys, x, y));
	}

	public Set<Entity> getEntities() {
		return getEntities(0,0,this.width,this.height);
	}
	
	public List<Tile> getTiles(int x0, int y0, int x1, int y1) {
		// assume x and y are in map coordinates
		if(x0 < 0)
			x0 = 0;
		if(y0 < 0)
			y0 = 0;
		if(x1 > map.width)
			x1 = map.width;
		if(y1 > map.height)
			y1 = map.height;
		
		List<Tile> result = new ArrayList<Tile>();

		for(int x = x0; x < x1; x++) {
			for(int y = y0; y < y1; y++) {
				Tile e = tiles[x][y];
				result.add(e);
			}
		}
		
		return result;
	}
	
	public List<Entity> getStains(int x0, int y0, int x1, int y1) {
		
		List<Entity> result = new ArrayList<Entity>();

		for(int i = 0; i < stains.size(); i++) {
			Entity e = stains.get(i);
			if(e.removed) continue;
			if(e.getBoundingBox().intersects(x0, y0, x1, y1)){
				result.add(e);
			}
		}
		
		return result;
	}
	
	public List<Entity> getParticles(int x0, int y0, int x1, int y1) {
		
		List<Entity> result = new ArrayList<Entity>();

		for(int i = 0; i < particles.size(); i++) {
			Entity e = particles.get(i);
			if(e.removed) continue;
			if(e.getBoundingBox().intersects(x0, y0, x1, y1)){
				result.add(e);
			}
		}
		
		return result;
	}
	
	public List<Entity> getDamageText(int x0, int y0, int x1, int y1) {
		
		List<Entity> result = new ArrayList<Entity>();

		for(int i = 0; i < damagetext.size(); i++) {
			Entity e = damagetext.get(i);
			if(e.removed) continue;
			if(e.getBoundingBox().intersects(x0, y0, x1, y1)){
				result.add(e);
			}
		}
		
		return result;
	}
	
	public Set<Entity> getEntities(int x0, int y0, int x1, int y1) {
		
		Set<Entity> result = new TreeSet<Entity>(new EntityComparator());

		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if(e.removed) continue;
			if(e.getBoundingBox().intersects(x0, y0, x1, y1)){
				result.add(e);
			}
		}
		for(int i = 0; i < bullets.size(); i++) {
			Entity e = bullets.get(i);
			if(e.removed) continue;
			if(e.getBoundingBox().intersects(x0, y0, x1, y1)){
				result.add(e);
			}
		}
		for(int i = 0; i < items.size(); i++) {
			Entity e = items.get(i);
			if(e.removed) continue;
			if(e.getBoundingBox().intersects(x0, y0, x1, y1)){
				result.add(e);
			}
		}
		
		return result;
	}

	public List<Bullet> getBullets() {
		return getBullets(0,0,width,height);
	}
	
	public List<Bullet> getBullets(int x0, int y0, int x1, int y1) {
		List<Bullet> result = new ArrayList<Bullet>();

		for(int i = 0; i < bullets.size(); i++) {
			Bullet e = bullets.get(i);
			if(e.removed) continue;
			if(e.getBoundingBox().intersects(x0, y0, x1, y1)){
				result.add(e);
			}
		}

		return result;
	}

	public List<Item> getItems() {
		return getItems(0,0,width,height);
	}
	
	public List<Item> getItems(int x0, int y0, int x1, int y1) {
		List<Item> result = new ArrayList<Item>();

		for(int i = 0; i < items.size(); i++) {
			Item e = items.get(i);
			if(e.removed) continue;
			if(e.getBoundingBox().intersects(x0, y0, x1, y1)){
				result.add(e);
			}
		}

		return result;
	}

	public List<Particle> getStains() {
		return stains;
	}
	
	public List<Particle> getParticles() {
		return particles;
	}

	public Player getPlayer() {
		Player player = null;
		for(Entity e : entities) {
			if(e instanceof Player){
				player = (Player) e;
				break;
			}
		}
		return player;
		
	}

	public void newBullet(int x, int y, int strength, double crit) {
		Player player = (Player) getPlayer();
		if(player != null && player.ammo > 0) {
			double px = player.x;
			double py = player.y - 4;
			double tx = (x) - (px);
			double ty = (y) - (py);
			
			if(viewRadius < px)
				tx += 2*(px - viewRadius);
			if((viewRadius*9/16) < py)
				ty += 2*(py - (viewRadius*9/16));
			if( width  - viewRadius < px)
			    tx -= 2*(px - (width - viewRadius));
			if( height - (viewRadius*9/16) < py)
				ty -= 2*(py - (height - (viewRadius*9/16)));

			bullets.add(new Bullet(this, px, py, tx, ty, player.velocity, false, strength, crit, 8));
			player.useAmmo();
		}
	}
	
	public void enemyBullet(Entity enemy, int tx, int ty, int strength, double crit) {
		Player player = (Player) getPlayer();
		if(player != null) {
			double px = tx + rand.nextGaussian()*3;
			double py = ty + rand.nextGaussian()*3;
			bullets.add(new Bullet(this, enemy.x, enemy.y, px, py, enemy.velocity, true, strength, crit, 3));
		}
	}
	
	public void startBullets(int tx, int ty) {
		firing = true;
		fireTx = tx;
		fireTy = ty;
	}


	public void targetBullets(int tx, int ty) {
		fireTx = tx;
		fireTy = ty;
		
	}
	
	public void stopBullets() {
		firing = false;
	}
	
	public void render(Screen screen, int xScroll, int yScroll) {
		int tX = xScroll - viewRadius;
		int tY = yScroll - (viewRadius*9/16);

		if(tX < 0)
			tX = 0;
		if(tY < 0)
			tY = 0;
		if(tX + 2*viewRadius > width)
			tX = width - 2*viewRadius;
		if(tY + (viewRadius*9/8) > height)
			tY = height - (viewRadius*9/8);
		
		
		screen.getGraphics().translate(-tX,-tY);
		//screen.getGraphics().translate(xScroll - viewRadius, yScroll - (viewRadius*3/4));
		List<Tile> visibleTiles = getTiles((xScroll - 2*viewRadius)/32, (yScroll - (2*viewRadius*9/16))/32, (xScroll + 2*viewRadius)/32, (yScroll + (2*viewRadius*9/16))/32);
		Set<Entity> visibleEntities = getEntities(xScroll - 2*viewRadius, yScroll - (2*viewRadius*9/16), xScroll + 2*viewRadius, yScroll + (2*viewRadius*9/16));
		List<Entity> visibleStains = getStains(xScroll - 2*viewRadius, yScroll - (2*viewRadius*9/16), xScroll + 2*viewRadius, yScroll + (2*viewRadius*9/16));
		List<Entity> visibleParticles = getParticles(xScroll - 2*viewRadius, yScroll - (2*viewRadius*9/16), xScroll + 2*viewRadius, yScroll + (2*viewRadius*9/16));
		List<Entity> visibleDamage = getDamageText(xScroll - 2*viewRadius, yScroll - (2*viewRadius*9/16), xScroll + 2*viewRadius, yScroll + (2*viewRadius*9/16));

		sortAndRender(screen, tX, tY, visibleTiles, visibleStains, visibleParticles, visibleEntities, visibleDamage);

	}

	private void sortAndRender(Screen screen, int tX, int tY,
			List<Tile> visibleTiles, List<Entity> visibleStains,
			List<Entity> visibleParticles, Set<Entity> visibleEntities, List<Entity> visibleDamage) {
		screen.getGraphics().setColor(Color.black);
        screen.getGraphics().fillRect(0,0, tX+2*viewRadius, tY+2*(viewRadius*9/16));
        screen.getGraphics().clipRect(0,0, tX+2*viewRadius, tY+2*(viewRadius*9/16));
		for(int i = 0; i < visibleTiles.size(); i++) {
			if(visibleTiles.get(i) instanceof FloorTile)
				screen.draw(visibleTiles.get(i));
		}
		for(int i = 0; i < visibleStains.size(); i++) {
			screen.draw(visibleStains.get(i));
		}
		
		//quicker way to do this?
		for(int y = 0; y < map.height; y++) {
			for(Entity e : visibleEntities) {
				if(e.y/map.tileSize <= y && e.y/map.tileSize > y - 1)
					screen.draw(e);
			}
			for(int i = 0; i < visibleTiles.size(); i++) {
				if(visibleTiles.get(i) instanceof WallTile) {
					if(visibleTiles.get(i).y/map.tileSize <= y && visibleTiles.get(i).y/map.tileSize > y-1)
						screen.draw(visibleTiles.get(i));
				}
			}
		
		}

		for(int i = 0; i < visibleParticles.size(); i++) {
			screen.draw(visibleParticles.get(i));
		}
		
		for(int i = 0; i < visibleDamage.size(); i++) {
			screen.draw(visibleDamage.get(i));
		}
		
		screen.getGraphics().translate(tX,tY);
		
	}

	public void gameOver() {
		
		gameOver = true;
	}
}
