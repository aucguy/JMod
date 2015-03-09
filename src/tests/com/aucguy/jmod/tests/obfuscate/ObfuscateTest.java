package com.aucguy.jmod.tests.obfuscate;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.aucguy.jmod.data.BiRemapper;
import com.aucguy.jmod.load.JModClassLoader;
import com.aucguy.jmod.load.Processor;
import com.aucguy.jmod.load.Stage;
import com.aucguy.jmod.load.StagedProcessor;
import com.aucguy.jmod.obfuscate.ObfuscateStage;

/**
 * tests the runtime obfuscation system
 * 
 * @author aucguy
 */
public class ObfuscateTest {
	/**
	 * run the test
	 */
	public void run() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		BiRemapper remappings = ObfuscateStage.loadRemappings("/com/aucguy/jmod/tests/obfuscate/remappings.properties");
		Stage stage = new ObfuscateStage(remappings);
		Processor processor = new StagedProcessor(new Stage[] { stage });
		File[] dirs = new File[] { new File(System.getProperty("jmod.testsModdableDir")) };
		Class<?> clazz = JModClassLoader.getClassWith("com.aucguy.jmod.tests.obfuscate.App",
				new Processor[] { processor }, dirs);
		Runnable app = (Runnable) clazz.newInstance();
		app.run();
	}

	@Test
	public final void test() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		run();
	}
}
