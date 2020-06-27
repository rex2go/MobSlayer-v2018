package com.rex2go.mobslayer_game.misc;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.util.NBT;
import com.rex2go.mobslayer_core.util.Particle;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.item.GameItem;
import com.rex2go.mobslayer_game.item.Item;
import com.rex2go.mobslayer_game.mob.Drop;
import com.rex2go.mobslayer_game.mob.Drop.Rarity;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutBlockBreakAnimation;

public class AirDrop {

	Location loc;
	ArmorStand armorStand;
	Block block;
	boolean landed = false;
	int state = 3;
	
	protected int count = 0;
	
	ArrayList<Drop> drops;
	
	public AirDrop(Location loc) {
		this.loc = loc.clone();
		this.drops = new ArrayList<>();
		
		int r = new Random().nextInt((4 - 1) + 1) + 1; // 1 = HEAL, 2 = ENCHANTMENT, 3 = RESOURCE, 4 = TOOLS
		
		if(r == 1) {
			drops.add(new Drop(Item.HEAL_1, Rarity.STANDARD));
			drops.add(new Drop(Item.HEAL_2, Rarity.STANDARD));
			drops.add(new Drop(Item.HEAL_1, Rarity.FREQUENTLY));
			drops.add(new Drop(Item.HEAL_2, Rarity.FREQUENTLY));
			drops.add(new Drop(Item.HEAL_3, Rarity.COMMON));
			drops.add(new Drop(Item.HEAL_3, Rarity.COMMON));
		} else if(r == 2) {
			drops.add(new Drop(Item.ENCH_FIRE_ASPECT, Rarity.FREQUENTLY));
			drops.add(new Drop(Item.ENCH_FIRE_ASPECT, Rarity.COMMON));
			
			drops.add(new Drop(Item.ENCH_POISON_ASPECT, Rarity.FREQUENTLY));
			drops.add(new Drop(Item.ENCH_POISON_ASPECT, Rarity.COMMON));
			
			drops.add(new Drop(Item.ENCH_FIRE_EXTINGUISH, Rarity.FREQUENTLY));
			drops.add(new Drop(Item.ENCH_FIRE_EXTINGUISH, Rarity.COMMON));
			
			drops.add(new Drop(Item.ENCH_INFINITE, Rarity.SPECIAL));
			
			drops.add(new Drop(Item.BOOK, Rarity.STANDARD));
			drops.add(new Drop(Item.BOOK, Rarity.STANDARD));
			drops.add(new Drop(Item.BOOK, Rarity.FREQUENTLY));
			drops.add(new Drop(Item.BOOK, Rarity.FREQUENTLY));
		} else if(r == 3) {
			drops.add(new Drop(Item.DIAMOND, Rarity.SPECIAL));
			drops.add(new Drop(Item.DIAMOND, Rarity.SPECIAL));
			drops.add(new Drop(Item.GOLD_INGOT, Rarity.FREQUENTLY));
			drops.add(new Drop(Item.GOLD_INGOT, Rarity.FREQUENTLY));
			drops.add(new Drop(Item.GOLD_INGOT, Rarity.COMMON));
			drops.add(new Drop(Item.GOLD_INGOT, Rarity.COMMON));
			drops.add(new Drop(Item.CRYSTAL, Rarity.RARE));
		} else if(r == 4) {
			drops.add(new Drop(Item.GRENADE, Rarity.STANDARD));
			drops.add(new Drop(Item.GRENADE, Rarity.STANDARD));
			drops.add(new Drop(Item.GRENADE, Rarity.STANDARD));
			drops.add(new Drop(Item.GRENADE, Rarity.FREQUENTLY));
			drops.add(new Drop(Item.GRENADE, Rarity.FREQUENTLY));
			drops.add(new Drop(Item.GRENADE, Rarity.FREQUENTLY));
			
			drops.add(new Drop(Item.KNOCKBACK_GRENADE, Rarity.STANDARD));
			drops.add(new Drop(Item.KNOCKBACK_GRENADE, Rarity.STANDARD));
			drops.add(new Drop(Item.KNOCKBACK_GRENADE, Rarity.STANDARD));
			drops.add(new Drop(Item.KNOCKBACK_GRENADE, Rarity.FREQUENTLY));
			drops.add(new Drop(Item.KNOCKBACK_GRENADE, Rarity.FREQUENTLY));
			drops.add(new Drop(Item.KNOCKBACK_GRENADE, Rarity.FREQUENTLY));
			
			drops.add(new Drop(Item.ARMOR_BREAKER, Rarity.SPECIAL));
			drops.add(new Drop(Item.ARMOR_BREAKER, Rarity.RARE));
			
			drops.add(new Drop(Item.DIAMOND_SWORD, Rarity.SPECIAL));
			drops.add(new Drop(Item.DIAMOND_SWORD, Rarity.RARE));
			
			drops.add(new Drop(Item.DIAMOND_CHESTPLATE, Rarity.SPECIAL));
			drops.add(new Drop(Item.DIAMOND_CHESTPLATE, Rarity.RARE));
		}
	}
	
