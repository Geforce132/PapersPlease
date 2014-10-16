package me.Geforce.plugin.scheduledevents;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.Geforce.plugin.plugin_PapersPlease;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ArenaTimer extends BukkitRunnable{
	
	private plugin_PapersPlease plugin;
	private Player player;
	
	private List<ScheduledMessage> messageQuery = new ArrayList<ScheduledMessage>();
	private List<ScheduledEvent> eventQuery = new ArrayList<ScheduledEvent>();
	
	public ArenaTimer(plugin_PapersPlease plugin, Player player){
		this.plugin = plugin;
		this.player = player;
	}

	public void run() {
		System.out.println("Running");
		for(int i = 0; i < messageQuery.size(); i++){
			ScheduledMessage message = messageQuery.get(i);
			
			message.setTime(message.getTime() - 1);
			
			if(message.getTime() == 0){
				player.sendMessage(message.getMessage());
				messageQuery.remove(i);
			}
		}
		
		for(int i = 0; i < eventQuery.size(); i++){
			ScheduledEvent event = eventQuery.get(i);
			System.out.println("Event " + i + "'s time is now " + (event.getTime() - 1) + " from " + event.getTime());
			event.setTime(event.getTime() - 1);
			
			if(event.getTime() == 0){
				event.run();
				System.out.println("Event " + i + " toggling gate");
				eventQuery.remove(i);
			}
		}
	}
	
	public void addMessage(String message, int time){
		this.messageQuery.add(new ScheduledMessage(message, time));
	}
	
	public void addEvent(EventType event, int time){
		System.out.println("Creating new event");
		this.eventQuery.add(new ScheduledEvent(plugin, player, event, time));
	}

}
