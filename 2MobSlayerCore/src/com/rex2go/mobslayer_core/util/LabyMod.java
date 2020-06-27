package com.rex2go.mobslayer_core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.server.v1_8_R3.PacketDataSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutCustomPayload;

public class LabyMod {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void disableFeature(Player player, LabyModFeature feature) throws IOException {
		HashMap<String, Boolean> list = new HashMap();
		list.put(feature.name(), false);
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(list);
		ByteBuf a = Unpooled.copiedBuffer(byteOut.toByteArray());
		PacketDataSerializer b = new PacketDataSerializer(a);
		PacketPlayOutCustomPayload packet = new PacketPlayOutCustomPayload("LABYMOD", b);
		((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
	}
	
	public static enum LabyModFeature {
		FOOD, GUI, NICK, BLOCKBUILD, CHAT, EXTRAS, ANIMATIONS, POTIONS, ARMOR, DAMAGEINDICATOR, MINIMAP_RADAR;
	}
}
