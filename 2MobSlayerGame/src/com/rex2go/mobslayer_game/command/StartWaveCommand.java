package com.rex2go.mobslayer_game.command;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.user.GameUser;
import com.rex2go.mobslayer_game.wave.Wave;

public class StartWaveCommand extends Command {

	public StartWaveCommand() {
		// TODO
		super(new String[] { "startwave" }, Rank.MODERATOR, "");
	}

	@Override
	public void handle(User user, String[] args) {
		if (MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
			Wave wave = MobSlayerGame.getWaveManager().getActiveWave();

			if(wave != null) {
				if (wave.getPrepareTime() > 0) {
					wave.setPrepareTime(0);
				}
			}
			
			for(User user1 : MobSlayerCore.getUserManager().getStorage()) {
				GameUser gameUser = (GameUser) user1;
				
				gameUser.updateScoreboard();
			}
		} else {
			user.sendTranslatedMessage("command.only_in_game", Color.ERROR, null);
		}
	}
}
