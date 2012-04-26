package com.dungeon;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import com.dungeon.map.Map;

public class MapGenerationApplet extends Applet implements MouseListener {
    private static final long serialVersionUID = 1L;

	private static Map map = new Map(250,250);
	private static BufferedImage bi;

    public void init() {
		map.generate();
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500,500));
        addMouseListener(this);
    }
    
    public void start() {
    	bi = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
    	Graphics2D g2 = bi.createGraphics();
    	g2.setColor(Color.BLACK);

    	for(int x=0; x < map.width; x++) {
    		for(int y=0; y < map.height; y++) {
    			if(map.getSquare(x, y) == 0)
    				continue;
    			g2.fillRect(x*2, y*2, 2, 2);
    		}
    	}
    }
    
    public void paint(Graphics g) {
    	g.drawImage(bi, 0, 0, null);
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		map.generate();
		start();
		repaint();
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}