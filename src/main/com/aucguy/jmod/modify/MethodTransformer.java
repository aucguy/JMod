package com.aucguy.jmod.modify;

import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import com.aucguy.jmod.data.AsmMethod;

/**
 * used to transform methods
 * 
 * @author aucguy
 * @version 1.0
 */
public abstract class MethodTransformer extends MethodVisitor {
	/**
	 * allows for deobfuscated names to go through the visit methods
	 */
	final class Wrapper extends MethodVisitor {
		Wrapper(MethodVisitor mv) {
			super(Opcodes.ASM5, mv);
		}
	}

	/**
	 * the method id of this transformer a method id is in the format 'class:method:descriptor'
	 */
	AsmMethod method;

	/**
	 * constructs a MethodTransformer
	 * 
	 * @param id - the id for this instance
	 */
	public MethodTransformer(AsmMethod method) {
		super(Opcodes.ASM5);
		// String[] changed = convertMethod(parts[0], parts[1], parts[2]); //TODO add back for obfuscation support
		this.method = method;
	}

	/**
	 * gives this MethodTrasnformer the method writer instance
	 * 
	 * @param mv - the MethodWriter created for this instance. Made just before this instance will visit things
	 */
	abstract void injectMethodWriter(MethodVisitor mv);

	/**
	 * gets the applicable method
	 * 
	 * @return the method that this transformer modifies
	 */
	public AsmMethod getApplicableMethod() {
		return this.method;
	}

	/**
	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		String[] parts = convertMethod(owner.replace('/', '.'), name.replace('/', '.'), desc.replace('/', '.'));
		super.visitMethodInsn(opcode, parts[0].replace('.', '/'), parts[1].replace('.', '/'),
				parts[2].replace('.', '/'));
	}
	**/

	public void visitMethodInsn(int opcode, AsmMethod method) {
		this.visitMethodInsn(opcode, method.getOwner().getInternalName(), method.getName(), method.getDescriptor());
	}

	/**
	 * generates an array of MethodTransformers from a class of annotations. This allows for easy use of ASM
	 * modifications See com.sijobe.asm.SimpleHooked for an example.
	 * 
	 * @param clazz - the class containing the modification methods.
	 * @return an array of MethodTransformers that were generated
	 */
	public static MethodTransformer[] generateFromFunctions(Class<?> clazz) {
		List<MethodTransformer> modifiers = new LinkedList<MethodTransformer>();
		for(java.lang.reflect.Method method : clazz.getDeclaredMethods()) {
			if(Modifier.isStatic(method.getModifiers())) {
				if(method.isAnnotationPresent(MethodReplacer.Hook.class)) {
					String annotation = method.getAnnotation(MethodReplacer.Hook.class).value();
					modifiers.add(new MethodReplacer(getMethod(annotation), method));
				} else if(method.isAnnotationPresent(MethodPrefixer.Hook.class)) {
					String annotation = method.getAnnotation(MethodPrefixer.Hook.class).value();
					modifiers.add(new MethodPrefixer(getMethod(annotation), method));
				}
			}
		}

		MethodTransformer[] r = new MethodTransformer[modifiers.size()];
		return modifiers.toArray(r);
	}
	
	/**
	 * takes a method id and turns it into an AsmMethod
	 * @param id - in the format of '<class>:<name>:<descriptor>'
	 * @return
	 */
	public static AsmMethod getMethod(String id) {
		String[] parts = id.split(":");
		return new AsmMethod(Type.getObjectType(parts[0]), parts[1], parts[2]);
	}
}
