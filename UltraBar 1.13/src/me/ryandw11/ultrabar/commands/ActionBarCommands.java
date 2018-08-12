package me.ryandw11.ultrabar.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ryandw11.ultrabar.core.UltraBar;

public class ActionBarCommands implements CommandExecutor {
	
	private UltraBar plugin;
	public ActionBarCommands(UltraBar plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		CommandSender p = sender;
		if(cmd.getName().equalsIgnoreCase("actionbar")){
			if(args.length == 0){
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7===========[&6ActionBars&7]==========="));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/actionbar send (player) (message) &7- Send a player an actionbar!"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/actionbar sendall (message) &7- Send the server an actionbar!"));
			}
			else if(args[0].equalsIgnoreCase("send")){
				if(p.hasPermission("ultrabar.actionbar")){
					if(args.length >= 3){
						String Message = "";
						for (int i = 2; i < args.length; i++){
							Message = Message + " " + args[i];
						}
						Player p2 = (Player) Bukkit.getServer().getPlayer(args[1]);
						plugin.mgr.actionBar(p2, Message.replace("&", "§"));
					}
					else{
						p.sendMessage(ChatColor.RED + "Invalid usage: /actionbar send (player) (message)");
					}
				}
				else{
					p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				}
			}
			else if(args[0].equalsIgnoreCase("sendall")){
				if(p.hasPermission("ultrabar.actionbar.sendall")){
					if(args.length >= 2){
					
						String Message = "";
						for (int i = 1; i < args.length; i++){
						Message = Message + " " + args[i];
						}
						for(Player p1 : Bukkit.getOnlinePlayers()){
						plugin.mgr.actionBar(p1,(Message.replace("&", "§")));
						}	
					}
					else{
						p.sendMessage(ChatColor.RED + "Invalid usage: /actionbar sendall (message)");
					}
				}
				else{
					p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				}
			}
		}
		return false;
	}

}
