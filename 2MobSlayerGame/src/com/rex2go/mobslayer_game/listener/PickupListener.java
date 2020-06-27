package com.rex2go.mobslayer_game.listener;

import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class PickupListener implements Listener {

	public PickupListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		Player player = event.getPlayer();
		GameUser user = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
		
		if(user.isDead()) {
			event.setCancelled(true);
		} else {
			if(event.getItem() != null) {
				ItemStack itemStack = event.getItem().getItemStack();
				net.minecraft.server.v1_8_R3.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
				NBTTagCompound tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
				
				if(tag.hasKey("noPickup")) {
					event.setCancelled(true);
					return;
				}
				
				if(MobSlayerGame.getGameItemManager().isGameItem(itemStack)) {
					event.getItem().setItemStack(GameItem.translate(itemStack, user));
				}
			}
		}
	}
}
