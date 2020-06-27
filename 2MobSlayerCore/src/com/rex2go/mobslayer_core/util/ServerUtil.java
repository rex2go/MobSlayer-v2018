package com.rex2go.mobslayer_core.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.entity.Player;

import com.rex2go.mobslayer_core.MobSlayerCore;

public class ServerUtil {

	public static void connect(Player player, String servername){
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(stream);
		
		try {
			out.writeUTF("Connect");
			out.writeUTF(servername);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		player.sendPluginMessage(MobSlayerCore.getInstance(), "BungeeCord", stream.toByteArray());
	}
}
