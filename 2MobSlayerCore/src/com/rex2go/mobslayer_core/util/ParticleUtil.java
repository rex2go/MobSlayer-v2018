package com.rex2go.mobslayer_core.util;

import org.bukkit.Location;

import net.minecraft.server.v1_8_R3.EnumParticle;

public class ParticleUtil {

	public static void circle(EnumParticle particleType, Location location, int particles, float radius) {
		for (int i = 0; i < particles; i++) {
			double angle, x, z;
			angle = 2 * Math.PI * i / particles;
			x = Math.cos(angle) * radius;
			z = Math.sin(angle) * radius;
			location.add(x, 0, z);
			Particle particle = new Particle(particleType, location, true, 0, 0, 0, 0, 1);;
			particle.sendAll();
			location.subtract(x, 0, z);
		}
	}
}
