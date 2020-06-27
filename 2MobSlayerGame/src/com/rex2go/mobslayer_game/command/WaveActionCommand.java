package com.rex2go.mobslayer_game.command;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;

public class WaveActionCommand extends Command {

	public WaveActionCommand() {
		// TODO
		super(new String[] { "waveaction" }, Rank.ADMIN, "");
	}

	@Override
	public void handle(User user, String[] args) {
		if (MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
			if(!MobSlayerGame.getWaveManager().getWaves().isEmpty()) {
				if(MobSlayerGame.getWaveManager().getActiveWave() != null) {
					if(args.length > 0) {
//						WaveAction waveAction = MobSlayerGame.getWaveManager().getWaveActionByName(args[0]);
//						if(waveAction != null) {
//							waveAction.handle(Arrays.copyOfRange(args, 1, args.length));
//						}
					}
				}
			}
		} else {
			user.sendTranslatedMessage("command.only_in_game", Color.ERROR, null);
		}
	}
}
