package com.rex2go.mobslayer_bungee.util;

import java.util.HashMap;
import java.util.logging.Level;

import com.rex2go.mobslayer_bungee.MobSlayerBungee;

public class Translation {
	
	private static HashMap<String, String[]> translations = new HashMap<>();
	
	public Translation() {
		
	}
	
	public static String getTranslation(String path, Language language) {
		if(!translations.isEmpty()) {
			if(translations.containsKey(path)) {
				switch (language) {
				case ENGLISH:
					return translations.get(path)[1];
				case GERMAN:
					return translations.get(path)[0];
				default:
					return path;
				}
			}
		}
		MobSlayerBungee.getInstance().getLogger().log(Level.WARNING, "Missing translation: " + path);
		return path;
	}
	
	public static void addTranslation(String path, String[] translations) {
		if(Translation.translations.containsKey(path)) {
			Translation.translations.remove(path);
		}
		Translation.translations.put(path, translations);
	}
}
