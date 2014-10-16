package me.Geforce.plugin.scheduledevents;

import me.Geforce.plugin.plugin_PapersPlease;
import me.Geforce.plugin.misc.HelpfulMethods;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class VillagerSwitchDelay extends BukkitRunnable{
	
	private plugin_PapersPlease plugin;
	private Player player;
	
	public VillagerSwitchDelay(plugin_PapersPlease plugin, Player player){
		this.plugin = plugin;
		this.player = player;
	}

	public void run() {
		plugin.getArena(player).getTimer().setTimeRemaining(59);
		plugin.getArena(player).startArena(player, false);
		
		DocumentCollectionCheck task1 = new DocumentCollectionCheck(plugin.getArena(player).getDocumentCollectionLocation(), plugin, player);
        
        HelpfulMethods.runTimedTask(plugin, task1, 0, 1);
            
        plugin.getArena(player).setCollectionCheckTimer(task1);
		
        cancel();
	}

}
