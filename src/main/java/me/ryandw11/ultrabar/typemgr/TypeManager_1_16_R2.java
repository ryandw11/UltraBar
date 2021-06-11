package me.ryandw11.ultrabar.typemgr;


import net.minecraft.server.v1_16_R2.ChatMessageType;
import net.minecraft.server.v1_16_R2.IChatBaseComponent;
import net.minecraft.server.v1_16_R2.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_16_R2.PacketPlayOutChat;
import net.minecraft.server.v1_16_R2.SystemUtils;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class TypeManager_1_16_R2 implements TypeManager {

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
