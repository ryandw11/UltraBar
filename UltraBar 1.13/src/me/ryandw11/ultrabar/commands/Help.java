package me.ryandw11.ultrabar.commands;

import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;

import me.ryandw11.ultrabar.BossBarSced;
import me.ryandw11.ultrabar.core.UltraBar;

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
				}
				else{
					p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				}
			}
			else if(args.length == 1 && args[0].equalsIgnoreCase("reload")){
				if(p.hasPermission("ultrabar.reload")){
					if(plugin.getConfig().getBoolean("BossBarMessages.Enabled")){
						for(BossBar b : UltraBar.bossbars){
							b.setVisible(false);
							b.removeAll();
						}
						UltraBar.bossbars.clear();
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
				}
				else{
					p.sendMessage(ChatColor.RED + "You do not have permission for this command!");
				}
			}
		return false;
	}

}
