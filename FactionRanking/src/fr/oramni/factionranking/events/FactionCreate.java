package fr.oramni.factionranking.events;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.massivecraft.factions.Rel;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.factions.event.EventFactionsChunksChange;
import com.massivecraft.factions.event.EventFactionsCreate;
import com.massivecraft.factions.event.EventFactionsDescriptionChange;
import com.massivecraft.factions.event.EventFactionsDisband;
import com.massivecraft.factions.event.EventFactionsMembershipChange;
import com.massivecraft.factions.event.EventFactionsMembershipChange.MembershipChangeReason;
import com.massivecraft.factions.event.EventFactionsNameChange;
import com.massivecraft.factions.event.EventFactionsPowerChange;
import com.massivecraft.factions.event.EventFactionsRankChange;


import fr.oramni.factionranking.FactionRanking;

public class FactionCreate implements Listener {
	
	
	@EventHandler
	public void factionLeader(EventFactionsRankChange e){
		MPlayer mp = e.getMPlayer();
		Faction f = mp.getFaction();
		if(e.getNewRank() == Rel.LEADER){
			FactionRanking.api.setFactionLeader(f, mp.getPlayer());
		}
	}
	
	@EventHandler
	public void factionChunk(EventFactionsChunksChange e){
		Player p = e.getMPlayer().getPlayer();
		Faction f = e.getNewFaction();
		FactionRanking.api.setFactionLandCount(f);
		if(f.getLandCount() != 0){
		FactionRanking.api.setFactionPoint(f, FactionRanking.api.getFactionClaims(f));
		p.sendMessage(FactionRanking.prefix + FactionRanking.pluspoint);
		}
	}
	
	@EventHandler
	public void factionCreate(EventFactionsCreate e){
		MPlayer mp = e.getMPlayer();
		String f = e.getFactionName().replaceAll("§2", "").replaceAll("§4", "");
		FactionRanking.api.createFactions(f);
		Player p = mp.getPlayer();
		FactionRanking.api.setFactionForPlayer(p, f);
		p.sendMessage(FactionRanking.prefix + FactionRanking.createfaction.replaceAll("%p%", p.getName()).replaceAll("%f%", f));
	}
	
	
	@EventHandler
	public void factionnewName(EventFactionsNameChange e){
		MPlayer mp = e.getMPlayer();
		String f = e.getNewName();
		FactionRanking.api.setFactionNewName(mp.getFaction(), f);
	}
	
	
	@EventHandler
	public void factionDisband(EventFactionsDisband e){
		MPlayer mp = e.getMPlayer();
		Faction f = e.getFaction();
		FactionRanking.api.deleteFaction(f);
		Player p = mp.getPlayer();
		p.sendMessage(FactionRanking.prefix + FactionRanking.disbandfaction.replaceAll("%p%", mp.getPlayer().getName()).replaceAll("%f%", f.getName().replaceAll("§2", "").replaceAll("§4", "")));
		for(MPlayer mps : f.getMPlayers()){
			Player ps = mps.getPlayer();
			FactionRanking.api.setFactionForPlayer(ps, FactionRanking.sansfaction());
		}
	}
	
	@EventHandler
	public void factionDescription(EventFactionsDescriptionChange e){
		MPlayer mp = e.getMPlayer();
		Faction f = e.getFaction();
		String desc = e.getNewDescription();
		Player p = mp.getPlayer();
		p.sendMessage(FactionRanking.prefix + FactionRanking.modifyfactiondesc);
		FactionRanking.api.setFactionDescription(f, desc);
	}
	
	@EventHandler
	public void powerChange(EventFactionsPowerChange e){
		MPlayer mp = e.getMPlayer();
		Player p = mp.getPlayer();
		Double power = e.getNewPower();
		Faction f = mp.getFaction();
		FactionRanking.api.setFactionPowerChange(f, f.getPower());
		FactionRanking.api.setPowerChange(p, power);
	}
	
	@EventHandler
	public void factionJoin(EventFactionsMembershipChange e){
		MPlayer mp = e.getMPlayer();
		Faction f = e.getNewFaction();
		Player p = mp.getPlayer();
		if(e.getReason() == MembershipChangeReason.JOIN){
		p.sendMessage(FactionRanking.prefix + FactionRanking.joinfaction.replaceAll("%p%", mp.getPlayer().getName()).replaceAll("%f%", f.getName().replaceAll("§2", "").replaceAll("§4", "")));
		FactionRanking.api.setFactionForPlayer(p, f.getName().replaceAll("§2", "").replaceAll("§4", "").replaceAll("§2", "").replaceAll("§4", ""));
		FactionRanking.api.setFactionMaxPower(f);
		FactionRanking.api.setFactionPowerChange(f, f.getPower());
		FactionRanking.api.addPlayer(f);
		FactionRanking.api.addOnline(f);
		}
		if(e.getReason() == MembershipChangeReason.DISBAND){
			FactionRanking.api.setFactionForPlayer(p, FactionRanking.sansfaction());
			FactionRanking.api.removeOnline(f);
			FactionRanking.api.removePlayer(f);
		}
		if(e.getReason() == MembershipChangeReason.KICK){
			p.sendMessage(FactionRanking.prefix + FactionRanking.kickfaction.replaceAll("%p%", mp.getPlayer().getName()).replaceAll("%f%", f.getName().replaceAll("§2", "").replaceAll("§4", "")));
			FactionRanking.api.setFactionForPlayer(p, FactionRanking.sansfaction());
			FactionRanking.api.setFactionMaxPower(f);
			FactionRanking.api.setFactionPowerChange(f, f.getPower());
			FactionRanking.api.removeOnline(f);
			FactionRanking.api.removePlayer(f);
		}
		if(e.getReason() == MembershipChangeReason.LEAVE){
			p.sendMessage(FactionRanking.prefix + FactionRanking.leavefaction.replaceAll("%p%", mp.getPlayer().getName()).replaceAll("%f%", f.getName().replaceAll("§2", "").replaceAll("§4", "")));
		FactionRanking.api.setFactionForPlayer(p, FactionRanking.sansfaction());
			FactionRanking.api.setFactionMaxPower(f);
			FactionRanking.api.setFactionPowerChange(f, f.getPower());
			FactionRanking.api.removeOnline(f);
			FactionRanking.api.removePlayer(f);
		}
		if(e.getReason() == MembershipChangeReason.CREATE){
			FactionRanking.api.setFactionForPlayer(p, f.getName().replaceAll("§2", "").replaceAll("§4", ""));
			FactionRanking.api.setFactionPowerChange(f, f.getPower());
			FactionRanking.api.setFactionLeader(f, f.getLeader().getPlayer());
			FactionRanking.api.setFactionLandCount(f);
			FactionRanking.api.setFactionMaxPower(f);
			FactionRanking.api.addOnline(f);
		}
		FactionRanking.api.createFactions(f.getName().replaceAll("§2", "").replaceAll("§4", ""));
		FactionRanking.api.setFactionDescription(f, f.getDescription());
	}
	
	
}
