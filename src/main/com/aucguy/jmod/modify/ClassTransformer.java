package com.aucguy.jmod.modify;

import java.util.Map;
import java.util.HashMap;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import com.aucguy.jmod.data.AsmMethod;

/**
 * used to transform a class
 * 
 * @author aucguy
 * @version 1.0
 */
public class ClassTransformer extends ClassVisitor {
	/**
	 * the the class this transformer modifies
	 */
	protected Type clazz;

	/**
	 * binds method ids to methodTransformerss
	 */
	protected Map<Method, MethodTransformer> methodTransformers;

	/**
	 * creates a ClassTransformer
	 * 
	 * @param name - the binary name of the class this transformer modifies
	 */
	ClassTransformer(Type clazz) {
		super(Opcodes.ASM5, new ClassWriter(Opcodes.ASM5));
		this.clazz = clazz;
		this.methodTransformers = new HashMap<Method, MethodTransformer>();
	}

	/**
	 * @return the binary name of the class this transformer modifies
	 */
	Type getApplicableClass() {
		return this.clazz;
	}

	/**
	 * sets this transformers delegate
	 */
	public void injectDelegate(ClassVisitor writer) {
		this.cv = writer;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor writer = super.visitMethod(access, name, desc, signature, exceptions);
		Method method = new AsmMethod(this.getApplicableClass(), name, desc);

		if(this.methodTransformers.containsKey(method)) {
			MethodTransformer transformer = this.methodTransformers.get(method);
			transformer.injectMethodWriter(writer);
			return transformer.new Wrapper(transformer);
		} else {
			return writer;
		}
	}

	/**
	 * registers the given method transformer for the with this instance
	 * 
	 * @param mt - the methodTransformer to register
	 * @throws IllegalArgumentExceptions - if the tranformer doesn't modify the a method of this instance's class
	 */
	void registerMethodTransformer(MethodTransformer mt) throws IllegalArgumentException {
		AsmMethod method = mt.getApplicableMethod();
		Type clazz = method.getOwner();
		if(!clazz.equals(this.getApplicableClass())) {
			throw(new IllegalArgumentException("MethodTransformer not of correct class"));
		}
		this.methodTransformers.put(mt.getApplicableMethod(), mt);
	}

	/**
	 * registers multiple methodTransformers
	 * 
	 * @param mt - the MethodTransformers to register
	 * @throws IllegalArgumentException - if the tranformer doesn't modify the a method of this instance's class
	 */
	void regsiterMethodTransformers(MethodTransformer[] mt) throws IllegalArgumentException {
		for(MethodTransformer i : mt) {
			this.registerMethodTransformer(i);
		}
	}
}
