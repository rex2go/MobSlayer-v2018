package com.rex2go.mobslayer_core.user;

public enum Rank {

	USER(0, "§9"), PREMIUM(1, "§6"), VIP(2, "§5"), BUILDER(3, "§3"), MODERATOR(4, "§c"), ADMIN(5, "§4");
	
	private int rankId;
	private String color;
	
	Rank(int rankId, String color) {
		this.rankId = rankId;
		this.color = color;
	}
	
	public int getRankId() {
		return rankId;
	}
	
	public String getRankColor() {
		return color;
	}
}
