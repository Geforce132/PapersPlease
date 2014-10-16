package me.Geforce.plugin.misc;

import java.io.File;
import java.util.List;
import java.util.Random;

import me.Geforce.plugin.plugin_PapersPlease;
import me.Geforce.plugin.patrons.PPVillager;
import net.minecraft.server.v1_7_R3.EntityCreature;
import net.minecraft.server.v1_7_R3.EntityHuman;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_7_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R3.entity.CraftCreature;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class HelpfulMethods {
	
	public static String parseLocationToString(Location location){
        String worldName = location.getWorld().getName();
        String xValue = (new StringBuilder(String.valueOf(location.getX()))).toString();
        String yValue = (new StringBuilder(String.valueOf(location.getY()))).toString();
        String zValue = (new StringBuilder(String.valueOf(location.getZ()))).toString();
        String locationString = worldName + ";" + xValue + ";" + yValue + ";" + zValue;
        return locationString;
    }
	
	public static Location parseLocationFromString(String locationString){
        String locationParsed[] = locationString.split(";");
        World world = Bukkit.getWorld(locationParsed[0]);
        Double x = Double.valueOf(Double.parseDouble(locationParsed[1]));
        Double y = Double.valueOf(Double.parseDouble(locationParsed[2]));
        Double z = Double.valueOf(Double.parseDouble(locationParsed[3]));
        Location l = new Location(world, x.doubleValue(), y.doubleValue(), z.doubleValue());
        return l;
    }
	
	public static Location parseDetailedLocationFromString(String locationString){
        String locationParsed[] = locationString.split(";");
        World world = Bukkit.getWorld(locationParsed[0]);
        Double x = Double.valueOf(Double.parseDouble(locationParsed[1]));
        Double y = Double.valueOf(Double.parseDouble(locationParsed[2]));
        Double z = Double.valueOf(Double.parseDouble(locationParsed[3]));
        float pitch = Float.parseFloat(locationParsed[4]);
        float yaw = Float.parseFloat(locationParsed[5]);
        Location l = new Location(world, x.doubleValue(), y.doubleValue(), z.doubleValue(), yaw, pitch);
        return l;
    }

	public static String parseDetailedLocationToString(Location location){
        String worldName = location.getWorld().getName();
        String xValue = (new StringBuilder(String.valueOf(location.getX()))).toString();
        String yValue = (new StringBuilder(String.valueOf(location.getY()))).toString();
        String zValue = (new StringBuilder(String.valueOf(location.getZ()))).toString();
        String pitch = (new StringBuilder(String.valueOf(location.getPitch()))).toString();
        String yaw = (new StringBuilder(String.valueOf(location.getYaw()))).toString();
        String locationString = (new StringBuilder(String.valueOf(worldName))).append(";").append(xValue).append(";").append(yValue).append(";").append(zValue).append(";").append(pitch).append(";").append(yaw).toString();
        return locationString;
    }
	
	public static String createVillagerName(plugin_PapersPlease plugin){
		FileConfiguration nameConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "info.yml"));
		
		List namesList = nameConfig.getStringList("Names");
		Random random = new Random();
		int nameIndex = random.nextInt(namesList.size());
		
		return (String) namesList.get(nameIndex);
	}
	
	public static String createCountry(plugin_PapersPlease plugin){
		FileConfiguration infoConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "info.yml"));
		
		List countryList = infoConfig.getStringList("IssuingCountries");
		Random random = new Random();
		int nameIndex = random.nextInt(countryList.size());
		
		return (String) countryList.get(nameIndex);
	}

	public static String getCityForCountry(plugin_PapersPlease plugin, String country) {
		FileConfiguration infoConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "info.yml"));
		
		List cityList = infoConfig.getStringList(country + "IssuingCities");
		Random random = new Random();
		int nameIndex = random.nextInt(cityList.size());
		
		return (String) cityList.get(nameIndex);
	}
	
	public static void moveEntityToLocation(LivingEntity entity, Location targetLocation, float speed){
		EntityCreature nmsEntity = ((CraftCreature) entity).getHandle();
		//PathEntity path = nmsEntity.getNavigation().a(targetLocation.getX(), targetLocation.getY(), targetLocation.getZ());
		//nmsEntity.setPathEntity(path);
		//nmsEntity.getNavigation().a(path, speed);
		//nmsEntity.set
		nmsEntity.getNavigation().a(targetLocation.getX(), targetLocation.getY() + 1, targetLocation.getZ(), speed);
	}
	
	/**
	 * Switches given lever's state (on, off).
	 * 
	 * Uses both the Bukkit API and CraftBukkit.
	 */
	
	public static void toggleLever2(World world, Block block){
		HelpfulMethods.toggleLever2(world, block.getLocation());
	}
	
	public static void toggleLever2(World world, Location location){
		Block block = world.getBlockAt(location);
		if(block.getType() != Material.LEVER){ return; }
		
		boolean wasOn = (block.getData() & 0x8) > 0;

		boolean shouldBeOn = wasOn ? false : true;
		
		
		if(wasOn != shouldBeOn){
			net.minecraft.server.v1_7_R3.Block vBlock = net.minecraft.server.v1_7_R3.Block.e(Material.LEVER.getId());
			net.minecraft.server.v1_7_R3.World vWorld = ((CraftWorld) block.getWorld()).getHandle();
			
			vBlock.interact(vWorld, block.getX(), block.getY(), block.getZ(), (EntityHuman)null, 0, 0, 0, 0);
		}
	}
	
	public static void toggleBlock(World world, Location location, Material material){
		if(world.getBlockAt(location).getType() == material){
			world.getBlockAt(location).setType(Material.AIR);
		}else{
			world.getBlockAt(location).setType(material);
		}
	}
	
	public static ItemStack createNewBook(String author, String title, String... pages){
		ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
		
		BookMeta meta = (BookMeta) book.getItemMeta();
		
		meta.setTitle(title);
		meta.setAuthor(author);
		meta.addPage(pages);
		
		book.setItemMeta(meta);
				
		return book;
	}
	
	public static ItemStack createCustomItem(Material itemType, String name) {
		ItemStack item = new ItemStack(itemType);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		
		return item;
	}
	
	public static void runTimedTask(Plugin plugin, BukkitRunnable task, long delay, long time){
		task.runTaskTimer(plugin, (delay * 20), (time * 20));
	}

	public static boolean playerHasEmptyInventory(Player player) {
		for(ItemStack stack : player.getInventory().getContents()){
			if(stack != null){
				return false;
			}else{
				continue;
			}
		}
		
		return true;
	}

	
}
