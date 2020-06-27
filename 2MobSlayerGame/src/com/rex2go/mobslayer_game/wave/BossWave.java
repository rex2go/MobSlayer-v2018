package com.rex2go.mobslayer_game.wave;

import java.util.ArrayList;

import org.bukkit.Location;

import com.rex2go.mobslayer_game.mob.BossGameEntity;
import com.rex2go.mobslayer_game.wave.action.WaveAction;

public class BossWave extends Wave {

	BossGameEntity bossGameEntity;
	Location bossLocation;
	boolean bossSpawned = false;
	
	public BossWave(int prepareTime, int waveLevel, ArrayList<SpawnRequest> spawnRequests, BossGameEntity boss, Location bossLocation, ArrayList<WaveAction> waveActions) {
		super(prepareTime, waveLevel, spawnRequests, waveActions);
		this.bossGameEntity = boss;
		this.bossLocation = bossLocation;
	}

	public BossGameEntity getBossGameEntity() {
		return bossGameEntity;
	}

	public void setBossGameEntity(BossGameEntity bossGameEntity) {
		this.bossGameEntity = bossGameEntity;
	}

	public Location getBossLocation() {
		return bossLocation;
	}

	public void setBossLocation(Location bossLocation) {
		this.bossLocation = bossLocation;
	}
	
	public boolean isBossSpawned() {
		return bossSpawned;
	}

	public void setBossSpawned(boolean bossSpawned) {
		this.bossSpawned = bossSpawned;
	}
}
