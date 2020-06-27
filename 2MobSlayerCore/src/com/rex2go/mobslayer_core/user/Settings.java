package com.rex2go.mobslayer_core.user;

import java.util.ArrayList;

import org.bukkit.Color;

import com.rex2go.mobslayer_core.util.Language;

public class Settings {

	Language language;
	ArrayList<Color> armorColors;
	boolean blockBadWords, enableAutoJoin, showBlood, enableScoreboard, attNotification, simpleCrafting, autoRespawn, showStars;

	public Settings(Language language, ArrayList<Color> armorColors, boolean blocKBadWords, boolean enableAutoJoin, boolean showBlood, boolean enableScoreboard, boolean attNotification,
			boolean simpleCrafting, boolean autoRespawn, boolean showStars) {
		this.language = language;
		this.armorColors = armorColors;
		this.blockBadWords = blocKBadWords;
		this.enableAutoJoin = enableAutoJoin;
		this.showBlood = showBlood;
		this.enableScoreboard = enableScoreboard;
		this.attNotification = attNotification;
		this.simpleCrafting = simpleCrafting;
		this.autoRespawn = autoRespawn;
		this.showStars = showStars;
	}
	
	@Deprecated
	public Language getLanguage() {
		return language;
	}
	
	@Deprecated
	public void setLanguage(Language language) {
		this.language = language;
	}
	
	public ArrayList<Color> getArmorColors() {
		return armorColors;
	}

	public void setArmorColors(ArrayList<Color> armorColors) {
		this.armorColors = armorColors;
	}
	
	public boolean blockBadWords() {
		return blockBadWords;
	}

	public void setBlockBadWords(boolean blockBadWords) {
		this.blockBadWords = blockBadWords;
	}
	
	public boolean hasAutoJoinEnabled() {
		return enableAutoJoin;
	}

	public void setEnableAutoJoin(boolean enableAutoJoin) {
		this.enableAutoJoin = enableAutoJoin;
	}
	
	public boolean showBlood() {
		return showBlood;
	}

	public void setShowBlood(boolean showBlood) {
		this.showBlood = showBlood;
	}

	public boolean hasScoreboardEnabled() {
		return enableScoreboard;
	}

	public void setEnableScoreboard(boolean enableScoreboard) {
		this.enableScoreboard = enableScoreboard;
	}
	
	public boolean attNotification() {
		return attNotification;
	}
	
	public void setAttNotification(boolean attNotification) {
		this.attNotification = attNotification;
	}
	
	public boolean useSimpleCrafting() {
		return simpleCrafting;
	}
	
	public void setUseSimpleCrafting(boolean simpleCrafting) {
		this.simpleCrafting = simpleCrafting;
	}
	
	public boolean autoRespawn() {
		return autoRespawn;
	}
	
	public void setAutoRespawn(boolean autoRespawn) {
		this.autoRespawn = autoRespawn;
	}
	
	public boolean showStars() {
		return showStars;
	}
	
	public void setShowStars(boolean showStars) {
		this.showStars = showStars;
	}
}
