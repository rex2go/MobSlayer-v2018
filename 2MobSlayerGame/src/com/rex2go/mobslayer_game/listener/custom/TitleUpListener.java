package com.rex2go.mobslayer_game.listener.custom;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.meta.FireworkMeta;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.event.TitleUpEvent;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.user.GameUser;

public class TitleUpListener implements Listener {

	public TitleUpListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onTitleUp(TitleUpEvent event) {
		User user = event.getUser();
		GameUser gameUser = (GameUser) user;
		Player player = user.getPlayer();
		Location loc = player.getLocation();
		
		player.sendMessage("§a" + Translation.getTranslation("general.title.you_are_now", user.getLanguage(), Translation.getTranslation(user.getTitle().getTranslationPath(), user.getLanguage())));
		gameUser.sendProgress();
		player.playSound(loc, Sound.ORB_PICKUP, 1, 1);
		
		Firework firework = loc.getWorld().spawn(loc, Firework.class);
        FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
        data.addEffects(FireworkEffect.builder().withColor(Color.FUCHSIA).with(Type.BURST).withFade(Color.PURPLE).with(Type.BURST).withColor(Color.ORANGE).withFlicker().build());
        firework.setFireworkMeta(data);
	}
}
