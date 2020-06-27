package com.rex2go.mobslayer_core.util;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagInt;
import net.minecraft.server.v1_8_R3.Navigation;
import net.minecraft.server.v1_8_R3.PathEntity;

public class NBT {

	public static void noAI(Entity entity) {
		net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		((CraftEntity) entity).getHandle().e(tag);
		nmsEntity.c(tag);
		tag.setInt("NoAI", 1);
		nmsEntity.f(tag);
	}
	
	public static void noSound(Entity entity) {
		net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		((CraftEntity) entity).getHandle().e(tag);
		nmsEntity.c(tag);
		tag.setInt("Silent", 1);
		nmsEntity.f(tag);
	}

	public static void noGravity(Entity entity) {
		net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		((CraftEntity) entity).getHandle().e(tag);
		nmsEntity.c(tag);
		tag.setInt("NoGravity", 1);
		nmsEntity.f(tag);
	}
	
	public static void gravity(Entity entity) {
		net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		((CraftEntity) entity).getHandle().e(tag);
		nmsEntity.c(tag);
		tag.setInt("NoGravity", 0);
		nmsEntity.f(tag);
	}
	
	public static void noBaseplate(Entity entity) {
		net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		((CraftEntity) entity).getHandle().e(tag);
		nmsEntity.c(tag);
		tag.setInt("NoBasePlate", 1);
		nmsEntity.f(tag);
	}
	
	public static void setSmall(Entity entity) {
		net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		((CraftEntity) entity).getHandle().e(tag);
		nmsEntity.c(tag);
		tag.setInt("Small", 1);
		nmsEntity.f(tag);
	}
	
	public static ItemStack hideFlags(ItemStack i) {
		net.minecraft.server.v1_8_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(i);
		NBTTagCompound tag;
		if(!nmsItemStack.hasTag()) {
			tag = new NBTTagCompound();
			nmsItemStack.setTag(tag);
		} else {
			tag = nmsItemStack.getTag();
		}
		tag.set("HideFlags", new NBTTagInt(62));
		nmsItemStack.setTag(tag);
		return CraftItemStack.asBukkitCopy(nmsItemStack);
	}

	public static void setTime(Entity entity, int time) {
		net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		((CraftEntity) entity).getHandle().e(tag);
		nmsEntity.c(tag);
		tag.setInt("Time", time);
		nmsEntity.f(tag);
	}

	public static void setInvulnerable(Entity entity, int invulnerable) {
		net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		((CraftEntity) entity).getHandle().e(tag);
		nmsEntity.c(tag);
		tag.setInt("Invulnerable", invulnerable);
		nmsEntity.f(tag);
	}

	public static void setInvisible(Entity entity, int invisible) {
		net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
		NBTTagCompound tag = new NBTTagCompound();
		((CraftEntity) entity).getHandle().e(tag);
		nmsEntity.c(tag);
		tag.setInt("Invisible", invisible);
		nmsEntity.f(tag);
	}
	
	public static void moveEntity(LivingEntity e, Location l, float speed) {
		Navigation nav = (Navigation) ((EntityInsentient) ((CraftEntity) e).getHandle()).getNavigation();
		nav.a(true);
		PathEntity path = nav.a(l.getX(), l.getY(), l.getZ());
		nav.a(path, speed);
	}
	
	public static void setSpeed(Entity entity, double speed) {
		AttributeInstance attributes = ((EntityInsentient) ((CraftEntity) entity).getHandle())
				.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
		attributes.setValue(speed);
	}
	
	public static void setKnockback(Entity entity, double knockback) {
		AttributeInstance attributes = ((EntityInsentient) ((CraftEntity) entity).getHandle())
				.getAttributeInstance(GenericAttributes.c);
		attributes.setValue(knockback);
	}
	
	public static void setDamage(Entity entity, double damage) {
		AttributeInstance attributes = ((EntityInsentient) ((CraftEntity) entity).getHandle())
				.getAttributeInstance(GenericAttributes.ATTACK_DAMAGE);
		attributes.setValue(damage);
	}
}
