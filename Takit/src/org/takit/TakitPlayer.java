package org.takit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.entity.Player;

public class TakitPlayer  {
	private static final String NICKNAME_PREFIX = "~";
	
	private Takit plugin;
	private Player player;
	private TakitPlayer takitPlayerPM;
	private String playerEmail;
	private String playerNickname;
	private int nicknameCount;
	private ArrayList<String> mutedList;
	private boolean globalMute;
	
	public TakitPlayer(Takit plugin, Player player) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			this.player = player;
			this.takitPlayerPM = null;
			this.mutedList = new ArrayList<String>();
			this.plugin = plugin;
			
			ps = plugin.connection.prepareStatement(
				"select player_nickname,player_email,nickname_count,global_mute " +
				"from players " +
				"where player_name=?"
			);
			ps.setString(1, player.getName());
			rs = ps.executeQuery();
			if ( rs.next() ) {
				this.playerNickname = rs.getString("player_nickname");
				this.playerEmail = rs.getString("player_email");
				this.nicknameCount = rs.getInt("nickname_count");
				this.globalMute = rs.getInt("global_mute")==1 ? true:false;
				if ( this.playerNickname!=null && this.playerNickname.length()>0 ) {
					player.setDisplayName(NICKNAME_PREFIX+playerNickname);
				}
				
				rs.close();
				ps = plugin.connection.prepareStatement(
					"select player_muted from players_muted where player_name=?"
				);
				rs = ps.executeQuery();
				while ( rs.next() ) {
					mutedList.add(rs.getString("player_muted"));
				}
			}
			else {
				rs.close();
				ps.close();
				rs = null;
				ps = plugin.connection.prepareStatement(
					"insert into players (player_name,nickname_count,global_mute) values (?,?,?)"
				);
				ps.setString(1, player.getName());
				ps.setInt(2, 0);
				ps.setInt(3, 0);
				ps.executeUpdate();
				
				this.nicknameCount = 0;
				this.globalMute = false;
			}
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		finally {
			try {
				if ( rs!=null ) rs.close();
				if ( ps!=null ) ps.close();
			}
			catch ( Exception ignore ) { }
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public String getPlayerEmail() {
		return playerEmail;
	}
	public void setPlayerEmail(String playerEmail) {
		this.playerEmail = playerEmail;
	}
	
	public String getPlayerNickname() {
		return playerNickname;
	}
	public void setPlayerNickname(String playerNickname) {
		if ( playerNickname!=null && playerNickname.length()>0 ) {
			if ( this.nicknameCount>=plugin.maxNicknameCount ) {
				//TODO display error
				return;
			}
			if ( playerNickname.toLowerCase().equals("all") || playerNickname.toLowerCase().equals("reset") ) {
				//TODO display error
				return;
			}
			
			PreparedStatement ps = null;
			ResultSet rs = null;
			
			try {
				ps = plugin.connection.prepareStatement(
					"select player_name from players where lower(player_nickname)=?"
				);
				ps.setString(1, playerNickname.toLowerCase());
				rs = ps.executeQuery();
				if ( rs.next() ) {
					//TODO display error
					return;
				}
				
				this.playerNickname = playerNickname;
				this.nicknameCount++;
				player.setDisplayName(NICKNAME_PREFIX+playerNickname);
				
				rs.close();
				rs = null;
				ps.close();
				ps = plugin.connection.prepareStatement(
					"update players set player_nickname=?,nickname_count=? where player_name=?"
				);
				ps.setString(1, this.playerNickname);
				ps.setInt(2, this.nicknameCount);
				ps.setString(3, player.getName());
				ps.executeUpdate();
				//TODO display nickname change
			}
			catch ( Exception e ) {
				e.printStackTrace();
			}
			finally {
				try {
					if ( rs!=null ) rs.close();
					if ( ps!=null ) ps.close();
				}
				catch ( Exception ignore ) { }
			}
		}
	}
	public void resetNicknameCount() {
		PreparedStatement ps = null;
		try {
			ps = plugin.connection.prepareStatement(
				"update players set nickname_count=0 where player_name=?"
			);
			ps.setString(1, player.getName());
			ps.executeUpdate();
			this.nicknameCount = 0;
		}
		catch ( Exception e ) {
			e.printStackTrace();
		}
		finally {
			try {
				if ( ps!=null ) ps.close();
			}
			catch ( Exception ignore ) { }
		}
			
	}
	
	public void setGlobalMute(boolean globalMute) {
		this.globalMute = globalMute;
	}
	public void addMute(String playerName) {
		if ( !this.player.hasPermission("takit.chat.mute") ) {
			//TODO display error
			return;
		}
		
		if ( !mutedList.contains(playerName) ) {
			mutedList.add(playerName);
		}
	}
	public void removeMute(String playerName) {
		if ( !this.player.hasPermission("takit.chat.mute") ) {
			//TODO display error
			return;
		}
		
		mutedList.remove(playerName);
	}
	
	public void sendPrivateMessage(TakitPlayer to, String msg) {
		if ( to==null ) {
			to = this.takitPlayerPM;
		}
		if ( to==null ) {
			//TODO display error
			return;
		}
		
		Player playerTo = to.getPlayer();
		if ( globalMute || mutedList.contains(playerTo.getName().toLowerCase()) ) {
			//TODO display error
			return;
		}
		
		playerTo.sendMessage(String.format(Messages.PLAYER_MSG,
			player.getDisplayName(),
			to.getPlayer().getDisplayName(),
			msg
		));
		to.takitPlayerPM = this;
	}
	public void sendGlobalMessage(String msg) {
		Iterator<TakitPlayer> i = plugin.getTakitPlayers();
		
		while ( i.hasNext() ) {
			TakitPlayer tp  = i.next(); 
			if  ( !tp.globalMute ) {
				tp.player.sendMessage(String.format(Messages.PLAYER_CHAT, 
					player.getName(),
					msg
				));
			}
		}
	}
}
