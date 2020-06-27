package com.rex2go.mobslayer_game.command;

import java.util.ArrayList;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;
import com.rex2go.mobslayer_game.mob.GameEntityType;
import com.rex2go.mobslayer_game.user.GameUser;

public class SummonCommand extends Command {

	public SummonCommand() {
		// TODO
		super(new String[] { "summon" }, Rank.ADMIN, "");
		
		ArrayList<String> asd = new ArrayList<>();
		for(GameEntityType type : GameEntityType.values()) {
			asd.add(type.name());
		}
		
		addArgSuggestion(asd.toArray(new String[0]));
	}

	@Override
	public void handle(User user, String[] args) {
		if (MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
			if(args.length > 0) {
				try {
					GameEntityType type = GameEntityType.valueOf(args[0]);
					if(args.length > 1) {
						int amount = Integer.valueOf(args[1]);
						
						for(int i = 0; i<amount; i++) {
							type.getClazz().newInstance().spawn(user.getPlayer().getLocation());
						}
						
						for(User user1 : MobSlayerCore.getUserManager().getStorage()) {
							GameUser gameUser = (GameUser) user1;
							
							gameUser.updateScoreboard();
						}
					} else {
						type.getClazz().newInstance().spawn(user.getPlayer().getLocation());
					}
				} catch (Exception e) {
					user.sendTranslatedMessage("command.wrong_argument", Color.ERROR, null);
					System.out.println(e.getMessage());
				}
			} else {
				user.getPlayer().sendMessage("§7/summon <mob> <amount>");
			}
		} else {
			user.sendTranslatedMessage("command.only_in_game", Color.ERROR, null);
		}
	}
}
