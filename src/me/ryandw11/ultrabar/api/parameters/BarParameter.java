package me.ryandw11.ultrabar.api.parameters;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.ryandw11.ultrabar.api.BossBarBuilder;

public interface BarParameter {
	
	public String getPluginName();
	public List<String> aliases();
	public String tab();
	public void barCreation(String value, BossBarBuilder barBuilder, CommandSender creator);

}
