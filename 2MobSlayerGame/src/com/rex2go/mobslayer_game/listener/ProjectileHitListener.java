package com.rex2go.mobslayer_game.listener;

import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

import com.rex2go.mobslayer_core.MobSlayerCore;
import com.rex2go.mobslayer_core.util.Particle;

import net.minecraft.server.v1_8_R3.EnumParticle;

public class ProjectileHitListener implements Listener {

	public ProjectileHitListener() {
		MobSlayerCore.getInstance().getServer().getPluginManager().registerEvents(this, MobSlayerCore.getInstance());
	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Entity entity = event.getEntity();
		
		if(entity instanceof Arrow) {
			entity.remove();
		} else if(entity instanceof Snowball) {
			Particle particle = new Particle(EnumParticle.FIREWORKS_SPARK, entity.getLocation(), true, 0.5, 0.5, 0.5, 0.65F, 45);
			entity.getWorld().playSound(entity.getLocation(), Sound.PISTON_EXTEND, 1, 1);
			entity.getWorld().playSound(entity.getLocation(), Sound.FIREWORK_BLAST, 1, 1);
			particle.sendAll();
			
			for(Entity entity2 : entity.getNearbyEntities(8, 4, 8)) {
				if(!(entity2 instanceof Player)) {
					if(entity2 instanceof LivingEntity) {
						Vector unitVector = entity2.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
						unitVector = unitVector.add(new Vector(0, 0.5, 0));
						entity2.setVelocity(unitVector.multiply(1.5));
						
//						Vector unitVector = entity2.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize().multiply(1.5);
//						unitVector.add(new Vector(0, entity2.getLocation().getDirection().getY() + 0.5, 0));
//						entity2.setVelocity(unitVector);
					}
				}
			}
		}
	}
}
