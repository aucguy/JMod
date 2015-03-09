package com.aucguy.jmod.load;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

/**
 * represents a 'stage' in the delegation model
 * 
 * @author aucguy
 */
public interface Stage {
	/**
	 * sets up the class visitor for this stage
	 * 
	 * @param clazz - the type to be processed
	 * @param next - the next visitor in the delegation model(ie the delegate of the returned value)
	 * @return - the class visitor for this stage
	 */
	ClassVisitor setupClass(Type clazz, ClassVisitor next);

	/**
	 * returns whether or does any modifications on the bytecode
	 */
	boolean doesModify(Type clazz, byte[] data);

	/**
	 * modifies the flags needed for the class reader to be constructed with
	 * 
	 * @param flags - the flags as returned by the previous stage. If there are no flags then it is Opcodes.ASM5
	 * @return - the flags this stage wants
	 */
	default int modifyReaderFlags(int flags) {
		return flags;
	}
	
	/**
	 * changes the resource location where the vanilla bytecode for this class is found
	 */
	default String transformResource(String resource) {
		return resource;
	}
}
