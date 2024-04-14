package me.ryandw11.ultrabar.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The tab completion for the ultrabar command.
 */
public class UltraBarCommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
        List<String> completions;
        if (args.length == 1) {
            completions = new ArrayList<>(Arrays.asList("reload", "toggle", "hooks", "cancel", "clear"));
        } else {
            completions = new ArrayList<>();
        }
        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }
}
