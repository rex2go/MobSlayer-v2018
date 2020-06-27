package com.rex2go.mobslayer_game.command;

import java.util.ArrayList;

import org.bukkit.Sound;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.misc.HealingStation;
import com.rex2go.mobslayer_game.user.GameUser;

public class ReviveCommand extends Command {

	public ReviveCommand() {
		// TODO
		super(new String[] { "revive" }, Rank.USER, "");
		
		ArrayList<String> arr = new ArrayList<>();
		
		for(User user : MobSlayerCore.getUserManager().getStorage()) {
			GameUser gameUser = (GameUser) user;
			
			if(gameUser.isDead()) {
				if(!MobSlayerGame.getGameManager().isSpectator(gameUser)) {
					arr.add(user.getPlayer().getName());
				}
			}
		}
		
		addArgSuggestion(new String[] { "%player%" });
	}

	@Override
	public void handle(User user, String[] args) {
		GameUser gameUser = (GameUser) user;

		if (MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
			if (!MobSlayerGame.getGameManager().isSpectator(gameUser)) {
				if (gameUser.getCoins() >= 500) {
					if (args.length > 0) {
						if(!gameUser.isDead()) {
							GameUser target = null;
	
							for (User user1 : MobSlayerCore.getUserManager().getStorage()) {
								if (args[0].equalsIgnoreCase(user1.getPlayer().getName())) {
									GameUser gameUser1 = (GameUser) user1;
	
									if (gameUser1.isDead()) {
										target = gameUser1;
										break;
									}
								}
							}
	
							if (target != null) {
								gameUser.setCoins(gameUser.getCoins() - 500);
								gameUser.setInGameCoins(gameUser.getInGameCoins()-500);
								target.revive(gameUser.getPlayer().getLocation());
	
								for (User user1 : MobSlayerCore.getUserManager().getStorage()) {
									user1.getPlayer().sendMessage(MobSlayerCore.PREFIX + " " + Translation
											.getTranslation("game.revive.player_revived_player", user1.getLanguage(), user.getRank().getRankColor() + user.getPlayer().getName(), 
													target.getRank().getRankColor() + target.getPlayer().getName()));
									user1.getPlayer().playSound(user1.getPlayer().getLocation(), Sound.ZOMBIE_REMEDY, 1, 1);
								}
								
								target.getPlayer().sendMessage(Translation.getTranslation("game.revive.you_have_been_revived_by", target.getLanguage(), user.getPlayer().getName()));
								user.getPlayer().sendMessage(Translation.getTranslation("game.revive.you_revived_player", user.getLanguage(), target.getPlayer().getName()));
							} else {
								user.sendTranslatedMessage("command.revive.user_not_dead", Color.ERROR, null);
							}
						} else {
							// TODO
						}
					} else {
						if(gameUser.isDead()) {
							gameUser.setCoins(gameUser.getCoins() - 500);
							gameUser.setInGameCoins(gameUser.getInGameCoins()-500);
							
							if(!HealingStation.getHealingStations().isEmpty() && gameUser.getDeathLocation() != null) {
								HealingStation nearest = null;
								
								for(HealingStation hs : HealingStation.getHealingStations()) {
									if(nearest == null) {
										nearest = hs;
									} else if(nearest.getLocation().distanceSquared(gameUser.getDeathLocation()) > hs.getLocation().distanceSquared(gameUser.getDeathLocation())) {
										nearest = hs;
									}
								}
								
								if(nearest != null) {
									gameUser.revive(nearest.getLocation().add(0.5, 1, 0.5));
								} else {
									gameUser.revive(MobSlayerGame.getMapManager().getGameMap().getPlayerSpawns().get(0));
								}
							} else {
								gameUser.revive(MobSlayerGame.getMapManager().getGameMap().getPlayerSpawns().get(0));
							}
	
							for (User user1 : MobSlayerCore.getUserManager().getStorage()) {
								user1.getPlayer().sendMessage(MobSlayerCore.PREFIX + " " + Translation.getTranslation("game.revive.player_revived", user1.getLanguage(), 
										user.getRank().getRankColor() + user.getPlayer().getName()));
								user1.getPlayer().playSound(user1.getPlayer().getLocation(), Sound.ZOMBIE_REMEDY, 1, 1);
							}
							user.getPlayer().sendMessage(Translation.getTranslation("game.revive.you_revived_yourself", user.getLanguage()));
						} else {
							user.sendTranslatedMessage("command.revive.not_dead", "§4", null);
						}
					}
				} else {
					user.sendTranslatedMessage("coins.not_enough_coins", "§4", null);
				}
			} else {
				user.sendTranslatedMessage("command.not_available_in_spectator_mode", Color.ERROR, null);
			}
		} else {
			user.sendTranslatedMessage("command.only_in_game", Color.ERROR, null);
		}
	}
}
