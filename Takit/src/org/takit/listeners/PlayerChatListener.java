package org.takit.listeners;

import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;
import org.takit.Takit;

public class PlayerChatListener extends PlayerListener {
	private Takit plugin;
	public PlayerChatListener(Takit plugin) {
		this.plugin = plugin;
	}
	
	public void onPlayerChat(PlayerChatEvent event) {
		plugin.getTakitPlayer(event.getPlayer().getName()).sendGlobalMessage(event.getMessage());
		event.setCancelled(true);
	}
}
