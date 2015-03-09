package com.aucguy.jmod.modify;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Method;

import com.aucguy.jmod.data.AsmMethod;

/**
 * replaces a method call with another static call
 * 
 * @author aucguy
 * @version 1.0
 */
public class CallReplacer extends MethodTransformer {

	/**
	 * the method to be replaced
	 */
	AsmMethod oldCall;

	/**
	 * the method to be added in
	 */
	AsmMethod newCall;

	CallReplacer(AsmMethod method, AsmMethod oldMethod, AsmMethod newMethod) {
		super(method);
		this.oldCall = oldMethod;
		this.newCall = newMethod;
	}

	@Override
	void injectMethodWriter(MethodVisitor mv) {
		this.mv = mv;
	}

	@Override
	public void visitMethodInsn(int opcode, String owner, String name, String desc) {
		if(new Method(name, desc).equals(this.oldCall)) {
			super.visitMethodInsn(Opcodes.INVOKESTATIC, this.newCall);
		} else {
			super.visitMethodInsn(opcode, owner, name, desc);
		}
	}
}
