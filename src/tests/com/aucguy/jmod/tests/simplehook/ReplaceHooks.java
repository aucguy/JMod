package com.aucguy.jmod.tests.simplehook;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.aucguy.jmod.modify.MethodReplacer;

public class ReplaceHooks {
	@MethodReplacer.Hook("com/aucguy/jmod/tests/simplehook/Counter:increment:()V")
	public static void counterIncrement(MethodVisitor mv) {
		mv.visitInsn(Opcodes.ICONST_1);
		mv.visitFieldInsn(Opcodes.PUTSTATIC, "com/aucguy/jmod/tests/simplehook/SimpleHookTest", "counterIncremented", "Z");
		mv.visitInsn(Opcodes.RETURN); //don't forget to return!
		mv.visitMaxs(1, 1); //don't forget this too; you must inform the MethodWriter of the maxs
	}
}
