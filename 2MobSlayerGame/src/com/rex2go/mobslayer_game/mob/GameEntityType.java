package com.rex2go.mobslayer_game.mob;

import org.bukkit.entity.EntityType;

import com.rex2go.mobslayer_game.mob.entity.MobChicken;
import com.rex2go.mobslayer_game.mob.entity.blaze.MobBlaze;
import com.rex2go.mobslayer_game.mob.entity.boss.MagmaKing;
import com.rex2go.mobslayer_game.mob.entity.cave_spider.MobCaveSpider;
import com.rex2go.mobslayer_game.mob.entity.creeper.MobCreeper;
import com.rex2go.mobslayer_game.mob.entity.iron_golem.MobIronGolem;
import com.rex2go.mobslayer_game.mob.entity.pig_zombie.MobPigZombieTNTMonster;
import com.rex2go.mobslayer_game.mob.entity.pig_zombie.MobPigZombieTank;
import com.rex2go.mobslayer_game.mob.entity.skeleton.MobSkeletonArcher;
import com.rex2go.mobslayer_game.mob.entity.skeleton.MobSkeletonArcherFast;
import com.rex2go.mobslayer_game.mob.entity.skeleton.MobSkeletonEasy;
import com.rex2go.mobslayer_game.mob.entity.skeleton.MobSkeletonMedium;
import com.rex2go.mobslayer_game.mob.entity.slime.MobSlime;
import com.rex2go.mobslayer_game.mob.entity.spider.MobSpider;
import com.rex2go.mobslayer_game.mob.entity.witch.MobWitch;
import com.rex2go.mobslayer_game.mob.entity.zombie.MobZombieEasy;
import com.rex2go.mobslayer_game.mob.entity.zombie.MobZombieFire;
import com.rex2go.mobslayer_game.mob.entity.zombie.MobZombieMedium;

public enum GameEntityType {

	ZOMBIE_EASY(EntityType.ZOMBIE, 5, MobZombieEasy.class, false, 1), 
	ZOMBIE_MEDIUM(EntityType.ZOMBIE, 10, MobZombieMedium.class, false, 2),
	ZOMBIE_FIRE(EntityType.ZOMBIE, 15, MobZombieFire.class, false, 3),
	
	PIG_ZOMBIE_TANK(EntityType.PIG_ZOMBIE, 25, MobPigZombieTank.class, false, 4),
	PIG_ZOMBIE_CHICKEN(EntityType.PIG_ZOMBIE, 0, MobChicken.class, false, 5),
	
	SLIME(EntityType.SLIME, 15, MobSlime.class, false, 6),
	
	WITCH(EntityType.WITCH, 17, MobWitch.class, false, 7),
	
	SPIDER(EntityType.SPIDER, 7, MobSpider.class, false, 8),
	
	CAVE_SPIDER(EntityType.CAVE_SPIDER, 20, MobCaveSpider.class, false, 9),
	
	CREEPER(EntityType.CREEPER, 12, MobCreeper.class, false, 10),
	
	SKELETON_ARCHER(EntityType.SKELETON, 12, MobSkeletonArcher.class, false, 11),
	SKELETON_ARCHER_FAST(EntityType.SKELETON, 22, MobSkeletonArcherFast.class, false, 12),
	
	IRON_GOLEM(EntityType.IRON_GOLEM, 22, MobIronGolem.class, false, 13),
	
	MAGMA_KING(EntityType.MAGMA_CUBE, 5, MagmaKing.class, true, 14),
	
	SKELETON_EASY(EntityType.SKELETON, 5, MobSkeletonEasy.class, false, 15),
	
	PIG_ZOMBIE_TNT_MONSTER(EntityType.PIG_ZOMBIE, 17, MobPigZombieTNTMonster.class, false, 16),
	
	SKELETON_MEDIUM(EntityType.SKELETON, 12, MobSkeletonMedium.class, false, 17),
	
	BLAZE(EntityType.BLAZE, 5, MobBlaze.class, false, 18);

	private EntityType entityType;
	private int points, id; 
	private Class<? extends GameEntity> clazz;
	private boolean boss;

	private GameEntityType(EntityType entityType, int points, Class<? extends GameEntity> clazz, boolean boss, int id) {
		this.entityType = entityType;
		this.points = points;
		this.clazz = clazz;
		this.boss = boss;
		this.id = id;
	}

	public String getName() {
		return this.name();
	}

	public EntityType getEntityType() {
		return this.entityType;
	}
	
	public int getPoints() {
		return points;
	}
	
	public Class<? extends GameEntity> getClazz() {
		return clazz;
	}
	
	public boolean isBoss() {
		return boss;
	}
	
	public int getId() {
		return id;
	}
}
