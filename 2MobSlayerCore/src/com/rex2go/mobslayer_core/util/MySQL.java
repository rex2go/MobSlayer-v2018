package com.rex2go.mobslayer_core.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import com.rex2go.mobslayer_core.MobSlayerCore;

public class MySQL {
	
	private static String HOST = "";
	private static String DATABASE = "";
	private static String USER = "";
	private static String PASSWORD = "";
	
	public static Connection con;
	
	public MySQL(String host, String database, String user, String password) {
		MySQL.HOST = host;
		MySQL.DATABASE = database;
		MySQL.USER = user;
		MySQL.PASSWORD = password;
		
		try {
			connect();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void connect() throws ClassNotFoundException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://"
					+ HOST + ":" + "3306" + "/" + DATABASE, USER, PASSWORD);
		} catch (SQLException e) {
			System.out.println("jdbc:mysql://" + HOST + ":3306/" + DATABASE + "?autoreconnect=true" + USER + PASSWORD + e.getMessage());
		}
	}
	
	public static void close() {
		try {
			if(con != null) {
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void update(String qry) {
		
		try {
			Statement st = con.createStatement();
			st.executeUpdate(qry);
			st.close();
		} catch (SQLException e) {
			try {
				connect();
				MobSlayerCore.getInstance().getLogger().log(Level.WARNING, "MySQL connection lost but reconnected successfully.");
				try {
					Statement st = con.createStatement();
					st.executeUpdate(qry);
					st.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public static ResultSet query(String qry) {
		ResultSet rs = null;
		try {
			Statement st = con.createStatement();
			rs = st.executeQuery(qry);
		} catch (SQLException e) {
			try {
				connect();
				try {
					Statement st = con.createStatement();
					rs = st.executeQuery(qry);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		return rs;
	}
}
