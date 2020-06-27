package com.rex2go.mobslayer_core.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer {

	public static String serialize(Location location) {
		if (location != null) {
			String world = location.getWorld() != null ? location.getWorld().getName() : "";
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();
			float yaw = location.getYaw();
			float pitch = location.getPitch();

			return "[" + world + "," + x + "," + y + "," + z + "," + yaw + "," + pitch + "]";
		}
		return null;
	}

	public static Location deserialize(String serialized) {
		if (!serialized.equals("") && serialized != null) {
			serialized = serialized.substring(1, serialized.length() - 1);
			String[] elements = serialized.split(",");
			String world = elements[0];
			double x = Double.parseDouble(elements[1]);
			double y = Double.parseDouble(elements[2]);
			double z = Double.parseDouble(elements[3]);
			float yaw = Float.parseFloat(elements[4]);
			float pitch = Float.parseFloat(elements[5]);

			return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
		}
		return null;
	}
}
