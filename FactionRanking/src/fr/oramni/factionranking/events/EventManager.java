package fr.oramni.factionranking.events;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

public class EventManager {
	
	public static void registerEvents(Plugin pl){
		PluginManager pm = (PluginManager) Bukkit.getPluginManager();
		
		pm.registerEvents(new FactionCreate(), pl);
		pm.registerEvents(new PlayerJoin(), pl);
	}

}
