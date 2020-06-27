package com.rex2go.mobslayer_game.listener.custom;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.BountifulAPI;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.event.GameEndEvent;
import com.rex2go.mobslayer_game.event.WaveDoneEvent;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.map.Section;
import com.rex2go.mobslayer_game.misc.HealingStation;
import com.rex2go.mobslayer_game.user.GameUser;
import com.rex2go.mobslayer_game.wave.BossWave;
import com.rex2go.mobslayer_game.wave.Wave;

public class WaveDoneListener implements Listener {

	public WaveDoneListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onWaveDone(WaveDoneEvent event) {
		Wave wave = event.getWave();
		
		// Welle beendet Nachricht
		for(User user : MobSlayerCore.getUserManager().getStorage()) {
			GameUser gameUser = (GameUser) user;
			gameUser.removeTracker();
			
			if(MobSlayerGame.getWaveManager().isBossWave(wave)) {
				user.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §e" 
						+ Translation.getTranslation("game.wave.successfully_completed", user.getLanguage(), "§cBOSS§e")); // BOSS
			} else {
				user.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §e" + Translation.getTranslation("game.wave.successfully_completed", user.getLanguage(), 
						MobSlayerGame.getWaveManager().getActiveWave().getWaveLevel()));
			}
		}
		
		HealingStation.changeStateAll();
		
		GameUser best = MobSlayerGame.getGameManager().getBestPlayer();
		
		if(best != null) {
			BountifulAPI.sendActionBar(best.getPlayer(), "§6§lMVP §7(+50 Coins)");
			
			best.getPlayer().playSound(best.getPlayer().getLocation(), Sound.SUCCESSFUL_HIT, 1, 1);
			
			best.setCoins(best.getCoins()+50);
			best.setInGameCoins(best.getInGameCoins()+50);
		}
		
		if(MobSlayerGame.getWaveManager().getNextWave() != null) {
			Wave wave1 = MobSlayerGame.getWaveManager().getNextWave();
			
			// XP zurücksetzen
			for(Player all : Bukkit.getOnlinePlayers()) {
				all.setExp(0);
			}
			
			// Nächste Welle Nachricht
			for(User user : MobSlayerCore.getUserManager().getStorage()) {
				user.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §7" 
							+ Translation.getTranslation("game.wave.next_wave_starting_in", user.getLanguage(), wave1.getPrepareTime()));
			}
			
			// Wiederbeleben der Spieler + 50 Coins + wavekills = 0
			int spawn = 0;
			
			for(Player all : Bukkit.getOnlinePlayers()) {
				GameUser gameUser = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(all.getUniqueId());
				gameUser.setWaveKills(0);
				
				if(gameUser.isDead()) {	
					if(!MobSlayerGame.getGameManager().isSpectator(gameUser)) {
						if(MobSlayerGame.getMapManager().getGameMap().getPlayerSpawns().get(spawn) != null) {
							gameUser.revive(MobSlayerGame.getMapManager().getGameMap().getPlayerSpawns().get(spawn));
							
							spawn++;
						} else {
							gameUser.revive(MobSlayerGame.getMapManager().getGameMap().getPlayerSpawns().get(0));
						}
					}
				} else {
					gameUser.setCoins(gameUser.getCoins()+50);
					gameUser.setInGameCoins(gameUser.getInGameCoins()+50);
					
					gameUser.getPlayer().getInventory().addItem(GameItem.translate(Item.DIAMOND.assignNBTData(), gameUser));
				}
			}
			
			if(MobSlayerGame.getWaveManager().isBossWave(wave1)) {
				@SuppressWarnings("unused")
				BossWave bossWave = (BossWave) wave1;
				
				// Door öffnen	
				for(Location loc : MobSlayerGame.getMapManager().getGameMap().getSections().get(
						MobSlayerGame.getMapManager().getGameMap().getSections().size() - 2).getDoorBlocks()) {
					loc.getBlock().setType(Material.AIR);
				}
				
				MobSlayerGame.getWaveManager().setActiveWave(wave1);
				
				// Section setzen
				Section section = MobSlayerGame.getMapManager().getGameMap().getSections().get(
						MobSlayerGame.getMapManager().getGameMap().getSections().size()-1);
				MobSlayerGame.getSectionManager().getActiveSections().clear();
				MobSlayerGame.getSectionManager().getActiveSections().add(section);
			} else {
				// Nächste Welle starten
				MobSlayerGame.getWaveManager().setActiveWave(wave1);
				
				// Door entfernen
				if(MobSlayerGame.getMapManager().getGameMap().getSections().size() > MobSlayerGame.getWaveManager().getActiveWave().getWaveLevel()) {
					for(Location loc : MobSlayerGame.getMapManager().getGameMap().getSections().get(
							MobSlayerGame.getWaveManager().getActiveWave().getWaveLevel() - 1).getDoorBlocks()) {
						loc.getBlock().setType(Material.AIR);
					}
					
					// Section erweitern
					Section section = MobSlayerGame.getMapManager().getGameMap().getSections().get(MobSlayerGame.getWaveManager().getActiveWave().getWaveLevel()-1);
					MobSlayerGame.getSectionManager().getActiveSections().add(section);
				}
			}
		} else {
			MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new GameEndEvent(true));
		}
	}
}
