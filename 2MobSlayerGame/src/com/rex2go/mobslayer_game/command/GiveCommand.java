package com.rex2go.mobslayer_game.command;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.manager.GameManager.GameState;

public class GiveCommand extends Command {

	public GiveCommand() {
		super(new String[] { "give" }, Rank.MODERATOR, "");
		
		addArgSuggestion(new String[] {});
		addArgSuggestion(new String[] {"1", "2", "4", "8", "16", "32", "64"});
		addArgSuggestion(new String[] {"%player%"});
	}

	@Override
	public void handle(User user, String[] args) {
		if (MobSlayerGame.getGameManager().getGameState() == GameState.INGAME) {
			if (args.length == 0) {
				int size = 9;

				while (MobSlayerGame.getGameItemManager().getStorage().size() > size) {
					size += 9;
				}

				Inventory inv = Bukkit.createInventory(user.getPlayer(), size);

				MobSlayerGame.getGameItemManager().getStorage().forEach((key, value) -> {
					inv.addItem(value.toItemStack(user));
				});

				user.getPlayer().openInventory(inv);
			} else {
				try {
					int id = Integer.parseInt(args[0]);
					GameItem gameItem = MobSlayerGame.getGameItemManager().getStorage().get(id);

					if (gameItem != null) {
						int amount = 1;
						Player target = user.getPlayer();

						if (args.length > 1) {
							amount = Integer.valueOf(args[1]);
							
							if(args.length > 2) {
								for(Player all : Bukkit.getOnlinePlayers()) {
									if(all.getName().equalsIgnoreCase(args[2])) {
										target = all;
										break;
									}
								}
							}
						}

						for (int i = 0; i < amount; i++) {
							target.getInventory().addItem(gameItem.toItemStack(user));
						}
					} else {
						user.sendTranslatedMessage("command.give.no_item_with_id", Color.ERROR, null);
					}
				} catch (Exception e) {
					user.sendTranslatedMessage("command.wrong_argument", Color.ERROR, null);
				}
			}
		} else {
			user.sendTranslatedMessage("command.only_in_game", Color.ERROR, null);
		}
	}
}
