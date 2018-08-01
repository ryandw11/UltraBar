package me.ryandw11.ultrabar.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ryandw11.ultrabar.core.UltraBar;

public class TitleCommands implements CommandExecutor {

	private UltraBar plugin;

	public TitleCommands(UltraBar plugin){
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		CommandSender p = sender;

			if(args.length == 0){
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7===========[&6Title&7]==========="));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/utitle send (player) (message) &7- Send a player a title"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/utitle sendall (message) &7- Send the server a title"));
			}
			else if(args[0].equalsIgnoreCase("send")){
				if(p.hasPermission("ultrabar.title")){
					if(args.length >= 3){
						String Message = "";
						for (int i = 2; i < args.length; i++){
							Message = Message + " " + args[i];
						}
						Player p2 = (Player) Bukkit.getServer().getPlayer(args[1]);
						plugin.mgr.title(Message.replace("&", "§"), p2, 5, 20, 5, "");
					}
					else{
						p.sendMessage(ChatColor.RED + "Invalid usage: /utitle send (player) (message)");
					}
				}
				else{
					p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				}
			}
			else if(args[0].equalsIgnoreCase("sendall")){
				if(p.hasPermission("ultrabar.title.sendall")){
					if(args.length >= 2){

						String Message = "";
						for (int i = 1; i < args.length; i++){
						Message = Message + " " + args[i];
						}
						for(Player p1 : Bukkit.getOnlinePlayers()){
						plugin.mgr.title(Message.replace("&", "§"), p1, 5, 20, 5, "");
						}	
					}
					else{
						p.sendMessage(ChatColor.RED + "Invalid usage: /utitle sendall (message)");
					}
				}
				else{
					p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				}
			}
		return false;
	}

}
