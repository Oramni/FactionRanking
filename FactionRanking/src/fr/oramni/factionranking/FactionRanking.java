package fr.oramni.factionranking;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.spigotmc.Metrics;

import fr.oramni.factionranking.events.EventManager;

public class FactionRanking extends JavaPlugin {
	
	public static String prefix;
	public static String createfaction;
	public static String disbandfaction;
	public static String modifyfactiondesc;
	public static String joinfaction;
	public static String kickfaction;
	public static String leavefaction;
	public static String pluskill;
	public static String plusdeath;
	public static String pluspoint;
	public static String minuspoint;
	public static String nopoint;
	public static String servername;
	
	public static MySQL api;
	String host;
	String database;
	String username;
	String password;
	
	
	
	private String traduc(String config){
		return config.replaceAll("&", "§");
	}
	
	public static String sansfaction(){
		String sansfaction = null;
			sansfaction = "Without a Faction";
		return sansfaction;
	}
	
	@Override
	public void onEnable(){
		EventManager.registerEvents(this);
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = getLogger();
		logger.info("=========================[FactionRanking]=========================");
		logger.info("This plugin has been created by Oramni");
		logger.info("This plugin version is: " + pdfFile.getVersion());
		logger.info("=========================[FactionRanking]=========================");
		registerConfig();
		 try {
		        Metrics metrics = new Metrics();
		        metrics.start();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
	}
	
	private void registerConfig(){
		getConfig().options().copyDefaults(true);
		saveConfig();
		host = getConfig().getString("host");
		database = getConfig().getString("database");
		username = getConfig().getString("username");
		password = getConfig().getString("password");
		prefix =  traduc(getConfig().getString("prefix")) + " ";
		createfaction = traduc(getConfig().getString("createfaction"));
		disbandfaction = traduc(getConfig().getString("disbandfaction"));
		modifyfactiondesc = traduc(getConfig().getString("modifyfactiondesc"));
		joinfaction = traduc(getConfig().getString("joinfaction"));
		kickfaction = traduc(getConfig().getString("kickfaction"));
		leavefaction = traduc(getConfig().getString("leavefaction"));
		pluskill = traduc(getConfig().getString("pluskill"));
		plusdeath = traduc(getConfig().getString("plusdeath"));
		pluspoint = traduc(getConfig().getString("pluspoint"));
		minuspoint = traduc(getConfig().getString("minuspoint"));
		nopoint = traduc(getConfig().getString("nopoint"));
		servername = traduc(getConfig().getString("servername"));
		api = new MySQL("jdbc:mysql://", host, database, username, password);
		api.connection();
		api.createTableFactions();
		api.createTableUsers();
		api.createTableSettings();
		api.setSettings(servername);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		
		Player p = (Player) sender;
		
		if(label.equalsIgnoreCase("factionranking")){
			if(args.length == 0){
			p.sendMessage(prefix + "§eThanks for buying ! Plugin created by §6Oramni");
			if(p.hasPermission("factionranking.reload")){
			p.sendMessage(prefix + "§eTo reload the config: §6/factionranking reload");
			}
			return true;
			}
			if(p.hasPermission("factionranking.reload")){
			if(args[0].equalsIgnoreCase("reload")){
				api.setSettings(servername);
				reloadConfig();
				p.sendMessage(prefix + "§cConfig reloading.");
				p.sendMessage(prefix + "§cConfig reloading..");
				p.sendMessage(prefix + "§cConfig reloading...");
				p.sendMessage(prefix + "§aConfig reloaded !");
				return true;
			} else {
				p.sendMessage(prefix + "§cInvalid arguments");
			}
			} else {
				p.sendMessage(prefix + "§cYou don't have the permission !");
			}
		}
		
		
		return false;
	}
	
}
