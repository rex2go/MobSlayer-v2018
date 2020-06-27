package com.rex2go.mobslayer_game.wave.action;

import java.util.ArrayList;

import org.bukkit.Sound;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Translation;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.user.GameUser;
import com.rex2go.mobslayer_game.wave.SpawnRequest.TriggerType;

public class GiveItemWaveAction extends WaveAction {

	public GiveItemWaveAction(int time, int count, int delay, TriggerType triggerType, ArrayList<Object> args) {
		super("GIVE_ITEM", time, count, delay, triggerType, args);
	}

	@Override
	public void go() {
		if(args == null) {
			return;
		}
		
		if(MobSlayerGame.getMapManager().getGameMap() == null) {
			return;
		}
		
		if(MobSlayerGame.getMapManager().getGameMap().getWorld() == null) {
			return;
		}
		
		GameItem gameItem = MobSlayerGame.getGameItemManager().getStorage().get(new Double((double) args.get(0)).intValue());
		
		for(User user : MobSlayerCore.getUserManager().getStorage()) {
			GameUser gameUser = (GameUser) user;
			
			if(gameUser.isDead()) {
				gameUser.getItemQue().add(gameItem);
			} else {
				gameUser.getPlayer().getInventory().addItem(gameItem.toItemStack(gameUser));
				
				gameUser.getPlayer().playSound(gameUser.getPlayer().getLocation(), Sound.LEVEL_UP, 1, 1);
				String trans = Translation.getTranslation(gameItem.getTranslationPath(), gameUser.getLanguage());

				if(trans != null) {
                    gameUser.getPlayer().sendMessage("§7" + Translation.getTranslation("general.wave_script.obtain_item", gameUser.getLanguage()) + ": §6" +
                            Translation.getTranslation(gameItem.getTranslationPath(), gameUser.getLanguage()));
				} else {
                    gameUser.getPlayer().sendMessage("§6" + Translation.getTranslation("general.wave_script.obtain_item", gameUser.getLanguage()) + ".");
				}
			}
		}
		
		setActive(false);
	}
}
