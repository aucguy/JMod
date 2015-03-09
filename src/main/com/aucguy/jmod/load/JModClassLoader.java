package com.aucguy.jmod.load;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.objectweb.asm.Type;

/**
 * a class loader that allows modification of classes when they are loaded
 * 
 * @author aucguy
 */
public class JModClassLoader extends ClassLoader {

	/**
	 * loads a class with JMod
	 * 
	 * @param clazz - the binary name of the class to load
	 * @param processors - modifies the bytecode of a class that is located in a modifiable directory and is loaded via
	 *            JMod
	 * @param directories - the directories that store modifiable classes
	 * @return - the loaded class
	 * @throws ClassNotFoundException - thrown if the class isn't found
	 * @throws MalformedURLException - thrown if files contains an invalid directory
	 */
	public static Class<?> getClassWith(String clazz, Processor[] processors, File[] directories) throws ClassNotFoundException,
			MalformedURLException {
		URL[] urls = new URL[directories.length];
		for(int i = 0; i < directories.length; i++) {
			urls[i] = directories[i].toURI().toURL();
		}

		URLClassLoader urlloader = new URLClassLoader(urls);
		ClassLoader defaultloader = Thread.currentThread().getContextClassLoader();
		JModClassLoader jmodloader = new JModClassLoader(defaultloader, processors, new CLResourceProvider(urlloader));
		return jmodloader.loadClass(clazz);
	}

	public Processor[] processors;
	protected ResourceProvider provider;

	/**
	 * create a JModClassLoader
	 * 
	 * @param parent - the delegated class loader
	 */
	public JModClassLoader(ClassLoader parent, Processor[] processors, ResourceProvider provider) {
		super(parent);
		this.processors = processors;
		this.provider = provider;
	}

	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {
		System.out.println("loading "+name);
		String pathName =  name.replace('.', '/');
		byte[] data = null;
		try {
			InputStream stream = this.getResourceAsStream(pathName + ".class");
			data = readAllData(stream);
		} catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		if(data == null) {
			throw new ClassNotFoundException();
		}
		
		Type type = Type.getObjectType(pathName);
		for(Processor processor : this.processors) {
			data = processor.process(type, data, this);
		}
		Class<?> clazz = this.defineClass(name, data, 0, data.length);
		return clazz;
	}

	@Override
	protected URL findResource(String name) {
		for(Processor processor : this.processors) {
			String changed = processor.transformName(name);
			if(changed != null) {
				name = changed;
			}
		}
		return this.provider.findResource(name);
	}
	
	/**
	 * returns all readable data from a stream
	 */
	public static byte[] readAllData(InputStream stream) throws IOException {
		if(stream == null) {
			return null;
		}
		byte[] data = new byte[stream.available()];
		stream.read(data);
		return data;
	}
}
