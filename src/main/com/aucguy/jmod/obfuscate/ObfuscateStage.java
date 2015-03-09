package com.aucguy.jmod.obfuscate;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.RemappingClassAdapter;

import com.aucguy.jmod.data.BiRemapper;
import com.aucguy.jmod.load.Stage;

/**
 * modifies bytecode so that it gets obfuscated. 
 * This is useful in situations the distributed enviroment is obfuscated
 * and you don't want to keep on redeploying it there instead of a development enviroment
 * 
 * @author aucguy
 */
public class ObfuscateStage implements Stage {
	/**
	 * the deobfuscated to obfuscated mappings
	 */
	BiRemapper remapping;
	
	public ObfuscateStage(BiRemapper renames) {
		this.remapping = renames;
	}
	
	@Override
	public ClassVisitor setupClass(Type clazz, ClassVisitor next) {
		return new RemappingClassAdapter(next, this.remapping);
	}

	@Override
	public boolean doesModify(Type clazz, byte[] data) {
		return this.remapping.mapType(clazz.getInternalName()) != clazz.getInternalName();
	}
	
	@Override
	public int modifyReaderFlags(int flags) {
		return flags | ClassReader.EXPAND_FRAMES;
	}
	
	@Override
	public String transformResource(String resource) {
		if(this.remapping.resourceLocations.containsKey(resource)) {
			return this.remapping.resourceLocations.get(resource);
		}
		return resource;
	}
	
	public static BiRemapper loadRemappings(String resource) throws IOException {
		Properties properties = new Properties();
		InputStream stream = ObfuscateStage.class.getResourceAsStream(resource);
		properties.load(stream);
		return new BiRemapper(new HashMap<String, String>((Map) properties));
	}
}
