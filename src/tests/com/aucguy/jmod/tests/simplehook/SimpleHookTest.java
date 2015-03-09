package com.aucguy.jmod.tests.simplehook;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import com.aucguy.jmod.load.JModClassLoader;
import com.aucguy.jmod.load.Processor;
import com.aucguy.jmod.load.Stage;
import com.aucguy.jmod.load.StagedProcessor;
import com.aucguy.jmod.modify.MethodTransformer;
import com.aucguy.jmod.modify.ModifierStage;

public class SimpleHookTest {
	public static boolean counterIncremented = false;
	public static boolean counterLoadingAllowed = false;
	public static int expectedCounterValue = 0;

	public void run(Class<?> hooks) throws Exception {
		ModifierStage stage = new ModifierStage();
		stage.registerMethodTransformers(MethodTransformer.generateFromFunctions(hooks));
		StagedProcessor processor = new StagedProcessor(new Stage[] { stage });

		File[] path = new File[] { new File(System.getProperty("jmod.testsModdableDir")) };
		counterLoadingAllowed = true;
		Runnable app = (Runnable) JModClassLoader.getClassWith("com.aucguy.jmod.tests.simplehook.App",
				new Processor[] { processor }, path).newInstance();

		app.run();
		System.out.println("counter incremented: " + counterIncremented);
		Assert.assertTrue(counterIncremented);
	}

	public void reset() {
		counterIncremented = false;
		counterLoadingAllowed = false;
	}

	@Test
	public final void test() throws Exception {
		this.reset();
		expectedCounterValue = 1;
		this.run(PrefixHooks.class);
		this.reset();
		expectedCounterValue = -1;
		this.run(ReplaceHooks.class);
	}
}
