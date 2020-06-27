package com.rex2go.mobslayer_game.map;

import org.bukkit.Location;

public class BossSection extends Section {

	Location bossSpawn;
	
	public BossSection(Section section, Location bossSpawn) {
		super(section.getDoorBlocks(), section.getDeadSpawn(), section.getMobSpawns());
		this.bossSpawn = bossSpawn;
	}
	
	public Location getBossSpawn() {
		return bossSpawn;
	}

	public void setBossSpawn(Location bossSpawn) {
		this.bossSpawn = bossSpawn;
	}
}
