package com.rex2go.mobslayer_core.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class Particle {

	EnumParticle particletype;
	boolean longdistance;
	Location location;
	double offsetx;
	double offsety;
	double offsetz;
	double speed;
	int amount;
	int data;
	
	public Particle(EnumParticle particletype, Location location, boolean longdistance, double offsetx, double offsety, double offsetz, double speed, int amount) {
		this.particletype = particletype;
		this.location = location;
		this.longdistance = longdistance;
		this.offsetx = offsetx;
		this.offsety = offsety;
		this.offsetz = offsetz;
		this.speed = speed;
		this.amount = amount;
	}

	public void sendAll() {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particletype, this.longdistance, (float)this.location.getX(), (float)this.location.getY(), (float)this.location.getZ(), (float)this.offsetx, (float)this.offsety, (float)this.offsetz, (float)this.speed, this.amount);
		for(Player all : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)all).getHandle().playerConnection.sendPacket(packet);
		}
	}
	
	public void sendPlayer(Player player) {
		PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(this.particletype, this.longdistance, (float)this.location.getX(), (float)this.location.getY(), (float)this.location.getZ(), (float)this.offsetx, (float)this.offsety, (float)this.offsetz, (float)this.speed, this.amount);
		((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
		}
}
