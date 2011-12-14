package org.takit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.takit.Takit;
import org.takit.TakitPlayer;

public class MsgCommand implements CommandExecutor {
	private Takit plugin;
	public MsgCommand(Takit plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if ( sender instanceof Player ) {
			Player playerTo = ((Player)sender);
			if ( playerTo.hasPermission("takit.chat.msg") ) {
				if ( args.length>1 ) {
					TakitPlayer from = plugin.getTakitPlayer(playerTo.getName());
					TakitPlayer to = plugin.getTakitPlayer(args[0]);
					
					if ( to!=null ) {
						StringBuffer msg = new StringBuffer();
						
						for ( int i=1; i<args.length; ++i ) {
							msg.append(args[i]).append(" ");
						}
						from.sendPrivateMessage(to, msg.toString());
					}
					else {
						//TODO display error
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
