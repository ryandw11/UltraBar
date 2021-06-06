package me.ryandw11.ultrabar.commands;

import me.ryandw11.ultrabar.UltraBar;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * The ActionBar command.
 */
public class ActionBarCommands implements CommandExecutor {

    private final UltraBar plugin;

    public ActionBarCommands(UltraBar plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7===========[&6ActionBars&7]==========="));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/actionbar send (player) (message) &7- Send a player an actionbar!"));
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6/actionbar sendall (message) &7- Send the server an actionbar!"));
        } else if (args[0].equalsIgnoreCase("send")) {
            if (sender.hasPermission("ultrabar.actionbar")) {
                if (args.length >= 3) {
                    StringBuilder message = new StringBuilder();
                    for (int i = 2; i < args.length; i++) {
                        message.append(" ").append(args[i]);
                    }
                    Player otherPlayer = Bukkit.getServer().getPlayer(args[1]);
                    if (otherPlayer == null) {
                        sender.sendMessage(ChatColor.RED + "That player is not currently online!");
                        return true;
                    }
                    plugin.typeManager.actionBar(otherPlayer, plugin.chatColorUtil.translateChatColor(
                            plugin.papiTranslate.getMessage(message.toString(), otherPlayer)));
                } else {
                    sender.sendMessage(ChatColor.RED + "Invalid usage: /actionbar send (player) (message)");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
            }
        } else if (args[0].equalsIgnoreCase("sendall")) {
            if (sender.hasPermission("ultrabar.actionbar.sendall")) {
                if (args.length >= 2) {
                    StringBuilder message = new StringBuilder();
                    for (int i = 1; i < args.length; i++) {
                        message.append(" ").append(args[i]);
                    }
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        plugin.typeManager.actionBar(player, plugin.chatColorUtil.translateChatColor(message.toString()));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Invalid usage: /actionbar sendall (message)");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "You do not have permission for this command!");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "Unknown subcommand! Do /actionbar for help.");
        }

        return false;
    }

}
