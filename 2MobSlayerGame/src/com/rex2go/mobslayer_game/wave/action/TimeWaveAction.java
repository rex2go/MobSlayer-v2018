package com.rex2go.mobslayer_game.wave.action;

import java.util.ArrayList;

import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.wave.SpawnRequest.TriggerType;

public class TimeWaveAction extends WaveAction {

	public TimeWaveAction(int time, int count, int delay, TriggerType triggerType, ArrayList<Object> args) {
		super("TIME", time, count, delay, triggerType, args);
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
				
				@Override
				public void run() {
					if(!(world.getTime() > time-50 && world.getTime() < time+50)) {
						if(world.getTime() < time) {
							world.setTime(world.getTime()+100);
						} else {
							world.setTime(world.getTime()-100);
						}
					} else {
						this.cancel();
						setActive(false);
					}
					
				}
			}.runTaskTimer(MobSlayerCore.getInstance(), 0, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
