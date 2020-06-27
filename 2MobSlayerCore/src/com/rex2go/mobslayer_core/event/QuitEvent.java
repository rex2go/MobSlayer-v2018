package com.rex2go.mobslayer_core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.rex2go.mobslayer_core.user.User;

public class QuitEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private User user;
	
	public QuitEvent(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
}
