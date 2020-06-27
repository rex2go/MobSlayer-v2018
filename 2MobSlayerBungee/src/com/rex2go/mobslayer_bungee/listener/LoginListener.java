package com.rex2go.mobslayer_bungee.listener;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.rex2go.mobslayer_bungee.MobSlayerBungee;
import com.rex2go.mobslayer_bungee.util.MySQL;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class LoginListener implements Listener {

	public LoginListener() {
		ProxyServer.getInstance().getPluginManager().registerListener(MobSlayerBungee.getInstance(), this);
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onLogin(LoginEvent e) {
		ResultSet rs;
		rs = MySQL.query("SELECT * FROM mobslayer_alpha_access WHERE uuid = '" + e.getConnection().getUUID() + "'");
		
		try {
			if(rs.next()) {
				if(!rs.getString("username").equals(e.getConnection().getName())) {
					MySQL.update("UPDATE mobslayer_alpha_access SET username = '" + e.getConnection().getName() + "' WHERE uuid = '" +  e.getConnection().getUUID() + "'");
				}
				return;
			} else {
				rs = MySQL.query("SELECT * FROM mobslayer_alpha_access WHERE username = '" + e.getConnection().getName() + "'");
				
				if(rs.next()) {
					MySQL.update("UPDATE mobslayer_alpha_access SET uuid = '" + e.getConnection().getUUID() + "' WHERE username = '" +  e.getConnection().getName() + "'");
					return;
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		e.setCancelled(true);
		e.setCancelReason("§cDer Server befindet sich derzeit \nin der closed Alpha. "
				+ "\n\n§6Nützliche Links:\n§7Alphakey einlösen: §ehttps://mobslayergame.com/alpha-access \n§7Alphakey bekommen: §ehttps://mobslayergame.com/apply");
	}
}
