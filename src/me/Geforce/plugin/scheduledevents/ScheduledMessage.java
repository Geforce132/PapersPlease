package me.Geforce.plugin.scheduledevents;

public class ScheduledMessage {
	
	private String message;
	private int time;
	
	public ScheduledMessage(String message, int time){
		this.message = message;
		this.time = time;
	}

	public String getMessage() {
		return message;
	}

	public int getTime() {
		return time;
	}
	
	public void setTime(int par1){
		this.time = par1;
	}

}
