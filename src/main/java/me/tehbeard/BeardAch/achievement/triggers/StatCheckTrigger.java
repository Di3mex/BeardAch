package me.tehbeard.BeardAch.achievement.triggers;
import me.tehbeard.BeardAch.BeardAch;
import me.tehbeard.BeardAch.achievement.Achievement;
import me.tehbeard.BeardAch.dataSource.configurable.Configurable;
import me.tehbeard.BeardAch.dataSource.json.editor.EditorField;
import com.tehbeard.BeardStat.BeardStat;
import com.tehbeard.BeardStat.containers.PlayerStatManager;
import org.bukkit.Bukkit;
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
    @EditorField(alias="Domain")
    String domain = "default";
    @Expose
    @EditorField(alias="World")
    String world = "*";
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
            //if player exceeds threshold
            BeardStat.printCon(String.format("Checking achievement %s %s %s %s", domain, world, cat, stat));
            BeardStat.printCon(String.format("Values is %d", manager.findPlayerBlob(player.getName()).getStats(domain,world,cat, stat).getValue()));
            return manager.findPlayerBlob(player.getName()).getStats(domain,world,cat, stat).getValue()>=threshold;
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


    /**
     * Get the category of the stat
     * @return
     */
    public String getCategory(){
        return cat;
    }


    /**
     * Get the actual identifier of the stat
     * @return
     */
    public String getStat(){
        return  stat;
    }


    /**
     * Get the domain, whatever that is supposed to mean
     * @return
     */
    public String getDomain(){
        return domain;
    }


    /**
     * Get the world of this stat
     * @return
     */
    public String getWorld(){
        return world;
    }


    /**
     * Get the threshold when the trigger will fire
     * @return
     */
    public int getThreshold(){
        return threshold;
    }
}
