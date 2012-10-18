package com.dungeon.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.dungeon.boundingbox.BoundingBox;
import com.dungeon.entities.items.ManaPotion;
import com.dungeon.image.Art;
import com.dungeon.image.ImageProcessing;
import com.dungeon.level.Level;
import com.dungeon.math.Combat;
import com.dungeon.math.Node;
import com.dungeon.math.Vector;

public class Mob extends Entity {

	Random rand = new Random();
	
	BufferedImage bi = Art.zombie1;
    public int facing;
    public int walkTime;
    public int stepTime;
    public boolean isDead = false;
	boolean bounce = false;
	boolean playerbounce = false;
	public int health = 20;
	private boolean flash = false;
	private int flashTime = 0;
	
	//stats!
	public int strength = 3;
	public int defense = 3;
	public double crit = 0.01;

	private int bounceTime = 0;
	private boolean shootyMob = false;
	private boolean firing = false;
	
	private int vomitCount = 0;
	private boolean fixDirection = false;
	int tx = 0,ty = 0;
	
	//for pathfinding
	public int sightRadius = 512;
	int speed = 1;
	int currentx, currenty;
	int targetx, targety;
	boolean canSeePlayer = false;
	private boolean shotat = false;
	private int attentionSpan = 1000;
	boolean hasPath = false;
	List<Node> path = new ArrayList<Node>();
	
	
	public Mob(Level level, double x, double y){
		super(level);
		this.x = x;
		this.y = y;
		currentx = (int) (this.x / 32);
		currenty = (int) (this.y / 32);
		this.radiusx = 10;
		this.radiusy = 10;
    	colour = Color.RED;
		velocity = new Vector();
		facing = rand.nextInt(4);
		if(rand.nextDouble() > 0.85)
			shootyMob = true;
	}
	
	public void die() {
		remove();
		level.score++;
		spray(new Vector(), 250, 200, 3);
		if(rand.nextDouble() > 0.85)
			level.items.add(new ManaPotion(level, x, y, 15));
	}
	
	public void tick() {	
		
		
		if(health <= 0)
			die();
		
		if(flash)
			flashTime--;
		if(flashTime <= 0) {
			flash = false;
			colour = Color.RED;
		}
		

		BoundingBox mybb = getBoundingBox();
		List<Bullet> bullets = level.getBullets((int) (this.x - (radiusx+1)), (int) (this.y  - (radiusy+1)), (int) (this.x + (radiusx+1)), (int) (this.y + (radiusy+1)));
		for(int i = 0; i < bullets.size(); i++) {
			Bullet bullet = bullets.get(i);
				if(bullet.hostile)
					continue;
				if(bullet.removed) {
					break;
				}
				BoundingBox playerbb = bullet.getBoundingBox();
				if(mybb.intersects(playerbb)) {
					bullet.remove();
					hurt(Combat.bulletDamage(bullet.damage, bullet.crit, this.defense), bullet);
					
				}
		}
		
		Player player = (Player) level.getPlayer();
		
		if(player != null) {
			Vector metoplayer = new Vector(x,y,player.x,player.y);
			if(metoplayer.length() > sightRadius*4)
				return;
			if(metoplayer.length() < sightRadius && mybb.intersects(player.getBoundingBox())) {
				//TODO: collision hurt
				player.hurt( (int) Math.max(1, strength*(1+rand.nextGaussian())) );
				bounce = true;
				playerbounce = true;
				//die();
				bounceTime = 10;
			}
			if(bounce) {
				bounce(metoplayer);
				if(playerbounce)
					player.bouncePlayer(metoplayer, bounceTime);
				bounceTime--;
				if(bounceTime <= 0) {
					bounce = false;
					playerbounce = false;
				}
			}
			if(metoplayer.length() < sightRadius) {
				canSeePlayer = true;
			}
			else{
				canSeePlayer = false;
			}
			if(!firing && metoplayer.length() < sightRadius / 3 )
				firing = true;
			
			if(shootyMob && firing && vomitCount > 0) {
				if(!fixDirection) {
					tx = (int) player.x;
					ty = (int) player.y;
					fixDirection = true;
				}
				//System.out.println("metoplayer = "+metoplayer.printVec());
				level.enemyBullet(this, tx, ty, strength, crit);
				vomitCount--;
			}
			
			if(shootyMob && vomitCount <= 0) {

				firing = false;
				if(rand.nextDouble() > 0.995) {
					vomitCount = 30;
					fixDirection = false;
				}
			}
			
			
		}
        velocity.x = 0;
        velocity.y = 0;
		
		xto = 0;
		yto = 0;
        
        walkTime++;

        
//        if(canSeePlayer)
//        	findPath();
//        else {
//        	if (facing == 0) velocity.y += speed;
//            if (facing == 1) velocity.x -= speed;
//            if (facing == 2) velocity.y -= speed;
//            if (facing == 3) velocity.x += speed;
//        }
        
		if(shootyMob && firing) {
			xto = 0;
			yto = 0;
		}
		
        if(walkTime / 12 % 3 != 0) {
        	stepTime++;
        	if(walkTime > 4 && rand.nextInt(200) == 0){
        		facing = rand.nextInt(4);
        		walkTime = 0;
        	}
        	Vector metoplayer = new Vector(x,y,player.x,player.y);
    		if(canSeePlayer && (metoplayer.length() < sightRadius / 2 || shotat)){
            	findPath();
            	if(shotat) {
            		attentionSpan--;
            		if(attentionSpan <= 0)
            			shotat = false;
            	}
    		}
            else {
            	if (facing == 0) velocity.y += speed;
                if (facing == 1) velocity.x -= speed;
                if (facing == 2) velocity.y -= speed;
                if (facing == 3) velocity.x += speed;
            }
    		
            if(velocity.x != 0 && velocity.y != 0)
            	velocity.extend(speed);
            
            xto+=x+velocity.x;
            yto+=y+velocity.y;
    		
            if(canMoveX())
            	moveX();
            if(canMoveY())
            	moveY();

        }
	}

