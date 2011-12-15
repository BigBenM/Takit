package org.takit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.takit.commands.MsgCommand;
import org.takit.commands.MuteCommand;
import org.takit.commands.NicknameCommand;
import org.takit.commands.ReplyCommand;
import org.takit.listeners.PlayerChatListener;
import org.takit.listeners.PlayerJoinListener;
import org.takit.listeners.PlayerQuitListener;

public class Takit extends JavaPlugin {
	public Logger log = Logger.getLogger("Minecraft");
	
	public Connection connection = null;
	public int maxNicknameCount = 0;
	
	private HashMap<String,TakitPlayer> playersByName;
	private HashMap<String,TakitPlayer> playersByNickname;
	
	public void onDisable() {
		try {
			connection.close();
		}
		catch ( Exception ignore) { }
		
		log.info(String.format(Messages.PLUGIN_DISABLE, getDescription().getName()));
	}
	public void onEnable() {
		PluginManager pm = this.getServer().getPluginManager();
		
		initStorage();
		initConfig();
		playersByName = new HashMap<String,TakitPlayer>();
		playersByNickname = new HashMap<String,TakitPlayer>();
		
		pm.registerEvent(Event.Type.PLAYER_JOIN, new PlayerJoinListener(this), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_CHAT, new PlayerChatListener(this), Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_QUIT, new PlayerQuitListener(this), Event.Priority.Normal, this);
		
		getCommand("nickname").setExecutor(new NicknameCommand(this));
		getCommand("msg").setExecutor(new MsgCommand(this));
		getCommand("r").setExecutor(new ReplyCommand(this));
		
		MuteCommand muteCommand = new MuteCommand(this);
		getCommand("mute").setExecutor(muteCommand);
		getCommand("unmute").setExecutor(muteCommand);
		
		log.info(String.format(Messages.PLUGIN_ENABLE, getDescription().getName()));
	}
	
	public void initConfig() {
		FileConfiguration config = getConfig();
		
		File configFile = new File(this.getDataFolder(), "config.yml");
		if ( !configFile.exists() ) {
			config.set("chat.max-nickname-count", 1);
			saveConfig();
			reloadConfig();
		}
		
		maxNicknameCount = config.getInt("chat.max-nickname-count");
	}
	public void initStorage() {
		String path = "plugins" + File.separator + "Takit" + File.separator;
		File filePath = new File(path);
		if ( !filePath.exists() ) {
			filePath.mkdir();
		}
		
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:"+path+"data.sqlite");
			connection.setAutoCommit(false);
			Statement statement = connection.createStatement();
			statement.executeUpdate("create table if not exists players (" +
					"player_name varchar(255)," +
					"player_nickname varchar(255)," +
					"player_email varchar(255)," +
					"nickname_count int," +
					"global_mute int" +
			")");
			statement.executeUpdate("create table if not exists players_muted( " +
					"player_name varchar(255)," +
					"player_muted varchar(255)" +
			")");
			connection.commit();
			connection.setAutoCommit(true);
		}
		 catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public TakitPlayer getTakitPlayer(String playerName) {
		TakitPlayer takitPlayer = null;
		playerName = playerName.toLowerCase();
		
		takitPlayer = playersByName.get(playerName);
		if ( takitPlayer!=null ) {
			return takitPlayer;
		}
		
		takitPlayer = playersByNickname.get(playerName);
		if ( takitPlayer!=null ) {
			return takitPlayer;
		}
		
		return null;
	}
	public void addTakitPlayer(TakitPlayer takitPlayer) {
		playersByName.put(takitPlayer.getPlayer().getName().toLowerCase(), takitPlayer);
		
		if ( takitPlayer.getPlayerNickname()!=null ) {
			playersByNickname.put(takitPlayer.getPlayerNickname().toLowerCase(), takitPlayer);
		}
	}
	public void removeTakitPlayer(String playerName) {
		playersByName.remove(playerName.toLowerCase());
		playersByNickname.remove(playerName.toLowerCase());
	}
	public Iterator<TakitPlayer> getTakitPlayers() {
		return this.playersByName.values().iterator();
	}
	
	
	/*public static void main(String args[]) {
		initStorage();
		
		String playerName = "JoeTac";
		PlayerPreference pp = PlayerPreference.get(playerName);
		
		pp.setPlayerNickname("Joe");
		pp.save();
		
		try {
			connection.close();
		} catch (Exception ingore) { }
	}*/
}
