package org.BrokenWorlds;

import org.BrokenWorlds.BardicMusic.Cheers;
import org.BrokenWorlds.BardicMusic.Fair;
import org.BrokenWorlds.BardicMusic.Mellow;
import org.BrokenWorlds.BardicMusic.Stall;
import org.BrokenWorlds.BardicMusic.Strategy;
import org.BrokenWorlds.Pyromancy.FireCircle;
import org.BrokenWorlds.Telekinetic.KineticShield;
import org.BrokenWorlds.Telekinetic.Skip;
import org.BrokenWorlds.Telekinetic.StasisField;
import org.BrokenWorlds.Telekinetic.Vortex;
import org.BrokenWorlds.Water.CleansingWater;
import org.BrokenWorlds.Water.FlashFloods;
import org.BrokenWorlds.Water.WaterWalk;
import org.bukkit.plugin.java.JavaPlugin;

public class SkillTesting extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new FlashFloods(), this);
        getServer().getPluginManager().registerEvents(new WaterWalk(), this);
        getServer().getPluginManager().registerEvents(new CleansingWater(), this);
        getServer().getPluginManager().registerEvents(new Cheers(), this);
        getServer().getPluginManager().registerEvents(new Fair(), this);
        getServer().getPluginManager().registerEvents(new Mellow(), this);
        getServer().getPluginManager().registerEvents(new Stall(), this);
        getServer().getPluginManager().registerEvents(new Strategy(), this);
        getServer().getPluginManager().registerEvents(new Vortex(), this);
        getServer().getPluginManager().registerEvents(new Skip(), this);
        getServer().getPluginManager().registerEvents(new StasisField(), this);
        getServer().getPluginManager().registerEvents(new KineticShield(), this);
        getServer().getPluginManager().registerEvents(new FireCircle(), this);
    }

    @Override
    public void onDisable() {
    }

}
