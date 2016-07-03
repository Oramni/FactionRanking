package fr.oramni.factionranking.events;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;

import fr.oramni.factionranking.FactionRanking;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onKill(PlayerDeathEvent e){
		Player dead = e.getEntity();
		MPlayer deadm = MPlayer.get(dead);
		Faction deadf = deadm.getFaction();
		if(e.getEntity().getKiller() instanceof Player){
		Player killer = e.getEntity().getKiller();
		MPlayer killerm = MPlayer.get(killer);
		Faction killerf = killerm.getFaction();
		dead.sendMessage(FactionRanking.prefix + FactionRanking.plusdeath);
		FactionRanking.api.addPlayerDeath(dead);
		FactionRanking.api.addFactionDeath(deadf);
		FactionRanking.api.addFactionKill(killerf);
		if(FactionRanking.api.getFactionPoints(deadf) != 0){
			FactionRanking.api.removeFactionPoint(deadf);
			dead.sendMessage(FactionRanking.prefix + FactionRanking.minuspoint);
		} else {
			dead.sendMessage(FactionRanking.prefix + FactionRanking.nopoint);
		}
		killer.sendMessage(FactionRanking.prefix + FactionRanking.pluskill);
		FactionRanking.api.addPlayerKill(killer);
		FactionRanking.api.addFactionPoint(killerf);
		killer.sendMessage(FactionRanking.prefix + FactionRanking.pluspoint);
		} else {
			FactionRanking.api.addPlayerDeath(dead);
			FactionRanking.api.addFactionDeath(deadf);
			if(FactionRanking.api.getFactionPoints(deadf) != 0){
				FactionRanking.api.removeFactionPoint(deadf);
				dead.sendMessage(FactionRanking.prefix + FactionRanking.minuspoint);
			} else {
				dead.sendMessage(FactionRanking.prefix + FactionRanking.nopoint);
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		Player p = e.getPlayer();
		MPlayer mp = MPlayer.get(p);
		if(!(FactionRanking.api.isConnected())){
			p.sendMessage(" ");
			p.sendMessage(" ");
			p.sendMessage(" ");
			p.sendMessage(FactionRanking.prefix + "§cPlease put valid database informations in the config.yml and restart !");
			p.sendMessage(" ");
		}
		FactionRanking.api.createAccount(p);
		FactionRanking.api.addOnline(mp.getFaction());
		
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		Player p = e.getPlayer();
		MPlayer mp = MPlayer.get(p);
		FactionRanking.api.removeOnline(mp.getFaction());
	}
	
}
