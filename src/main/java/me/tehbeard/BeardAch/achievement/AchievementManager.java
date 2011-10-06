package me.tehbeard.BeardAch.achievement;
import java.util.*;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.tehbeard.BeardAch.BeardAch;
import me.tehbeard.BeardAch.dataSource.IDataSource;
import me.tehbeard.BeardAch.dataSource.NullDataSource;
import me.tehbeard.BeardAch.dataSource.SqlDataSource;
/**
 * Manages the link between achievements and players
 * @author James
 *
 */
public class AchievementManager {

	private static HashMap<String,HashSet<String>> playerHasCache = new  HashMap<String,HashSet<String>>();
	private static HashMap<Achievement,HashSet<String>> playerCheckCache = new  HashMap<Achievement,HashSet<String>>();


	private static HashMap<String,Achievement> achievements = new HashMap<String,Achievement>();
	
	public static IDataSource database = null;


	/**
	 * Add achievement to the manager
	 * @param ach
	 */
	public static void addAchievement(Achievement ach){
		achievements.put(ach.getName(),ach);
	}

	/**
	 * Load the achievements for a player
	 * @param player
	 */
	public static void loadAchievements(String player){
		HashSet<String> got = database.getPlayersAchievements(player);
		//put to cache
		playerHasCache.put(player,got);
		//cycle all loaded achievements
		for(Achievement ach :achievements.values()){

			//if they don't have that achievement, add them to the check cache
			if(!got.contains(ach.getName())){
				//push key if it doesn't exist
				if(!playerCheckCache.containsKey(ach)){
					playerCheckCache.put(ach, new HashSet<String>());
				}
				//add player to cache
				playerCheckCache.get(ach).add(player);
			}
		}
	}



	/**
	 * Check all players online
	 */
	public static void checkPlayers(){

		//wipe players not online
		//for each achievement
		for( Entry<Achievement, HashSet<String>> entry : playerCheckCache.entrySet()){
			BeardAch.printDebugCon("ach:"+entry.getKey().getName());
			//loop all players, check them.
			Iterator<String> it = entry.getValue().iterator();
			String ply;
			Player p;
			while(it.hasNext()){
				ply = it.next();

				//get player object for offline detection
				p =Bukkit.getPlayer(ply);
				if(p instanceof Player){
					BeardAch.printDebugCon("Player "+ply+" online");
					//check for bleep bloop
					if(entry.getKey().checkAchievement(p)){
						BeardAch.printDebugCon("Achievement Get! " + ply + "=>" + entry.getKey().getName());
						//push to cache
						playerHasCache.get(ply).add(entry.getKey().getName());
						//push to DB
						database.setPlayersAchievements(ply, entry.getKey().getName());
						it.remove();
					}

				}else{
					it.remove();
				}

			}


		}
		
		
	}




	
	public static HashSet<String> getAchievements(String player){
		if(playerHasCache.containsKey(player)){
			return playerHasCache.get(player);
		}
		return null;
		
	}

}