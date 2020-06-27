package com.rex2go.mobslayer_game.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.rex2go.mobslayer_core.manager.Manager;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.map.Section;
import com.rex2go.mobslayer_game.user.GameUser;

public class SectionManager extends Manager {

	ArrayList<Section> activeSections = new ArrayList<>();

	public ArrayList<Section> getActiveSections() {
		return activeSections;
	}

	public void setActiveSections(ArrayList<Section> activeSections) {
		this.activeSections = activeSections;
	}

	public Location getRandomMobSpawn() {
		if (!activeSections.isEmpty()) {
			Section section = null;
			Location loc = null;
			int trys = 0;
			
			int maxDistance = 36;
			
			while(section == null) {
				Section section1 = activeSections.get(new Random().nextInt(activeSections.size()));
				Collections.shuffle(MobSlayerGame.getGameManager().alive);
				
				trys++;
				
				for(GameUser gameUser : MobSlayerGame.getGameManager().alive) {
					for(int i = 0; i<5; i++) {
						if(section1.getMobSpawns().get(i) != null) {
							double distance = section1.getMobSpawns().get(i).distance(gameUser.getPlayer().getLocation());
							
							if(distance < maxDistance) {
								section = section1;
								break;
							}
						}
					}
				}
				
				if(trys > 10) {
					System.out.println("No section found");
					section = activeSections.get(new Random().nextInt(activeSections.size()));
					break;
				}
			}
			
			trys = 0;
			
			while(loc == null) {
				Location loc1 = section.getMobSpawns().get(new Random().nextInt(section.getMobSpawns().size()-1));
				Collections.shuffle(MobSlayerGame.getGameManager().alive);
				
				trys++;
				
				for(GameUser gameUser : MobSlayerGame.getGameManager().alive) {
					double distance = loc1.distance(gameUser.getPlayer().getLocation());
					
					if(distance > 4 && distance < maxDistance) {
						boolean player = false;
						
						for(Entity entity : MobSlayerGame.getNearbyEntities(loc1, 4)) {
							if(entity instanceof Player) {
								player = true;
							}
						}
						
						if(!player) {
							loc = loc1;
						}
					}
				}
				
				if(trys > 50) {
					System.out.println("No mob spawn found");
					loc = section.getMobSpawns().get(new Random().nextInt(section.getMobSpawns().size()-1));
					break;
				}
			}
			
			return loc;
		}
		return null;
	}
}
