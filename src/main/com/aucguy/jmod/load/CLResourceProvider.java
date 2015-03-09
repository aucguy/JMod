package com.aucguy.jmod.load;

import java.net.URL;

/**
 * takes resources from a classloader and feeds them into JModClassLoader when asked
 * 
 * @author aucguy
 */
public class CLResourceProvider implements ResourceProvider {
	protected ClassLoader classloader;

	public CLResourceProvider(ClassLoader classloader) {
		this.classloader = classloader;
	}

	@Override
	public URL findResource(String name) {
		return this.classloader.getResource(name);
	}
}
