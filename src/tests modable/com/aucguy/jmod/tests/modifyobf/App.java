package com.aucguy.jmod.tests.modifyobf;

import org.junit.Assert;

import com.aucguy.jmod.tests.obfuscate.Box;
import com.aucguy.jmod.tests.obfuscate.Color;

public class App implements Runnable {
	static {
		if(!ModifyObfTest.appLoadingAllowed) {
			Assert.fail("App loaded prematurely");
		}
	}
	
	@Override
	public void run() {
		Box box = new Box(10, 10, new Color("red"));
		Color color = new Color("blue");
		box.setColor(color);
		Assert.assertTrue(ModifyObfTest.lastColor == color);
	}
}
