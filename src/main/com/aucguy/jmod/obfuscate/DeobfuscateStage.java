package com.aucguy.jmod.obfuscate;

import com.aucguy.jmod.data.BiRemapper;

public class DeobfuscateStage extends ObfuscateStage {
	public DeobfuscateStage(BiRemapper renames) {
		super(renames.reverse);
	}
	
	@Override
	public String transformResource(String resource) {
		return resource;
	}
}
