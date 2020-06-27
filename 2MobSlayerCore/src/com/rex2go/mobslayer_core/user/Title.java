package com.rex2go.mobslayer_core.user;

public enum Title {

	BEGINNER("§7", "chat.title.beginner", 0), RECRUIT("§7", "chat.title.recruit", 2500), SOLDIER("§7", "chat.title.soldier", 5000), VETERAN("§6", "chat.title.veteran", 10000),
	HUNTER("§6", "chat.title.hunter", 25000), MASTER("§4", "chat.title.master", 50000), LEGEND("§4§o", "chat.title.legend", 100000);
	
	String path, color;
	int points;
	
	private Title(String color, String path, int points) {
		this.path = path;
		this.points = points;
		this.color = color;
	}
	
	public String getColor() {
		return color;
	}
	
	public String getTranslationPath() {
		return path;
	}
	
	public int getPoints() {
		return points;
	}
}
