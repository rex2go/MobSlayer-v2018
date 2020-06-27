package com.rex2go.mobslayer_game.wave.action;

import java.util.ArrayList;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.wave.SpawnRequest.TriggerType;

public class MessageWaveAction extends WaveAction {

	public MessageWaveAction(int time, int count, int delay, TriggerType triggerType, ArrayList<Object> args) {
		super("MESSAGE", time, count, delay, triggerType, args);
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
		
		try {
			String message = (String) args.get(0);
			String color = (String) args.get(1);
			String prefix = (String) args.get(2);
		
			for(User user : MobSlayerCore.getUserManager().getStorage()) {
				user.sendTranslatedMessage(message, color, prefix);
			}
			
			setActive(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
