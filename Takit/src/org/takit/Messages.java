package org.takit;

import org.bukkit.ChatColor;

public class Messages {
	public static String PLUGIN_ENABLE = "[%s] Enabled";
	public static String PLUGIN_DISABLE = "[%s] Disabled";
	public static String PLAYER_JOIN = ChatColor.YELLOW+"<"+ChatColor.GREEN+"%s"+ChatColor.YELLOW+"> has joined the game.";
	public static String PLAYER_QUIT = ChatColor.YELLOW+"<"+ChatColor.GREEN+"%s"+ChatColor.YELLOW+"> has left the game.";
	public static String PLAYER_CHAT = ChatColor.WHITE+"<"+ChatColor.GREEN+"%s"+ChatColor.WHITE+"> %s";
	public static String PLAYER_MSG = ChatColor.WHITE+"<"+ChatColor.GREEN+"%s"+ChatColor.GRAY+"->%s"+ChatColor.WHITE+"> %s";
	public static String PLAYER_MSG_ERROR = ChatColor.RED+"Unable to deliver your message";
	public static String NICKNAME_CHANGE = ChatColor.YELLOW+"You will now be known as <"+ChatColor.GREEN+"%s"+ChatColor.YELLOW+">.";
	public static String NICKNAME_IN_USE = ChatColor.RED+"Nickname already in use.";
	public static String NICKNAME_EXCEEDED = ChatColor.RED+"You cannot change your nickname again. Contact administrator.";
}
