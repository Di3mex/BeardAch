package me.tehbeard.BeardAch.achievement;
import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import me.tehbeard.BeardAch.BeardAch;
import me.tehbeard.BeardAch.achievement.triggers.CuboidCheckTrigger;
import me.tehbeard.BeardAch.achievement.triggers.SpeedRunTrigger;
import me.tehbeard.BeardAch.achievement.triggers.ITrigger;
import me.tehbeard.BeardAch.dataSource.AchievementLoader;
import me.tehbeard.BeardAch.dataSource.IDataSource;
import me.tehbeard.BeardAch.dataSource.configurable.RunnableTime;
import me.tehbeard.utils.cuboid.ChunkCache;
import me.tehbeard.utils.cuboid.CuboidEntry;

import me.tehbeard.utils.cuboid.Cuboid;

/**
 * Manages the link between achievements and players
 * @author James
 *
 */
public class AchievementManager implements Listener {

	private HashMap<String,HashSet<AchievementPlayerLink>> playerHasCache = new  HashMap<String,HashSet<AchievementPlayerLink>>();
	private HashMap<Achievement,HashSet<String>> playerCheckCache = new  HashMap<Achievement,HashSet<String>>();
	public IDataSource database = null;
	private final ChunkCache<Achievement> chunkCache = new ChunkCache<Achievement>();

	private List<Achievement> achievements = new LinkedList<Achievement>();


	public AchievementManager(){
		playerHasCache = new HashMap<String,HashSet<AchievementPlayerLink>>();

	}
	public void loadAchievements(){
		//clear cache
		clearAchievements();
		//reset Achievement Id's
		Achievement.resetId();
		//load achievements
		AchievementLoader.loadAchievements();

		//load each players 
		for(Player p :Bukkit.getOnlinePlayers()){
			loadPlayersAchievements(p.getName());
		}
	}
	/**
	 * Clear the caches.
	 */
	public void clearAchievements(){
		database.flush();
		chunkCache.clearCache();
		achievements = new LinkedList<Achievement>();
		playerCheckCache = new HashMap<Achievement,HashSet<String>>();
	}

	/**
	 * Return the list of visible achievements
	 * @return
	 */
	public List<Achievement> getAchievementsList() {
		List<Achievement> ret = new ArrayList<Achievement>();
		for(Achievement a: achievements){
			if(!a.isHidden()){ret.add(a);}
		}
		return ret;
	}

	/**
	 * Get a list of all achievements
	 * @return
	 */
	public List<Achievement> getLoadedAchievements() {
		List<Achievement> ret = new ArrayList<Achievement>();
		for(Achievement a: achievements){
			ret.add(a);
		}
		return ret;
	}



	public Achievement getAchievementSlug(String slug){
		for(Achievement a :achievements){
			if(a.getSlug().equals(slug)){
				return a;
			}
		}
		return null;
	}

	/**
	 * Add achievement to the manager
	 * @param ach
	 */
	public void addAchievement(Achievement ach){
		achievements.add(ach);
		for(ITrigger t : ach.getTrigs()){
			if(t instanceof CuboidCheckTrigger){
				Cuboid cuboid = ((CuboidCheckTrigger)t).getCuboid();
				chunkCache.addEntry(cuboid, ach);
			}

			if(t instanceof SpeedRunTrigger){

				Cuboid cuboid = ((SpeedRunTrigger)t).getStartCuboid();
				chunkCache.addEntry(cuboid, ach);
				cuboid = ((SpeedRunTrigger)t).getEndCuboid();
				chunkCache.addEntry(cuboid, ach);
			}
			if(t instanceof Listener){
				BeardAch.printDebugCon("Adding listener trigger");
				Bukkit.getPluginManager().registerEvents((Listener)t, BeardAch.self);
			}
			if(t instanceof Runnable){
				RunnableTime rt = t.getClass().getAnnotation(RunnableTime.class);
				if(rt != null){
					switch(rt.type()){
					case SYNC:
						Bukkit.getScheduler().scheduleSyncRepeatingTask(BeardAch.self, (Runnable)t, rt.value(), rt.value());
						break;
					case ASYNC:
						Bukkit.getScheduler().runTaskTimerAsynchronously(BeardAch.self, (Runnable)t, rt.value(), rt.value());
					}
				}
			}
		}

	}

