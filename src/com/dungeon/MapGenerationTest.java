package com.dungeon;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.dungeon.map.Map;


public class MapGenerationTest extends Canvas implements MouseListener {

	private static final long serialVersionUID = 1L;
	private static Map map = new Map(300,200);
	
	public MapGenerationTest() {
		this.setPreferredSize(new Dimension(map.width*2, map.height*2));
		this.addMouseListener(this);
	}
	
	public void paint(Graphics g) {
		BufferedImage bi = new BufferedImage(map.width*2, map.height*2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bi.createGraphics();
		g2.setColor(Color.BLACK);
		
		for(int x=0; x < map.width; x++) {
			for(int y=0; y < map.height; y++) {
				if(map.getSquare(x, y) == 0)
					continue;
				g2.fillRect(x*2, y*2, 2, 2);
			}
		}
		
		g.drawImage(bi, 0, 0, null);
	}
	
	public static void main(String[] args) {
		
		map.generate();
		
		MapGenerationTest mgt = new MapGenerationTest();
		
        JFrame frame = new JFrame();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mgt);
        frame.setContentPane(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
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
		map.generate();
		invalidate();
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
