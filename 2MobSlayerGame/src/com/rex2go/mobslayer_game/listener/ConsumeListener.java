package com.rex2go.mobslayer_game.listener;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.util.ParticleUtil;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.EnumParticle;

public class ConsumeListener implements Listener {

	public ConsumeListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onItemConsume(PlayerItemConsumeEvent event) {
		Player player = event.getPlayer();
		GameUser user = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());
		
		if(user.isDead()) {
			event.setCancelled(true);
		} else {
			if(event.getItem() != null) {
				ItemStack itemStack = event.getItem();
				if(MobSlayerGame.getGameItemManager().isGameItem(itemStack)) {
					GameItem gameItem = GameItem.fromItemStack(itemStack);
					event.setCancelled(true);
					
					if(gameItem.getGameItemId() == Item.EXTRA_HEART.getGameItemId()) {
						ParticleUtil.circle(EnumParticle.HEART, player.getEyeLocation(), 5, 0.5F);
						player.setMaxHealth(player.getMaxHealth()+2);
						player.getLocation().getWorld().playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 1, 1);
						
						new BukkitRunnable() {
							
							@Override
							public void run() {
								if(itemStack.getAmount() > 1) {
									itemStack.setAmount(itemStack.getAmount()-1);
								} else {
									player.setItemInHand(new ItemStack(Material.AIR));
								}
								
							}
						}.runTaskLater(MobSlayerCore.getInstance(), 1);
					}
					
					if(gameItem.getGameItemId() == Item.SPEED.getGameItemId()) {
						ParticleUtil.circle(EnumParticle.SNOWBALL, player.getEyeLocation(), 5, 0.5F);
						player.setWalkSpeed((float) (player.getWalkSpeed()+0.05));
						player.getLocation().getWorld().playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 1, 1);
						
						new BukkitRunnable() {
							
							@Override
							public void run() {
								if(itemStack.getAmount() > 1) {
									itemStack.setAmount(itemStack.getAmount()-1);
								} else {
									player.setItemInHand(new ItemStack(Material.AIR));
								}
								
							}
						}.runTaskLater(MobSlayerCore.getInstance(), 1);
					}
				}
			}
		}
	}
}
