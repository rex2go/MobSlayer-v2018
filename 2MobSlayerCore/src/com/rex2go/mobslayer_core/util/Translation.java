package com.rex2go.mobslayer_core.util;

import java.util.Formatter;
import java.util.HashMap;
import java.util.logging.Level;

import com.rex2go.mobslayer_core.MobSlayerCore;

public class Translation {
	
	private static HashMap<String, String[]> translations = new HashMap<>();
	
	public static String getTranslation(String path, Language language, Object ... args) {
		if(!translations.isEmpty() && translations.containsKey(path)) {
			@SuppressWarnings("resource")
			Formatter formatter = new Formatter();
			
			switch (language) {
			case ENGLISH:
				return formatter.format(translations.get(path)[1], args).toString();
			case GERMAN:
				return formatter.format(translations.get(path)[0], args).toString();
			default:
				return path;
			}
		}
		
		MobSlayerCore.getInstance().getLogger().log(Level.WARNING, "Missing translation: " + path);
		return path;
	}
	
	public static void addTranslation(String path, String[] translations) {
		if(!Translation.translations.containsKey(path)) Translation.translations.put(path, translations);
	}
}
