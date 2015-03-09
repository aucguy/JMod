package com.aucguy.jmod.modify;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.objectweb.asm.MethodVisitor;

import com.aucguy.jmod.data.AsmMethod;

/**
 * used to prefix vanilla methods with custom code
 * 
 * @author aucguy
 * @version 1.0
 */
public class MethodPrefixer extends MethodHooker {
	/**
	 * constructs a MethodPrefixer
	 * 
	 * @param id - the id of the vanilla method
	 * @param method - the method that calls the necessary 'visit' methods
	 * @throws IllegalArgumentException - if the method is not static
	 */
	public MethodPrefixer(AsmMethod id, Method method) throws IllegalArgumentException {
		super(id, method);
	}

	/**
	 * used to signify that a method is supposed to be used with a MethodPrefixer
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Hook {
		/**
		 * the stringified vanilla method
		 */
		String value();
	}

	@Override
	public void injectMethodWriter(MethodVisitor mv) {
		this.mv = mv;
	}

	@Override
	protected MethodVisitor getWriter() {
		return this.mv;
	}
}
