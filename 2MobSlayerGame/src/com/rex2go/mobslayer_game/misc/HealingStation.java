package com.rex2go.mobslayer_game.misc;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.util.Particle;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.user.GameUser;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;

public class HealingStation {

	protected static ArrayList<HealingStation> healingStations = new ArrayList<>();

	GameUser owner;
	Location location;
	int state = 3;

	public HealingStation(GameUser owner, Location location) {
		this.owner = owner;
		this.location = location;

		healingStations.add(this);
	}

	public void heal() {
		Particle particle = new Particle(EnumParticle.FIREWORKS_SPARK, location, true, 2, 2, 2, 0.01, 10);
		particle.sendAll();

		for (Entity entity : MobSlayerGame.getNearbyEntities(location, 6)) {
			if (entity instanceof Player) {
				Player player = (Player) entity;
				GameUser gameUser2 = (GameUser) MobSlayerCore.getUserManager().getUserByUUID(player.getUniqueId());

				if (player.getHealth() < player.getMaxHealth()) {
					gameUser2.heal(1, true);
				}
			}
		}
	}

	public static void healAll() {
		for (HealingStation healingStation : healingStations) {
			healingStation.heal();
		}
	}

	public void sendParticle() {	
		if(getState() != 3) {
			int a = 9 - getState() * 3 - 1;
	
			if (a > 0) {
				Block block = getLocation().getBlock();
				PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(0,
						new BlockPosition(block.getX(), block.getY(), block.getZ()), a);
	
				for (Player all : Bukkit.getOnlinePlayers()) {
					((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
				}
			}
		}
	}

	public static void sendParticleAll() {
		for (HealingStation healingStation : healingStations) {
			healingStation.sendParticle();
		}
	}

	public static void changeStateAll() {
		ArrayList<HealingStation> remove = new ArrayList<>();

		for (HealingStation healingStation : healingStations) {
			if (healingStation.setState(healingStation.getState() - 1)) {
				remove.add(healingStation);
			}
		}

		for (HealingStation healingStation : remove) {
			healingStations.remove(healingStation);
		}

		remove.clear();
	}

	public GameUser getOwner() {
		return owner;
	}

	public void setOwner(GameUser owner) {
		this.owner = owner;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public int getState() {
		return state;
	}

	@Deprecated
	public boolean setState(int state) {
		this.state = state;

		for (Player all : Bukkit.getOnlinePlayers()) {
			all.playEffect(getLocation(), Effect.STEP_SOUND, getLocation().getBlock().getTypeId());
		}

		if (state == 0) {
			location.getBlock().setType(Material.AIR);
			return true;
		} else {
			sendParticle();
		}
		return false;
	}
	
	public static ArrayList<HealingStation> getHealingStations() {
		return healingStations;
	}
}
