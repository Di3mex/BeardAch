package me.tehbeard.BeardAch.achievement.triggers;
import me.tehbeard.BeardAch.BeardAch;
import me.tehbeard.BeardAch.achievement.Achievement;
import me.tehbeard.BeardAch.dataSource.configurable.Configurable;
import me.tehbeard.BeardAch.dataSource.json.editor.EditorField;
import me.tehbeard.BeardStat.BeardStat;
import me.tehbeard.BeardStat.containers.PlayerStatManager;
import org.bukkit.entity.Player;

import com.google.gson.annotations.Expose;

/**
 * Checks if a players stat is between certain threshold, then triggers. 
 * @author James
 *
 */
@Configurable(tag="stat",name="stat above threshold")
public class StatCheckTrigger implements ITrigger {


    @Expose
    @EditorField(alias="Category")
	String cat;
    @Expose
    @EditorField(alias="Statistic")
	String stat;
    @Expose
    @EditorField(alias="Lower threshold")
	int threshold;
	private static PlayerStatManager manager = null;

	public void configure(Achievement ach,String config) {
		
		String[] opt = config.split("\\:");
		if(opt.length==3){
			cat = opt[0];
			stat = opt[1];
			threshold = Integer.parseInt(opt[2]);

		}
	}

	public boolean checkAchievement(Player player) {
		//if player has stat
		if(manager!=null){
			if(manager.findPlayerBlob(player.getName()).hasStat(cat, stat)){
				//if player exceeds threshold
				return manager.findPlayerBlob(player.getName()).getStat(cat, stat).getValue()>=threshold;
			}
		}
		else
        {
            BeardStat.printCon("[PANIC] Attempting to use Stat trigger when BeardStat not loaded!!!");
        }
		return false;
	}

    public void configure(Achievement ach) {
        if(manager==null){
            manager = BeardAch.self.getStats();
        }
        
    }

}
