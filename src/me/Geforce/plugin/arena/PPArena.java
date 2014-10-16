package me.Geforce.plugin.arena;

import me.Geforce.plugin.plugin_PapersPlease;
import me.Geforce.plugin.misc.HelpfulMethods;
import me.Geforce.plugin.patrons.PPVillager;
import me.Geforce.plugin.scheduledevents.ArenaTimer;
import me.Geforce.plugin.scheduledevents.DocumentCollectionCheck;
import me.Geforce.plugin.scheduledevents.EventType;
import me.Geforce.plugin.scheduledevents.TimedScore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class PPArena {
	
	private plugin_PapersPlease plugin;
	private OfflinePlayer player;
	private FileConfiguration arenaConfig;
	private PPVillager villager;
	private TimedScore time;
	private ArenaTimer timer;
	private DocumentCollectionCheck collectionTimer;
	private Score playerScore;
	private Score playerErrors;

	private Location playerJoinLocation;
	private Location documentSpawnLocation;
	private Location documentCollectionLocation;

	
	public PPArena(plugin_PapersPlease plugin, OfflinePlayer player, FileConfiguration arenaConfig){
		this.plugin = plugin;
		this.player = player;
		this.arenaConfig = arenaConfig;
		this.playerJoinLocation = HelpfulMethods.parseDetailedLocationFromString((String) arenaConfig.get("JoinArenaLocation"));
		this.documentSpawnLocation = HelpfulMethods.parseLocationFromString((String) arenaConfig.get("PlacePapersLocation"));
		this.documentCollectionLocation = HelpfulMethods.parseLocationFromString((String) arenaConfig.get("DocumentCollectionLocation"));
		this.timer = new ArenaTimer(plugin, (Player) player);
		HelpfulMethods.runTimedTask(plugin, timer, 0, 1);
	}
	
	
	@SuppressWarnings("deprecation")
	public void startArena(Player player, boolean teleportPlayer){
		if(teleportPlayer){
			player.teleport(HelpfulMethods.parseDetailedLocationFromString((String) arenaConfig.get("JoinArenaLocation")));
		}
		
		this.givePlayerItems();
		
		Objective o = player.getScoreboard().getObjective("Papers please");
		playerScore = o.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.GOLD + "Score: "));
		playerScore.setScore(playerScore.getScore() + 1);
		
		playerErrors = o.getScore(Bukkit.getServer().getOfflinePlayer(ChatColor.RED + "Errors: "));
		playerErrors.setScore(0);
		
        Entity entity = player.getWorld().spawnEntity(HelpfulMethods.parseDetailedLocationFromString((String) arenaConfig.get("EntityDeskLocation")), EntityType.VILLAGER);	       
        
        String name = HelpfulMethods.createVillagerName(plugin);
    
        PPVillager villager = new PPVillager(plugin, this, entity, name, arenaConfig, true);
        this.villager = villager;
        
        this.sendDescriptionMessages();
        
        this.onArenaCreated();
	}

	private void givePlayerItems() {
		ItemStack inspectorsBook = HelpfulMethods.createNewBook("Geforce", "Inspector's Guidebook", ChatColor.UNDERLINE + "Inspector's guidebook:" + ChatColor.RESET + "\n \nTake the villager's documents, and check them for any discrepencies. \n \nUse your " + ChatColor.DARK_GREEN + "'approved'" + ChatColor.BLACK + " stamp to approve entry. \n \nUse your " + ChatColor.RED + "'denied'" + ChatColor.BLACK + " stamp to deny entry.", "\n\n       " + ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Countries", ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Minecraftia" + ChatColor.RESET + "\n\nIssuing cities: \n-Mineberg\n-Stoneville", ChatColor.BOLD + "" + ChatColor.UNDERLINE + "Enderberg" + ChatColor.RESET + "\n\nIssuing cities: \n-The End\n-Endslot");
        if(!((Player) player).getInventory().contains(inspectorsBook)){ ((Player) player).getInventory().addItem(inspectorsBook); }
        
        ItemStack approvedStamp = new ItemStack(351, 1, (short) 2);
        ItemMeta meta1 = approvedStamp.getItemMeta();
        meta1.setDisplayName(ChatColor.GREEN + "'Approved' stamp");
        approvedStamp.setItemMeta(meta1);
        if(!((Player) player).getInventory().contains(approvedStamp)){ 
        	((Player) player).getInventory().addItem(approvedStamp);
        }
        
        ItemStack deniedStamp = new ItemStack(351, 1, (short) 1);
        ItemMeta meta2 = deniedStamp.getItemMeta();
        meta2.setDisplayName(ChatColor.RED + "'Denied' stamp");
        deniedStamp.setItemMeta(meta2);
        if(!((Player) player).getInventory().contains(deniedStamp)){
        	((Player) player).getInventory().addItem(deniedStamp);
        }
	}


	private void onArenaCreated() {
		//HelpfulMethods.toggleBlock(getPlayer().getWorld(), HelpfulMethods.parseLocationFromString((String) arenaConfig.get("GateLeverLocation")), Material.REDSTONE_BLOCK);
	}
	

	public void onTimeChanged(TimedScore score) {
		if(score.getTimeRemaining() == (score.fullTime - 2)){
			//HelpfulMethods.toggleBlock(getPlayer().getWorld(), HelpfulMethods.parseLocationFromString((String) arenaConfig.get("GateLeverLocation")), Material.REDSTONE_BLOCK);
		}
	}
	
	public void toggleGate(){
		HelpfulMethods.toggleBlock(getPlayer().getWorld(), HelpfulMethods.parseLocationFromString((String) arenaConfig.get("GateLeverLocation")), Material.REDSTONE_BLOCK);
	}
		
	public void leaveArena(String leaveMessage){
		if(plugin.getArena(player).getTimer() != null){ plugin.getArena(player).getTimer().cancel(); }
		if(plugin.getArena(player).getCollectionCheckTimer() != null){ plugin.getArena(player).getCollectionCheckTimer().cancel(); }

		if(plugin.getArena(player).getVillager() != null){ plugin.getArena(player).getVillager().despawn(); }
		
		if(plugin.getArena(player).getArenaTimer() != null){ plugin.getArena(player).getArenaTimer().cancel(); }
		int tempScore = plugin.getArena(player).getScore();
		plugin.getArena(player).setScore(0);
		
		((Player) player).getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		
		Block chest = ((Player) player).getWorld().getBlockAt(HelpfulMethods.parseLocationFromString((String) arenaConfig.get("DocumentCollectionLocation")));
		BlockState state = chest.getState();

		if(state instanceof Chest){
			Inventory inv = ((Chest) state).getBlockInventory();
			inv.clear();
		}
				
		((Player) player).teleport(plugin.getArena(player).getPlayerJoinLocation());
		plugin.removeArena(player);
		((Player) player).sendMessage(leaveMessage);
		
		((Player) player).getInventory().clear();
		
		plugin.getEconomy().depositPlayer(player, (tempScore * 2));
		((Player) player).sendMessage(ChatColor.GOLD + "You have earned $" + (tempScore * 2) + ".");

	}
	
	private void sendDescriptionMessages() {
		timer.addMessage("<" + getPlayer().getName() + "> Papers, please.", 1);
		timer.addMessage("<" + villager.getVillagerName() + "> Here you go.", 2);
		timer.addEvent(EventType.SPAWN_DOCUMENTS, 2);
		
		timer.addEvent(EventType.TOGGLE_GATE, 1);
		timer.addEvent(EventType.TOGGLE_GATE, 2);
	}


	public PPVillager getVillager(){ 
		return this.villager;
	}
	
	public void setVillager(PPVillager villager){
		this.villager = villager;
	}
	
	public Player getPlayer(){
		return (Player) this.player;
	}
	
	public TimedScore getTimer(){
		if(time != null){
			return this.time;
		}else{
			return null;
		}
	}
	
	public void setTimer(TimedScore par1){
		this.time = par1;
	}
	
	public ArenaTimer getArenaTimer(){
		return this.timer;
	}

	public void setRoundTimeRemaining(String par1, int par2){
		if(time == null){
			time = new TimedScore(plugin, player, ((Player) player).getScoreboard(), par1, par2);
		}else{
			time.setTimeRemaining(par2);
		}
	}
	
	public int getScore(){
		return this.playerScore.getScore();
	}
	
	public void setScore(int par1){
		this.playerScore.setScore(par1);
	}
	
	public Location getPlayerJoinLocation(){
		return this.playerJoinLocation;
	}
	
	public Location getDocumentSpawnLocation(){
		return this.documentSpawnLocation;
	}
	
	public Location getDocumentCollectionLocation(){
		return this.documentCollectionLocation;
	}
	
	public Scoreboard getScoreboard(){
		return this.plugin.getInfoBoard();
	}
	
	public void setScoreboard(Scoreboard board){
		this.plugin.setInfoBoard(board);
	}
	
	public DocumentCollectionCheck getCollectionCheckTimer(){
		return this.collectionTimer;
	}
	
	public void setCollectionCheckTimer(DocumentCollectionCheck par1){
		this.collectionTimer = par1;
	}

}