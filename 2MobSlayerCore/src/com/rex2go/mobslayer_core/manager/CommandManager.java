package com.rex2go.mobslayer_core.manager;

import java.util.ArrayList;

import com.rex2go.mobslayer_core.command.Command;

public class CommandManager extends Manager {

	private ArrayList<Command> commands = new ArrayList<>();
	
	public void registerCommand(Command command) {
		commands.add(command);
	}
	
	public ArrayList<Command> getCommands() {
		return commands;
	}
	
	public Command getCommandByName(String name) {
		for(Command command : getCommands()) {
			for(String s : command.getCommands()) {
				if(s.equalsIgnoreCase(name)) {
					return command;
				}
			}
		}
		return null;
	}
}
