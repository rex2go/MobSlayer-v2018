package com.rex2go.mobslayer_game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.rex2go.mobslayer_game.wave.Wave;

public class WaveHandleEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	Wave wave;
	
	public WaveHandleEvent(Wave wave) {
		this.wave = wave;
	}
	
	public Wave getWave() {
		return wave;
	}

	public void setWave(Wave wave) {
		this.wave = wave;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
}
