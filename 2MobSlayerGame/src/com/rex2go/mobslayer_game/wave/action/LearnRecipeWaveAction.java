package com.rex2go.mobslayer_game.wave.action;

import java.util.ArrayList;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.crafting.Craft;
import com.rex2go.mobslayer_game.user.GameUser;
import com.rex2go.mobslayer_game.wave.SpawnRequest.TriggerType;

public class LearnRecipeWaveAction extends WaveAction {

	public LearnRecipeWaveAction(int time, int count, int delay, TriggerType triggerType, ArrayList<Object> args) {
		super("LEARN_RECIPE", time, count, delay, triggerType, args);
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
		
		Craft craft = Craft.valueOf((String) args.get(0));
		
		for(User user : MobSlayerCore.getUserManager().getStorage()) {
			GameUser gameUser = (GameUser) user;
			
			gameUser.learnCraft(craft);
		}
		
		setActive(false);
	}
}
