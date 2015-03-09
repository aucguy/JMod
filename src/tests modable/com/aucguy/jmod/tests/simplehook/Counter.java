package com.aucguy.jmod.tests.simplehook;

import org.junit.Assert;

import com.aucguy.jmod.tests.simplehook.SimpleHookTest;

/**
 * a test class that will recieve asm modifications
 * @author aucguy
 */
public class Counter {
	static {
		Assert.assertTrue(SimpleHookTest.counterLoadingAllowed);
	}
	
	int count;
	
	public Counter() {
		this.count = 0;
	}
	
	public void increment() {
		//SimpleHookTest.counterIncremented = true; //the added code
		this.count++;
	}
	
	public void decrement() {
		this.count--;
	}
	
	public int getCount() {
		return this.count;
	}
}
