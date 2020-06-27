package com.rex2go.mobslayer_game.user;

import org.bukkit.Color;

import com.rex2go.mobslayer_core.user.Title;

public class ArmorColor {

	Color color;
	Title title;
	
	public ArmorColor(Color color, Title title) {
		this.color = color;
		this.title = title;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Title getTitle() {
		return title;
	}
	
	public void setTitle(Title title) {
		this.title = title;
	}
}
