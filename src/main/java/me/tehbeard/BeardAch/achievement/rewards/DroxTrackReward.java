package me.tehbeard.BeardAch.achievement.rewards;


import me.tehbeard.BeardAch.BeardAch;
import me.tehbeard.BeardAch.achievement.Achievement;
import me.tehbeard.BeardAch.dataSource.configurable.Configurable;
import me.tehbeard.BeardAch.dataSource.json.editor.EditorField;

import org.bukkit.entity.Player;

import com.google.gson.annotations.Expose;

@Configurable(tag="promote",name="(DroxPerms) Promote along track")
public class DroxTrackReward implements IReward{
    @Expose
    @EditorField(alias="Track to promote along")
	String track = "";
	public void configure(Achievement Ach,String config) {
		
		track = config;
	
	}

	
	public void giveReward(Player player) {
		if(BeardAch.droxAPI!=null){
			BeardAch.droxAPI.promotePlayer(player.getName(), track);
			BeardAch.printDebugCon("Played Promoted");
		}
	}


    public void configure(Achievement ach) {
        
    }

}
