package com.aucguy.jmod.data;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.objectweb.asm.commons.Method;
import org.objectweb.asm.commons.SimpleRemapper;

/**
 * a two way remapper
 * 
 * @author aucguy
 */
public class BiRemapper extends SimpleRemapper {
	/**
	 * binds obfuscated class names to deobfuscated class names
	 */
	public Map<String, String> resourceLocations;

	/**
	 * the reverse of this map (value to key binding instead of key to value)
	 */
	public BiRemapper reverse;

	public BiRemapper(Map<String, String> mapping) {
		this(mapping, null);
	}

	protected BiRemapper(Map<String, String> mapping, BiRemapper other) {
		super(mapping);
		if(other == null) {
			Map<String, String> reverse = new HashMap<String, String>();
			List<String> methods = new LinkedList<String>();
			for(Entry<String, String> entry : mapping.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if(!key.contains(".")) { // class name
					reverse.put(value, key);
				} else { // method
					methods.add(key + ":" + value); // to be added to 'reverse' later
				}
			}

			for(String entry : methods) {
				String[] colonSplit = entry.split(":");
				String[] periodSplit = colonSplit[0].split("\\.");
				String oldClass = periodSplit[0];
				String[] parenSplit = periodSplit[1].split("\\(");
				String oldName = parenSplit[0];
				String oldDesc = "(" + parenSplit[1];
				String newName = colonSplit[1];
				String newClass = this.mapType(oldClass);
				String newDesc = this.mapMethodDesc(oldDesc);
				reverse.put(newClass + "." + newName + newDesc, oldName);
			}
			this.reverse = new BiRemapper(reverse, this);
		} else {
			this.reverse = other;
		}
		this.resourceLocations = new HashMap<String, String>();
		for(Entry<String, String> entry : mapping.entrySet()) {
			this.resourceLocations.put(entry.getValue() + ".class", entry.getKey() + ".class");
		}
	}
}
