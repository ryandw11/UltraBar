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
import me.ryandw11.ultrabar.api.bars.BossBarBuilder;

public class NewBarCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

		CommandSender p = sender;
		
		if(!p.hasPermission("ultrabar.bar")){
			p.sendMessage(ChatColor.RED + "You do not have permission for this command.");
			return true;
		}
		
		if(args.length == 0) {
			p.sendMessage(ChatColor.RED + "No parameter detected. Go to " + ChatColor.GREEN + "https://github.com/ryandw11/UltraBar/wiki/Bar-Command-Parameter " + ChatColor.RED + "for help!");
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
					mp.put(tempKey.replace(":", ""), tempValue);
				else if(tempValue.equals(""))
					mp.put(tempKey.replace(":", ""), tempString.replace('"', ' '));
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
	
	@SuppressWarnings("unchecked")
	protected void proccessInfo(CommandSender s, Map<String, String> mp) {
		@SuppressWarnings("rawtypes")
		Iterator it = mp.entrySet().iterator();
		BossBarBuilder bbb = new BossBarBuilder(false);
		bbb.setProgress(1);
		bbb.setColor(BarColor.PURPLE);
		bbb.setStyle(BarStyle.SEGMENTED_10);
		boolean perm = false;
		while(it.hasNext()) {
			Map.Entry<String,String> pair = (Map.Entry<String, String>)it.next();
			
			if(pair.getKey().equalsIgnoreCase("message") || pair.getKey().equalsIgnoreCase("msg")) {
				bbb.setMessage(ChatColor.translateAlternateColorCodes('&', pair.getValue()));
			}
			else if(pair.getKey().equalsIgnoreCase("color") || pair.getKey().equalsIgnoreCase("c")) {
				bbb.setColor(GrabBarStyles.barColor(pair.getValue()));
			}
			else if(pair.getKey().equalsIgnoreCase("style") || pair.getKey().equalsIgnoreCase("s")) {
				bbb.setStyle(GrabBarStyles.barStyle(pair.getValue()));
			}
			else if(pair.getKey().equalsIgnoreCase("progress") || pair.getKey().equalsIgnoreCase("prog")) {
				try {
					bbb.setProgress(Double.valueOf(pair.getValue()));
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
						@SuppressWarnings("deprecation")
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
				if(pair.getValue().equalsIgnoreCase("true")) {
					perm = true;
				}
				else {
					perm = false;
				}
			}
			
			it.remove();
		}
		
		if(perm) {
			boolean test = bbb.buildDead();
			if(!test) {
				s.sendMessage(ChatColor.RED + "Failed to send bossbar! Are you sure you have all of the required parameters?");
			}
			else {
				s.sendMessage(ChatColor.GREEN + "Successfully sent bossbar!");
			}
		}else {
			boolean test = bbb.build();
			if(!test) {
				s.sendMessage(ChatColor.RED + "Failed to send bossbar! Are you sure you have all of the required parameters?");
			}else {
				s.sendMessage(ChatColor.GREEN + "Successfully sent bossbar!");
			}
		}
		
	}
}
