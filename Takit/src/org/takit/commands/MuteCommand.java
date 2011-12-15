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
			Player player = (Player)sender;
			if ( player.hasPermission("takit.chat.mute") ) {
				TakitPlayer takitPlayer = plugin.getTakitPlayer(player.getName());
				if ( command.getName().equalsIgnoreCase("mute") ) {
					if ( args[0].toLowerCase().equals("all") ) {
						takitPlayer.setGlobalMute(true);
						//TODO display confirmation
					}
					else {
						TakitPlayer mutedPlayer = plugin.getTakitPlayer(args[0]);
						if ( mutedPlayer!=null ) {
							takitPlayer.addMute(mutedPlayer.getPlayer().getName());
							//TODO display confirmation
						}
						else {
							//TODO display error
						}
					}
				}
				else if ( command.getName().equalsIgnoreCase("unmute") ) {
					if ( args[0].toLowerCase().equals("all") ) {
						takitPlayer.setGlobalMute(false);
						//TODO display confirmation
					}
					else {
						TakitPlayer mutedPlayer = plugin.getTakitPlayer(args[0]);
						if ( mutedPlayer!=null ) {
							takitPlayer.removeMute(mutedPlayer.getPlayer().getName());
							//TODO display confirmation
						}
						else {
							player.sendMessage("Unable to unmute");
						}
					}
				}
			}
			else {
				//TODO display error
			}
				
			return true;
		}
		return false;
	}
}
