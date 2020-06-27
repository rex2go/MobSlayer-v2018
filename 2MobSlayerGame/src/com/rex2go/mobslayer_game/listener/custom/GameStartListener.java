package com.rex2go.mobslayer_game.listener.custom;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.ParticleUtil;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.event.GameStartEvent;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.task.Task;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.EnumParticle;

public class GameStartListener implements Listener {

	public GameStartListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}
	
	@EventHandler
	public void onGameStart(GameStartEvent event) {
		Task.startGameTask();
		
		for (User user : MobSlayerCore.getUserManager().getStorage()) {
			GameUser gameUser = (GameUser) user;
			MobSlayerGame.getGameManager().alive.add(gameUser);
		}

		MobSlayerGame.getMapManager().teleportPlayersToMap(MobSlayerGame.getMapManager().getGameMap());
		MobSlayerGame.getGameManager().setGameState(GameState.INGAME);

		// Section erweitern
		MobSlayerGame.getSectionManager().getActiveSections()
				.add(MobSlayerGame.getMapManager().getGameMap().getSections().get(0));

		// Welle 1 starten
		MobSlayerGame.getWaveManager().setActiveWave(MobSlayerGame.getWaveManager().getNextWave());

		for (User user : MobSlayerCore.getUserManager().getStorage()) {
			GameUser gameUser = (GameUser) user;
			user.reset();
			user.getStatistics().setGames(user.getStatistics().getGames()+1);
			gameUser.updateScoreboard();
			gameUser.showHealthScoreboard();
			user.getPlayer().setGameMode(GameMode.SURVIVAL);

			user.getPlayer()
					.sendMessage(MobSlayerCore.PREFIX + " §7" + Translation.getTranslation("game.wave.first_wave_starting_in", user.getLanguage(), 
							MobSlayerGame.getWaveManager().getActiveWave().getPrepareTime()));

			gameUser.loadEquip();

			ParticleUtil.circle(EnumParticle.FIREWORKS_SPARK, user.getPlayer().getLocation().clone().add(0, 0.5, 0), 35, 0.5f);
			ParticleUtil.circle(EnumParticle.FIREWORKS_SPARK, user.getPlayer().getLocation().clone().add(0, 1, 0), 35, 0.5f);
		}
	}
}
