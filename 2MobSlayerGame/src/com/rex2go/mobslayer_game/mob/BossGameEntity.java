package com.rex2go.mobslayer_game.mob;

import org.bukkit.entity.Entity;

public abstract class BossGameEntity extends GameEntity {

	protected String color;
	protected String[] dialogue;
	
	public BossGameEntity(GameEntityType gameEntityType, String color, String[] dialogue) {
		super(gameEntityType);
		this.color = color;
		this.dialogue = dialogue;
	}
	
	public abstract void setupBossBar(Entity entity);
	
	public abstract void updateBossBar(Entity entity);

}
