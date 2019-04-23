package me.ryandw11.ultrabar.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ryandw11.ultrabar.BossBarSced;
import me.ryandw11.ultrabar.api.UltraBarAPI;
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
				}
				else{
					p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				}
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
				if(p.hasPermission("ultrabar.reload")){
					if(plugin.getConfig().getBoolean("BossBarMessages.Enabled")){
						UltraBarAPI ubapi = new UltraBarAPI();
						ubapi.clearBar();
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
				int num = 0;
				p.sendMessage(ChatColor.GREEN + "Active Plugin Hooks:");
				if(plugin.worldguard) {
					p.sendMessage(ChatColor.BLUE + "- WorldGuard");
					num++;
				}
				if(plugin.placeholderAPI) {
					p.sendMessage(ChatColor.BLUE + "- PlaceHolderAPI");
					num++;
				}
				if(num == 0)
					p.sendMessage(ChatColor.BLUE + "There are currently no active plugin hooks!");
			}
			else {
				p.sendMessage(ChatColor.RED + "Unknown SubCommand. Type /ultrabar for help!");
			}
		
		return false;
	}

}
