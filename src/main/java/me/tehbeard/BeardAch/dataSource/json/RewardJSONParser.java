package me.tehbeard.BeardAch.dataSource.json;

import java.lang.reflect.Type;

import org.bukkit.Location;

import me.tehbeard.BeardAch.achievement.rewards.IReward;
import me.tehbeard.BeardAch.dataSource.AchievementLoader;
import me.tehbeard.BeardAch.dataSource.configurable.Configurable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class RewardJSONParser implements JsonSerializer<IReward>,JsonDeserializer<IReward>{

    private Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeHierarchyAdapter(Location.class,new LocationJSONParser()).create();
    public JsonElement serialize(IReward trigger, Type type,
            JsonSerializationContext context) {
        JsonElement element = gson.toJsonTree(trigger);
        element.getAsJsonObject().addProperty("_type", trigger.getClass().getAnnotation(Configurable.class).tag());
        return element;
    }

    public IReward deserialize(JsonElement element, Type type,
            JsonDeserializationContext context) throws JsonParseException {
        
        return gson.fromJson(element, AchievementLoader.rewardFactory.get(element.getAsJsonObject().get("_type").getAsString()));
        
    }

}
