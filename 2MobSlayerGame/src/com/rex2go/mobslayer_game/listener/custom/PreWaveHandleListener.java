package com.rex2go.mobslayer_game.listener.custom;

import java.util.concurrent.ThreadLocalRandom;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.event.PreWaveHandleEvent;
import com.rex2go.mobslayer_game.misc.AirDrop;
import com.rex2go.mobslayer_game.misc.HealingStation;
import com.rex2go.mobslayer_game.mob.entity.MobChicken;
import com.rex2go.mobslayer_game.user.GameUser;
import com.rex2go.mobslayer_game.wave.Wave;

public class PreWaveHandleListener implements Listener {

	public PreWaveHandleListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onPreWaveHandle(PreWaveHandleEvent event) {
		Wave wave = event.getWave();
		
		HealingStation.sendParticleAll();
		
		for(Player all : Bukkit.getOnlinePlayers()) {
			all.setLevel(wave.getPrepareTime());
		}
		
		wave.setPrepareTime(wave.getPrepareTime() - 1);
		
		for(AirDrop airDrop : MobSlayerGame.getAirDropManager().getStorage()) {
			airDrop.handle();
		}
		
		// Chicken spawn
		if(MobSlayerGame.getGameManager().alive.size() > 0) {
			int random = ThreadLocalRandom.current().nextInt(1, (MobSlayerGame.getGameManager().alive.size() + 35) 
					/ MobSlayerGame.getGameManager().alive.size() + 1);
			
			if(random == 1) {
				new MobChicken().spawn(MobSlayerGame.getSectionManager().getRandomMobSpawn());
			}
		}
		
		if(wave.getPrepareTime() == 0) {
			for(User user : MobSlayerCore.getUserManager().getStorage()) {
				GameUser gameUser = (GameUser) user;
				gameUser.updateScoreboard();
				gameUser.getPlayer().setExp(0);
			}
		}
	}
}
