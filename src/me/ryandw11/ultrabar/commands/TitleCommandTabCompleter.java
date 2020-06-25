package me.ryandw11.ultrabar.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TitleCommandTabCompleter implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String s, String[] args) {
		ArrayList<String> completions = new ArrayList<>();
		completions.addAll(Arrays.asList("player:", "title:", "subtitle:", "fadein:", "fadeout:", "time:"));
		return StringUtil.copyPartialMatches(args[args.length - 1], completions, new ArrayList<>());
	}
}
