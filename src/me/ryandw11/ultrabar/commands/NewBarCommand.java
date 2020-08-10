package me.ryandw11.ultrabar.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ryandw11.ultrabar.GrabBarStyles;
import me.ryandw11.ultrabar.api.BossBarBuilder;
import me.ryandw11.ultrabar.api.UBossBar;
import me.ryandw11.ultrabar.api.parameters.BarParameter;
import me.ryandw11.ultrabar.UltraBar;

public class NewBarCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

		if(!sender.hasPermission("ultrabar.bar")){
			sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
			return true;
		}
		
		if(args.length == 0) {
			sender.sendMessage(ChatColor.RED + "No parameter detected. Go to " + ChatColor.GREEN + "https://github.com/ryandw11/UltraBar/wiki/Bar-Command-Parameter " + ChatColor.RED + "for help!");
			return true;
		}
		
		StringBuilder finalarg = new StringBuilder();
		for(String c : args) {
			finalarg.append(c).append(" ");
		}
		finalarg = new StringBuilder(finalarg.toString().trim());
		finalarg.append(" ");
		
		int length = finalarg.length();
		
		StringBuilder tempKey = new StringBuilder();
		boolean inKey = true;
		StringBuilder tempValue = new StringBuilder();
		boolean inValue = false;
		StringBuilder tempString = new StringBuilder();
		boolean inString = false;
		
		Map<String, String> mp = new HashMap<>();
		
		for(int i = 0; i < length; i++) {
			char c = finalarg.charAt(i);
			if(c != ' ' && inKey) {
				tempKey.append(c);
			}
			if(!inString && !inKey && c == '"') {
				inString = true;
				inValue = false;
			}
			else if(inString && c == '"') {
				inString = false;
				inValue = false;
				tempValue = new StringBuilder();
				
			}
			if(c != ' ' && inValue) {
				tempValue.append(c);
			}
			if(inString) {
				tempString.append(c);
				
			}
			if(!inString && c == ' ') {
				inValue = false;
				inString = false;
				inKey = true;
				final String key = tempKey.toString().replace(":", "").toLowerCase();
				if(tempString.toString().equals(""))
					mp.put(key, tempValue.toString().toLowerCase());
				else if(tempValue.toString().equals(""))
					mp.put(key, tempString.toString().replace('"', ' '));
				tempKey = new StringBuilder();
				tempValue = new StringBuilder();
				tempString = new StringBuilder();
				
			}
			if(!inString && inKey && c == ':') {
				inKey = false;
				inValue = true;
			}
			
		}
		
		/*
		 * Process the info.
		 * 
		 */
		processInfo(sender, mp);
		
		return false;
	}

	protected void processInfo(CommandSender s, Map<String, String> mp) {
		final Map<String, String> param = new HashMap<>(mp); //Creates a copy of the params
		Iterator<Map.Entry<String, String>> it = mp.entrySet().iterator();
		BossBarBuilder bbb = new BossBarBuilder(true);
		bbb.setProgress(1);
		bbb.setColor(BarColor.PURPLE);
		bbb.setStyle(BarStyle.SEGMENTED_10);
		boolean perm = false;
		while(it.hasNext()) {
			Map.Entry<String,String> pair = it.next();
			
			if(pair.getKey().equalsIgnoreCase("message") || pair.getKey().equalsIgnoreCase("msg")) {
				bbb.setMessage(UltraBar.plugin.chatColorUtil.translateChatColor(pair.getValue()));
			}
			else if(pair.getKey().equalsIgnoreCase("color") || pair.getKey().equalsIgnoreCase("c")) {
				bbb.setColor(GrabBarStyles.barColor(pair.getValue()));
			}
			else if(pair.getKey().equalsIgnoreCase("style") || pair.getKey().equalsIgnoreCase("s")) {
				bbb.setStyle(GrabBarStyles.barStyle(pair.getValue()));
			}
			else if(pair.getKey().equalsIgnoreCase("progress") || pair.getKey().equalsIgnoreCase("prog")) {
				try {
					bbb.setProgress(Double.parseDouble(pair.getValue()));
				}
				catch(NumberFormatException ex) {
					s.sendMessage(ChatColor.RED + "The progress must be a number!");
					return;
				}
			}
			else if(pair.getKey().equalsIgnoreCase("time") || pair.getKey().equalsIgnoreCase("t")) {
				try {
					bbb.setTime(Integer.parseInt(pair.getValue()));
				}catch (NumberFormatException ex) {
					s.sendMessage(ChatColor.RED + "The time must be a number!");
					return;
				}
			}
			else if(pair.getKey().equalsIgnoreCase("world") || pair.getKey().equalsIgnoreCase("w")) {
				if(Bukkit.getWorld(pair.getValue()) == null) {
					s.sendMessage(ChatColor.RED + "The world requested does not exist!");
					return;
				}
				bbb.setWorld(Bukkit.getWorld(pair.getValue()));
			}
			else if(pair.getKey().equalsIgnoreCase("p") || pair.getKey().equalsIgnoreCase("player") || pair.getKey().equalsIgnoreCase("players")) {
				if(pair.getValue().equalsIgnoreCase("@a") || pair.getValue().equalsIgnoreCase("all") || pair.getValue().equalsIgnoreCase("*")) {
					bbb.setPlayerCollection((Collection<Player>) Bukkit.getOnlinePlayers());
					bbb.setPublicBar(true);
					bbb.setTracked(true);
					if(!s.hasPermission("ultrabar.bar.player.all")) {
						s.sendMessage(ChatColor.RED + "You do not have permission to send bars to everyone.");
						return;
					}
				}
				else if(pair.getValue().contains(",")) {
					String sp = pair.getValue();
					String[] ls = sp.split(",");
					Collection<Player> players = new ArrayList<>();
					for(String sa : ls) {
						OfflinePlayer ptemp = Bukkit.getOfflinePlayer(sa);
						if(ptemp.isOnline()) {
							players.add(Bukkit.getPlayer(sa));
						}
					}
					bbb.setPlayerCollection(players);
					if(!s.hasPermission("ultrabar.bar.player.list")) {
						s.sendMessage(ChatColor.RED + "You do not have permission to send bars to a list of people.");
						return;
					}
				}
				else {
					if(!Bukkit.getOfflinePlayer(pair.getValue()).isOnline()) {
						s.sendMessage(ChatColor.RED + "The player you requested is not online!");
						return;
					}
					bbb.setSinglePlayer(Bukkit.getPlayer(pair.getValue()));
				}
			}
			else if(pair.getKey().equalsIgnoreCase("perm")) {
				if(!s.hasPermission("ultrabar.bar.perm")) {
					s.sendMessage(ChatColor.RED + "You do not have permission to make permanent bars.");
					return;
				}
				perm = pair.getValue().equalsIgnoreCase("true");
			}
			else if(pair.getKey().equalsIgnoreCase("clear")) {
				if(!s.hasPermission("ultrabar.bar.clear")) {
					s.sendMessage(ChatColor.RED + "You do not have permission to set clear conditions on bars.");
					return;
				}
				if(!pair.getValue().equalsIgnoreCase("death") || !pair.getValue().equalsIgnoreCase("world")){
					s.sendMessage(ChatColor.RED + "Invalid clear condition. It must be set to death or world");
					return;
				}
			}
			else if(pair.getKey().equalsIgnoreCase("id")) {
				if(!s.hasPermission("ultrabar.bar.id")) {
					s.sendMessage(ChatColor.RED + "You do not have permission to set an id on bars.");
					return;
				}
				try {
					bbb.setId(Integer.parseInt(pair.getValue()));
				}
				catch(NumberFormatException e) {
					s.sendMessage(ChatColor.RED + "Unable to set the id! The value provided was not a valid number. Set id to default (-1)");
					bbb.setId(-1);
				}
			}
			else {
				for(BarParameter bp : UltraBar.plugin.getBarParameters()) {
					if(bp.aliases().contains(pair.getKey())) {
						bp.barCreation(pair.getValue(), bbb, s);
						break;
					}
				}
			}
			
			it.remove();
		}
		
		if(perm) {
			UBossBar bar = bbb.buildDead();
			if(bar == null){
				s.sendMessage(ChatColor.RED + "Failed to send bossbar! Are you sure you have all of the required parameters?");
			}
			else {
				s.sendMessage(ChatColor.GREEN + "Successfully sent bossbar!");
				bar.setParameters(mp);
			}
		}else {
			UBossBar bar = bbb.build();
			if(bar == null) {
				s.sendMessage(ChatColor.RED + "Failed to send bossbar! Are you sure you have all of the required parameters?");
			}else {
				s.sendMessage(ChatColor.GREEN + "Successfully sent bossbar!");
				bar.setParameters(param);
			}
		}
		
	}
}
