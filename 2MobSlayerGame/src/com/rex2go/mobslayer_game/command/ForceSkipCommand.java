package com.rex2go.mobslayer_game.command;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.event.WaveDoneEvent;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.user.GameUser;

public class ForceSkipCommand extends Command {

	public ForceSkipCommand() {
		// TODO
		super(new String[] { "forceskip" }, Rank.MODERATOR, "");
	}

	@Override
	public void handle(User user, String[] args) {
		if(MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
			if(MobSlayerGame.getWaveManager().getActiveWave() != null) {
				if(!MobSlayerGame.getWaveManager().isBossWave(MobSlayerGame.getWaveManager().getActiveWave())) {
					if(MobSlayerGame.getWaveManager().getActiveWave().isSkippable()) {
						MobSlayerGame.getGameEntityManager().removeAll();
						MobSlayerGame.getWaveManager().getActiveWave().getSpawnRequests().clear();
						
						MobSlayerCore.getInstance().getServer().getPluginManager().callEvent(new WaveDoneEvent(MobSlayerGame.getWaveManager().getActiveWave()));
						
						for(GameUser gameUser1 : MobSlayerGame.getGameManager().alive) {
							gameUser1.updateScoreboard();
						}
					}
				} else {
					// boss wave
				}
			} else {
				// keine Wave
			}
		} else {
			user.sendTranslatedMessage("command.only_in_game", Color.ERROR, null);
		}
	}
}
