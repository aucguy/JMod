package com.aucguy.jmod.tests.simplehook;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.aucguy.jmod.modify.MethodPrefixer;

public class PrefixHooks {
	@MethodPrefixer.Hook("com/aucguy/jmod/tests/simplehook/Counter:increment:()V")
	public static void counterIncrement(MethodVisitor mv) {
		mv.visitInsn(Opcodes.ICONST_1);
		mv.visitFieldInsn(Opcodes.PUTSTATIC, "com/aucguy/jmod/tests/simplehook/SimpleHookTest", "counterIncremented", "Z");
	}
}
