package com.rex2go.mobslayer_game.crafting;

import com.rex2go.mobslayer_game.item.GameItem;

public class CraftRecipe {

	GameItem[] resources;
	GameItem result;

	public CraftRecipe(GameItem[] resources, GameItem result) {
		this.resources = resources;
		this.result = result;
	}
	
	public GameItem[] getResources() {
		return resources;
	}

	public void setResources(GameItem[] resources) {
		this.resources = resources;
	}
	
	public GameItem getResult() {
		return result;
	}

	public void setResult(GameItem result) {
		this.result = result;
	}
}
