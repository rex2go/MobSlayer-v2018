package com.rex2go.mobslayer_game.map;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;

public class Map {

	String mapName, worldName, author;
	ArrayList<Location> playerSpawns;
	ArrayList<Section> sections;
	BossSection bossSection;
	World world;

	public Map(String mapName, String worldName, String author, ArrayList<Location> playerSpawns,
			ArrayList<Section> sections, BossSection bossSection) {
		this.mapName = mapName;
		this.worldName = worldName;
		this.author = author;
		this.playerSpawns = playerSpawns;
		this.sections = sections;
		this.bossSection = bossSection;
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

	public ArrayList<Location> getPlayerSpawns() {
		return playerSpawns;
	}

	public void setPlayerSpawns(ArrayList<Location> playerSpawns) {
		this.playerSpawns = playerSpawns;
	}

	public ArrayList<Section> getSections() {
		return sections;
	}

	public void setSections(ArrayList<Section> sections) {
		this.sections = sections;
	}

	public BossSection getBossSection() {
		return bossSection;
	}

	public void setBossSection(BossSection bossSection) {
		this.bossSection = bossSection;
	}

	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}
