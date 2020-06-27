package com.rex2go.mobslayer_core.command;

import java.util.ArrayList;

import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;

public abstract class Command {

	private String[] command;
	private Rank requiredRank;
	private String description;
	private ArrayList<String[]> argSuggestion;
	
	public Command(String[] command, Rank requiredRank, String description) {
		this.command = command;
		this.requiredRank = requiredRank;
		this.description = description;
	}
	
	public abstract void handle(User user, String[] args);
	
	public String[] getCommands() {
		return command;
	}
	
	public Rank getRequiredRank() {
		return requiredRank;
	}
	
	public String getDescription() {
		return description;
	}
	
	public ArrayList<String[]> getArgSuggestion() {
		return argSuggestion;
	}
	
	public void addArgSuggestion(String[] suggestions) {
		if(argSuggestion == null) {
			argSuggestion = new ArrayList<>();
		}
		argSuggestion.add(suggestions);
	}
	
	public void error(User user, String path) {
		user.getPlayer().sendMessage("§c" + path);
	}
}
