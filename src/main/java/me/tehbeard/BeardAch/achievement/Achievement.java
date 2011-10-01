package me.tehbeard.BeardAch.achievement;

import java.util.HashSet;

import org.bukkit.entity.Player;

import me.tehbeard.BeardAch.achievement.rewards.IReward;
import me.tehbeard.BeardAch.achievement.triggers.ITrigger;

/**
 * Represents an achievement.
 * @author James
 *
 */
public class Achievement {

	private String name;
	private String descrip;
	private HashSet<ITrigger> triggers = new HashSet<ITrigger>();
	private HashSet<IReward> rewards = new HashSet<IReward>();
	public Achievement(String name,String descrip) {
	this.name = name;
	this.descrip = descrip;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDescrip(){
		return descrip;
	}
	
	public void addTrigger(ITrigger trigger){
		if(trigger==null){return;}
		triggers.add(trigger);
	}
	
	public void addReward(IReward reward){
		if(reward==null){return;}
		rewards.add(reward);
	}
	
	public boolean checkAchievement(Player player){
		if(player==null){
			return false;
		}
		for(ITrigger trigger:triggers){
			if(trigger.checkAchievement(player)){
				return false;
			}
		}
		for(IReward reward:rewards){
			reward.giveReward(player);
		}
		return true;
	}
}
