package me.Geforce.plugin.scheduledevents;

import me.Geforce.plugin.plugin_PapersPlease;
import me.Geforce.plugin.misc.HelpfulMethods;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class DocumentCollectionCheck extends BukkitRunnable{
	
	private Location location;
	private plugin_PapersPlease plugin;
	private Player player;
	
	public DocumentCollectionCheck(Location loc, plugin_PapersPlease plugin, Player player){
		this.location = loc;
		this.plugin = plugin;
		this.player = player;
	}

	public void run() {
		if(player.getWorld().getBlockAt(location).getType() == Material.CHEST){
			Block chest = player.getWorld().getBlockAt(location);
			BlockState state = chest.getState();
			
			if(state instanceof Chest){
				Inventory inv = ((Chest) state).getBlockInventory();
				
				for(int i = 0; i < inv.getContents().length; i++){
					if(inv.getContents()[i] != null && inv.getContents()[i].getType() == Material.WRITTEN_BOOK && plugin.getArena(player).getVillager() != null && ((BookMeta) inv.getContents()[i].getItemMeta()).getAuthor().matches(plugin.getArena(player).getVillager().getVillagerName())){				           	       
						plugin.getArena(player).getVillager().despawn();				
						
						HelpfulMethods.runTimedTask(plugin, new VillagerSwitchDelay(plugin, player), 1, 0);
						
				        cancel();
					}
				}
			}
		}
	}

}
