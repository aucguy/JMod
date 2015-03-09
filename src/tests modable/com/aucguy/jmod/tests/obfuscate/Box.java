package com.aucguy.jmod.tests.obfuscate;

/**
 * a class that is going to be obfuscated
 * 
 * @author aucguy
 */
public class Box {
	int width;
	int height;
	Color color;

	public Box(int width, int height, Color color) {
		this.width = width;
		this.height = height;
		this.color = color;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public Color getColor() {
		return this.color;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void describe() {
		System.out.println("I am a " + this.getColor().getName() + "box "
				+ this.getWidth() + " cm wide and " + this.getHeight()
				+ " cm long.");
	}
}
