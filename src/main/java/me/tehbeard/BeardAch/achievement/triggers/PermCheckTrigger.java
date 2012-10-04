package me.tehbeard.BeardAch.achievement.triggers;


import me.tehbeard.BeardAch.achievement.Achievement;
import me.tehbeard.BeardAch.dataSource.configurable.Configurable;

import org.bukkit.entity.Player;

/**
 * Checks if a player has a permission node
 * @author James
 *
 */
@Configurable(tag="perm")
public class PermCheckTrigger implements ITrigger {

	String perm;

	public void configure(Achievement ach,String config) {
		perm = config;
	}
	
	public String save(){return "perm|" + perm;}

	
	public boolean checkAchievement(Player player) {
		//if player has stat
		return player.hasPermission(perm);
	}

}
