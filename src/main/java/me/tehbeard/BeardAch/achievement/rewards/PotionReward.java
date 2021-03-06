package me.tehbeard.BeardAch.achievement.rewards;

import me.tehbeard.BeardAch.BeardAch;
import me.tehbeard.BeardAch.achievement.Achievement;
import me.tehbeard.BeardAch.dataSource.configurable.Configurable;
import me.tehbeard.BeardAch.dataSource.json.editor.EditorField;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.gson.annotations.Expose;

@Configurable(tag="potion",name="Apply potion effect")
public class PotionReward implements IReward {

    @Expose
    @EditorField(alias="Potion Type")
    private String potionType;
    @Expose
    @EditorField(alias="Amplifier")
    private int amplifier;
    @Expose
    @EditorField(alias="Duration")
    private int duration;
    
    private PotionEffect effect;
    public void configure(Achievement ach, String config) {
        String[] c = config.split(":");
        if(c.length!=3){BeardAch.printError("Invalid potion config");return;}
        potionType = c[0].toUpperCase();
        amplifier = Integer.parseInt(c[1]);
        duration = Integer.parseInt(c[2]) * 20;
        

    }

    public void giveReward(Player player) {
        if(effect!=null){
            effect.apply(player);
        }
    }

    public void configure(Achievement ach) {
        PotionEffectType type = PotionEffectType.getByName(potionType);
        effect = new PotionEffect(type, duration, amplifier);
        
    }

}
