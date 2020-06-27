package com.rex2go.mobslayer_game.command;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.user.GameUser;

public class AliveCommand extends Command {

	public AliveCommand() {
		// TODO
		super(new String[] { "alive" }, Rank.USER, "");
	}

	@Override
	public void handle(User user, String[] args) {
		if (MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
			String s = "";
			
			for(GameUser gameUser : MobSlayerGame.getGameManager().alive) {
				s += gameUser.getPlayer().getName() + ", ";
			}
			
			if(!s.equals("")) {
				s = s.substring(0, s.length()-2);
			}
			
			user.getPlayer().sendMessage("§7" + Translation.getTranslation("command.alive.players_alive", user.getLanguage()) + ": §e" + MobSlayerGame.getGameManager().alive.size() 
					+ " §8(" + s + ")");
		} else {
			user.sendTranslatedMessage("command.only_in_game", Color.ERROR, null);
		}
	}
}
