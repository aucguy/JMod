package com.aucguy.jmod.data;

import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

/**
 * an objectweb method (stores the name and descriptor), but also has a belonging type
 * 
 * @author aucguy
 */
public class AsmMethod extends Method {
	Type owner;

	public AsmMethod(Type owner, String name, String desc) {
		super(name, desc);
		this.owner = owner;
	}

	public Type getOwner() {
		return owner;
	}
}
