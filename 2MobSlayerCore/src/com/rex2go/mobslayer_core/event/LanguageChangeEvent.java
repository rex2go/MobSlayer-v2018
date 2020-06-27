package com.rex2go.mobslayer_core.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Language;

public class LanguageChangeEvent extends Event implements Cancellable {

	private static final HandlerList handlers = new HandlerList();
	private User user;
	private Language language;
	private boolean cancelled = false;
	
	public LanguageChangeEvent(User user, Language language) {
		this.user = user;
		this.language = language;
	}
	
	public User getUser() {
		return user;
	}
	
	public Language getLanguage() {
		return language;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
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
