package com.rex2go.mobslayer_game.wave.action;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.wave.SpawnRequest.TriggerType;

public class LightningWaveAction extends WaveAction {

	public LightningWaveAction(int time, int count, int delay, TriggerType triggerType, ArrayList<Object> args) {
		super("LIGHTNING", time, count, delay, triggerType, args);
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
		
		World world = MobSlayerGame.getMapManager().getGameMap().getWorld();
		
		try {
			double time = (double) args.get(0);
		
			new BukkitRunnable() {
				
				int i = 0;
				
				@Override
				public void run() {
					if(i < time) {
						i += 5;
						world.strikeLightningEffect(MobSlayerGame.getSectionManager().getRandomMobSpawn());
					} else {
						this.cancel();
						setActive(false);
					}
					
				}
			}.runTaskTimer(MobSlayerCore.getInstance(), 0, 5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
