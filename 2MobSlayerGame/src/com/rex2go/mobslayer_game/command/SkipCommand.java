package com.rex2go.mobslayer_game.command;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.event.WaveDoneEvent;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.user.GameUser;

public class SkipCommand extends Command {

	public SkipCommand() {
		// TODO
		super(new String[] { "skip" }, Rank.USER, "");
	}

	@Override
	public void handle(User user, String[] args) {
		if(MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
			GameUser gameUser = (GameUser) user;
			if(!gameUser.isDead()) {
				if(MobSlayerGame.getWaveManager().getActiveWave() != null) {
					if(!MobSlayerGame.getWaveManager().isBossWave(MobSlayerGame.getWaveManager().getActiveWave())) {
						if(MobSlayerGame.getWaveManager().getActiveWave().isSkippable()) {
							if(!gameUser.skipWave()) {
								gameUser.setSkipWave(true);
								
								for(User user1 : MobSlayerCore.getUserManager().getStorage()) {
									user1.getPlayer().sendMessage(MobSlayerCore.PREFIX + " " + Translation.getTranslation("game.skip_message", 
											user1.getLanguage(), user.getRank().getRankColor() + user.getPlayer().getName()));
								}
								
								for(GameUser gameUser1 : MobSlayerGame.getGameManager().alive) {
									if(!gameUser1.skipWave()) {
										return;
									}
								}
								
								MobSlayerGame.getGameEntityManager().removeAll();
								MobSlayerGame.getWaveManager().getActiveWave().getSpawnRequests().clear();
								
								MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new WaveDoneEvent(MobSlayerGame.getWaveManager().getActiveWave()));
								
								for(GameUser gameUser1 : MobSlayerGame.getGameManager().alive) {
									gameUser1.updateScoreboard();
								}
							}
						} else {
							// schon geskippt
						}
					} else {
						// boss wave
					}
				} else {
					// keine Wave
				}
			} else {
				user.sendTranslatedMessage("command.not_available_in_spectator_mode", Color.ERROR, null);
			}
		} else {
			user.sendTranslatedMessage("command.only_in_game", Color.ERROR, null);
		}
	}
}
