package com.rex2go.mobslayer_game.wave;

import com.rex2go.mobslayer_game.mob.GameEntity;

public class SpawnRequest implements Cloneable {

	GameEntity gameEntity;
	int time, count;
	TriggerType triggerTyoe;
	
	public SpawnRequest(GameEntity gameEntity, int time, int count, TriggerType triggerTyoe) {
		this.gameEntity = gameEntity;
		this.triggerTyoe = triggerTyoe;
		this.time = time;
		this.count = count;
	}
	
	
	public GameEntity getGameEntity() {
		return gameEntity;
	}

	public void setGameEntity(GameEntity gameEntity) {
		this.gameEntity = gameEntity;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public TriggerType getTriggerType() {
		return triggerTyoe;
	}
	
	public void setTriggerType(TriggerType triggerTyoe) {
		this.triggerTyoe = triggerTyoe;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
	    return super.clone();
	}
	
	public enum TriggerType {
		TIME, COUNT, COUNT_AFTER_TIME
	}
}
