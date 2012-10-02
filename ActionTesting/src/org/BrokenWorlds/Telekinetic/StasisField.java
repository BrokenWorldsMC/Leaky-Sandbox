package org.BrokenWorlds.Telekinetic;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

public class StasisField implements Listener {

    List<String> timeFrozen = Lists.newArrayList();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            if (event.getDamager() instanceof Player) {
                final Player damagee = (Player) event.getEntity();
                Player damager = (Player) event.getDamager();
                ItemStack item = damager.getItemInHand();
                String sItem = item.getType().name();
                if (sItem.equals("WRITTEN_BOOK")) {
                    String title = ((CraftItemStack) item).getHandle().tag.getString("title");
                    if (title.equals("Stasis Field")) {
                        addTimeFreeze(damagee);
                        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {
    
                            public void run() {
                                removeTimeFreeze(damagee);
                            }
                        }, 100L);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (timeFrozen.contains(event.getPlayer().getName())) {
            event.setCancelled(true);
            return;
        }
    }

    public void addTimeFreeze(Player player) {
        timeFrozen.add(player.getName());
    }

    public void removeTimeFreeze(Player player) {
        timeFrozen.remove(player.getName());
    }
}
