package com.rex2go.mobslayer_game.listener.custom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.event.WaveStartEvent;
import com.rex2go.mobslayer_game.user.GameUser;
import com.rex2go.mobslayer_game.wave.BossWave;
import com.rex2go.mobslayer_game.wave.Wave;

public class WaveStartListener implements Listener {

	public WaveStartListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onWaveStart(WaveStartEvent event) {
		Wave wave = event.getWave();
		
		// TODO Nachrichten
		
		for(User user : MobSlayerCore.getUserManager().getStorage()) {
			GameUser gameUser = (GameUser) user;
			gameUser.setSkipWave(false);;
		}
		
		// Boss spawnen
		if(MobSlayerGame.getWaveManager().isBossWave(wave)) {
			BossWave bossWave = (BossWave) wave;
			if(!bossWave.isBossSpawned()) {
				bossWave.getBossLocation().setWorld(MobSlayerGame.getMapManager().getGameMap().getWorld());
				bossWave.getBossGameEntity().spawn(bossWave.getBossLocation());
				bossWave.setBossSpawned(true);
			}
			
			// Door öffnen										
			for(Location loc : MobSlayerGame.getMapManager().getGameMap().getSections().get(
					MobSlayerGame.getMapManager().getGameMap().getSections().size() - 1).getDoorBlocks()) {
				loc.getBlock().setType(Material.AIR);
			}
			
			// TODO Andere Door schließen										
			for(Location loc : MobSlayerGame.getMapManager().getGameMap().getSections().get(
					MobSlayerGame.getMapManager().getGameMap().getSections().size() - 2).getDoorBlocks()) {
				loc.getBlock().setType(Material.WOOL);
			}
			
			// TODO Spieler außerhalb teleportieren
		}
	}
}
