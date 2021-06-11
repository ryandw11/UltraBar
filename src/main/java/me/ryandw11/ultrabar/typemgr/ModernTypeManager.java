package me.ryandw11.ultrabar.typemgr;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * 1.17+ now relies on Spigot to use.
 */
public class ModernTypeManager implements TypeManager {

	@Override
	public void title(String message, Player p, int fadein, int time, int fadeout, String subtitle){
		p.sendTitle(message, subtitle, fadein, time, fadeout);
	}

	@Override
	public void actionBar(Player player, String message){
		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
	}
}
