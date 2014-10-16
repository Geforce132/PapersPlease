package me.Geforce.plugin.patrons;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.Geforce.plugin.plugin_PapersPlease;
import me.Geforce.plugin.arena.PPArena;
import me.Geforce.plugin.documents.Document;
import me.Geforce.plugin.documents.DocumentType;
import me.Geforce.plugin.documents.EntryPermit;
import me.Geforce.plugin.documents.IDCard;
import me.Geforce.plugin.documents.Passport;
import me.Geforce.plugin.misc.HelpfulMethods;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class PPVillager {
	
	private plugin_PapersPlease plugin;
	private PPArena arena;
	private Entity entityVillager;
	private String villagerName;
	private Birthdate birthdate = new Birthdate();
	
	private List<Document> documents = new ArrayList<Document>();
	
	public PPVillager(plugin_PapersPlease plugin, PPArena arena, Entity entityVillager, String villagerName, FileConfiguration arenaConfig, boolean shouldPlacePapers){
		this.plugin = plugin;
		this.arena = arena;
		this.entityVillager = entityVillager;
		this.villagerName = villagerName;
	}
	
	public void createVillagerDocuments(){
		String country = this.generateCountry();
		String city = this.generateCity(country);
		ItemStack docItem = HelpfulMethods.createNewBook(villagerName, "Passport", ChatColor.BOLD + "" + ChatColor.UNDERLINE + country + ChatColor.RESET + "\n\nName: " + this.villagerName + "\nCity: " + city);
		Item doc = arena.getPlayer().getWorld().dropItem(plugin.getArena(arena.getPlayer()).getDocumentSpawnLocation(), docItem);
		
		ItemStack idCardItem = HelpfulMethods.createCustomItem(Material.NAME_TAG, "ID Card");
		Item idCard = arena.getPlayer().getWorld().dropItem(plugin.getArena(arena.getPlayer()).getDocumentSpawnLocation(), idCardItem);

		this.addPassport(doc, country, city);
		this.addIDCard(idCard);
	}
	
	private String generateCountry() {
		return HelpfulMethods.createCountry(plugin);
	}
	
	private String generateCity(String country) {
		return HelpfulMethods.getCityForCountry(plugin, country);
	}

	public void despawn(){
		this.entityVillager.remove();
		this.arena.setVillager(null);
	}
	
	public void addPassport(Item item, String country, String city){
		if(item.getItemStack().getItemMeta() instanceof BookMeta){
			BookMeta bookMeta = (BookMeta) item.getItemStack().getItemMeta();
			
			if(bookMeta.getTitle().matches("Passport")){
				this.documents.add(new Passport(item.getItemStack(), country, city, this.villagerName, this.getVillager().getProfession()));
			}
		}
	}
	
	public void addEntryPermit(Item item){
		ItemMeta meta = item.getItemStack().getItemMeta();
		if(meta.getDisplayName().matches("Entry Permit")){
			this.documents.add(new EntryPermit(item.getItemStack()));
		}
	}
	
	public void addIDCard(Item item){
		ItemMeta meta = item.getItemStack().getItemMeta();
		if(meta.getDisplayName().matches("ID Card")){
			this.documents.add(new IDCard(item.getItemStack()));	
		}
	}
	
	public Document getDocument(DocumentType docType){
		for(Document document : this.documents){
			if(document.getDocumentType() == docType){
				return document;
			}
		}
		
		return null;
	}
	
	public String getVillagerName(){
		return this.villagerName;
	}
	
	public Birthdate getBirthdate(){
		return this.birthdate;
	}
	
	public Villager getVillager(){
		return (Villager) this.entityVillager;
	}

}
