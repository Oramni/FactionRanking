package fr.oramni.factionranking;

import java.sql.*;

import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

public class MySQL {

	private String url_base, host, name, username, password;
	private String factions = "factions";
	private String users = "users";
	private String settings = "settings";
	private static Connection connection;
	
	public MySQL(String url_base, String host, String name, String username, String password){
		
		this.url_base = url_base;
		this.host = host;
		this.name = name;
		this.username = username;
		this.password = password;
		
	}
	
	public void connection() {
		
		if(!isConnected()){
			try{
				connection = DriverManager.getConnection(url_base + host + "/" + name, username, password);
			}catch(SQLException e){
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void deconnection() {
		
		if(isConnected()){
			try{
				connection.close();
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		
	}
	
	public boolean isConnected(){
		try{
			if((connection == null) || (connection.isClosed()) || (!connection.isValid(5))){
				return false;
			}else{
				return true;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return false;
	}
	
	private Connection getConnection(){
		
		
		return connection;
	}
	
	public void createTableUsers(){
		
			try{
			
				
				PreparedStatement sts2 = getConnection().prepareStatement(
						"CREATE TABLE IF NOT EXISTS `users` (`id` int(11) NOT NULL, `username` varchar(250) NOT NULL, `faction` varchar(250) NOT NULL, `power` double DEFAULT NULL, `firstplayed` varchar(250) NOT NULL, `kills` int(11) NOT NULL DEFAULT '0', `deaths` int(11) NOT NULL DEFAULT '0') ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;");
				sts2.executeUpdate();
				PreparedStatement sts3 = getConnection().prepareStatement(
						"ALTER TABLE `users` ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `id` (`id`);");
				sts3.executeUpdate();
				PreparedStatement sts4 = getConnection().prepareStatement(
						"ALTER TABLE `users` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=19;");
				sts4.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	public void createTableFactions(){
		
		try{
		
			
			PreparedStatement sts2 = getConnection().prepareStatement(
					"CREATE TABLE IF NOT EXISTS `factions` (`id` int(11) NOT NULL, `name` varchar(250) NOT NULL, `description` varchar(250) NOT NULL, `power` double NOT NULL, `leader` varchar(250) NOT NULL, `landcount` int(250) NOT NULL, `maxpower` double NOT NULL, `online` int(11) NOT NULL DEFAULT '0', `players` int(11) NOT NULL DEFAULT '1', `kills` int(11) NOT NULL DEFAULT '0', `deaths` int(11) NOT NULL DEFAULT '0', `points` int(11) NOT NULL DEFAULT '0') ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=latin1;");
			sts2.executeUpdate();
			PreparedStatement sts3 = getConnection().prepareStatement(
					"ALTER TABLE `factions` ADD PRIMARY KEY (`id`), ADD UNIQUE KEY `id` (`id`);");
			sts3.executeUpdate();
			PreparedStatement sts4 = getConnection().prepareStatement(
					"ALTER TABLE `factions` MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=19;");
			sts4.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	
	}
	
	public void createTableSettings(){
		
		try{
		
			
			PreparedStatement sts2 = getConnection().prepareStatement(
					"CREATE TABLE IF NOT EXISTS `settings` (`id` int(1) NOT NULL DEFAULT '1', `name` varchar(250) NOT NULL DEFAULT 'Hypixel', `login` varchar(250) NOT NULL DEFAULT 'unicorn') ENGINE=InnoDB DEFAULT CHARSET=latin1;");
			sts2.executeUpdate();
			PreparedStatement sts5 = getConnection().prepareStatement(
					"INSERT INTO `settings` (`id`, `name`, `login`) VALUES(1, 'Hypixel', 'unicorn');");
			sts5.executeUpdate();
			PreparedStatement sts3 = getConnection().prepareStatement(
					"ALTER TABLE `settings` ADD UNIQUE KEY `id` (`id`);");
			sts3.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	
	}
	
	public void setSettings(String name, String login){
		try{		
				PreparedStatement sts2 = getConnection().prepareStatement("UPDATE "+settings+" SET name = ? WHERE id = 1");
				sts2.setString(1, name);
				sts2.executeUpdate();
				PreparedStatement sts3 = getConnection().prepareStatement("UPDATE "+settings+" SET login = ? WHERE id = 1");
				sts3.setString(1, login);
				sts3.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	public void createAccount(Player p){
		
		MPlayer mp = MPlayer.get(p);
		try{
			
			PreparedStatement sts = getConnection().prepareStatement("SELECT username FROM "+users+" WHERE username = ?");
			sts.setString(1, p.getName());
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				sts.close();
				
				PreparedStatement sts2 = getConnection().prepareStatement("INSERT INTO "+users+" (username, faction, power, firstplayed) VALUES (?, ?, ?, ?)");
				sts2.setString(1, p.getName());
				if(mp.hasFaction()){
				sts2.setString(2, mp.getFactionName());
				} else {
				sts2.setString(2, FactionRanking.sansfaction());
				}
				sts2.setDouble(3, mp.getPower());
				sts2.setLong(4, mp.getFirstPlayed());
				sts2.executeUpdate();
				
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}
	
	public void setPowerChange(Player p, Double power){
			try{
			
			PreparedStatement sts = getConnection().prepareStatement("UPDATE "+users+" SET power = ? WHERE username = ?");
			sts.setDouble(1, power);
			sts.setString(2, p.getName());
			sts.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void addOnline(Faction f){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET online = online + 1 WHERE name = ?");
		sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void setPlayerKill(Player p, int value){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+users+" SET kills = ? WHERE username = ?");
		sts.setInt(1, value);
		sts.setString(2, p.getName());
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void setPlayerDeath(Player p, int value){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+users+" SET deaths = ? WHERE username = ?");
		sts.setInt(1, value);
		sts.setString(2, p.getName());
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void removePlayerKill(Player p){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+users+" SET kills = kills - 1 WHERE username = ?");
		sts.setString(1, p.getName());
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void removePlayerDeath(Player p){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+users+" SET deaths = deaths - 1 WHERE username = ?");
		sts.setString(1, p.getName());
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void addPlayerKill(Player p){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+users+" SET kills = kills + 1 WHERE username = ?");
		sts.setString(1, p.getName());
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void addPlayerDeath(Player p){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+users+" SET deaths = deaths + 1 WHERE username = ?");
		sts.setString(1, p.getName());
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void addFactionKill(Faction f){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET kills = kills + 1 WHERE name = ?");
		sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void addFactionDeath(Faction f){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET deaths = deaths + 1 WHERE name = ?");
		sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void addFactionPoint(Faction f){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET points = points + 1 WHERE name = ?");
		sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void setFactionKill(Faction f, int value){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET kills = ? WHERE name = ?");
		sts.setInt(1, value);
		sts.setString(2, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void setFactionDeath(Faction f, int value){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET deaths = ? WHERE name = ?");
		sts.setInt(1, value);
		sts.setString(2, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void setFactionPoint(Faction f, int value){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET points = ? WHERE name = ?");
		sts.setInt(1, value);
		sts.setString(2, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void removeFactionKill(Faction f){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET kills = kills - 1 WHERE name = ?");
		sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void removeFactionDeath(Faction f){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET deaths = deaths - 1 WHERE name = ?");
		sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void removeFactionPoint(Faction f){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET points = points - 1 WHERE name = ?");
		sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void removeOnline(Faction f){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET online = online - 1 WHERE name = ?");
		sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void addPlayer(Faction f){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET players = players + 1 WHERE name = ?");
		sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void removePlayer(Faction f){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET players = players - 1 WHERE name = ?");
		sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void setFactionCreated(String s, Faction f){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET created = ? WHERE name = ?");
		sts.setString(1, s);
		sts.setString(2, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void setFactionPowerChange(Faction f, Double power){
		try{
		
		PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET power = ? WHERE name = ?");
		sts.setDouble(1, power);
		sts.setString(2, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		sts.executeUpdate();
		
	}catch(SQLException e){
		e.printStackTrace();
	}
	}
	
	public void deleteFaction(Faction f){
		try{
			
			PreparedStatement sts = getConnection().prepareStatement("DELETE FROM "+factions+" WHERE name = ?");
			sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
			sts.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void setFactionForPlayer(Player p, String f){
		try{
			
			PreparedStatement sts = getConnection().prepareStatement("UPDATE "+users+" SET faction = ? WHERE username = ?");
			sts.setString(1, f);
			sts.setString(2, p.getName());
			sts.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void setFactionNewName(Faction f, String name){
		try{
			
			PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET name = ? WHERE name = ?");
			sts.setString(1, name.replaceAll("§2", "").replaceAll("§4", ""));
			sts.setString(2, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
			sts.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void setFactionLeader(Faction f, Player leader){
		try{
			
			PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET leader = ? WHERE name = ?");
			sts.setString(1, leader.getName());
			sts.setString(2, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
			sts.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	public void setFactionMaxPower(Faction f){
		try{
			
			PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET maxpower = ? WHERE name = ?");
			sts.setDouble(1, f.getPowerMax());
			sts.setString(2, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
			sts.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void setFactionDescription(Faction f, String desc){
		try{
			
			PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET description = ? WHERE name = ?");
			sts.setString(1, desc.replaceAll("§o", "").replaceAll("§7", ""));
			sts.setString(2, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
			sts.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}
	
	public void setFactionLandCount(Faction f){
		try{
			
			PreparedStatement sts = getConnection().prepareStatement("UPDATE "+factions+" SET landcount = ? WHERE name = ?");
			sts.setInt(1, f.getLandCount());
			sts.setString(2, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
			sts.executeUpdate();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
	}

	
	public Integer getFactionPoints(Faction f){
		
		int points = 0;
		
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT points FROM "+factions+" WHERE name = ?");
			sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return points;
			}
			points = rs.getInt("points");
			sts.close();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return points;
		
	}
	
	public Integer getFactionClaims(Faction f){
		
		int points = 0;
		
		try{
			PreparedStatement sts = getConnection().prepareStatement("SELECT landcount FROM "+factions+" WHERE name = ?");
			sts.setString(1, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				return points;
			}
			points = rs.getInt("landcount");
			sts.close();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return points;
		
	}
	
	public void createFactions(String f){
		try{
			
			PreparedStatement sts = getConnection().prepareStatement("SELECT name FROM "+factions+" WHERE name = ?");
			sts.setString(1, f);
			ResultSet rs = sts.executeQuery();
			if(!rs.next()){
				sts.close();
				
				PreparedStatement sts2 = getConnection().prepareStatement("INSERT INTO "+factions+" (name, description, power, leader, landcount, maxpower) VALUES (?, ?, ?, ?, ?, ?)");
				sts2.setString(1, f);
				sts2.setString(2, "");
				sts2.setDouble(3, 0);
				sts2.setString(4, "none");
				sts2.setInt(5, 0);
				sts2.setInt(6, 0);
				sts2.executeUpdate();
				
			}
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
	}

}
