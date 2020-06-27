package com.rex2go.mobslayer_game.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.user.GameUser;

public class ChatListener implements Listener {

	public ChatListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		GameUser user = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());

		if (!event.isCancelled()) {
			if (MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
				if(user.isDead()) {
					if(MobSlayerGame.getGameManager().isSpectator(user)) {
						event.setFormat("§e[SPECTATOR] " + user.getRank().getRankColor()
								+ user.getPlayer().getName() + " §8» §7");
					} else {
						event.setFormat("§c[DEAD] " + user.getRank().getRankColor()
								+ user.getPlayer().getName() + " §8» §7");
					}
				} else {
					event.setFormat("§e[" + ((GameUser) user).getInGamePoints() + "] " + user.getRank().getRankColor()
							+ user.getPlayer().getName() + " §8» §7");
				}
			}

			for (User user1 : MobSlayerCore.getUserManager().getStorage()) {
				String message = event.getMessage().replaceAll("%%", "%");
				String format = MobSlayerGame.getGameManager().getGameState() == GameState.INGAME ? event.getFormat() : 
					" §8| §e" + user.getPoints() + " §8| " + (user1.getSettings().showStars() ? (user.getStars() == "" ? "" : user.getStars() + " ") : "") + user.getTitle().getColor() 
						+ Translation.getTranslation(user.getTitle().getTranslationPath(), user1.getLanguage()) + " " + user.getRank().getRankColor() + user.getPlayer().getName() + " §8» §7";

				if (user1.getSettings().blockBadWords()) {
					message = MobSlayerCore.filter(message);
				}
				
				if(message.contains("@" + user1.getPlayer().getName())) {
					if(user1.getSettings().attNotification()) {
						String name = "@" + user1.getPlayer().getName();
						
						message = message.replaceAll(name, "§e" + name + "§7");
						
						user1.getPlayer().playSound(user.getPlayer().getLocation(), Sound.NOTE_PLING,
								1, 1);
					}
				}

				user1.getPlayer().sendMessage(format + message);
			}
		}

		event.setCancelled(true);
	}
}
