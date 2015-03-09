package com.aucguy.jmod.load;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * a processor that setups up visitor delegation
 * 
 * @author aucguy
 */
public class StagedProcessor implements Processor {
	/**
	 * the stages that setup the delegation model. Ordered first call to last.
	 */
	Stage[] stages;
	
	Map<Type, Type[]> typeCache;
	
	public StagedProcessor(Stage[] stages) {
		this.stages = stages;
		this.typeCache = new HashMap<Type, Type[]>();
	}
	
	@Override
	public byte[] process(Type clazz, byte[] data, JModClassLoader classloader) {
		//check if modifications are needed
		Type[] cache = this.typeCache.get(clazz);
		boolean needModify = false;
		int i = 0;
		for(Stage stage : this.stages) {
			if(stage.doesModify(cache[i], data)) {
				needModify = true;
				break;
			}
			i++;
		}
		if(!needModify) {
			return data;
		}
		
		//actual modifications
		int readerFlags = Opcodes.ASM5;
		for(Stage stage : this.stages) {
			readerFlags = stage.modifyReaderFlags(readerFlags);
		}
		
		ClassReader reader = new ClassReader(data);
		ClassWriter writer = new ClassWriter(reader, readerFlags);
		ClassVisitor next = writer;
		for(i=this.stages.length-1; i>-1; i--) { //going in reverse
			if(this.stages[i].doesModify(cache[i], data)) {
				next = this.stages[i].setupClass(cache[i], next);
			}
		}
		reader.accept(next, readerFlags);
		this.typeCache.remove(clazz);
		return writer.toByteArray();
	}
	
	@Override
	public String transformName(String name) {
		Type orginal = resourceToType(name);
		Type[] cache = new Type[this.stages.length];
		int i = 0;
		for(Stage stage : this.stages) {
			name = stage.transformResource(name);
			cache[i] = resourceToType(name);
			i++;
		}
		this.typeCache.put(orginal, cache);
		return name;
	}
	
	public static Type resourceToType(String resource) {
		String clazz = resource;
		if(resource.endsWith(".class")) {
			clazz = resource.substring(0, resource.length()-6);
		}
		return Type.getObjectType(clazz);
	}
}
