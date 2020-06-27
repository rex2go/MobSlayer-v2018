package com.rex2go.mobslayer_core.manager;

import com.rex2go.mobslayer_core.util.Language;

public class LanguageManager extends Manager {
	
	public Language getLanguageByCode(String langCode) {
		for(Language language : Language.values()) {
			if(language.getCode().equalsIgnoreCase(langCode)) {
				return language;
			}
		}
		return Language.ENGLISH;
	}
}
