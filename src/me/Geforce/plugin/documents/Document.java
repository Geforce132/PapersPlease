package me.Geforce.plugin.documents;

import org.bukkit.inventory.ItemStack;

public abstract class Document {
	
	private ItemStack document;
	
	public Document(ItemStack item){
		this.document = item;
	}
	
	public abstract DocumentType getDocumentType();
	
	public ItemStack getDocumentAsItem(){
		return this.document;
	}

}
