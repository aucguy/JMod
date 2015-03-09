package com.aucguy.jmod.load;

import org.objectweb.asm.Type;

public interface Processor {
	/**
	 * modifies the given bytecode with previously given Transformers
	 * 
	 * @param name - the binary name of the class
	 * @param data - the class's bytecode
	 * @param classloader - the classloader that is loading the class
	 * @return the modified bytecode
	 */
	byte[] process(Type clazz, byte[] data, JModClassLoader classloader);
	
	/**
	 * Renames a class
	 * returns where the given class's bytecode file is found
	 * @param name - the class that was asked for from the classloader
	 * @return - the what the classloader should actually load
	 */
	default String transformName(String name) {
		return name;
	}
}
