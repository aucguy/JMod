package com.aucguy.jmod.tests.modifyobf;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.aucguy.jmod.modify.MethodPrefixer;

public class SimpleHooked {
	@MethodPrefixer.Hook("com/aucguy/jmod/tests/obfuscate/Box:setColor:(Lcom/aucguy/jmod/tests/obfuscate/Color;)V")
	public static void prefixSetColor(MethodVisitor mv) {
		mv.visitVarInsn(Opcodes.ALOAD, 1);
		mv.visitFieldInsn(Opcodes.PUTSTATIC, "com/aucguy/jmod/tests/modifyobf/ModifyObfTest", "lastColor", "Ljava/lang/Object;");
	}
}
