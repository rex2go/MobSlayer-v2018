package com.rex2go.mobslayer_core.command;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;

public class CoinsCommand extends Command {

	public CoinsCommand() {
		super(new String[] { "coins" }, Rank.USER, "");
		addArgSuggestion(new String[] { "%player%" });
	}

	@Override
	public void handle(User user, String[] args) {
		if(args.length > 0) {
			@SuppressWarnings("deprecation")
			User user1 = MobSlayerCore.getUserManager().loadUser(args[0]);
			int coins = user1 == null ? 0 : user1.getCoins();
			
			user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.coins.coins_of", user.getLanguage(), 
					user1 == null ? "§9" + args[0] : user1.getRank().getRankColor() + (user1.getPlayer() == null ? args[0] : user1.getPlayer().getName())) + "§7 » §f" + coins);
		} else {
			user.getPlayer().sendMessage("§6» §7" + Translation.getTranslation("command.coins.coins", user.getLanguage()) + "§7 » §f" + user.getCoins());
		}
	}
}
