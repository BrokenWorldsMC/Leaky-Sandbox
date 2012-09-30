package org.BrokenWorlds;

import org.BrokenWorlds.Water.*;
import org.bukkit.plugin.java.JavaPlugin;

public class SkillTesting extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new FlashFloods(), this);
        getServer().getPluginManager().registerEvents(new WaterWalk(), this);
    }

    @Override
    public void onDisable() {
    }

}
