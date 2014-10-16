package me.Geforce.plugin.documents;

import org.bukkit.inventory.ItemStack;

public class EntryPermit extends Document {

	public EntryPermit(ItemStack item) {
		super(item);
	}

	public DocumentType getDocumentType() {
		return DocumentType.ENTRY_PERMIT;
	}

}
