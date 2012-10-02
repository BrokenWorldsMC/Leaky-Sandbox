package org.BrokenWorlds.BardicMusic;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

public class Cheers implements Listener {

    private List<String> cheersBuff = Lists.newArrayList();
    private List<String> coolDown = Lists.newArrayList();

    public List<Entity> nearBy = Lists.newArrayList();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Location loc = player.getLocation();
        ItemStack item = player.getItemInHand();
        if (item.getTypeId() == 2259) {
            player.getWorld().playEffect(loc, Effect.RECORD_PLAY, 2259, 10);
            nearBy = player.getNearbyEntities(5, 5, 5);
            ListIterator<Entity> it = nearBy.listIterator();
            while (it.hasNext()) {
                Entity next = it.next();
                if (next instanceof Player) {
                    addBuffedPlayer(((Player) next).getPlayer());
                }
            }
        }

        if ((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            if (item.getType() == Material.POTION)
                Bukkit.getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new CheckDrink(player, player.getInventory().getHeldItemSlot()), 33L);
        }

    }

    public void addCoolDown(Player player) {
        coolDown.add(player.getName());
    }

    public void removeCoolDown(Player player) {
        coolDown.remove(player.getName());
    }

    public void addBuffedPlayer(Player player) {
        cheersBuff.add(player.getName());
    }

    public void removeBuffedPlayer(Player player) {
        cheersBuff.remove(player.getName());
    }
}
