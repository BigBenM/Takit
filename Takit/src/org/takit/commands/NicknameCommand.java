package org.takit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.takit.Takit;
import org.takit.TakitPlayer;

public class NicknameCommand implements CommandExecutor {
	private Takit plugin = null;
	
	public NicknameCommand(Takit plugin) {
		this.plugin = plugin;
	}
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if ( sender instanceof Player ) {
			Player player = ((Player)sender);
			switch ( args.length ) {
			case 1:
				if ( player.hasPermission("takit.chat.nickname") ) {
					plugin.getTakitPlayer(player.getName()).setPlayerNickname(args[0]);
				}
				else {
					//TODO display error
				}
				break;
			case 2:
				if ( player.hasPermission("takit.admin.chat.nickname") ) {
					TakitPlayer tp = plugin.getTakitPlayer(args[0]);
					if ( tp!=null ) {
						if ( args[1].toLowerCase().equals("reset") ) {
							tp.resetNicknameCount();
						}
						else {
							tp.setPlayerNickname(args[1]);
						}
					}
					else {
						//TODO display error
					}
				}
				else {
					//TODO display error
				}
				break;
			}
		}
		return false;
	}
}
