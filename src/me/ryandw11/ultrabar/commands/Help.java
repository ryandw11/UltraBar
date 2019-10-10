package me.ryandw11.ultrabar.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ConcurrentModificationException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.ryandw11.ultrabar.BossBarSced;
import me.ryandw11.ultrabar.api.UltraBarAPI;
import me.ryandw11.ultrabar.api.bars.UBossBar;
import me.ryandw11.ultrabar.core.UltraBar;
import me.ryandw11.ultrabar.schedulers.ActionBarSched;
import me.ryandw11.ultrabar.schedulers.TitleSched;

public class Help implements CommandExecutor {

	private UltraBar plugin;
	public Help(UltraBar plugin){
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
		CommandSender p = sender;
			if(args.length == 0){
				if(p.hasPermission("ultrabar.help")){
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7==========[&6UltraBar Help&7]=========="));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/bar &7- Commands for the boss bar"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/utitle &7- Commands for the title command"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/actionbar &7- Commands for the actionbar command"));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/ultrabar reload &7- Reload the plugin."));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/ultrabar toggle &7- Toggle if you will recive bossbar announcements."));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/ultrabar hooks &7- See the active hooks."));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/ultrabar cancel [id] &7- Remove all active bars."));
					p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/ultrabar clear (player) &7- Remove all active bars for that player."));
				}
				else{
					p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				}
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
				if(p.hasPermission("ultrabar.reload")){
					if(plugin.getConfig().getBoolean("BossBarMessages.Enabled")){
						for(UBossBar b : UltraBar.trackedBars){
							b.delete();
						}
						UltraBar.barMessage.setVisible(false);
						UltraBar.barMessage.removeAll();
						UltraBar.barMessage = null;
					}
					
					plugin.reloadConfig();
					p.sendMessage(ChatColor.GREEN + "The config file was reloaded!");
					plugin.getLogger().info("[UltraBar] The config files were reloaded!");
					if(plugin.getConfig().getBoolean("BossBarMessages.Enabled")){
						BossBarSced b = new BossBarSced();
						b.startProgram();
					}
					if(plugin.getConfig().getBoolean("Title_Announcements.Enabled")){
						TitleSched ts = new TitleSched();
						ts.startProgram();
					}
					if(plugin.getConfig().getBoolean("Action_Announcements.Enabled")){
						ActionBarSched as = new ActionBarSched();
						as.startProgram();
					}
				}
				else{
					p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				}
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("toggle")){
				if(!(p instanceof Player)){
					p.sendMessage(ChatColor.RED + "Only players can use this command!");
					return true;
				}
				if(!p.hasPermission("ultrabar.toggle")){
					p.sendMessage(ChatColor.RED + "You do not have permission for this command.");
					return true;
				}
				if(plugin.getToggledPlayers().contains((Player) p)){
					plugin.removeTogglePlayer((Player) p);
					p.sendMessage(ChatColor.GREEN + "Boss Bar announcements will now show!");
				}else{
					plugin.addTogglePlayer((Player) p);
					p.sendMessage(ChatColor.RED + "Boss Bar announcements will no longer show!");
				}
			}
			else if(args.length == 1 && (args[0].equalsIgnoreCase("hooks") || args[0].equalsIgnoreCase("hook"))) {
				if(!p.hasPermission("ultrabar.hooks")) {
					p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
					return true;
				}
				
				String version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];
				
				int num = 0;
				p.sendMessage(ChatColor.GREEN + "Active Plugin Hooks:");
				if(plugin.worldguard) {
					if(version.equals("v1_14_R1") || version.equals("v1_13_R1") || version.equals("v1_13_R2"))
						p.sendMessage(ChatColor.BLUE + "- WorldGuard (1.13+)");
					else
						p.sendMessage(ChatColor.BLUE + "- WorldGuard (1.11.x - 1.12.x)");
					num++;
				}
				if(plugin.placeholderAPI) {
					p.sendMessage(ChatColor.BLUE + "- PlaceHolderAPI");
					num++;
				}
				if(num == 0)
					p.sendMessage(ChatColor.BLUE + "There are currently no active plugin hooks!");
			}
			else if(args[0].equalsIgnoreCase("cancelbars") || args[0].equalsIgnoreCase("cancel")){
				if(!p.hasPermission("ultrabar.cancel")) {
					p.sendMessage(ChatColor.RED + "You do not have permission for this command");
					return true;
				}
				if(args.length == 2) {
					int id = -1;
					try {
						id = Integer.parseInt(args[1]);
					}
					catch(NumberFormatException e) {
						p.sendMessage(ChatColor.RED + "Invalid number!");
						return true;
					}
					
					UltraBarAPI uapi = new UltraBarAPI();
					for(UBossBar b : uapi.getBarsWithId(id)) {
						b.delete();
					}
					p.sendMessage(ChatColor.GREEN + "Removed all bossbars with that id!");
					
				}else {
					try {
						for(UBossBar b : UltraBar.trackedBars){
							b.delete();
						}
					}catch(ConcurrentModificationException e) {
					
					}
					plugin.getLogger().info("All of the boss bars have been removed.");
					p.sendMessage(ChatColor.GREEN + "Cleared all of the active bars!");
				}
			}
			else if(args.length == 2 && args[0].equalsIgnoreCase("clear")) {
				if(!p.hasPermission("ultrabar.clear")) {
					p.sendMessage(ChatColor.RED + "You do not have permission for this command");
					return true;
				}
				UltraBarAPI ubapi = new UltraBarAPI();
				if(Bukkit.getOfflinePlayer(args[1]) == null) {
					p.sendMessage(ChatColor.RED + "That player does not exist!");
					return false;
				}
				if(!Bukkit.getPlayer(args[1]).isOnline()) {
					p.sendMessage(ChatColor.RED + "That player is not online!");
					return false;
				}
				for(UBossBar ub : ubapi.getBarsForPlayer(Bukkit.getPlayer(args[1]))) {
					ub.removePlayer(Bukkit.getPlayer(args[1]));
					ub.updatePlayers();
				}
				p.sendMessage(ChatColor.GREEN + "Cleared all bars for that player!");
			}
			else {
				p.sendMessage(ChatColor.RED + "Unknown SubCommand. Type /ultrabar for help!");
			}
		
		return false;
	}

}
