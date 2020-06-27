package com.rex2go.mobslayer_game.wave.action;

public enum WaveActionType {

	TIME(TimeWaveAction.class), LIGHTNING(LightningWaveAction.class), MESSAGE(MessageWaveAction.class), SPAWN_REQUEST(SpawnRequestWaveAction.class), GIVE_ITEM(GiveItemWaveAction.class), 
	LEARN_RECIPE(LearnRecipeWaveAction.class);
	
	private Class<? extends WaveAction> clazz;
	
	private WaveActionType(Class<? extends WaveAction> clazz) {
		this.clazz = clazz;
	}
	
	public Class<? extends WaveAction> getClazz() {
		return clazz;
	}
}
