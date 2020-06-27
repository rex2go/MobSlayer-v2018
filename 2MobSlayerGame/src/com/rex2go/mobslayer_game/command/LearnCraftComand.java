package com.rex2go.mobslayer_game.command;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.crafting.Craft;
import com.rex2go.mobslayer_game.user.GameUser;

public class LearnCraftComand extends Command {

	public LearnCraftComand() {
		super(new String[] { "learncraft" }, Rank.ADMIN, "");
	}

	@Override
	public void handle(User user, String[] args) {
		if(args.length > 0) {
			Craft craft = Craft.valueOf(args[0]);
			GameUser target = (GameUser) user;
			
			if(args.length > 1) {
				String name = args[1];
				
				for(User usr : MobSlayerCore.getUserManager().getStorage()) {
					if(usr.getPlayer().getName().equalsIgnoreCase(name)) {
						target = (GameUser) usr;
						break;
					}
				}
			}
			
			target.learnCraft(craft);
		}
	}
}
