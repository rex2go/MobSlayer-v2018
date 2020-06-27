package com.rex2go.mobslayer_core.command;

import java.util.ArrayList;

import com.rex2go.mobslayer_core.user.Rank;
import com.rex2go.mobslayer_core.user.User;
import com.rex2go.mobslayer_core.util.Language;
import com.rex2go.mobslayer_core.util.Translation;

public class LanguageCommand extends Command {

	public LanguageCommand() {
		super(new String[] { "language" }, Rank.USER, "");
		
		ArrayList<String> arr = new ArrayList<>();
		
		for(Language language : Language.values()) {
			arr.add(language.name());
		}
		
		addArgSuggestion(arr.toArray(new String[0]));
	}

	@Override
	public void handle(User user, String[] args) {
		if(args.length > 0) {
			for(Language lang : Language.values()) {
				if(args[0].equalsIgnoreCase(lang.name())) {
					if(user.setLanguage(lang)) {
						String languageName = Character.toUpperCase(lang.name().charAt(0)) + lang.name().toLowerCase().substring(1);
						user.getPlayer().sendMessage("§7" + Translation.getTranslation("language.changed_language_to", lang, languageName));
					}
					return;
				}
			}
		}
		
		String languages = "";
		for(Language lang : Language.values()) {
			languages += Character.toUpperCase(lang.name().charAt(0)) + lang.name().toLowerCase().substring(1) + ", ";
		}
		languages = languages.substring(0, languages.length() - 2);
		user.getPlayer().sendMessage("§7" + Translation.getTranslation("command.language.supported_languages", user.getLanguage()) + ": §e" + languages);
	}
}
