package com.rex2go.mobslayer_game.mob;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import com.rex2go.mobslayer_game.user.GameUser;

public abstract class GameEntity {

	ArrayList<Drop> drops = new ArrayList<>();
	GameEntityType gameEntityType;
	GameUser damager;
	int poisonTime = 0;
	
	public boolean blood = true;
	
	public GameEntity(GameEntityType gameEntityType) {
		this.gameEntityType = gameEntityType;
	}
	
	public ArrayList<Drop> getDrops() {
		return drops;
	}

	public void setDrops(ArrayList<Drop> drops) {
		this.drops = drops;
	}

	public GameEntityType getGameEntityType() {
		return gameEntityType;
	}
	
	public EntityType getEntityType() {
		return gameEntityType.getEntityType();
	}
	
	public String getName() {
		return gameEntityType.getName();
	}

	public int getPoints() {
		return gameEntityType.getPoints();
	}
	
	public GameUser getDamager() {
		return damager;
	}

	public void setDamager(GameUser damager) {
		this.damager = damager;
	}
	
	public boolean showBlood() {
		return blood;
	}
	
	public void setPoison(int seconds) {
		this.poisonTime = seconds;
	}
	
	public int getPoisonTime() {
		return poisonTime;
	}
	
	public abstract Entity spawn(Location location);
	
	public abstract void attack(GameUser user, Entity attacker);
	
	public abstract void damage(Entity entity);
	
	public abstract void die(Entity entity);
	
	public abstract Entity getTarget(Entity entity);
	
	public abstract void clearTarget(Entity entity);
}
