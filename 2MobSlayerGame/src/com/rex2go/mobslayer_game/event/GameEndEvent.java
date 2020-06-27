package com.rex2go.mobslayer_game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameEndEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	boolean win;
	
	public GameEndEvent(boolean win) {
		this.win = win;
	}
	
	public boolean isWin() {
		return win;
	}

	public void setWin(boolean win) {
		this.win = win;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
}
