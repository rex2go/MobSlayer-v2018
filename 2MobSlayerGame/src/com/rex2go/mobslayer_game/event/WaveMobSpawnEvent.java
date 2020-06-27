package com.rex2go.mobslayer_game.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.rex2go.mobslayer_game.mob.GameEntity;

public class WaveMobSpawnEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	GameEntity gameEntity;
	boolean cancelled;
	
	public WaveMobSpawnEvent(GameEntity gameEntity) {
		this.gameEntity = gameEntity;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }

	public GameEntity getGameEntity() {
		return gameEntity;
	}
	
	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
}
