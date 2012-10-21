package com.dungeon.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import com.dungeon.MainComponent;

public class Art {

	public static BufferedImage playerImage = loadImage("/playerlo.png", 32, 20);
	public static BufferedImage[][] playerImages = chop("/playersheet.png", 32, 32);
	public static BufferedImage floorTest = loadImage("/floortest.png", 32, 32);
	public static BufferedImage black = blackImage(32,32);
	public static BufferedImage wallTest = loadImage("/walltest.png", 32, 48);
	public static BufferedImage zombie1 = loadImage("/zombie1.png", 32, 32);
	public static BufferedImage lessermana = loadImage("/lessermana.png", 32, 32);
	public static BufferedImage fullmana = loadImage("/fullmana.png", 32, 32);
	public static BufferedImage ammo = loadImage("/ammo.png", 32, 32);
	public static BufferedImage pistol = loadImage("/pistol.png", 32, 32);
	public static BufferedImage shotgun = loadImage("/pistol.png", 32, 32);
	public static BufferedImage machinegun = loadImage("/pistol.png", 32, 32);
	public static BufferedImage fullhealth = loadImage("/fullhealth.png", 32, 32);

	public static BufferedImage loadImage(String name, int w, int h) {
		String imageLoc = name;
		URL imgURL = MainComponent.class.getResource(imageLoc);
		Image img=null;
		try {
			img=ImageIO.read(imgURL);
		}
		catch(IOException e)
		{
			System.out.println("Could not read image " + imageLoc);
			System.exit(0);
		}
		
		BufferedImage bi = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);
		Graphics gbi = bi.createGraphics();
		gbi.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
		if(name == "/zombie1.png")
			ImageProcessing.recolourImage(bi, -50, -50, -50);
		else
			ImageProcessing.recolourImage(bi, -20, -20, -20);
		
		return bi;
	}
	
	private static BufferedImage blackImage(int i, int j) {
		BufferedImage bi = new BufferedImage(i,j,BufferedImage.TYPE_INT_RGB);
		Graphics gbi = bi.createGraphics();
		gbi.setColor(Color.black);
		gbi.fillRect(0, 0, i, j);
		return bi;
	}

	public static BufferedImage[][] chop(String name, int w, int h) {
        
		String imageLoc = name;
		URL imgURL = MainComponent.class.getResource(imageLoc);
		Image img=null;
		try {
			img=ImageIO.read(imgURL);
		}
		catch(IOException e)
		{
			System.out.println("Could not read image " + imageLoc);
			System.exit(0);
		}
		
		BufferedImage bi = new BufferedImage(img.getWidth(null),img.getHeight(null),BufferedImage.TYPE_INT_ARGB);

		Graphics gbi = bi.createGraphics();
		gbi.drawImage(img, 0, 0, img.getWidth(null), img.getHeight(null), null);
		
		int xTiles = (bi.getWidth()) / w;
        int yTiles = (bi.getHeight()) / h;

        BufferedImage[][] result = new BufferedImage[xTiles][yTiles];
		
        for (int x = 0; x < xTiles; x++) {
            for (int y = 0; y < yTiles; y++) {
                result[x][y] = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                result[x][y].createGraphics().drawImage(img, 0, 0, w, h, x*w , y*h, x*w + w, y*h + h, null);
        		ImageProcessing.recolourImage(result[x][y], -50, -50, -50);
            }
        }
		
		return result;
		
	}
	
	
}
