package org.takit.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.takit.Messages;
import org.takit.Takit;
import org.takit.TakitPlayer;

public class PlayerJoinListener extends PlayerListener {
	private Takit plugin;
	public PlayerJoinListener(Takit plugin) {
		this.plugin = plugin;
	}
	
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		TakitPlayer takitPlayer = new TakitPlayer(plugin, player);
		plugin.addTakitPlayer(takitPlayer);
		event.setJoinMessage(String.format(Messages.PLAYER_JOIN, player.getDisplayName()));
	}
}
