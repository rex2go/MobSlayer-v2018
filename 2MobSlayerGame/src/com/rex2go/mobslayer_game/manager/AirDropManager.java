package com.rex2go.mobslayer_game.manager;

import java.util.ArrayList;

import org.bukkit.Location;

import com.rex2go.mobslayer_core.manager.Manager;
import com.rex2go.mobslayer_game.misc.AirDrop;

public class AirDropManager extends Manager {

	ArrayList<AirDrop> airDrops = new ArrayList<>();

	public boolean isAirDrop(Location loc) {
		return getAirDrop(loc) != null;
	}
	
	public AirDrop getAirDrop(Location loc) {
		for(AirDrop airDrop : airDrops) {
			if(airDrop.getBlock() != null) {
				if(airDrop.getBlock().getLocation().equals(loc.getBlock().getLocation())) {
					return airDrop;
				}
			}
		}
		return null;
	}
	
	public ArrayList<AirDrop> getStorage() {
		return airDrops;
	}

}
