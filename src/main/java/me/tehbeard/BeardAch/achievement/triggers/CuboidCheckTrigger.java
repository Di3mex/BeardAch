package me.tehbeard.BeardAch.achievement.triggers;

import java.util.ArrayList;

import me.tehbeard.BeardAch.achievement.Achievement;
import me.tehbeard.BeardAch.dataSource.configurable.Configurable;
import me.tehbeard.BeardAch.dataSource.configurable.Usage;
import me.tehbeard.utils.cuboid.Cuboid;


import org.bukkit.entity.Player;

/**
 * Checks if a players is in a cuboid
 * @author James
 *
 */
@Configurable(tag="cuboid")
@Usage(arguments={"world|World for cuboid","x1|","y1|","z1|","x2|","y2|","z2|"},packageName="base",blurb="Provides a dependency free area entry trigger")
public class CuboidCheckTrigger implements ITrigger {


	private Cuboid c = new Cuboid();

	public Cuboid getCuboid(){
	    return c;
	}
	
	public void configure(Achievement ach,String config) {
			c.setCuboid(config);
	}
	
	public String save(){return "cuboid|" + cuboid.toString();}

	public boolean checkAchievement(Player player) {
		//if player has stat
		//if(player.getWorld().getName().equals(world)){

		return c.isInside(player.getLocation());
	}
	
	public ArrayList<String> getCache(){
		return c.getChunks();
	}

    

}
