package com.rex2go.mobslayer_game.wave.action;

import java.util.ArrayList;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.wave.SpawnRequest.TriggerType;

public class SpawnRequestWaveAction extends WaveAction {

	public SpawnRequestWaveAction(int time, int count, int delay, TriggerType triggerType, ArrayList<Object> args) {
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
			MobSlayerGame.getWaveManager().getActiveWave().getSpawnRequests().addAll(MobSlayerGame.getWaveManager().getSpawnRequestsFromJson(((String) args.get(0)).replaceAll("#", "\"")));
			
			for(User user : MobSlayerCore.getUserManager().getStorage()) {
				user.updateScoreboard();
			}
			
			setActive(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
