package com.dungeon;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.dungeon.entities.Entity;
import com.dungeon.entities.Mob;
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
    public boolean paused = false;
    public int ticks = 0;
    
    public int highscore = 0;

    public Keys keys = new Keys();
    
    public Screen screen = new Screen(GAME_WIDTH, GAME_HEIGHT);
    
    public Map map = new Map(100, 100);
    
    
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

    private void createLevel() {
    	map.generate();
		level = new Level(map);
	}

	public void start() {
        running = true;
        paused = false;
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
    	level.addPlayer(80, 80, keys);
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

        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        int toTick = 0;

        long lastRenderTime = System.nanoTime();
        int min = 999999999;
        int max = 0;
		while(running) {
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

        g.fillRect(0, 0, GAME_WIDTH*SCALE, GAME_HEIGHT*SCALE);
        g.clipRect(0, 0, GAME_WIDTH*SCALE, GAME_HEIGHT*SCALE);

        String msg = "SCORE: " + level.score;
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH - 100, 11);
		g.setColor(Color.WHITE);
		g.drawString(msg, GAME_WIDTH - 101, 10);
		
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
		
        msg = "HIGH SCORE: " + highscore;
		g.setColor(Color.BLACK);
		g.drawString(msg, GAME_WIDTH, 11);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH, 10);
		
		int badGuyCount = 0;
		for(int i = 0; i < level.entities.size(); i++) {
			Entity e = level.entities.get(i);
			if(!e.removed) {
				if(e instanceof Mob)
					badGuyCount++;
			}
			if(e.removed) {
				level.entities.remove(i--);
			}
		}

        msg = "ZOMBIES: " + badGuyCount;
		g.setColor(Color.BLACK);
		g.drawString(msg, GAME_WIDTH * SCALE - 250, 11);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH * SCALE - 251, 10);
		
		/*
        msg = "MANA: " +  level.playermana;
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH * SCALE - 90, 11);
		g.setColor(Color.WHITE);
		g.drawString(msg, GAME_WIDTH * SCALE - 91, 10);
		*/
		ammoStatus(g);
		healthBar(g);
			
	}

	private void ammoStatus(Graphics g) {
//		g.setColor(Color.BLUE);
//		g.fillRect(GAME_WIDTH * SCALE - 90, 26,(int)(50*(level.getPlayer().ammo/(float) level.getPlayer().maxammo)), 10);
//		g.setColor(Color.DARK_GRAY);
//		g.drawRect(GAME_WIDTH * SCALE - 90, 26,50, 10);
//
        String msg = "AMMO"+" "+level.getPlayer().ammo+" / "+level.getPlayer().maxammo;
		g.setColor(Color.BLACK);
		g.drawString(msg, GAME_WIDTH * SCALE - 145, 26+9);
		g.setColor(Color.LIGHT_GRAY);
		g.drawString(msg, GAME_WIDTH * SCALE - 146, 26+8);
		
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

		if(level != null) {
			level.tick();
		}
		keys.tick();
		ticks++;
	}

	public void mouseClicked(MouseEvent e) {
		if(level.gameOver){
			stop();
			init();
			start();
		}
	}

	public void mousePressed(MouseEvent e) {
		if(!level.gameOver) {
			level.startBullets(e.getX(), e.getY());
		}
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
		if(!level.gameOver) {
			level.targetBullets(e.getX(), e.getY());
		}
//		if(!level.gameOver) {
//			level.newBullet(e.getX(),e.getY());
//		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
