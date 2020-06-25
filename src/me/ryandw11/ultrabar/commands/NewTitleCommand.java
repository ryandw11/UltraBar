package me.ryandw11.ultrabar.commands;

import me.ryandw11.ultrabar.api.title.TitleBuilder;
import me.ryandw11.ultrabar.core.UltraBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.*;

public class NewTitleCommand implements CommandExecutor {

	private UltraBar plugin;
	public NewTitleCommand(UltraBar plugin){
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

		if(!sender.hasPermission("ultrabar.title")){
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
			return true;
		}

		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "No parameter detected. Go to " + ChatColor.GREEN + "https://github.com/ryandw11/UltraBar/wiki/Title-Command-Parameter " + ChatColor.RED + "for help!");
			return true;
		}

		String finalarg = "";
		for(String c : args) {
			finalarg += c + " ";
		}
		finalarg.trim();
		finalarg = finalarg + " ";

		int length = finalarg.length();

		String tempKey = "";
		boolean inKey = true;
		String tempValue = "";
		boolean inValue = false;
		String tempString = "";
		boolean inString = false;

		Map<String, String> mp = new HashMap<>();

		for(int i = 0; i < length; i++) {
			char c = finalarg.charAt(i);
			if(c != ' ' && inKey) {
				tempKey += c;
			}
			if(!inString && !inKey && c == '"') {
				inString = true;
				inValue = false;
			}
			else if(inString && c == '"') {
				inString = false;
				inValue = false;
				tempValue = "";

			}
			if(c != ' ' && inValue) {
				tempValue += c;
			}
			if(inString) {
				tempString += c;

			}
			if(!inString && c == ' ') {
				inValue = false;
				inString = false;
				inKey = true;
				if(tempString == "")
					mp.put(tempKey.replace(":", "").toLowerCase(), tempValue.toLowerCase());
				else if(tempValue.equals(""))
					mp.put(tempKey.replace(":", "").toLowerCase(), tempString.replace('"', ' '));
				tempKey = "";
				tempValue = "";
				tempString = "";

			}
			if(!inString && inKey && c == ':') {
				inKey = false;
				inValue = true;
			}

		}

		/*
		 * Proccess the info.
		 *
		 */
		proccessInfo(sender, mp);

		return false;
	}

	protected void proccessInfo(CommandSender s, Map<String, String> mp) {
		final Map<String, String> param = new HashMap<>(mp); //Creates a copy of the params
		Iterator<Map.Entry<String, String>> it = mp.entrySet().iterator();

		TitleBuilder titleBuilder = new TitleBuilder(plugin);

		while(it.hasNext()) {
			Map.Entry<String, String> pair = it.next();

			if (pair.getKey().equalsIgnoreCase("title")) {
				titleBuilder.setTitle(pair.getValue());
			} else if (pair.getKey().equalsIgnoreCase("subtitle")) {
				titleBuilder.setSubTitle(pair.getValue());
			} else if (pair.getKey().equalsIgnoreCase("fadeIn")) {
				try {
					int fadeIn = Integer.parseInt(pair.getValue());
					titleBuilder.setFadeIn(fadeIn);
				} catch (NumberFormatException ex) {
					s.sendMessage(ChatColor.RED + "Error: The value for fadeIn is not a number!");
				}
			} else if (pair.getKey().equalsIgnoreCase("fadeOut")) {
				try {
					int fadeIn = Integer.parseInt(pair.getValue());
					titleBuilder.setFadeOut(fadeIn);
				} catch (NumberFormatException ex) {
					s.sendMessage(ChatColor.RED + "Error: The value for fadeOut is not a number!");
				}
			}
			else if(pair.getKey().equalsIgnoreCase("time")) {
				try{
					int fadeIn = Integer.parseInt(pair.getValue());
					titleBuilder.setTime(fadeIn);
				}catch(NumberFormatException ex){
					s.sendMessage(ChatColor.RED + "Error: The value for time is not a number!");
				}
			}
			else if(pair.getKey().equalsIgnoreCase("p") || pair.getKey().equalsIgnoreCase("player") || pair.getKey().equalsIgnoreCase("players")) {
				if(pair.getValue().equalsIgnoreCase("@a") || pair.getValue().equalsIgnoreCase("all") || pair.getValue().equalsIgnoreCase("*")) {
					titleBuilder.addAllPlayers();
					if(!s.hasPermission("ultrabar.title.player.all")) {
						s.sendMessage(ChatColor.RED + "You do not have permission to send titles to everyone.");
						return;
					}
				}
				else if(pair.getValue().contains(",")) {
					String sp = pair.getValue();
					String[] ls = sp.split(",");
					for(String sa : ls) {
						OfflinePlayer ptemp = Bukkit.getOfflinePlayer(sa);
						if(ptemp.isOnline()) {
							titleBuilder.addPlayer(Bukkit.getPlayer(sa));
						}
					}
					if(!s.hasPermission("ultrabar.title.player.list")) {
						s.sendMessage(ChatColor.RED + "You do not have permission to send titles to a list of people.");
						return;
					}
				}
				else {
					if(!Bukkit.getOfflinePlayer(pair.getValue()).isOnline()) {
						s.sendMessage(ChatColor.RED + "The player you requested is not online!");
						return;
					}
					titleBuilder.addPlayer(Bukkit.getPlayer(pair.getValue()));
				}
			}
		}

		if(!titleBuilder.hasPlayers()){
			s.sendMessage(ChatColor.RED + "You must define what players to send the title to.");
			return;
		}

		titleBuilder.send();
	}

}
