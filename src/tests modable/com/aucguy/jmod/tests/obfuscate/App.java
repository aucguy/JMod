package com.aucguy.jmod.tests.obfuscate;

import java.lang.reflect.Method;

import static org.junit.Assert.*;

/**
 * the app for the obfuscation test. Actuall
 * 
 * @author aucguy
 */
public class App implements Runnable {
	public void run() {
		assertTrue(Box.class.getName().startsWith("_"));
		for (Method method : Box.class.getDeclaredMethods()) {
			System.out.println("checking method " + method.getName());
			assertTrue(method.getName().startsWith("_")); // method name is obfuscated
			for (Class<?> argType : method.getParameterTypes()) { // arg types are obfuscated
				if (argType.getName().startsWith("com.aucguy.jmod.tests.obfuscate")) {
					assertTrue(argType.getName().startsWith("_"));
				}
			}
		}
	}
}
