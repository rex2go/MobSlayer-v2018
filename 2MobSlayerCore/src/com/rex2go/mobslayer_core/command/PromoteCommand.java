package com.rex2go.mobslayer_core.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.MySQL;

public class PromoteCommand extends Command {

	public PromoteCommand() {
		// TODO
		super(new String[] { "promote" }, Rank.ADMIN, "");
		addArgSuggestion(new String[] { "%player%" });
	}

	@Override
	public void handle(User user, String[] args) {
		Player player = user.getPlayer();
		if (args.length > 0) {
			String target = args[0];
			for (Player all : Bukkit.getOnlinePlayers()) {
				if (all.getName().equalsIgnoreCase(target)) {
					User user1 = MobSlayerCore.getUserManager().getUserByName(target);
					if (user1 != null) {
						int currentRankId = user1.getRank().getRankId();
							if (currentRankId < 4) {
							user1.setRank(MobSlayerCore.getRankManager().getRankById(currentRankId + 1));
							user1.sendTranslatedMessage("command.promote.your_rank_has_been_updated", Color.RANK, null);
							user.sendTranslatedMessage("command.promote.rank_updated", Color.RANK, null);
						}
						return;
					}
				}
			}
			
			@SuppressWarnings("deprecation")
			User user1 = MobSlayerCore.getUserManager().loadUser(target);
			if (user1 != null) {
				int currentRankId = user1.getRank().getRankId();
				if (currentRankId < 4) {
					user1.setRank(MobSlayerCore.getRankManager().getRankById(currentRankId + 1));
					MySQL.update("UPDATE mobslayer_users SET rank='" + user1.getRank().getRankId() + "' WHERE username='" + target + "'");
					user.sendTranslatedMessage("command.promote.rank_updated", Color.RANK, null);
				}
				return;
			} else {
				user.sendTranslatedMessage("command.promote.user_not_existing", Color.ERROR, null);
			}
		} else {
			player.sendMessage("§7/promote <name>");
		}
	}
}
