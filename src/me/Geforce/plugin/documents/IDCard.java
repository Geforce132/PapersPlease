package me.Geforce.plugin.documents;

import org.bukkit.inventory.ItemStack;

public class IDCard extends Document{

	public IDCard(ItemStack item) {
		super(item);
	}

	public DocumentType getDocumentType() {
		return DocumentType.ID_CARD;
	}

}
