package com.rex2go.mobslayer_game.command;

import org.bukkit.inventory.ItemStack;

import com.rex2go.mobslayer_core.Color;
import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.GameItem;

public class ItemInfoCommand extends Command {

	public ItemInfoCommand() {
		super(new String[] { "iteminfo", "ii" }, Rank.MODERATOR, "");
		
	}

	@Override
	public void handle(User user, String[] args) {
		ItemStack hand = user.getPlayer().getItemInHand();
		
		if(hand != null) {
			if(MobSlayerGame.getGameItemManager().isGameItem(hand)) {
				GameItem gameItem = GameItem.fromItemStack(hand);
				
				user.getPlayer().sendMessage("§7     --- §c" + Translation.getTranslation("command.iteminfo.title", user.getLanguage()) + " §7---");
				user.getPlayer().sendMessage("§c» §7" + Translation.getTranslation("command.iteminfo.gameitem_id", user.getLanguage()) + ": §f" + gameItem.getGameItemId());
				user.getPlayer().sendMessage("§c» §7" + Translation.getTranslation("command.iteminfo.name", user.getLanguage()) + ": §f" + Translation.getTranslation(gameItem.getTranslationPath(), 
						user.getLanguage()));
				user.getPlayer().sendMessage("§c» §7" + Translation.getTranslation("command.iteminfo.translation_path", user.getLanguage()) + ": §f" + gameItem.getTranslationPath());
				user.getPlayer().sendMessage("§c» §7" + Translation.getTranslation("command.iteminfo.worth", user.getLanguage()) + ": §f" + gameItem.getWorth());
				user.getPlayer().sendMessage("§c» §7" + Translation.getTranslation("command.iteminfo.attributes", user.getLanguage()) + ":");
				
				for(GameItem.Attribute attr : gameItem.getAttributes()) {
					user.getPlayer().sendMessage("§7- §f" + Translation.getTranslation(attr.getTranslationPath(), user.getLanguage()) + " §8(" + attr.name() + ")");
				}
			} else {
				user.sendTranslatedMessage("command.iteminfo.no_game_item", Color.ERROR, "");
			}
		} else {
			user.sendTranslatedMessage("command.iteminfo.no_item_in_hand", Color.ERROR, "");
		}
	}
}
