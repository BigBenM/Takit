package org.takit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.takit.Messages;
import org.takit.Takit;

public class PlayerQuitListener extends PlayerListener {
	private Takit plugin;
	public PlayerQuitListener(Takit plugin) {
		this.plugin = plugin;
	}
	
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		plugin.removeTakitPlayer(player.getName());
		event.setQuitMessage(String.format(Messages.PLAYER_QUIT, player.getDisplayName()));
	}
}
