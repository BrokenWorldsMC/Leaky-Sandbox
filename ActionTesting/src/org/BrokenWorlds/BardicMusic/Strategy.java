package org.BrokenWorlds.BardicMusic;

import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import com.google.common.collect.Lists;

public class Strategy implements Listener {

    public List<Entity> nearBy = Lists.newArrayList();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // TODO: Coming soon, deals with Mana Regeneration!
    }
}
