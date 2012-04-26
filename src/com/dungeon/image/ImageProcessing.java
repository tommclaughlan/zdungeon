package com.dungeon.image;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class ImageProcessing {

	public static void invertColours(BufferedImage img) {
		for(int x=0;x<img.getWidth();x++){
			for(int y=0;y<img.getHeight();y++){
				int px = img.getRGB(x, y);
				int alpha = (px >> 24) & 0xFF;
				int red = (px >> 16) & 0xFF;
				int green = (px >> 8) & 0xFF;
				int blue = px & 0xFF;
				int pixel = (alpha<<24) + (Math.abs(red-255)<<16) + (Math.abs(green-255)<<8) + Math.abs(blue-255);
				img.setRGB(x, y, pixel);
			}
		}
	}

	public static void recolourImage(BufferedImage img, int r, int g, int b) {
		for(int x=0;x<img.getWidth();x++){
			for(int y=0;y<img.getHeight();y++){
				int px = img.getRGB(x, y);
				int alpha = (px >> 24) & 0xFF;
				int red = (px >> 16) & 0xFF;
				int green = (px >> 8) & 0xFF;
				int blue = px & 0xFF;
				int pixel = (alpha<<24) + (Math.max(Math.min((red+r),255),0)<<16) + (Math.max(Math.min((green+g),255),0)<<8) + Math.max(Math.min((blue+b),255),0);
				img.setRGB(x, y, pixel);
			}
		}
	}
	
	
	public static void changeBrightnessContrast(BufferedImage img, int bright, int cont) {

		RescaleOp colourChange = new RescaleOp(cont,bright,null);
		colourChange.filter(img, img);
	}
}
