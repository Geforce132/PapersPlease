package me.Geforce.plugin;

import java.util.HashMap;
import java.util.logging.Logger;

import me.Geforce.plugin.arena.PPArena;
import me.Geforce.plugin.commands.CommandPP;
import me.Geforce.plugin.listeners.PPEventHandler;
import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

public class plugin_PapersPlease extends JavaPlugin{
	
	private PluginDescriptionFile pdf = this.getDescription();
	public final Logger logger = Logger.getLogger("Minecraft");
	
	private Scoreboard infoBoard;
	private Economy economy;
	public HashMap<OfflinePlayer, PPArena> ppArenas = new HashMap<OfflinePlayer, PPArena>();
	
	public void onEnable(){
		logger.info(pdf.getName() + " v" + pdf.getVersion() + " has been enabled!");
		this.infoBoard = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
		Bukkit.getServer().getPluginManager().registerEvents(new PPEventHandler(this), this);
		this.getCommand("pp").setExecutor(new CommandPP(this));
		
		if(!setupEconomy()){
			logger.severe("Vault is not installed! Disabling Papers Please plugin!");
			Bukkit.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
	}

	public void onDisable(){
		logger.info(pdf.getName() + " v" + pdf.getVersion() + " has been disabled!");		
	}
	
	public Scoreboard getInfoBoard(){
		return this.infoBoard;
	}
	
	public void setInfoBoard(Scoreboard board){
		this.infoBoard = board;
	}
	
	public PPArena getArena(OfflinePlayer player){
		if(this.ppArenas.containsKey(player)){
			return this.ppArenas.get(player);
		}else{
			return null;
		}
	}

	public void removeArena(OfflinePlayer player){
		if(this.ppArenas.containsKey(player)){
			this.ppArenas.remove(player);
		}else{
			logger.warning("Tryed to remove a arena that doesn't exist!");
		}
	}
	

	private boolean setupEconomy() {
		if(getServer().getPluginManager().getPlugin("Vault") == null){
			return false;
		}
		
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp == null){
			return false;
		}
		
		economy = rsp.getProvider();
		return economy != null;
	}
	
	public Economy getEconomy(){
		return this.economy;
	}

}
