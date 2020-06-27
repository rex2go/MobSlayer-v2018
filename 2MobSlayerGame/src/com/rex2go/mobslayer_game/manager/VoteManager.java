package com.rex2go.mobslayer_game.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import com.rex2go.mobslayer_core.manager.Manager;
import com.rex2go.mobslayer_core.util.MySQL;
import com.rex2go.mobslayer_game.vote.Vote;

public class VoteManager extends Manager {

	ArrayList<Vote> availableVotes = new ArrayList<>();
	Vote forceVote = null;

	boolean allowVote = true;

	public VoteManager() {
		ResultSet rs = MySQL.query("SELECT * FROM mobslayer_maps");

		try {
			while (rs.next()) {
				Vote vote = new Vote(rs.getString("map_name"), rs.getString("world_name"), rs.getString("author"));
				availableVotes.add(vote);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public Vote getMostVoted() {
		if (forceVote == null) {
			Vote vote = null;

			for (Vote vote1 : availableVotes) {
				if (vote == null) {
					vote = vote1;
				} else if (vote.getVotes() < vote1.getVotes()) {
					vote = vote1;
				}
			}
			
			if(availableVotes.size() > 1) {
				if(vote == availableVotes.get(0)) {
					vote = availableVotes.get(new Random().nextInt(availableVotes.size()-1));
				}
			}

			return vote;
		}

		return forceVote;
	}

	public ArrayList<Vote> getAvailableVotes() {
		return availableVotes;
	}

	public void setAvailableVotes(ArrayList<Vote> availableVotes) {
		this.availableVotes = availableVotes;
	}

	public Vote getForceVote() {
		return forceVote;
	}

	public void setForceVote(Vote forceVote) {
		this.forceVote = forceVote;
	}

	public boolean isAllowVote() {
		return allowVote;
	}

	public void setAllowVote(boolean allowVote) {
		this.allowVote = allowVote;
	}
	
	public Vote getVoteByMapName(String mapName) {
		for(Vote vote : availableVotes) {
			if(vote.getMapName().equals(mapName)) {
				return vote;
			}
		}
		return null;
	}
}
