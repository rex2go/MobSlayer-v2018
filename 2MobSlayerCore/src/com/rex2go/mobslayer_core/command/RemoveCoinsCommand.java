package com.rex2go.mobslayer_core.command;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;

public class RemoveCoinsCommand extends Command {

	public RemoveCoinsCommand() {
		super(new String[] { "removecoins" }, Rank.ADMIN, "");
		addArgSuggestion(new String[] { "%player%" });
	}

	@Override
	public void handle(User user, String[] args) {
		if(args.length > 0) {
			@SuppressWarnings("deprecation")
			User user1 = MobSlayerCore.getUserManager().loadUser(args[0]);
			
			if(user1 != null) {
				if(args.length > 1) {
					try {
						int coins = Integer.parseInt(args[1]);
						
						if(user1.getCoins() >= coins) {
							user1.setCoins(user1.getCoins() - coins);
						} else {
							coins = 0;
							user1.setCoins(coins);
						}
						
						user.getPlayer().sendMessage("§e" + Translation.getTranslation("command.removecoins.removed", 
								user.getLanguage(), user1.getPlayer() == null ? args[0] : user1.getPlayer().getName(), coins));
						user.updateScoreboard();
						
						if(user1.getPlayer() != null) {
							user1.getPlayer().sendMessage("§e" + Translation.getTranslation("command.removecoins.received_remove", user1.getLanguage(), coins));
							user1.updateScoreboard();
						} else {
							MobSlayerCore.getUserManager().saveUser(user1);
						}
					} catch (Exception e) {
						user.getPlayer().sendMessage(Color.ERROR + Translation.getTranslation("command.wrong_argument", user.getLanguage())  + ": " + e.getMessage());
					}
				} else {
					user.sendTranslatedMessage("command.wrong_argument", Color.ERROR, null);
				}
			} else {
				user.sendTranslatedMessage("command.promote.user_not_existing", "§4", null);
			}
		} else {
			user.getPlayer().sendMessage("§7/removecoins <name> <coins>");
		}
	}
}
