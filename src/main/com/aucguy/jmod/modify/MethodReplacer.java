package com.aucguy.jmod.modify;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

import org.objectweb.asm.MethodVisitor;

import com.aucguy.jmod.data.AsmMethod;

/**
 * used to replace vanilla methods
 * 
 * @author aucguy
 * @version 1.0
 */
public class MethodReplacer extends MethodHooker {
	/**
	 * the MethodWriter instance. The normal 'mv' field is't used here because if it were then the 'visit' methods would
	 * get called on the 'mv' field instead of the 'writer' field
	 */
	protected MethodVisitor writer;

	MethodReplacer(AsmMethod id, Method method) throws IllegalArgumentException {
		super(id, method);
	}

	/**
	 * used to signify that a method is supposed to be used with a MethodReplacer
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface Hook {
		/**
		 * the stringified id of the vanilla method
		 */
		String value();
	}

	@Override
	void injectMethodWriter(MethodVisitor mv) {
		this.writer = mv;
	}

	@Override
	protected MethodVisitor getWriter() {
		return this.writer;
	}
}
