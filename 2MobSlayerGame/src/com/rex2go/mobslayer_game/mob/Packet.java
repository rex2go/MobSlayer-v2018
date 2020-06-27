package com.rex2go.mobslayer_game.mob;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

import com.mojang.authlib.GameProfile;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityLiving;
import net.minecraft.server.v1_8_R3.IBlockData;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockChange;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;

public class Packet {

	public static PacketPlayOutEntityDestroy destroyEntity(Entity entity) {
		return new PacketPlayOutEntityDestroy(entity.getId());
	}

	public static PacketPlayOutSpawnEntityLiving spawnEntityLiving(Entity entity, int entityId) {
		PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving((EntityLiving) entity);

		try {
			setField(packet, "a", entityId);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return packet;
	}

	public static PacketPlayOutBlockChange blockChange(Location location, Material material, byte data) {
		BlockPosition blockPosition = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
		
		@SuppressWarnings("deprecation")
		int combined = material.getId() + (data + 1 << 12);
		IBlockData iData = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined);
		
		PacketPlayOutBlockChange packet = new PacketPlayOutBlockChange(((CraftWorld) location.getWorld()).getHandle(), blockPosition);
		
		packet.block = iData;
		
		return packet;
	}
	
	public static int toFixedPoint(double d) {
		return (int) d * 32;
	}

	public static byte toPackedByte(float f) {
		return (byte) ((int) (f * 256.0F / 360.0F));
	}

	public static void setField(Object instance, String field, Object value) throws Exception {
		Field f = instance.getClass().getDeclaredField(field);
		f.setAccessible(true);
		f.set(instance, value);
	}

	public static Object getField(Object instance, String field) throws Exception {
		Field f = instance.getClass().getDeclaredField(field);
		f.setAccessible(true);
		return f.get(instance);
	}

	@SuppressWarnings("unchecked")
	public static void crash(int entityId, UUID uuid, String name, Location location) throws Exception {
		for (Field field : PacketPlayOutPlayerInfo.class.getDeclaredFields()) {
			System.out.println(field);
		}

		PacketPlayOutPlayerInfo packet1 = new PacketPlayOutPlayerInfo();
		PacketPlayOutPlayerInfo.PlayerInfoData data = packet1.new PlayerInfoData(new GameProfile(uuid, name), 1,
				EnumGamemode.NOT_SET, CraftChatMessage.fromString(name)[0]);
		List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getField(
				packet1, "b");
		players.add(data);

		setField(packet1, "a", EnumPlayerInfoAction.ADD_PLAYER);
		setField(packet1, "b", players);

		PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();

		for (Field field : PacketPlayOutNamedEntitySpawn.class.getDeclaredFields()) {
			System.out.println(field);
		}

		setField(packet, "a", entityId);
		setField(packet, "b", uuid);
		setField(packet, "c", (int) MathHelper.floor(location.getX() * 32.00D));
		setField(packet, "d", (int) MathHelper.floor(location.getY() * 32.00D));
		setField(packet, "e", (int) MathHelper.floor(location.getZ() * 32.00D));
		setField(packet, "f", (byte) ((int) (location.getYaw() * 256.0F / 360.0F)));
		setField(packet, "g", (byte) ((int) (location.getPitch() * 256.0F / 360.0F)));
		setField(packet, "h", 0);
		DataWatcher watcher = new DataWatcher(null);
		watcher.a(6, (float) 20);
		watcher.a(10, (float) 127);
		setField(packet, "i", watcher);

		for (Player all : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet1);
			((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
		}
	}
}
