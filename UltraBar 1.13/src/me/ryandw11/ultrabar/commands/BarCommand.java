package me.ryandw11.ultrabar.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ryandw11.ultrabar.BossBarMessage;
import me.ryandw11.ultrabar.GrabBarStyles;
import me.ryandw11.ultrabar.core.UltraBar;

public class BarCommand implements CommandExecutor {

	private UltraBar plugin;

	public BarCommand(){
		this.plugin = UltraBar.plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

		CommandSender p = sender;

		if(!p.hasPermission("ultrabar.bar")){
			p.sendMessage(ChatColor.RED + "You do not have permission for this command.");
			return true;
		}

		if(args.length == 0){
			if(args.length == 0){
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7==========[&6BarMessage&7]============"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/bar send (player) (message) (color) (style) (time) [health] &7- Send a boss bar message to a player!"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/bar sendall (message) (color) (style) (time) [health] [world] &7- Send a boss bar message to the server!"));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/bar cancel &7- Removes all of the bars."));
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Note: All times are in seconds!"));
			}
			
		}
		else if(args[0].equalsIgnoreCase("send")){
			if(!p.hasPermission("ultrabar.bar.send")){
				p.sendMessage(ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
			if(args.length != 6 && args.length != 7){
				p.sendMessage(ChatColor.RED + "Invalid format. /bar send (player) (message) (color) (style) (time) [health]");
				return true;
			}
			Player pl = Bukkit.getPlayer(args[1]);
			if(pl == null){
				p.sendMessage(ChatColor.RED + "That player does not exsist!");
				return true;
			}
			String message = ChatColor.translateAlternateColorCodes('&', args[2].replace("{player}", pl.getName()).replace("_", " "));
			BarColor color = GrabBarStyles.barColor(args[3]);
			BarStyle style = GrabBarStyles.barStyle(args[4]);
			int time;
			double progress = 1;
			try{
				time = Integer.valueOf(args[5]);
			}
			catch(NumberFormatException e){
				time = 1;
				p.sendMessage(ChatColor.RED + "Time was set to one(1). Due to the value being invalid.");
			}
			if(args.length == 7){
				try{ //Checking if it can turn arg[6] into an integer.
					progress = Double.valueOf(args[6]);
				}
				catch(NumberFormatException e){
					p.sendMessage(ChatColor.RED + "The bar's health was set to full due to the value being invalid.");
				}
				if(!(progress <= 1 && progress > 0)){
					p.sendMessage(ChatColor.RED + "The bar's health was set to full due to the value being invalid.");
					progress = 1;
				}
			}
			BossBarMessage b = new BossBarMessage();
			b.createMessage(pl, message, color, style, time, progress);
		}
		/*
		 * send all command
		 */
		else if (args[0].equalsIgnoreCase("sendall") && (args.length == 6 || args.length == 5)){ /*/bar sendall (message) (color) (style) (time) [health] */
			if(!p.hasPermission("ultrabar.bar.sendall")){
				p.sendMessage(ChatColor.RED + "You do not have permission for this command.");
				return true;
			}
			if(args.length != 5 && args.length != 6 && args.length != 7){ // checks if it has [health] or not and is vaild.
				p.sendMessage(ChatColor.RED + "Invalid format. /bar sendall (message) (color) (style) (time) [health] [world]");
				return true;
			}
			/*
			 * Var defining
			 */
			String message = ChatColor.translateAlternateColorCodes('&', args[1].replace("_", " "));
			BarColor color = GrabBarStyles.barColor(args[2]);
			BarStyle style = GrabBarStyles.barStyle(args[3]);
			World w;
			try{
			 w = Bukkit.getWorld(args[6]);
			}catch(ArrayIndexOutOfBoundsException e){
				w = null;
			}
			if(w != null && !Bukkit.getWorlds().contains(w)){
				p.sendMessage(ChatColor.RED + "The world you put was invalid and does not exist.");
			}
			int time;
			try{ //Checking to make sure args[4] is able to be converted into an int.
				time = Integer.valueOf(args[4]);
			}
			catch(NumberFormatException e){
				time = 1;
				p.sendMessage(ChatColor.RED + "Time was set to one(1). Due to the value being invalid.");
			}
			double progress = 1;
			if(args.length == 6){
				try{ //Checking if it can turn arg[6] into a double.
					progress = Double.valueOf(args[5]);
				}
				catch(NumberFormatException e){
					p.sendMessage(ChatColor.RED + "The bar's health was set to full due to the value being invalid.");
				}
				if(!(progress <= 1 && progress > 0)){
					p.sendMessage(ChatColor.RED + "The bar's health was set to full due to the value being invalid.");
					progress = 1;
				}
			}
			BossBarMessage b = new BossBarMessage();
			if(time == -1){
				if(w != null){
					b.createPermMessageWorld(Bukkit.getOnlinePlayers(), message, color, style, progress, w);
				}else{
					b.createPermMessage(Bukkit.getOnlinePlayers(), message, color, style, progress);
				}
				return true;
			}
			if(w == null){
				b.createMessageAll(message, color, style, time, progress); //creating the bar
			}else{
				b.createMessageAll(message, color, style, time, progress, w);
			}
		}
		else if(args[0].equalsIgnoreCase("cancel")){
			for(BossBar b : UltraBar.bossbars){
				b.setVisible(false);
				b.removeAll();
			}
			UltraBar.bossbars.clear();
			plugin.getLogger().info("All of the boss bars have been removed.");
		}
		else{
			p.sendMessage(ChatColor.RED + "Invalid command. Do /bar for list of commands.");
		}
		return false;
	}
}
