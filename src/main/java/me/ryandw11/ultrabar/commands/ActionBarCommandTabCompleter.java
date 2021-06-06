package me.ryandw11.ultrabar.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The tab completion for the actionbar command.
 */
public class ActionBarCommandTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        List<String> completions;
        if (args.length == 1) {
            completions = new ArrayList<>(Arrays.asList("send", "sendall"));
        } else {
            completions = new ArrayList<>();
        }
        return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
    }
}