	private void spray(Vector velocity, int num, int life, int rad) {
		for(int i=0; i < num; i++) {
			level.stains.add(new Particle(level, x, y, velocity, life, rad, 1, false));
		}
	}

	public void hurt(int damage, Bullet bullet) {
		if(damage > 0) {
			health-=damage;
			level.damagetext.add(new DamageText(level, x, y, new Vector(rand.nextGaussian(), -2), 20, 8, 1, true, damage, Color.YELLOW));
			flash();
			spray(bullet.vec, 20, 15, 2);
			if(!bounce)
				bounceTime = 5;
			bounce = true;
		}
		else {
			level.damagetext.add(new DamageText(level, x, y, new Vector(rand.nextGaussian(), -2), 20, 8, 1, true, "miss", Color.RED));
		}
		shotat = true;
		attentionSpan = 1000;
	}
	
	private void bounce(Vector metoplayer) {
		metoplayer.extend(1);
		if(metoplayer.x > 0 && x > 0 + radiusx)
			xto=x-bounceTime*0.8;
		if(metoplayer.x < 0 && x < level.width - radiusx)
			xto=x+bounceTime*0.8;
		if(metoplayer.y > 0 && y > 0 + radiusy)
			yto=y-bounceTime*0.8;
		if(metoplayer.y < 0 && y < level.height - radiusy)
			yto=y+bounceTime*0.8;

		if(canMoveX())
			moveX();
		if(canMoveY())
			moveY();
	}

	public void draw(Graphics g) {
		BufferedImage renderImage = new BufferedImage(bi.getWidth(),bi.getHeight(),bi.getType());
		Graphics gi = renderImage.createGraphics();
		gi.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
		if(flash)
			ImageProcessing.recolourImage(renderImage, 50, -255, -255);
		g.drawImage(renderImage, (int)(x-radiusx - 3), (int)(y-radiusy - 3), radiusx*2 + 6 , radiusy*2 + 6, null);
	}

	public void flash() {
		flash = true;
		flashTime = 5;
	}
	
	private void findPath() {
		
		if(!hasPath) {
			currentx = getMapX();
			currenty = getMapY();
			targetx = (int) (((level.getPlayer().x) / 32));
			targety = (int) (((level.getPlayer().y) / 32));
			path = AStar(new Node(currentx,currenty, null), new Node(targetx,targety,null));
		}
		if(hasPath && walkTime % 500 == 0)
			hasPath = false;

		if(path != null) {
			if(path.size()!=0) {
				Node nextNode = path.get(path.size() - 1);
				int nextx = nextNode.x*32 + 16;
				int nexty = nextNode.y*32 + 16;
				int myx = (int) x;
				int myy = (int) y;
				
				if(nextx > myx)
					velocity.x+=speed;
				if(nextx < myx)
					velocity.x-=speed;
				if(nexty > myy)
					velocity.y+=speed;
				if(nexty < myy)
					velocity.y-=speed;
				

				if(nextx == myx && nexty == myy) {
					path.remove(nextNode);
				}
			}
			if(path.size() == 0 || (canSeePlayer && walkTime % 7 == 0))
				hasPath = false;
		}
		if(canSeePlayer && path == null)
			canSeePlayer = false;
		
	}
	
	private boolean[][] getWalkableMap() {
		return level.getMap().getWalkableMap();
	}
	
	private List<Node> AStar(Node start, Node goal) {
		
		boolean[][] map = getWalkableMap();
		List<Node> nodes = level.getMap().nodes;
		List<Node> openSet = new ArrayList<Node>();
		List<Node> closedSet = new ArrayList<Node>();

		start.fillNeighbours(nodes);
		
		int pathFindTime = 0;

		openSet.add(start);
		
		while(!hasPath && pathFindTime < 50000){
			Node current = null;
			
			if(openSet.size() == 0) {
				hasPath = false;
				break;
			}
			
			for(Node n : openSet) {
				if(current == null || n.getFScore() < current.getFScore()) {
					current = n;
				}
			}
			
			if(current.x == goal.x && current.y == goal.y) {
				hasPath = true;
				goal.parent = current.parent;
				break;
			}
			
			openSet.remove(current);
			closedSet.add(current);
			
			for(Node n : current.neighbours) {
				if(n.x != current.x && n.y != currenty) {
					if(!map[n.x][current.y] && !map[current.x][n.y])
						continue;
				}
				
				int nextG = current.g + n.cost;
				if(nextG < n.g) {
					openSet.remove(n);
					closedSet.remove(n);
				}
				
				if(!openSet.contains(n) && !closedSet.contains(n)) {
					n.g = nextG;
					n.h = n.distance(goal);
					n.parent = current;
					openSet.add(n);
				}
				
			}
			
			pathFindTime++;
			
		}
		if(hasPath) {
			List<Node> path = new ArrayList<Node>();
			Node current = goal;
			while(current.parent != null) {
				path.add(current);
				current = current.parent;
			}
			return path;
		}
		return null;
	}

	public int getMapX() {
		return (int) ((this.x) / 32);
	}

	public int getMapY() {
		return (int) ((this.y) / 32);
	}
	
}