	public void drop() {
		MobSlayerGame.getAirDropManager().getStorage().add(this);
		armorStand = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0, 60, 0), EntityType.ARMOR_STAND);
		
		ArrayList<Material> blocks = new ArrayList<>();
		
		blocks.add(Material.YELLOW_FLOWER);
		blocks.add(Material.LONG_GRASS);
		blocks.add(Material.DEAD_BUSH);
		blocks.add(Material.SAPLING);
		blocks.add(Material.BROWN_MUSHROOM);
		blocks.add(Material.RED_MUSHROOM);
		blocks.add(Material.RED_ROSE);
		
		armorStand.setHelmet(new ItemStack(Material.CHEST));
		
		NBT.noGravity(armorStand);
		NBT.setInvisible(armorStand, 1);
		NBT.setInvulnerable(armorStand, 1);

		net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity)armorStand).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		((CraftEntity)armorStand).getHandle().e(tag);
		nmsEntity.c(tag);
		tag.setInt("DisabledSlots", 2096896);
		nmsEntity.f(tag);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(!landed) {
					count++;
					loc = loc.subtract(0, 0.05, 0);
					
					armorStand.teleport(loc);
					
					if(count % 20 == 0) {
						Particle particle = new Particle(EnumParticle.FLAME, loc.clone().add(0, 2, 0), true, 0.2, 0.2, 0.2, 0.01, 10);
						particle.sendAll();
						
						for(int i = 0; i<5; i++) {
							particle = new Particle(EnumParticle.FIREWORKS_SPARK, loc.clone().add(0, i + 3, 0), true, 0.5, 0.5, 0.5, 0.001, 5-i);
							particle.sendAll();
						}
						
						loc.getWorld().playSound(loc, Sound.NOTE_PLING, 1, 1);
					}
					
					Block block = loc.clone().add(0, 1, 0).getBlock();
					if(block.getType() != Material.AIR) {
						if(!blocks.contains(block.getType())) {
							land();
							this.cancel();
						}
					}
				} else {
					this.cancel();
				}
				
			}
		}.runTaskTimer(MobSlayerCore.getInstance(), 1, 1);
	}
	
	public void handle() {
		if(landed) {
			if(block != null) {
				if(block.getType() != Material.AIR) {
					sendParticle();
					
					Particle particle = new Particle(EnumParticle.FLAME, loc.clone().add(0, -0.5, 0), true, 0.4, 0.2, 0.4, 0.01, 10);
					particle.sendAll();
					
					loc.getWorld().playSound(loc, Sound.NOTE_BASS, 1, 1);
				} else {
					MobSlayerGame.getAirDropManager().getStorage().remove(this);
				}
			}
		}
	}
	
	private void land() {
		if(!landed) {
			landed = true;
			armorStand.remove();
			
			loc.getWorld().playSound(loc, Sound.FALL_BIG, 1, 1);
			
			loc = loc.add(0, 2, 0);
			
			block = loc.getBlock();
			block.setType(Material.CHEST);
			
			Firework firework = loc.getWorld().spawn(loc, Firework.class);
	        FireworkMeta data = (FireworkMeta) firework.getFireworkMeta();
	        data.addEffects(FireworkEffect.builder().withColor(Color.FUCHSIA).with(Type.BALL_LARGE).withFade(Color.YELLOW).with(Type.BURST).withColor(Color.ORANGE).withFlicker().build());
	        firework.setFireworkMeta(data);
		}
	}
	
	public void open() {
		if(landed) {
			MobSlayerGame.getAirDropManager().getStorage().remove(this);
			block.setType(Material.AIR);
			
			for(Drop drop : getDrops()) {
				GameItem itemStack = drop.drop();
				
				if(itemStack != null) {
					loc.getWorld().dropItem(loc, itemStack.assignNBTData());
				}
			}
		}
	}
	
	public boolean hasLanded() {
		return landed;
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public int getState() {
		return state;
	}
	
	public void setState(int i) {
		this.state = i;
	}
	
	public Block getBlock() {
		return block;
	}
	
	public ArrayList<Drop> getDrops() {
		return drops;	
	}
	
	protected void sendParticle() {	
		if(getState() != 3) {
			int a = 9 - getState() * 3 - 1;
	
			if (a > 0) {
				PacketPlayOutBlockBreakAnimation packet = new PacketPlayOutBlockBreakAnimation(0,
						new BlockPosition(block.getX(), block.getY(), block.getZ()), a);
	
				for (Player all : Bukkit.getOnlinePlayers()) {
					((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
				}
			}
		}
	}
}
