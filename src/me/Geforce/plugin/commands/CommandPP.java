package me.Geforce.plugin.commands;

import java.io.File;
import java.io.IOException;

import me.Geforce.plugin.plugin_PapersPlease;
import me.Geforce.plugin.arena.PPArena;
import me.Geforce.plugin.misc.HelpfulMethods;
import me.Geforce.plugin.scheduledevents.DocumentCollectionCheck;
import me.Geforce.plugin.scheduledevents.TimedScore;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;

public class CommandPP implements CommandExecutor {
	
	private plugin_PapersPlease plugin;

	public CommandPP(plugin_PapersPlease plugin) {
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender par1CommandSender, Command par2Command, String paramString, String[] commandArgs) {
		if(!(par1CommandSender instanceof Player)){
			plugin.logger.info("Only players can use this command!");
			return true;
		}
		
		final Player player = (Player) par1CommandSender;
		
		if(par2Command.getName().equalsIgnoreCase("pp")){
			if(commandArgs[0].equalsIgnoreCase("create")){
				System.out.println("Running");
				File folder = new File(plugin.getDataFolder(), "Arenas");
				File configFile = new File(plugin.getDataFolder(), (new StringBuilder("Arenas/")).append(commandArgs[1]).append(".yml").toString());
				if(!folder.exists()){
		            folder.mkdirs();
				}
				
				if(configFile.exists()){
					player.sendMessage(ChatColor.RED + "A arena called '" + commandArgs[1] + "' already exists!");
					return true;
				}else{
					try{
						configFile.createNewFile();
						
						FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(configFile);
						FileConfiguration namesList = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "names.yml"));
						arenaConfig.set("PlayerSpawn", HelpfulMethods.parseDetailedLocationToString(player.getLocation()));
						arenaConfig.set("EntityDeskLocation", "");
						arenaConfig.set("GateLeverLocation", "");
						arenaConfig.set("PlacePapersLocation", "");
						arenaConfig.set("DocumentCollectionLocation", "");
						
						
						arenaConfig.save(configFile);
					}catch(IOException e){
						e.printStackTrace();
					}	
				}
				
			
				
				player.sendMessage(ChatColor.GREEN + "Papers Please arena created, the player's spawnpoint is set at your current location.");
				
				return true;
			}else if(commandArgs[0].equalsIgnoreCase("delete")){
				File file = new File(plugin.getDataFolder(), "Arenas/" + commandArgs[1] + ".yml");
				
				if(file.exists()){
					file.delete();
					
					player.sendMessage(ChatColor.GREEN + "Arena '" + commandArgs[1] + "' successfully deleted!");
				}else{
					player.sendMessage(ChatColor.RED + "Arena '" + commandArgs[1] + "' does not exist!");
				}
				
				return true;
			}else if(commandArgs[0].equalsIgnoreCase("addgatelever")){
				File arenaFile = new File(plugin.getDataFolder(), "Arenas/" + commandArgs[4] + ".yml");
	            FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
	            arenaConfig.set("GateLeverLocation", HelpfulMethods.parseLocationToString(new Location(player.getWorld(), Double.parseDouble(commandArgs[1]), Double.parseDouble(commandArgs[2]), Double.parseDouble(commandArgs[3]))));
	            
	            try
	            {
	                arenaConfig.save(arenaFile);
	                player.sendMessage(ChatColor.GREEN + "Gate lever location added to arena '" + commandArgs[4] + "' at your current location.");
	            }
	            catch(IOException e)
	            {
	                e.printStackTrace();
	            }
				
				return true;
			}else if(commandArgs[0].equalsIgnoreCase("adddesklocation")){
				File arenaFile = new File(plugin.getDataFolder(), "Arenas/" + commandArgs[1] + ".yml");
	            FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
	            String startSpawn = HelpfulMethods.parseDetailedLocationToString(player.getLocation());
	            arenaConfig.set("EntityDeskLocation", startSpawn);
	            try
	            {
	                arenaConfig.save(arenaFile);
	                player.sendMessage(ChatColor.GREEN + "Desk location added to arena '" + commandArgs[1] + "' at your current location.");
	            }
	            catch(IOException e)
	            {
	                e.printStackTrace();
	            }
				
				return true;
			}else if(commandArgs[0].equalsIgnoreCase("addpaperslocation")){
				File arenaFile = new File(plugin.getDataFolder(), "Arenas/" + commandArgs[1] + ".yml");
	            FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
	            String startSpawn = HelpfulMethods.parseLocationToString(player.getLocation());
	            arenaConfig.set("PlacePapersLocation", startSpawn);
	            try
	            {
	                arenaConfig.save(arenaFile);
	                player.sendMessage(ChatColor.GREEN + "Document spawn location added to arena '" + commandArgs[1] + "' at your current location.");
	            }
	            catch(IOException e)
	            {
	                e.printStackTrace();
	            }
				
				return true;
			}else if(commandArgs[0].equalsIgnoreCase("addcollectionlocation")){
				File arenaFile = new File(plugin.getDataFolder(), "Arenas/" + commandArgs[4] + ".yml");
	            FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
	            String startSpawn = HelpfulMethods.parseLocationToString(new Location(player.getWorld(), Double.parseDouble(commandArgs[1]), Double.parseDouble(commandArgs[2]), Double.parseDouble(commandArgs[3])));
	            arenaConfig.set("DocumentCollectionLocation", startSpawn);
	            try
	            {
	                arenaConfig.save(arenaFile);
	                player.sendMessage(ChatColor.GREEN + "Document collection location added to arena '" + commandArgs[4] + "' at your current location.");
	            }
	            catch(IOException e)
	            {
	                e.printStackTrace();
	            }
				
				return true;
			}else if(commandArgs[0].equalsIgnoreCase("start")){
				if(plugin.ppArenas.containsKey(player)){ player.sendMessage(ChatColor.RED + "You are already in a arena!"); return true; }
				if(!HelpfulMethods.playerHasEmptyInventory(player)){ player.sendMessage("Your inventory must be empty before joining a Papers, Please arena!"); return true; }
				File arenaFile = new File(plugin.getDataFolder(), "Arenas/" + commandArgs[1] + ".yml");
	            FileConfiguration arenaConfig = YamlConfiguration.loadConfiguration(arenaFile);
	            
				arenaConfig.set("JoinArenaLocation", HelpfulMethods.parseDetailedLocationToString(player.getLocation()));
				
				try{
	                arenaConfig.save(arenaFile);
	            }
	            catch(IOException e){
	                e.printStackTrace();
	            }
				
				plugin.ppArenas.put(player, new PPArena(plugin, player, arenaConfig));
				
				plugin.getArena(player).startArena(player, true);
				         	            	            	         	            
	            TimedScore task = new TimedScore(plugin, player, player.getScoreboard(), ChatColor.GREEN + "Time: ", 60);
	            
	            HelpfulMethods.runTimedTask(plugin, task, 0, 1);
	            
	            plugin.getArena(player).setTimer(task);
	            
	            DocumentCollectionCheck task1 = new DocumentCollectionCheck(HelpfulMethods.parseLocationFromString((String) arenaConfig.get("DocumentCollectionLocation")), plugin, player);
	            
	            HelpfulMethods.runTimedTask(plugin, task1, 0, 1);
	            
	            plugin.getArena(player).setCollectionCheckTimer(task1);
	           	            
	            return true;
			}else if(commandArgs[0].equalsIgnoreCase("clearscore")){
				if(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) != null){
					player.getScoreboard().resetScores("Score: ");					player.getScoreboard().resetScores(ChatColor.GOLD + "Time: ");
					player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
				}
				
				return true;
			}else if(commandArgs[0].equalsIgnoreCase("leave")){
				if(plugin.getArena(player) == null){ player.sendMessage(ChatColor.RED + "You are not in a Papers Please arena!"); return true;}
								
				plugin.getArena(player).leaveArena(ChatColor.RED + "Sucessfully left arena.");
								
				return true;
			}
		}
		
		
		return false;
	}

}
