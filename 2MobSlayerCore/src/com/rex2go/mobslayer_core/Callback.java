package com.rex2go.mobslayer_core;

import java.util.UUID;

import org.bukkit.Bukkit;

import com.google.common.io.ByteArrayDataInput;
import com.rex2go.mobslayer_core.listener.PluginChannelListener;

public abstract class Callback {

	String id;
	int timeout = 20;
	boolean response = false;
	
	public Callback() {
		Bukkit.getScheduler().scheduleSyncDelayedTask(MobSlayerCore.getInstance(), new Runnable() {
			
			@Override
			public void run() {
				if(!response) {
					fail();
					PluginChannelListener.callbacks.remove(this);
				}
				
			}
		}, timeout);
		
		id = UUID.randomUUID().toString();
	}
	
	public void response(ByteArrayDataInput byteArrayDataInput) {
		doResponse(byteArrayDataInput);
		response = true;
	}
	
	public abstract void fail();
	
	protected abstract void doResponse(ByteArrayDataInput byteArrayDataInput);
	
	public String getId() {
		return id;
	}
}
