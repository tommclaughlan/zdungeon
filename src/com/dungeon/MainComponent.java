package com.dungeon;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.dungeon.entities.Entity;
import com.dungeon.entities.Mob;
import com.dungeon.entities.MobSpawner;
import com.dungeon.entities.Player;
import com.dungeon.entities.player.Inventory;
import com.dungeon.image.Art;
import com.dungeon.level.Level;
import com.dungeon.map.Map;
import com.dungeon.screen.Screen;
import com.dungeon.InputHandler;
import com.dungeon.Keys;

public class MainComponent extends Canvas implements Runnable, MouseMotionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	public static int GAME_WIDTH = 512;
	public static int GAME_HEIGHT = (9 * GAME_WIDTH) / 16;
	public static int SCALE = 2;
	private boolean running = true;
    private float framerate = 60;
    private int fps;
    private Level level;
    public boolean title = true;
    public boolean nextlevel = false;
    public int nextleveltimer = 100;
    public boolean paused = false;
    public boolean inventory = false;
    public int ticks = 0;
    public int levelnum = 1;

    public int highscore = 0;
    public int currentscore = 0;

    public Keys keys = new Keys();
    
    public Screen screen = new Screen(GAME_WIDTH, GAME_HEIGHT);
    
    public Map map;
    
    
	public MainComponent() {
		this.setPreferredSize(new Dimension(SCALE*GAME_WIDTH, SCALE*GAME_HEIGHT));
        this.addMouseMotionListener(this);
        this.addKeyListener(new InputHandler(keys));
        this.addMouseListener(this);
	}
	
	public static void main(String[] args) {
		MainComponent mc = new MainComponent();

        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mc);
        frame.setContentPane(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        mc.start();
	}

    private void createLevel(Player player) {
    	levelnum++;
    	map = new Map(Math.min((int)(Math.sqrt(levelnum)*32), 150),Math.min((int)(Math.sqrt(levelnum)*32), 150));
    	map.generate();
		level = new Level(map, currentscore, player, levelnum);
		player.x = 80;
		player.y = 80;
	}
    
    private void createLevel() {
    	levelnum = 1;
    	map = new Map((int)(Math.sqrt(levelnum)*32),(int)(Math.sqrt(levelnum)*32));
    	map.generate();
		level = new Level(map, 1, new Player(level, keys, 80,80));
	}

	public void start() {
        running = true;
        paused = false;
        inventory = false;
        Thread thread = new Thread(this);
        thread.setPriority(Thread.MAX_PRIORITY);
        thread.start();
    }
	
    public void stop() {
        running = false;
    }
    
    private void init() {
    	createLevel();
    	level.renderMap();
    	//level.addBadGuys(128);
    	level.addMobSpawners();
        setFocusTraversalKeysEnabled(false);
        requestFocus();

    }
    
    private void init(Player player) {
    	createLevel(player);
    	level.renderMap();
    	//level.addBadGuys(128);
    	level.addMobSpawners();
        setFocusTraversalKeysEnabled(false);
        requestFocus();
    }
    
	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
        int frames = 0;
        long lastTimer1 = System.currentTimeMillis();

        if(!nextlevel) {
        	try {
	            init();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return;
	        }
        }
        else
        	nextlevel = false;

        int toTick = 0;

        long lastRenderTime = System.nanoTime();
        int min = 999999999;
        int max = 0;
		while(running) {

            if(keys.pause.wasPressed()){
            	paused = !paused;
            	keys.pause.tick();
            }
            if(keys.inventory.wasPressed()){
            	inventory = !inventory;
            	keys.inventory.tick();
            }
			
            double nsPerTick = 1000000000.0 / framerate;
            boolean shouldRender = false;
            while (unprocessed >= 1) {
                toTick++;
                unprocessed -= 1;
            }

            int tickCount = toTick;
            if (toTick > 0 && toTick < 3) {
                tickCount = 1;
            }
            if (toTick > 20) {
                toTick = 20;
            }
            
            for (int i = 0; i < tickCount; i++) {
                toTick--;
                tick();
                shouldRender = true;
            }
			
			BufferStrategy bs = getBufferStrategy();
            if (bs == null) {
                createBufferStrategy(3);
                continue;
            }

            if (shouldRender) {
                frames++;
                Graphics g = bs.getDrawGraphics();
    			if(level.gameOver) {
    				renderGameOver(g);
    			}
    			else if(title)
    				renderTitle(g);
    			else if(nextlevel)
    				renderNextLevel(g);
    			else
    				render(g);

                long renderTime = System.nanoTime();
                int timePassed = (int) (renderTime - lastRenderTime);
                if (timePassed < min) {
                    min = timePassed;
                }
                if (timePassed > max) {
                    max = timePassed;
                }
                lastRenderTime = renderTime;
            }

            long now = System.nanoTime();
            unprocessed += (now - lastTime) / nsPerTick;
            lastTime = now;

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (shouldRender) {
                if (bs != null) {
                    bs.show();
                }
            }
            

            if (System.currentTimeMillis() - lastTimer1 > 1000) {
                lastTimer1 += 1000;
                fps = frames;
                frames = 0;
            }
        }

		
	}
	
	private void renderGameOver(Graphics g) {
		if(level.score > highscore)
			highscore = level.score;
        g.setColor(Color.BLACK);

        g.fillRect(0, 0, GAME_WIDTH*SCALE + 2, GAME_HEIGHT*SCALE + 2);
        g.clipRect(0, 0, GAME_WIDTH*SCALE, GAME_HEIGHT*SCALE);

        String msg = "SCORE: " + level.score;
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH - 100, 11);
		g.setColor(Color.WHITE);
		g.drawString(msg, GAME_WIDTH - 101, 10);

        msg = "LEVEL: " + levelnum;
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH - 100, 26);
		g.setColor(Color.WHITE);
		g.drawString(msg, GAME_WIDTH - 101, 25);
		
        msg = "HIGH SCORE: " + highscore;
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH, 11);
		g.setColor(Color.WHITE);
		g.drawString(msg, GAME_WIDTH, 10);
		
		Font font = new Font("", Font.BOLD, 100);
        msg = "GAME OVER!";
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(msg, 160, GAME_HEIGHT*SCALE / 2);
		g.setColor(Color.RED);
		g.drawString(msg, 159, (GAME_HEIGHT*SCALE / 2) -1);
		if(nextleveltimer <= 0){
			font = new Font("", Font.PLAIN, 32);
	        msg = "Click the mouse to play again!";
			g.setColor(Color.LIGHT_GRAY);
			g.setFont(font);
			g.drawString(msg, 200, GAME_HEIGHT*SCALE / 2 + 128);
			g.setColor(Color.WHITE);
			g.drawString(msg, 199, (GAME_HEIGHT*SCALE / 2) -1 + 128);
		}
	}	
	
	private void renderNextLevel(Graphics g) {
		if(level.score > highscore)
			highscore = level.score;
		currentscore = level.score;
        g.setColor(Color.BLACK);

        g.fillRect(0, 0, GAME_WIDTH*SCALE + 2, GAME_HEIGHT*SCALE + 2);
        g.clipRect(0, 0, GAME_WIDTH*SCALE, GAME_HEIGHT*SCALE);

        String msg = "SCORE: " + level.score;
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH - 100, 11);
		g.setColor(Color.WHITE);
		g.drawString(msg, GAME_WIDTH - 101, 10);
		
        msg = "LEVEL: " + levelnum;
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH - 100, 26);
		g.setColor(Color.WHITE);
		g.drawString(msg, GAME_WIDTH - 101, 25);
		
        msg = "HIGH SCORE: " + highscore;
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH, 11);
		g.setColor(Color.WHITE);
		g.drawString(msg, GAME_WIDTH, 10);
		
		Font font = new Font("", Font.BOLD, 100);
        msg = "NEXT LEVEL!";
		g.setColor(Color.WHITE);
		g.setFont(font);
		g.drawString(msg, 160, GAME_HEIGHT*SCALE / 2);
		g.setColor(Color.RED);
		g.drawString(msg, 159, (GAME_HEIGHT*SCALE / 2) -1);
		if(nextleveltimer <= 0){
			font = new Font("", Font.PLAIN, 32);
	        msg = "Click the mouse to continue!";
			g.setColor(Color.LIGHT_GRAY);
			g.setFont(font);
			g.drawString(msg, 200, GAME_HEIGHT*SCALE / 2 + 128);
			g.setColor(Color.WHITE);
			g.drawString(msg, 199, (GAME_HEIGHT*SCALE / 2) -1 + 128);
		}
	}
	
	private void renderTitle(Graphics g) {
        g.setColor(Color.BLACK);

        g.fillRect(0, 0, GAME_WIDTH*SCALE + 2, GAME_HEIGHT*SCALE + 2);
        g.clipRect(0, 0, GAME_WIDTH*SCALE, GAME_HEIGHT*SCALE);

        BufferedImage bi = Art.titlescreen;
        BufferedImage renderImage = new BufferedImage(bi.getWidth(),bi.getHeight(),bi.getType());
		Graphics gi = renderImage.createGraphics();
		gi.drawImage(bi,0,0,bi.getWidth(),bi.getHeight(),null);
		g.drawImage(renderImage, 0,0,GAME_WIDTH*SCALE + 2,GAME_HEIGHT*SCALE + 2, null);
	}
	
	private void render(Graphics g) {
		if(level.getPlayer() != null)
			level.render(screen, (int)level.getPlayer().x, (int)level.getPlayer().y);

		g.setColor(Color.BLACK);
		g.drawImage(screen, 0, 0, GAME_WIDTH*SCALE+2, GAME_HEIGHT*SCALE+2, null);


        String msg = "FPS: " + fps;
		g.setColor(Color.BLACK);
		g.drawString(msg, 11, 11);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, 10, 10);
		
        msg = "SCORE: " + level.score;
		g.setColor(Color.BLACK);
		g.drawString(msg, GAME_WIDTH - 100, 11);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH - 101, 10);

        msg = "LEVEL: " + levelnum;
		g.setColor(Color.BLACK);
		g.drawString(msg, GAME_WIDTH - 100, 26);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH - 101, 25);
		
        msg = "HIGH SCORE: " + highscore;
		g.setColor(Color.BLACK);
		g.drawString(msg, GAME_WIDTH, 11);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH, 10);

		int badGuyCount = 0;
		int spawnerCount = 0;
		for(int i = 0; i < level.entities.size(); i++) {
			Entity e = level.entities.get(i);
			if(!e.removed) {
				if(e instanceof Mob)
					badGuyCount++;
				if(e instanceof MobSpawner)
					spawnerCount++;
			}
		}

        msg = "ZOMBIES: " + badGuyCount;
		g.setColor(Color.BLACK);
		g.drawString(msg, GAME_WIDTH * SCALE - 250, 11);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH * SCALE - 251, 10);
		
        msg = "SPAWNERS: " + spawnerCount;
		g.setColor(Color.BLACK);
		g.drawString(msg, GAME_WIDTH * SCALE - 250, 26);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH * SCALE - 251, 25);
		
		/*
        msg = "MANA: " +  level.playermana;
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH * SCALE - 90, 11);
		g.setColor(Color.WHITE);
		g.drawString(msg, GAME_WIDTH * SCALE - 91, 10);
		*/
		ammoStatus(g);
		healthBar(g);
		expBar(g);
		
		Inventory inv = level.getPlayer().getInventory();
		
		if(paused) {
			inv.drawWeaponName(g, inv.getEquippedWeapon(), GAME_WIDTH * SCALE - 180, 55);
			inv.drawWeaponStats(g, inv.getEquippedWeapon(), GAME_WIDTH * SCALE - 180, 75, false);
			Font font = new Font("", Font.BOLD, 100);
	        msg = "PAUSED";
			g.setColor(Color.WHITE);
			g.setFont(font);
			g.drawString(msg, 300, GAME_HEIGHT*SCALE / 2);
			g.setColor(Color.RED);
			g.drawString(msg, 299, (GAME_HEIGHT*SCALE / 2) -1);
			drawHelp(g);
		}
		if(inventory) {
			inv.drawWeaponName(g, inv.getEquippedWeapon(), GAME_WIDTH * SCALE - 180, 55);
			inv.drawWeaponStats(g, inv.getEquippedWeapon(), GAME_WIDTH * SCALE - 180, 75, false);
			inv.drawInventory(g);
			inv.drawPlayerStats(g);
			drawHelp(g);
		}
			
	}

	private void drawHelp(Graphics g) {
		Font font = new Font("", Font.BOLD, 16);
		g.setFont(font);
		g.setColor(Color.WHITE);
		g.drawString("Use W,A,S,D or arrow keys to move, and mouse to shoot", 300, 500);
		g.drawString("'I' brings up the inventory, use 'E' or Enter to select an item", 300, 520);
		g.drawString("'Q' will drop an item from the inventory. Use 'E' or Enter to pick items up", 300, 540);
	}

	private void ammoStatus(Graphics g) {
		Inventory inv = level.getPlayer().getInventory();

        if(level.getPlayer().getInventory().changedweapon){
        	inv.drawWeaponName(g, inv.getEquippedWeapon(), GAME_WIDTH * SCALE - 180, 55);
        	inv.drawWeaponStats(g, inv.getEquippedWeapon(), GAME_WIDTH * SCALE - 180, 75, false);
        }
		
        String msg = "AMMO"+" "+level.getPlayer().ammo+" / "+level.getPlayer().maxammo;
		g.setColor(Color.BLACK);
		g.drawString(msg, GAME_WIDTH * SCALE - 145, 26+9);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH * SCALE - 146, 26+8);
		
	}
	
	private void expBar(Graphics g) {
		g.setColor(Color.YELLOW);
		g.drawString("PLAYER LEVEL: "+level.getPlayer().lvl, 15*SCALE, (GAME_HEIGHT-15)*SCALE);
		g.setColor(Color.BLUE);
		g.fillRect(15*SCALE, (GAME_HEIGHT-10)*SCALE, (int)((level.getPlayer().exp / (double)level.getPlayer().expperlvl)*(GAME_WIDTH-30)*SCALE), 5*SCALE);
		g.setColor(Color.WHITE);
		g.drawRect(15*SCALE, (GAME_HEIGHT-10)*SCALE, (GAME_WIDTH-30)*SCALE, 5*SCALE);
		
		if(level.getPlayer().leveledUp) {
			Font font = new Font("", Font.BOLD, 24);
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics();
			g.setColor(Color.BLACK);
			g.drawString("Level Up!", ((GAME_WIDTH/2)*SCALE) - (fm.stringWidth("Level Up!") / 2)+1, (GAME_HEIGHT-15)*SCALE +1);
			g.setColor(Color.YELLOW);
			g.drawString("Level Up!", ((GAME_WIDTH/2)*SCALE) - (fm.stringWidth("Level Up!") / 2), (GAME_HEIGHT-15)*SCALE);
		}
	}
	
	private void healthBar(Graphics g) {
		g.setColor(Color.GREEN);
		if(level.playerhealth/10.0 < 0.7)
			g.setColor(Color.YELLOW);
		if(level.playerhealth/10.0 < 0.5)
			g.setColor(Color.ORANGE);
		if(level.playerhealth/10.0 < 0.3)
			g.setColor(Color.RED);
		g.fillRect(GAME_WIDTH * SCALE - 90, 11,(int)(50*(level.getPlayer().health/(float) level.getPlayer().maxhealth)), 10);
		g.setColor(Color.DARK_GRAY);
		g.drawRect(GAME_WIDTH * SCALE - 90, 11,50, 10);

        String msg = "HEALTH";
		g.setColor(Color.BLACK);
		g.drawString(msg, GAME_WIDTH * SCALE - 145, 11+9);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH * SCALE - 146, 11+8);
	}

	public void tick() {

		if(level != null && !paused && !title && !nextlevel && !level.gameOver) {
			if(!inventory)
				level.tick();
			else
				level.getPlayer().getInventory().tick();
		}
		else if(nextlevel || level.gameOver)
			nextleveltimer--;
		keys.tick();
		ticks++;
		
		int badGuyCount = 0;
		int spawnerCount = 0;
		for(int i = 0; i < level.entities.size(); i++) {
			Entity e = level.entities.get(i);
			if(!e.removed) {
				if(e instanceof Mob)
					badGuyCount++;
				if(e instanceof MobSpawner)
					spawnerCount++;
			}
		}
		if(badGuyCount == 0 && spawnerCount == 0)
			nextlevel = true;
	}

	public void mouseClicked(MouseEvent e) {
		if(title || (level.gameOver && nextleveltimer <=0)){
			nextleveltimer = 100;
			stop();
			init();
			start();
		}
		else if(nextlevel && nextleveltimer <=0) {
			nextleveltimer = 100;
			stop();
			init(level.getPlayer());
			start();
		}
	}

	public void mousePressed(MouseEvent e) {
		if(!level.gameOver && !title && !paused && !inventory) {
			level.startBullets(e.getX(), e.getY());
		}
		else if(title)
			title = false;
	}

	public void mouseReleased(MouseEvent e) {
		level.stopBullets();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseDragged(MouseEvent e) {
		if(!level.gameOver && !title && !paused && !inventory) {
			level.targetBullets(e.getX(), e.getY());
		}
		else if(title)
			title = false;
//		if(!level.gameOver) {
//			level.newBullet(e.getX(),e.getY());
//		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
