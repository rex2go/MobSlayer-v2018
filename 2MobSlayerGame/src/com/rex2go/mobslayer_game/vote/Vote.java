package com.rex2go.mobslayer_game.vote;

public class Vote {

	String mapName, worldName, author;
	int votes;

	public Vote(String mapName, String worldName, String author) {
		this.mapName = mapName;
		this.worldName = worldName;
		this.author = author;
		this.votes = 0;
	}
	
	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public String getWorldName() {
		return worldName;
	}

	public void setWorldName(String worldName) {
		this.worldName = worldName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
}