	/**
	 * Load the achievements for a player
	 * @param player
	 */
	public void loadPlayersAchievements(String player){
		HashSet<AchievementPlayerLink> got = database.getPlayersAchievements(player);
		//put to cache
		if(got!=null){
			playerHasCache.put(player,got);
			//cycle all loaded achievements
			buildPlayerCheckCache(player);
		}
		else
		{
			BeardAch.printCon("Failed to load for " + player);
		}

	}

	private void buildPlayerCheckCache(String player){

		for(Achievement ach :achievements){

			//if they don't have that achievement, add them to the check cache
			HashSet<String> slugs = new HashSet<String>();
			for(AchievementPlayerLink link : playerHasCache.get(player)){
				slugs.add(link.getSlug());
			}
			if(!slugs.contains(ach.getSlug())){
				//push key if it doesn't exist
				if(!playerCheckCache.containsKey(ach)){
					playerCheckCache.put(ach, new HashSet<String>());
				}
				//add player to cache
				playerCheckCache.get(ach).add(player);
			}
		}
	}


	public boolean playerHas(String player,String slug){
		for(AchievementPlayerLink link : playerHasCache.get(player)){
			if(link.getSlug().equals(slug)){
				return true;
			}
		}
		return false;
	}

	/**
	 * Check all players online
	 */
	public void checkPlayers(){

		//wipe players not online
		//for each achievement

		boolean keepChecking = true;
		while(keepChecking){
			BeardAch.printDebugCon("Beggining Check player loop");
			keepChecking = false;

			for( Achievement ach : playerCheckCache.keySet()){

				BeardAch.printDebugCon("ach:"+ach.getName());
				//loop all players, check them.

				Set<String> list = getListOfPlayersToCheck(ach);
				Player p;
				for(String ply : list){

					//get player object for offline detection
					p =Bukkit.getPlayer(ply);
					if(p instanceof Player){
						BeardAch.printDebugCon("Player "+ply+" online");
						//check for bleep bloop
						if(ach.checkAchievement(p)){
							keepChecking = true;
						}

					}else{
						removeCheck(ach, ply);
					}

				}

			}
			BeardAch.printDebugCon("Ending Check player loop");
		}





	}

	public List<AchievementPlayerLink> getAchievements(String player){
		if(playerHasCache.containsKey(player)){
			List<AchievementPlayerLink> l = new LinkedList<AchievementPlayerLink>();

			for(AchievementPlayerLink link : playerHasCache.get(player)){
				Achievement a = getAchievementSlug(link.getSlug());
				if(a!=null){
					l.add(link);
				}
			}
			Collections.sort(l,new Comparator<AchievementPlayerLink>() {

				public int compare(AchievementPlayerLink o1, AchievementPlayerLink o2) {
					long res = o1.getDate().getTime() - o2.getDate().getTime();
					if(res==0){
						return 0;
					}
					if(res>0){return 1;}else{return -1;}
				}						
			});
			return l;
		}
		return null;

	}

	public Achievement getAchievement(int i) {
		if(i>0 && i<=achievements.size()){
			return achievements.get(i-1);
		}
		return null;
	}

	public void makeAchievementLink(String player,String slug){
		//push to cache
		playerHasCache.get(player).add(new AchievementPlayerLink(slug));
		//push to DB
		database.setPlayersAchievements(player, slug);
	}





	public void checkAchievement(Achievement ach){

		Iterator<String> it = getListOfPlayersToCheck(ach).iterator();
		String ply;
		Player p;
		while(it.hasNext()){
			ply = it.next();

			//get player object for offline detection
			p =Bukkit.getPlayer(ply);
			if(p instanceof Player){
				BeardAch.printDebugCon("Player "+ply+" online");
				//check for bleep bloop
				ach.checkAchievement(p);

			}else{
				it.remove();
			}

		}
	}

	public void removeCheck(Achievement ach,String player){
		playerCheckCache.get(ach).remove(player);
	}



	///LISTENER

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerMove(PlayerMoveEvent event){
		if(event.getTo().getBlockX() != event.getFrom().getBlockX() ||
				event.getTo().getBlockY() != event.getFrom().getBlockY() || 
				event.getTo().getBlockZ() != event.getFrom().getBlockZ()
				){

			for( CuboidEntry<Achievement> e : chunkCache.getEntries(event.getPlayer())){

				e.getEntry().checkAchievement(event.getPlayer());

			}
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		loadPlayersAchievements(event.getPlayer().getName());

	}


	private Set<String> getListOfPlayersToCheck(Achievement ach){
		HashSet<String> list = playerCheckCache.get(ach);
		return new HashSet<String>(list);
	}
}


