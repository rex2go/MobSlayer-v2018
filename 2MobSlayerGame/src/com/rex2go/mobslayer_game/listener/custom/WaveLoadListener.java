package com.rex2go.mobslayer_game.listener.custom;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_game.event.WaveLoadEvent;
import com.rex2go.mobslayer_game.wave.Wave;

public class WaveLoadListener implements Listener {

	public WaveLoadListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@SuppressWarnings("unused")
	@EventHandler
	public void onWaveLoad(WaveLoadEvent event) {
		Wave wave = event.getWave();
		
		
	}
}
