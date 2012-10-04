package me.tehbeard.BeardAch.achievement.triggers;

import java.util.HashMap;
import java.util.Map;

import me.tehbeard.BeardAch.achievement.Achievement;
import me.tehbeard.BeardAch.dataSource.configurable.Configurable;
import me.tehbeard.utils.cuboid.Cuboid;

import org.bukkit.entity.Player;

@Configurable(tag="speedrun")
public class SpeedRunTrigger implements ITrigger {

    private Cuboid startCuboid = new Cuboid();
    private Cuboid endCuboid = new Cuboid();
    long timing = 0L;

    Map<String,Long> states = new HashMap<String,Long> ();

    public Cuboid getStartCuboid(){
        return startCuboid;
    }

    public Cuboid getEndCuboid(){
        return endCuboid;
    }

    public void configure(Achievement ach,String config) {
        String[] c= config.split("\\/");
        if(c.length == 3){
            startCuboid.setCuboid(c[0]);
            endCuboid.setCuboid(c[1]);
            timing = Long.parseLong(c[2]);
        }else
        {
            System.out.println("speedrun invalid");
        }
    }
    
    public String save(){return "speedrun|" + startCuboid.toString() + "/" + + endCuboid.toString() + "/" + timing;}

    public boolean checkAchievement(Player player) {
        
        //if inside start cuboid and state does not exist, create record
        if((startCuboid.isInside(player.getLocation()))){
            startTimer(player.getName());
            return false;
        }
        //if inside end cuboid, and state exists, check and return value
        if(endCuboid.isInside(player.getLocation()) &&
                inTime(player.getName())){
            clearTimer(player.getName());
            return true;
        }
        return false;
    }



    /**
     * Start timer for a player
     * @param player
     */
    private void startTimer(String player){
        states.put(player, System.currentTimeMillis());
    }

    /**
     * Clear timer for a player
     * @param player
     */
    private void clearTimer(String player){
        states.remove(player);
    }

    /**
     * Has the players time expired?
     * @param player
     * @return
     */
    private boolean inTime(String player){
        if(hasTime(player)){
        return (System.currentTimeMillis() - states.get(player))/1000L <= timing;
        }
        return false;
            
    }
    
    /**
     * Do they have a time in the system
     * @param player
     * @return
     */
    private boolean hasTime(String player){
        return states.containsKey(player);
    }


}
