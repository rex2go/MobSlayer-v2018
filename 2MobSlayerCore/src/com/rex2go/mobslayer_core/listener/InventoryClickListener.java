package com.rex2go.mobslayer_core.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.ItemUtil;
import com.rex2go.mobslayer_core.util.Translation;

public class InventoryClickListener implements Listener {

	public InventoryClickListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		User user = MobSlayerCore.getUserManager().loadUser(player);

		if (user != null) {
			if (event.getClick() != null) {
				if (event.getClickedInventory() != null) {
					if (event.getCurrentItem() != null) {
						if (event.getClickedInventory().getName() != null) {

							// Settings Menü
							if (event.getClickedInventory().getName().equals(Translation.getTranslation("general.settings", user.getLanguage()))) {
								event.setCancelled(true);
								
								ItemStack itemStack = event.getCurrentItem();
								
								if(itemStack.getType() == Material.INK_SACK) {
									if (itemStack.getItemMeta() != null) {
										if (itemStack.getItemMeta().getDisplayName() != null) {
											
											// Auto join
											if (itemStack.getItemMeta().getDisplayName().equals("§f" 
													+ Translation.getTranslation("general.settings.auto_join", user.getLanguage()))) {
												if(itemStack.getDurability() == (short) 10) {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 8);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.auto_join", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§8<" + Translation.getTranslation("general.settings.disabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setEnableAutoJoin(false);
												} else {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 10);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.auto_join", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§a<" + Translation.getTranslation("general.settings.enabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setEnableAutoJoin(true);
												}
											}
											
											// Block Bad Words
											if (itemStack.getItemMeta().getDisplayName().equals("§f" 
													+ Translation.getTranslation("general.settings.block_bad_words", user.getLanguage()))) {
												if(itemStack.getDurability() == (short) 10) {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 8);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.block_bad_words", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§8<" + Translation.getTranslation("general.settings.disabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setBlockBadWords(false);
												} else {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 10);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.block_bad_words", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§a<" + Translation.getTranslation("general.settings.enabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setBlockBadWords(true);
												}
											}
											
											// Show Blood
											if (itemStack.getItemMeta().getDisplayName().equals("§f" 
													+ Translation.getTranslation("general.settings.show_blood", user.getLanguage()))) {
												if(itemStack.getDurability() == (short) 10) {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 8);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.show_blood", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§8<" + Translation.getTranslation("general.settings.disabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setShowBlood(false);
												} else {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 10);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.show_blood", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§a<" + Translation.getTranslation("general.settings.enabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setShowBlood(true);
												}
											}
											
											// Show Scoreboard
											if (itemStack.getItemMeta().getDisplayName().equals("§f" 
													+ Translation.getTranslation("general.settings.enable_scoreboard", user.getLanguage()))) {
												if(itemStack.getDurability() == (short) 10) {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 8);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.enable_scoreboard", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§8<" + Translation.getTranslation("general.settings.disabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setEnableScoreboard(false);
													user.updateScoreboard();
												} else {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 10);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.enable_scoreboard", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§a<" + Translation.getTranslation("general.settings.enabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setEnableScoreboard(true);
													user.updateScoreboard();
												}
											}
											
											// Att Notification
											if (itemStack.getItemMeta().getDisplayName().equals("§f" 
													+ Translation.getTranslation("general.settings.att_notification", user.getLanguage()))) {
												if(itemStack.getDurability() == (short) 10) {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 8);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.att_notification", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§8<" + Translation.getTranslation("general.settings.disabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setAttNotification(false);
													user.updateScoreboard();
												} else {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 10);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.att_notification", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§a<" + Translation.getTranslation("general.settings.enabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setAttNotification(true);
													user.updateScoreboard();
												}
											}
											
											// Simple Crafting
											if (itemStack.getItemMeta().getDisplayName().equals("§f" 
													+ Translation.getTranslation("general.settings.simple_crafting", user.getLanguage()))) {
												if(itemStack.getDurability() == (short) 10) {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 8);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.simple_crafting", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§8<" + Translation.getTranslation("general.settings.disabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setUseSimpleCrafting(false);
													user.updateScoreboard();
												} else {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 10);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.simple_crafting", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§a<" + Translation.getTranslation("general.settings.enabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setUseSimpleCrafting(true);
													user.updateScoreboard();
												}
											}
											
											// Auto Respawn
											if (itemStack.getItemMeta().getDisplayName().equals("§f" 
													+ Translation.getTranslation("general.settings.auto_respawn", user.getLanguage()))) {
												if(itemStack.getDurability() == (short) 10) {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 8);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.auto_respawn", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§8<" + Translation.getTranslation("general.settings.disabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setAutoRespawn(false);
													user.updateScoreboard();
												} else {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 10);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.auto_respawn", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§a<" + Translation.getTranslation("general.settings.enabled", user.getLanguage()) + ">");
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setAutoRespawn(true);
													user.updateScoreboard();
												}
											}
											
											// Show Stars
											if (itemStack.getItemMeta().getDisplayName().equals("§f" 
													+ Translation.getTranslation("general.settings.show_stars", user.getLanguage()))) {
												if(itemStack.getDurability() == (short) 10) {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 8);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.show_stars", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§8<" + Translation.getTranslation("general.settings.disabled", user.getLanguage()) + ">");
													
													itemStack2 = ItemUtil.addLore(itemStack2, "§7");
													itemStack2 = ItemUtil.addLore(itemStack2, "§7✯  §7= 1 " + Translation.getTranslation("general.win", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§6✯  §7= 5 " + Translation.getTranslation("general.wins", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§b✯  §7= 25 " + Translation.getTranslation("general.wins", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§f✯  §7= 125 " + Translation.getTranslation("general.wins", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§d✯  §7= 625 " + Translation.getTranslation("general.wins", user.getLanguage()));
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setShowStars(false);
													user.updateScoreboard();
												} else {
													
													ItemStack itemStack2 = new ItemStack(Material.INK_SACK, 1, (short) 10);
													itemStack2 = ItemUtil.setDisplayname(itemStack2, "§f" + Translation.getTranslation("general.settings.show_stars", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§a<" + Translation.getTranslation("general.settings.enabled", user.getLanguage()) + ">");
													
													itemStack2 = ItemUtil.addLore(itemStack2, "§7");
													itemStack2 = ItemUtil.addLore(itemStack2, "§7✯  §7= 1 " + Translation.getTranslation("general.win", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§6✯  §7= 5 " + Translation.getTranslation("general.wins", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§b✯  §7= 25 " + Translation.getTranslation("general.wins", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§f✯  §7= 125 " + Translation.getTranslation("general.wins", user.getLanguage()));
													itemStack2 = ItemUtil.addLore(itemStack2, "§d✯  §7= 625 " + Translation.getTranslation("general.wins", user.getLanguage()));
													
													event.getClickedInventory().setItem(event.getSlot(), itemStack2);
													
													user.getSettings().setShowStars(true);
													user.updateScoreboard();
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
