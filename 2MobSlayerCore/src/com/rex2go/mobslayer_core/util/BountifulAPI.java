package com.rex2go.mobslayer_core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.rex2go.mobslayer_core.MobSlayerCore;

//
//BountifulAPI by ConnorLinfoot https://www.spigotmc.org/resources/bountifulapi-1-8-1-9.1394/
//

public class BountifulAPI {
	@Deprecated
	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message) {
		sendTitle(player, fadeIn, stay, fadeOut, message, null);
	}

	@Deprecated
	public static void sendSubtitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String message) {
		sendTitle(player, fadeIn, stay, fadeOut, null, message);
	}

	@Deprecated
	public static void sendFullTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title,
			String subtitle) {
		sendTitle(player, fadeIn, stay, fadeOut, title, subtitle);
	}

	@Deprecated
	public static Integer getPlayerProtocol(Player player) {
		return Integer.valueOf(47);
	}

	public static void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", new Class[] { getNMSClass("Packet") })
					.invoke(playerConnection, new Object[] { packet });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Class<?> getNMSClass(String name) {
		String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		try {
			return Class.forName("net.minecraft.server." + version + "." + name);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title,
			String subtitle) {
		try {
			if (title != null) {
				title = ChatColor.translateAlternateColorCodes('&', title);
				title = title.replaceAll("%player%", player.getDisplayName());

				Object e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES")
						.get((Object) null);
				Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class })
						.invoke((Object) null, new Object[] { "{\"text\":\"" + title + "\"}" });
				Constructor subtitleConstructor = getNMSClass("PacketPlayOutTitle")
						.getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
								getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
				Object titlePacket = subtitleConstructor
						.newInstance(new Object[] { e, chatTitle, fadeIn, stay, fadeOut });
				sendPacket(player, titlePacket);

				e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get((Object) null);
				chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class })
						.invoke((Object) null, new Object[] { "{\"text\":\"" + title + "\"}" });
				subtitleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] {
						getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent") });
				titlePacket = subtitleConstructor.newInstance(new Object[] { e, chatTitle });
				sendPacket(player, titlePacket);
			}
			if (subtitle != null) {
				subtitle = ChatColor.translateAlternateColorCodes('&', subtitle);
				subtitle = subtitle.replaceAll("%player%", player.getDisplayName());

				Object e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES")
						.get((Object) null);
				Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class })
						.invoke((Object) null, new Object[] { "{\"text\":\"" + title + "\"}" });
				Constructor subtitleConstructor = getNMSClass("PacketPlayOutTitle")
						.getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
								getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
				Object subtitlePacket = subtitleConstructor
						.newInstance(new Object[] { e, chatSubtitle, fadeIn, stay, fadeOut });
				sendPacket(player, subtitlePacket);

				e = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get((Object) null);
				chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
						.getMethod("a", new Class[] { String.class })
						.invoke((Object) null, new Object[] { "{\"text\":\"" + subtitle + "\"}" });
				subtitleConstructor = getNMSClass("PacketPlayOutTitle")
						.getConstructor(new Class[] { getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
								getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE });
				subtitlePacket = subtitleConstructor
						.newInstance(new Object[] { e, chatSubtitle, fadeIn, stay, fadeOut });
				sendPacket(player, subtitlePacket);
			}
		} catch (Exception var11) {
			var11.printStackTrace();
		}
	}

	public static void clearTitle(Player player) {
		sendTitle(player, Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), "", "");
	}

	public static void sendTabTitle(Player player, String header, String footer) {
		if (header == null) {
			header = "";
		}
		header = ChatColor.translateAlternateColorCodes('&', header);
		if (footer == null) {
			footer = "";
		}
		footer = ChatColor.translateAlternateColorCodes('&', footer);

		header = header.replaceAll("%player%", player.getDisplayName());
		footer = footer.replaceAll("%player%", player.getDisplayName());
		try {
			Object tabHeader = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
					.getMethod("a", new Class[] { String.class })
					.invoke(null, new Object[] { "{\"text\":\"" + header + "\"}" });
			Object tabFooter = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
					.getMethod("a", new Class[] { String.class })
					.invoke(null, new Object[] { "{\"text\":\"" + footer + "\"}" });
			Constructor<?> titleConstructor = getNMSClass("PacketPlayOutPlayerListHeaderFooter")
					.getConstructor(new Class[] { getNMSClass("IChatBaseComponent") });
			Object packet = titleConstructor.newInstance(new Object[] { tabHeader });
			Field field = packet.getClass().getDeclaredField("b");
			field.setAccessible(true);
			field.set(packet, tabFooter);
			sendPacket(player, packet);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void sendActionBar(Player player, String message) {
		String nmsver = Bukkit.getServer().getClass().getPackage().getName();
		nmsver = nmsver.substring(nmsver.lastIndexOf(".") + 1);
		try {
			Class<?> c1 = Class.forName("org.bukkit.craftbukkit." + nmsver + ".entity.CraftPlayer");
			Object p = c1.cast(player);

			Class<?> c4 = Class.forName("net.minecraft.server." + nmsver + ".PacketPlayOutChat");
			Class<?> c5 = Class.forName("net.minecraft.server." + nmsver + ".Packet");
			Object ppoc;
			if (((nmsver.equalsIgnoreCase("v1_8_R1")) || (!nmsver.startsWith("v1_8_")))
					&& (!nmsver.startsWith("v1_10_"))) {
				Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatSerializer");
				Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
				Method m3 = c2.getDeclaredMethod("a", new Class[] { String.class });
				Object cbc = c3.cast(m3.invoke(c2, new Object[] { "{\"text\": \"" + message + "\"}" }));
				ppoc = c4.getConstructor(new Class[] { c3, Byte.TYPE })
						.newInstance(new Object[] { cbc, Byte.valueOf((byte) 2) });
			} else {
				Class<?> c2 = Class.forName("net.minecraft.server." + nmsver + ".ChatComponentText");
				Class<?> c3 = Class.forName("net.minecraft.server." + nmsver + ".IChatBaseComponent");
				Object o = c2.getConstructor(new Class[] { String.class }).newInstance(new Object[] { message });
				ppoc = c4.getConstructor(new Class[] { c3, Byte.TYPE })
						.newInstance(new Object[] { o, Byte.valueOf((byte) 2) });
			}
			Method m1 = c1.getDeclaredMethod("getHandle", new Class[0]);
			Object h = m1.invoke(p, new Object[0]);
			Field f1 = h.getClass().getDeclaredField("playerConnection");
			Object pc = f1.get(h);
			Method m5 = pc.getClass().getDeclaredMethod("sendPacket", new Class[] { c5 });
			m5.invoke(pc, new Object[] { ppoc });
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void sendActionBar(final Player player, final String message, int duration) {
		sendActionBar(player, message);
		if (duration >= 0) {
			new BukkitRunnable() {
				public void run() {
					BountifulAPI.sendActionBar(player, "");
				}
			}.runTaskLater(MobSlayerCore.getInstance(), duration + 1);
		}
		while (duration > 60) {
			duration -= 60;
			int sched = duration % 60;
			new BukkitRunnable() {
				public void run() {
					BountifulAPI.sendActionBar(player, message);
				}
			}.runTaskLater(MobSlayerCore.getInstance(), sched);
		}
	}

	public static void sendActionBarToAllPlayers(String message) {
		sendActionBarToAllPlayers(message, -1);
	}

	public static void sendActionBarToAllPlayers(String message, int duration) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			sendActionBar(p, message, duration);
		}
	}
}