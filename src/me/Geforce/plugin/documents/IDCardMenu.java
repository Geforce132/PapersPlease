package me.Geforce.plugin.documents;

import java.util.Arrays;
import java.util.List;

import me.Geforce.plugin.plugin_PapersPlease;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IDCardMenu implements Listener{
	
	private plugin_PapersPlease plugin;
	private Player player;
	private ItemStack[] keys = new ItemStack[9];
	private Inventory inventory;
	
	public IDCardMenu(plugin_PapersPlease plugin, Player player){
		this.plugin = plugin;
		this.player = player;
		this.inventory = Bukkit.getServer().createInventory(null, 9, "ID Card");
		
		this.keys[0] = this.createItem(Material.WATCH, 1, "Date of birth", Arrays.asList(plugin.getArena(player).getVillager().getBirthdate().getMonth() + "-" + plugin.getArena(player).getVillager().getBirthdate().getDay() + "-" + plugin.getArena(player).getVillager().getBirthdate().getYear()));
		this.keys[1] = this.createItem(Material.IRON_PICKAXE, 1, "Profession", Arrays.asList(plugin.getArena(player).getVillager().getVillager().getProfession().toString().toLowerCase()));
		
		for(int i = 0; i < keys.length; i++){
			this.inventory.setItem(i, keys[i]);
		}
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	private ItemStack createItem(Material item, int stackSize, String itemName, List<String> lore){
		ItemStack i = new ItemStack(item);
		ItemMeta im = i.getItemMeta();
		im.setLore(lore);
		im.setDisplayName(itemName);
		i.setItemMeta(im);
		i.setAmount(stackSize);
		return i;
	}
	
	public void openInventory(Player p){
		p.openInventory(inventory);
	}
	
	public Inventory asInventory(){
		return this.inventory;
	}
	
	@EventHandler
	public void onItemClicked(InventoryClickEvent event){
		if(!event.getInventory().equals(inventory) || event.getCurrentItem() == null || event.getCurrentItem().getItemMeta() == null || event.getCurrentItem().getItemMeta().getDisplayName() == null){ return; };
		
		if(event.getCurrentItem().getItemMeta().getDisplayName().matches("Date of birth") || event.getCurrentItem().getItemMeta().getDisplayName().matches("Profession")){
			event.setCancelled(true);
			return;
		}
	}

}
