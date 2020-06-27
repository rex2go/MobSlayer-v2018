package com.rex2go.mobslayer_core.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClientTabCompleteEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();

	private String message;
	private Player player;
	private boolean cancelled;

	public ClientTabCompleteEvent(String message, Player player) {
		this.message = message;
		this.player = player;
		cancelled = false;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Player getPlayer() {
		return player;
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
