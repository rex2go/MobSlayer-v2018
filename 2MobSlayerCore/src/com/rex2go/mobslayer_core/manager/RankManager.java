package com.rex2go.mobslayer_core.manager;

import java.util.ArrayList;

import com.rex2go.mobslayer_core.user.Rank;

public class RankManager extends Manager {

	private ArrayList<Rank> ranks = new ArrayList<>();
	
	public RankManager() {
		ranks.add(Rank.USER);
		ranks.add(Rank.PREMIUM);
		ranks.add(Rank.VIP);
		ranks.add(Rank.BUILDER);
		ranks.add(Rank.MODERATOR);
		ranks.add(Rank.ADMIN);
	}
	
	public Rank getRankById(int rankId) {
		for(Rank rank : ranks) {
			if(rank.getRankId() == rankId) {
				return rank;
			}
		}
		return Rank.USER;
	}
}
