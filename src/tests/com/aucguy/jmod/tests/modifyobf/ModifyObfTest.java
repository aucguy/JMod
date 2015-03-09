package com.aucguy.jmod.tests.modifyobf;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.aucguy.jmod.data.BiRemapper;
import com.aucguy.jmod.load.JModClassLoader;
import com.aucguy.jmod.load.Processor;
import com.aucguy.jmod.load.Stage;
import com.aucguy.jmod.load.StagedProcessor;
import com.aucguy.jmod.modify.MethodTransformer;
import com.aucguy.jmod.modify.ModifierStage;
import com.aucguy.jmod.obfuscate.DeobfuscateStage;
import com.aucguy.jmod.obfuscate.ObfuscateStage;

public class ModifyObfTest {
	public static boolean appLoadingAllowed = false;

	public static Object lastColor;

	public void run() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		BiRemapper renames = ObfuscateStage.loadRemappings("/com/aucguy/jmod/tests/obfuscate/remappings.properties");
		ModifierStage modifier = new ModifierStage();
		modifier.registerMethodTransformers(MethodTransformer.generateFromFunctions(SimpleHooked.class));
		
		ObfuscateStage obfuscator1 = new ObfuscateStage(renames);
		DeobfuscateStage deobfuscator = new DeobfuscateStage(renames);
		ObfuscateStage obfuscator2 = new ObfuscateStage(renames);
		Stage[] stages = new Stage[] { obfuscator1, deobfuscator, modifier, obfuscator2 };
		Processor[] processors = new Processor[] { new StagedProcessor(stages) };
		File[] dirs = new File[] { new File(System.getProperty("jmod.testsModdableDir")) };
		
		Class<?> clazz = JModClassLoader.getClassWith("com.aucguy.jmod.tests.modifyobf.App", processors, dirs);
		appLoadingAllowed = true;
		Runnable app = (Runnable) clazz.newInstance();
		app.run();
	}

	@Test
	public final void test() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		run();
	}
}
