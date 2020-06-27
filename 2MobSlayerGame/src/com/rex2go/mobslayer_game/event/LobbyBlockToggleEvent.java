package com.rex2go.mobslayer_game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.rex2go.mobslayer_game.user.GameUser;

public class LobbyBlockToggleEvent extends Event {

	private static final HandlerList handlers = new HandlerList();

	GameUser gameUser;
	
	public GameUser getGameUser() {
		return gameUser;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public void setGameUser(GameUser gameUser) {
		this.gameUser = gameUser;
	}

	public LobbyBlockToggleEvent(GameUser gameUser) {
		this.gameUser = gameUser;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
}
