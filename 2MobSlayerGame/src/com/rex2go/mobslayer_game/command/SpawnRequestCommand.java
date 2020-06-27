package com.rex2go.mobslayer_game.command;

import com.rex2go.mobslayer_core.command.Command;
import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_game.MobSlayerGame;
import com.rex2go.mobslayer_game.wave.SpawnRequest;

public class SpawnRequestCommand extends Command {

	public SpawnRequestCommand() {
		super(new String[] { "spawnrequest", "sr" }, Rank.MODERATOR, "");
	}

	@Override
	public void handle(User user, String[] args) {
		for(SpawnRequest spawnRequest : MobSlayerGame.getWaveManager().getActiveWave().getSpawnRequests()) {
			user.getPlayer().sendMessage(spawnRequest.getGameEntity().getGameEntityType().name() + " Trigger: " + spawnRequest.getTriggerType().name() + " Time: " + spawnRequest.getTime() 
			+ " Count: " + spawnRequest.getCount());
		}
	}
}
