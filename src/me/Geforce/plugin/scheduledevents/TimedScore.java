package me.Geforce.plugin.scheduledevents;

import me.Geforce.plugin.plugin_PapersPlease;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class TimedScore extends BukkitRunnable{
	
	private plugin_PapersPlease plugin;
	private OfflinePlayer player;
	private Scoreboard boardToAddTo;
	private String scoreName;
	public int fullTime;
	private int timeRemaining;
	
	private Score timeRemainingScore;
	
	public TimedScore(plugin_PapersPlease plugin, OfflinePlayer player, Scoreboard boardToAddTo, String scoreName, int time){
		this.plugin = plugin;
		this.player = player;
		this.boardToAddTo = boardToAddTo;
		this.scoreName = scoreName;
		this.timeRemaining = time;
		this.fullTime = time;
	}

	public void run() {
		if(this.timeRemaining == this.fullTime){
			Objective o = this.boardToAddTo.getObjective("Papers please");
			o.setDisplayName("Papers, Please");
			o.setDisplaySlot(DisplaySlot.SIDEBAR);
			
			//System.out.println("Running full");
			
			this.timeRemainingScore = o.getScore(Bukkit.getServer().getOfflinePlayer(scoreName));
			this.timeRemainingScore.setScore(this.timeRemaining - 1);
			this.timeRemaining = this.timeRemainingScore.getScore();
			//System.out.println("Setting time remaining to " + this.timeRemainingScore.getScore());
			plugin.getArena(player).onTimeChanged(this);
		}else if(this.timeRemaining < this.fullTime && this.timeRemaining <= 0){
			//System.out.println("Running cancel");
			this.boardToAddTo.clearSlot(DisplaySlot.SIDEBAR);
			plugin.getArena(player).leaveArena(ChatColor.RED + "You have ran out of time! Leaving arena....");
			cancel();
		}else{
			//System.out.println("Running regular");
			this.timeRemainingScore.setScore(this.timeRemainingScore.getScore() - 1);
			this.timeRemaining = this.timeRemainingScore.getScore();
			plugin.getArena(player).onTimeChanged(this);
		}
	}

	public Scoreboard getBoardToAddTo() {
		return boardToAddTo;
	}

	public String getScoreName() {
		return scoreName;
	}

	public void setScoreName(String scoreName) {
		this.scoreName = scoreName;
	}

	public int getTimeRemaining() {
		return timeRemaining;
	}

	public void setTimeRemaining(int timeRemaining) {
		this.timeRemainingScore.setScore(timeRemaining);
	}
	
	public Score getTimeAsScore(){
		if(this.timeRemainingScore != null){
			return this.timeRemainingScore;
		}else{
			return null;
		}
	}

}
