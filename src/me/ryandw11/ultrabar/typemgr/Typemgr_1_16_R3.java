package me.ryandw11.ultrabar.typemgr;


import net.minecraft.server.v1_16_R3.ChatMessageType;
import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutChat;
import net.minecraft.server.v1_16_R3.SystemUtils;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class Typemgr_1_16_R3 implements Typemgr{

	@Override
	public void title(String message, Player p, int fadein, int time, int fadeout, String subtitle){
		p.sendTitle(message, subtitle, fadein, time, fadeout);
	}

	@Override
	public void actionBar(Player player, String message){
		CraftPlayer p = (CraftPlayer) player;
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO, SystemUtils.b);
        p.getHandle().playerConnection.sendPacket(ppoc);
	}
}
