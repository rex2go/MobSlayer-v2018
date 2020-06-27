package com.rex2go.mobslayer_game.map;

import java.util.ArrayList;

import org.bukkit.Location;

public class Section {

	ArrayList<Location> doorBlocks;
	Location deadSpawn;
	ArrayList<Location> mobSpawns;

	public Section(ArrayList<Location> doorBlocks, Location deadSpawn, ArrayList<Location> mobSpawns) {
		this.doorBlocks = doorBlocks;
		this.deadSpawn = deadSpawn;
		this.mobSpawns = mobSpawns;
	}

	public ArrayList<Location> getDoorBlocks() {
		return doorBlocks;
	}

	public void setDoorBlocks(ArrayList<Location> doorBlocks) {
		this.doorBlocks = doorBlocks;
	}

	public Location getDeadSpawn() {
		return deadSpawn;
	}

	public void setDeadSpawn(Location deadSpawn) {
		this.deadSpawn = deadSpawn;
	}

	public ArrayList<Location> getMobSpawns() {
		return mobSpawns;
	}

	public void setMobSpawns(ArrayList<Location> mobSpawns) {
		this.mobSpawns = mobSpawns;
	}
}
