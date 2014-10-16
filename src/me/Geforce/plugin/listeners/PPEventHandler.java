package me.Geforce.plugin.listeners;

import me.Geforce.plugin.plugin_PapersPlease;
import me.Geforce.plugin.documents.Document;
import me.Geforce.plugin.documents.DocumentType;
import me.Geforce.plugin.documents.Passport;
import me.Geforce.plugin.documents.IDCardMenu;
import me.Geforce.plugin.misc.HelpfulMethods;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class PPEventHandler implements Listener {
	
	private plugin_PapersPlease plugin;

	public PPEventHandler(plugin_PapersPlease plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onItemUsed(PlayerInteractEvent event){
		if(plugin.ppArenas.containsKey((OfflinePlayer) event.getPlayer()) && event.hasItem()){	
			ItemStack stack = event.getItem();
			Inventory inventory = event.getPlayer().getInventory();
			if(stack.getType() != Material.WRITTEN_BOOK && stack.hasItemMeta() && stack.getItemMeta().getDisplayName().matches(ChatColor.GREEN + "'Approved' stamp")){
				event.setCancelled(true);
				for(ItemStack stackToCheck : inventory.getContents()){
					if(stackToCheck != null && stackToCheck.hasItemMeta() && stackToCheck.getItemMeta() instanceof BookMeta && ((BookMeta) stackToCheck.getItemMeta()).getTitle().matches("Passport")){
						BookMeta meta = (BookMeta) stackToCheck.getItemMeta();
						
						ItemStack newPassport = HelpfulMethods.createNewBook(meta.getAuthor(), "Passport - " + ChatColor.GREEN + "Approved", meta.getPage(1));
												
						inventory.removeItem(stackToCheck);
						
						event.getPlayer().getInventory().addItem(newPassport);
						Document doc = plugin.getArena((OfflinePlayer) event.getPlayer()).getVillager().getDocument(DocumentType.PASSPORT);
						((Passport) doc).setApprovalState(0);
						break;
					}
				}
			}else if(stack.getType() != Material.WRITTEN_BOOK && event.getItem().hasItemMeta() && event.getItem().getItemMeta().getDisplayName().matches(ChatColor.RED + "'Denied' stamp")){
				event.setCancelled(true);
				for(ItemStack stackToCheck : inventory.getContents()){
					if(stackToCheck != null && stackToCheck.hasItemMeta() && stackToCheck.getItemMeta() instanceof BookMeta && ((BookMeta) stackToCheck.getItemMeta()).getTitle().matches("Passport")){
						BookMeta meta = (BookMeta) stackToCheck.getItemMeta();
						
						ItemStack newPassport = HelpfulMethods.createNewBook(meta.getAuthor(), "Passport - " + ChatColor.RED + "Denied", meta.getPage(1));
						
						inventory.removeItem(stackToCheck);
						
						event.getPlayer().getInventory().addItem(newPassport);
						Document doc = plugin.getArena((OfflinePlayer) event.getPlayer()).getVillager().getDocument(DocumentType.PASSPORT);
						((Passport) doc).setApprovalState(1);
						break;
					}
				}
			}else if(stack.getType() == Material.NAME_TAG && event.getItem().getItemMeta().getDisplayName().matches("ID Card")){
				event.setCancelled(true);
				IDCardMenu menu = new IDCardMenu(plugin, event.getPlayer());
				event.getPlayer().openInventory(menu.asInventory());
			}else{
				return;
			}
		}
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event){
		if(plugin.ppArenas.containsKey((OfflinePlayer) event.getPlayer())){
			Document doc = plugin.getArena((OfflinePlayer) event.getPlayer()).getVillager().getDocument(DocumentType.PASSPORT);
			if(((Passport) doc).getApprovalState() == -1){
				event.setCancelled(true);
				event.getPlayer().sendMessage(ChatColor.RED + "Approve or deny the passport before giving the documents back to the villager.");
			}
		}
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		if(plugin.ppArenas.containsKey(event.getPlayer())){
			plugin.ppArenas.remove(event.getPlayer());
		}
	}
	
}
