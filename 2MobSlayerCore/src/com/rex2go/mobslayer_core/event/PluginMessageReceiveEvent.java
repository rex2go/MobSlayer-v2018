package com.rex2go.mobslayer_core.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.google.common.io.ByteArrayDataInput;

public class PluginMessageReceiveEvent extends Event {

	private static final HandlerList handlers = new HandlerList();
	private ByteArrayDataInput byteArrayDataInput;

	public PluginMessageReceiveEvent(ByteArrayDataInput byteArrayDataInput) {
		this.byteArrayDataInput = byteArrayDataInput;
	}
	
	public ByteArrayDataInput getByteArrayDataInput() {
		return byteArrayDataInput;
	}
	
	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
        return handlers;
    }
}
