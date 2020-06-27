package com.rex2go.mobslayer_game.mob;

import java.util.concurrent.ThreadLocalRandom;

import com.rex2go.mobslayer_game.item.GameItem;

public class Drop {

	GameItem itemStack;
	Rarity rarity;
	
	public Drop(GameItem itemStack, Rarity rarity) {
		this.itemStack = itemStack;
		this.rarity = rarity;
	}
	
	public GameItem getItemStack() {
		return itemStack;
	}

	public void setItemStack(GameItem itemStack) {
		this.itemStack = itemStack;
	}

	public Rarity getRarity() {
		return rarity;
	}

	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}
	
	public enum Rarity {
		STANDARD(1), FREQUENTLY(3), COMMON(6), SPECIAL(12), RARE(24), VERY_RARE(96);
		
		int max;
		
		private Rarity(int max) {
			this.max = max;
		}
		
		public int getMax() {
			return max;
		}
	}
	
	public GameItem drop() {
		int random = 0;
		
		switch (rarity) {
		
		case STANDARD:
			random = 1;
			break;
		default:
			random = ThreadLocalRandom.current().nextInt(1, rarity.getMax() + 1);
		}
		
		if(random == 1) {
			return itemStack;
		}
		
		return null;
	}
}
