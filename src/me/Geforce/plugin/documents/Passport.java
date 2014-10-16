package me.Geforce.plugin.documents;

import org.bukkit.entity.Villager.Profession;
import org.bukkit.inventory.ItemStack;

public class Passport extends Document {
	
	private String country;
	private String city;
	private String villagerName;
	private Profession villagerProfession;
	
	private int approvalState = -1;

	public Passport(ItemStack item, String country, String city, String villagerName, Profession profession) {
		super(item);
		this.country = country;
		this.city = city;
		this.villagerName = villagerName;
		this.villagerProfession = profession;
	}
	
	public int getApprovalState(){
		return this.approvalState;
	}
	
	public void setApprovalState(int par1) {
		this.approvalState = par1;
	}

	public DocumentType getDocumentType() {
		return DocumentType.PASSPORT;
	}

	public String getCountry() {
		return country;
	}

	public String getVillagerName() {
		return villagerName;
	}

	public Profession getVillagerProfession() {
		return villagerProfession;
	}


}
