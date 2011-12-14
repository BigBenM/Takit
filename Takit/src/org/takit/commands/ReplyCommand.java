package org.takit.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.takit.Takit;
import org.takit.TakitPlayer;

public class ReplyCommand implements CommandExecutor {
	private Takit plugin;
	public ReplyCommand(Takit plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if ( sender instanceof Player ) {
			TakitPlayer from = plugin.getTakitPlayer(((Player)sender).getName());
			if ( args.length>0 ) {
				StringBuffer msg = new StringBuffer();
				
				for ( int i=0; i<args.length; ++i ) {
					msg.append(args[i]).append(" ");
				}
				from.sendPrivateMessage(null, msg.toString());
			}
			else {
				//TODO display error
			}
			
			return true;
		}
		return false;
	}

}
