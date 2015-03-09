package com.aucguy.jmod.load;

import java.net.URL;

public interface ResourceProvider {
	/**
	 * returns the url associated with the given resource
	 */
	public URL findResource(String name);
}
