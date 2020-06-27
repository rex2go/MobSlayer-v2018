package com.rex2go.mobslayer_game.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.event.QuitEvent;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.event.GameEndEvent;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.task.Task;
import com.rex2go.mobslayer_game.user.GameUser;

public class QuitListener implements Listener {

	public QuitListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onQuit(QuitEvent event) {
		User user = event.getUser();
		Player player = user.getPlayer();
		GameUser gameUser = (GameUser) user;

		gameUser.saveGameData();
		
		if(MobSlayerGame.getGameManager().getGameState() != GameState.INGAME) {
			if (user != null) {
				if(gameUser.getVote() != null) {
					gameUser.getVote().setVotes(gameUser.getVote().getVotes()-1);
				}
				
				for (User user1 : MobSlayerCore.getUserManager().getStorage()) {
					user1.getPlayer().sendMessage(MobSlayerCore.PREFIX + " " + Translation.getTranslation("game.quit_message", user1.getLanguage(), 
							user.getRank().getRankColor() + player.getName()));
				}
			}
		} else {
			if(MobSlayerGame.getGameManager().alive.contains(((GameUser) user))) {
				MobSlayerGame.getGameManager().alive.remove(((GameUser) user));
				PlayerDeathListener.inventory.put(player.getUniqueId().toString(), player.getInventory().getContents().clone());
				PlayerDeathListener.inventoryArmor.put(player.getUniqueId().toString(), player.getInventory().getArmorContents().clone());
				
				user.getStatistics().setDeaths(user.getStatistics().getDeaths()+1);
				
				for(User user1 : MobSlayerCore.getUserManager().getStorage()) {
					GameUser gameUser1 = (GameUser) user1;
					gameUser1.updateScoreboard();
					
					gameUser1.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §c" + Translation.getTranslation("game.player_death", gameUser1.getLanguage(), 
							gameUser.getRank().getRankColor() + gameUser.getPlayer().getName()));
					
					if(MobSlayerGame.getGameManager().alive.size() != 1) {
						gameUser1.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §7" + Translation.getTranslation("game.remaing_players_count", gameUser1.getLanguage(), 
								MobSlayerGame.getGameManager().alive.size()));
					} else {
						gameUser1.getPlayer().sendMessage(MobSlayerCore.PREFIX + " §7" + Translation.getTranslation("game.remaing_player_count", gameUser1.getLanguage()));
					}
				}
			}
			
			if(MobSlayerGame.getGameManager().isSpectator(gameUser)) {
				MobSlayerGame.getGameManager().getSpectators().remove(gameUser);
			} else {
				GameUser clone = gameUser.clone();
				MobSlayerGame.getGameManager().offline.add(clone);
			}
			
			if(MobSlayerGame.getGameManager().alive.isEmpty()) {
				MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new GameEndEvent(false));
				Task.stopGameTask();
			}
		}
	}
}
