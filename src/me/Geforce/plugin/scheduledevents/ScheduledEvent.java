package me.Geforce.plugin.scheduledevents;

import me.Geforce.plugin.plugin_PapersPlease;

import org.bukkit.entity.Player;

public class ScheduledEvent {
	
	private plugin_PapersPlease plugin;
	private Player player;
	private EventType event;
	private int time;
	
	public ScheduledEvent(plugin_PapersPlease plugin, Player player, EventType event, int time){
		this.plugin = plugin;
		this.player = player;
		this.event = event;
		this.time = time;
	}
	
	protected void run() {
		if(event == EventType.SPAWN_DOCUMENTS){
			plugin.getArena(player).getVillager().createVillagerDocuments();
		}else if(event == EventType.TOGGLE_GATE){
			plugin.getArena(player).toggleGate();
		}
	}

	public EventType getEvent() {
		return event;
	}

	public int getTime() {
		return time;
	}
	
	public void setTime(int par1){
		this.time = par1;
	}

}
