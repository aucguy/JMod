package com.aucguy.jmod.modify;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.objectweb.asm.MethodVisitor;

import com.aucguy.jmod.data.AsmMethod;

/**
 * base class for simple method transformers based on methods like in SimpleHooked
 * 
 * @author aucguy
 * @version 1.0
 */
abstract class MethodHooker extends MethodTransformer {
	/**
	 * the method that will call the necessary 'visit' methods of the methodWriter instead of or before the vanilla
	 * method.
	 */
	protected Method method;

	/**
	 * constructs a MethodHooker
	 * 
	 * @param id - the id of the method to modify
	 * @param method - the method that will call the necessary 'visit' methods
	 * @throws IllegalArgumentException - the method is not static
	 */
	MethodHooker(AsmMethod id, Method method) throws IllegalArgumentException {
		super(id);
		if(!Modifier.isStatic(method.getModifiers())) {
			throw(new IllegalArgumentException("MethodTransformer not of correct class"));
		}
		this.method = method;
	}

	/**
	 * returns this instance's methodWriter
	 * 
	 * @return
	 */
	protected abstract MethodVisitor getWriter();

	@Override
	public void visitCode() {
		try {
			this.getWriter().visitCode();
			this.method.invoke(null, this.getWriter());
		} catch(IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
