package org.BrokenWorlds;

import org.BrokenWorlds.Telekinetic.*;
import org.BrokenWorlds.Water.*;
import org.bukkit.plugin.java.JavaPlugin;

public class SkillTesting extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new FlashFloods(), this);
        getServer().getPluginManager().registerEvents(new WaterWalk(), this);
        getServer().getPluginManager().registerEvents(new CleansingWater(), this);
        getServer().getPluginManager().registerEvents(new Vortex(), this);
    }

    @Override
    public void onDisable() {
    }

}
