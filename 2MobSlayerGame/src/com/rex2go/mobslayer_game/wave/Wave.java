package com.rex2go.mobslayer_game.wave;

import java.util.ArrayList;

import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.wave.action.WaveAction;

public class Wave {

	int prepareTime, waveLevel;
	ArrayList<SpawnRequest> spawnRequests;
	ArrayList<WaveAction> waveActions;
	
	ArrayList<WaveAction> waveActionsActive = new ArrayList<>();

	boolean started = false, skippable = false;

	SpawnRequest lastSpawnRequest;

	int maxTime = new Integer(MobSlayerGame.SKIP_AFTER_TIME);

	public Wave(int prepareTime, int waveLevel, ArrayList<SpawnRequest> spawnRequests, ArrayList<WaveAction> waveActions) {
		this.prepareTime = prepareTime;
		this.waveLevel = waveLevel;
		this.spawnRequests = spawnRequests;
		this.waveActions = waveActions;
		
		for (SpawnRequest spawnRequest : spawnRequests) {
			if (lastSpawnRequest != null) {
				if (lastSpawnRequest.getTime() < spawnRequest.getTime()) {
					lastSpawnRequest = spawnRequest;
				}
			} else {
				lastSpawnRequest = spawnRequest;
			}
		}
		
		try {
			lastSpawnRequest = (SpawnRequest) lastSpawnRequest.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}

	public int getPrepareTime() {
		return prepareTime;
	}

	public void setPrepareTime(int prepareTime) {
		this.prepareTime = prepareTime;
	}

	public int getWaveLevel() {
		return waveLevel;
	}

	public void setWaveLevel(int waveLevel) {
		this.waveLevel = waveLevel;
	}

	public ArrayList<SpawnRequest> getSpawnRequests() {
		return spawnRequests;
	}

	public void setSpawnRequests(ArrayList<SpawnRequest> spawnRequests) {
		this.spawnRequests = spawnRequests;
	}
	
	public ArrayList<WaveAction> getWaveActions() {
		return waveActions;
	}

	public void setWaveActions(ArrayList<WaveAction> waveActions) {
		this.waveActions = waveActions;
	}

	public int getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(int maxTime) {
		this.maxTime = maxTime;
	}

	public SpawnRequest getLastSpawnRequest() {
		return lastSpawnRequest;
	}
	
	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
	
	public boolean isSkippable() {
		return skippable;
	}

	public void setSkippable(boolean skippable) {
		this.skippable = skippable;
	}
	
	public void addWaveAction(WaveAction waveAction) {
		waveActions.add(waveAction);
	}
	
	public ArrayList<WaveAction> getActiveWaveAction() {
		return waveActionsActive;
	}
}
