package me.tehbeard.BeardAch.achievement.triggers;


import me.tehbeard.BeardAch.BeardAch;
import me.tehbeard.BeardAch.achievement.Achievement;
import me.tehbeard.BeardAch.dataSource.configurable.Configurable;

import org.bukkit.entity.Player;

/**
 * Always false
 * @author James
 *
 */
@Configurable(tag="locked")
public class LockedTrigger implements ITrigger {


	public void configure(Achievement ach,String config) {
	    BeardAch.printCon("[ALERT] Achievement " + ach.getName() + " is locked and will not trigger");
	}
	public String save(){
	    return "locked|";
	}

	public boolean checkAchievement(Player player) {

		return false;
	}

}
