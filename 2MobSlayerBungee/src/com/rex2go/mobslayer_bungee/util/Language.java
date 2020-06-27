package com.rex2go.mobslayer_bungee.util;

public enum Language implements Cloneable {
	
	ENGLISH("eng"), GERMAN("ger");
	
	private String code;
	
	Language(String code) {
		this.code = code;
	}
	
	public String getCode() {
		return code;
	}
}
