package org.takit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.takit.Takit;
import org.takit.TakitPlayer;

public class MuteCommand implements CommandExecutor {
	private Takit plugin;
	public MuteCommand(Takit plugin) {
		this.plugin = plugin;
		
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if ( sender instanceof Player ) {
			TakitPlayer takitPlayer = plugin.getTakitPlayer(((Player)sender).getName());
			if ( command.getName().equalsIgnoreCase("mute") ) {
				if ( args[0].toLowerCase().equals("all") ) {
					takitPlayer.setGlobalMute(true);
				}
				else {
					takitPlayer.addMute(args[0]);
				}
			}
			else if ( command.getName().equalsIgnoreCase("unmute") ) {
				if ( args[0].toLowerCase().equals("all") ) {
					takitPlayer.setGlobalMute(false);
				}
				else {
					takitPlayer.addMute(args[0]);
				}
			}
				
			return true;
		}
		return false;
	}
}
