package com.rex2go.mobslayer_game.item;

import java.util.ArrayList;

public class ShopItem {

	GameItem gameItem;
	int price;

	public ShopItem(GameItem gameItem, int price) {
		this.gameItem = gameItem;
		this.price = price;
	}

	public GameItem getGameItem() {
		return gameItem;
	}

	public void setGameItem(GameItem gameItem) {
		this.gameItem = gameItem;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getSellValue() {
		return (int) (price / 2.5);
	}

	public static ArrayList<ShopItem> getShopItems() {
		ArrayList<ShopItem> shopItems = new ArrayList<>();

		shopItems.add(new ShopItem(Item.STICK, 25));
		shopItems.add(new ShopItem(Item.FEATHER, 25));
		shopItems.add(new ShopItem(Item.STRING, 35));
		shopItems.add(new ShopItem(Item.SLIME_BALL, 50));
		shopItems.add(new ShopItem(Item.FIRE_POWDER, 50));
		shopItems.add(new ShopItem(Item.GUN_POWDER, 50));
		shopItems.add(new ShopItem(Item.SPIDER_EYE, 50));
		shopItems.add(new ShopItem(Item.GLOWSTONE_DUST, 75));
		shopItems.add(new ShopItem(Item.MAGIC_POWDER, 75));
		shopItems.add(new ShopItem(Item.IRON_INGOT, 100));
		shopItems.add(new ShopItem(Item.GOLD_INGOT, 125));
		shopItems.add(new ShopItem(Item.DIAMOND, 200));
		shopItems.add(new ShopItem(Item.CRYSTAL, 600));
		
		return shopItems;
	}
}
