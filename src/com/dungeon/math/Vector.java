package com.dungeon.math;

public class Vector {
	
	public double x,y;
	
	public Vector(){
		x = 0;
		y = 0;
	}
	
	public Vector(double x, double y) {
		this.x = x;
		this.y = y;
		validate();
	}

	public Vector(double x0, double y0, double x1, double y1) {
		this.x = x1 - x0;
		this.y = y1 - y0;
		validate();
	}
	
	public Vector(double x0, double y0, double x1, double y1, double forceLength) {
		this.x = x1 - x0;
		this.y = y1 - y0;
		validate();
		double length = length();
		this.x /= length;
		this.y /= length;
		validate();
	}
	
	public Vector(Vector vec) {
		this.x = vec.x;
		this.y = vec.y;
		validate();
	}

	public double length() {
		return Math.sqrt(x*x + y*y);
	}

	public double distance(double xto, double yto) {
		return Math.sqrt((x-xto)*(x-xto) + (y-yto)*(y-yto));
	}
	
	public void extend(double newLength) {
		double scale = newLength / length();
		this.x *= scale;
		this.y *= scale;
	}
	
	public String printVec() {
		return "(" + x + ", " + y + ")";
	}
	
	public void validate() {
		if (Double.isInfinite(x) || Double.isInfinite(y) || Double.isNaN(x) || Double.isNaN(y)) {
            System.out.println("Uh oh: " + printVec());
            x = 0;
            y = 0;
        }
	}
	
	public Vector clone() {
		return new Vector(this.x, this.y);
	}
}
