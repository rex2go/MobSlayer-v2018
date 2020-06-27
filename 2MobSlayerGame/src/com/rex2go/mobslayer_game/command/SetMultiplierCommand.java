package com.rex2go.mobslayer_game.command;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;

public class SetMultiplierCommand extends Command {

	public SetMultiplierCommand() {
		// TODO
		super(new String[] { "setmultiplier" }, Rank.ADMIN, "");
	}

	@Override
	public void handle(User user, String[] args) {
		if (MobSlayerGame.getGameManager().getGameState() == GameState.STARTING || MobSlayerGame.getGameManager().getGameState() == GameState.WAITING) {
			if(MobSlayerGame.getWaveManager().getWaves().isEmpty()) {
				if(args.length > 0) {
					try {
						int i = Integer.parseInt(args[0]);
						MobSlayerGame.getWaveManager().setWaveMultiplier(i);
					} catch (Exception e) {
						user.sendTranslatedMessage("command.wrong_argument", Color.ERROR, null);
					}
				} else {
					user.getPlayer().sendMessage("§7/setmultiplier <mulitplier>");
				}
			} else {
				user.sendTranslatedMessage("command.setmultiplier.waves_already_loaded", Color.ERROR, null);
			}
		} else {
			user.sendTranslatedMessage("command.only_in_lobby", Color.ERROR, null);
		}
	}
}
