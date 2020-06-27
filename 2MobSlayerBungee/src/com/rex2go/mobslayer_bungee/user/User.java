package com.rex2go.mobslayer_bungee.user;

import java.util.UUID;

import com.rex2go.mobslayer_bungee.util.Language;

public class User {

	UUID uuid;
	String name;
	boolean autoJoin;
	Rank rank;
	Language language;

	public User(UUID uuid, String name, boolean autoJoin, Rank rank, Language language) {
		this.uuid = uuid;
		this.name = name;
		this.autoJoin = autoJoin;
		this.rank = rank;
		this.language = language;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public void setUUID(UUID uuid) {
		this.uuid = uuid;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public boolean isAutoJoin() {
		return autoJoin;
	}

	public void setAutoJoin(boolean autoJoin) {
		this.autoJoin = autoJoin;
	}

	public Rank getRank() {
		return rank;
	}

	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	public Language getLanguage() {
		return language;
	}
	
	public void setLanguage(Language language) {
		this.language = language;
	}
}
