package org.BrokenWorlds.Telekinetic;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

public class KineticShield implements Listener {

    List<String> kinShield = Lists.newArrayList();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {

        final Player player = event.getPlayer();
        Action action = event.getAction();

        if (!action.equals(Action.LEFT_CLICK_AIR) && !action.equals(Action.LEFT_CLICK_BLOCK))
            return;

        ItemStack item = player.getItemInHand();
        String sItem = item.getType().name();
        if (sItem.equals("WRITTEN_BOOK")) {
            String title = ((CraftItemStack) item).getHandle().tag.getString("title");
            if (title.equals("Kinetic Shield") && !kinShield.contains(player.getName())) {
                addKineticShield(player);
                Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {

                    public void run() {
                        removeKineticShield(player);
                    }
                }, 100L);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            int damage = event.getDamage();
            if (kinShield.contains(player.getName())) {
                if (event.getCause().equals(DamageCause.PROJECTILE)) {
                    int projDmg = (int) (damage - (damage * 0.20));
                    event.setDamage(projDmg);
                } else
                    event.setDamage((int) (damage - (damage * 0.10)));
            }

        }

    }

    public void addKineticShield(Player player) {
        kinShield.add(player.getName());
        player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, 10);
        player.sendMessage(ChatColor.YELLOW + "Kinetic Shield" + ChatColor.GREEN + " activated!");
    }

    public void removeKineticShield(Player player) {
        kinShield.remove(player.getName());
        player.sendMessage(ChatColor.YELLOW + "Kinetic Shield" + ChatColor.GREEN + " has expired!");
    }
}
