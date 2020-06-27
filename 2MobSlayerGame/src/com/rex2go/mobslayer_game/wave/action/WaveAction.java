package com.rex2go.mobslayer_game.wave.action;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.wave.SpawnRequest.TriggerType;

public abstract class WaveAction {

	String action;
	int time, count, delay = 0;
	TriggerType triggerType;
	ArrayList<Object> args;
	
	boolean active = false;
	
	public WaveAction(String action, int time, int count, int delay, TriggerType triggerType, ArrayList<Object> args) {
		this.action = action;
		this.time = time;
		this.count = count;
		this.delay = delay;
		this.triggerType = triggerType;
		this.args = args;
	}
	
	public void handle() {
		if(delay != 0) {
			setActive(true);
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					go();
					
				}
			}.runTaskLater(MobSlayerCore.getInstance(), delay);
		}
	}
	
	public abstract void go();
	
	public String getAction() {
		return action;
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
	
	public int getDelay() {
		return delay;
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public TriggerType getTriggerType() {
		return triggerType;
	}
	
	public void setTriggerType(TriggerType triggerType) {
		this.triggerType = triggerType;
	}
	
	public ArrayList<Object> getArgs() {
		return args;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
		
		if(active) {
			if(!MobSlayerGame.getWaveManager().getActiveWave().getActiveWaveAction().contains(this))
				MobSlayerGame.getWaveManager().getActiveWave().getActiveWaveAction().add(this);
		} else {
			MobSlayerGame.getWaveManager().getActiveWave().getActiveWaveAction().remove(this);
		}
	}
}
