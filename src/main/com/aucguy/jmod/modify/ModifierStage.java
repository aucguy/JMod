package com.aucguy.jmod.modify;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;

import com.aucguy.jmod.data.AsmMethod;
import com.aucguy.jmod.load.Stage;

/**
 * a stage that allows transformers to change bytecode
 * 
 * @author aucguy
 * @version 1.0
 */
public class ModifierStage implements Stage {
	/**
	 * maps class binary names to classTransformers
	 */
	protected Map<Type, ClassTransformer> classTransformers;
	
	/**
	 * makes a Processor
	 */
	public ModifierStage() {
		this.classTransformers = new HashMap<Type, ClassTransformer>();
	}

	/**
	 * registers a classTransformer with this Processor
	 * 
	 * @param ct - the classTransformer to register
	 */
	public void registerClassTransformer(ClassTransformer ct) {
		this.classTransformers.put(ct.getApplicableClass(), ct);
	}

	/**
	 * registers the given method transformer with whatever classTransformer it goes with
	 * 
	 * @param mt - the methodTransformer to register
	 */
	public void registerMethodTransformer(MethodTransformer mt) {
		AsmMethod method = mt.getApplicableMethod();
		Type clazz = method.getOwner();
		if(!this.classTransformers.containsKey(clazz)) {
			this.classTransformers.put(clazz, new ClassTransformer(clazz));
		}
		this.classTransformers.get(clazz).registerMethodTransformer(mt);
	}

	/**
	 * registers multiple method transformers
	 * 
	 * @param mt - the methodTransformers to register
	 */
	public void registerMethodTransformers(MethodTransformer[] mt) {
		for(MethodTransformer i : mt) {
			this.registerMethodTransformer(i);
		}
	}

	@Override
	public ClassVisitor setupClass(Type clazz, ClassVisitor next) {
		ClassTransformer ct = this.classTransformers.get(clazz);
		ct.injectDelegate(next);
		return ct;
	}

	@Override
	public boolean doesModify(Type clazz, byte[] data) {
		return this.classTransformers.containsKey(clazz);
	}
}
