package org.BrokenWorlds;

import org.BrokenWorlds.Water.WaterSpells;
import org.bukkit.plugin.java.JavaPlugin;

public class SkillTesting extends JavaPlugin  {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new WaterSpells(), this);
    }

    @Override
    public void onDisable() {
    }
	
}
