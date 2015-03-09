package com.aucguy.jmod.tests.simplehook;

import org.junit.Assert;

import com.aucguy.jmod.tests.simplehook.SimpleHookTest;

/**
 * a test program
 * @author aucguy
 */
public class App implements Runnable {
	static {
		Assert.assertTrue(SimpleHookTest.counterLoadingAllowed);
	}
	
	public App() {
	}

	@Override
	public void run() {
		Counter counter = new Counter();
		counter.increment();
		counter.increment();
		counter.decrement();
		System.out.println("counter value is " + counter.getCount());
		Assert.assertTrue(SimpleHookTest.expectedCounterValue == counter.getCount());
	}
}
