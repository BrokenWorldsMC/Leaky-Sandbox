package org.BrokenWorlds.Water;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.google.common.collect.Lists;

public class FlashFloods implements Listener {

    private List<Block> flashFlooded = Lists.newArrayList();

    List<Block> blocks = Lists.newArrayList();
    List<String> flashCasters = Lists.newArrayList();

    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (event.getPlayer().getLocation().getBlock().hasMetadata("flashFlooded")) {
            event.getPlayer().damage(1);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Action action = event.getAction();
        if (!action.toString().equals("LEFT_CLICK_AIR") && !action.toString().equals("LEFT_CLICK_BLOCK"))
            return;

        ItemStack item = player.getItemInHand();
        String sItem = item.getType().name();
        if (sItem.equals("WRITTEN_BOOK")) {
            String title = ((CraftItemStack) item).getHandle().tag.getString("title");
            if (title.equals("Flash Flood")) {
                if (!flashCasters.contains(player.getName())) {

                    Block target = player.getTargetBlock(null, 8);

                    Location center = target.getLocation();
                    // Now we create the sphere in the direction they casted the
                    // spell
                    if (center != null) {
                        this.makeCircle(player, center, 2, 2, Material.STATIONARY_WATER, false, true, false);

                        addFlashCaster(player);
                        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {

                            public void run() {
                                removeFlashFlood(flashFlooded);
                            }
                        }, 100L);
                        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {

                            public void run() {
                                removeFlashCaster(player);
                            }
                        }, 600L);
                        player.sendMessage(ChatColor.GREEN + "Successfully casted " + ChatColor.YELLOW + "Flash Flood" + ChatColor.GREEN);
                    } else
                        player.sendMessage(ChatColor.RED + "Flash Flood failed to cast!");
                } else
                    player.sendMessage(ChatColor.YELLOW + "-- Flash Flood is on cooldown --");
            }
        }
    }

    public void removeFlashFlood(List<Block> flood) {
        ListIterator<Block> it = flood.listIterator();
        while (it.hasNext()) {
            Block next = it.next();
            next.setType(Material.AIR);
            next.removeMetadata("flashFlooded", Bukkit.getPluginManager().getPlugin("SkillTesting"));
        }
        flood.clear();
    }

    public void addFlashCaster(Player player) {
        flashCasters.add(player.getName());
    }

    public void removeFlashCaster(Player player) {
        flashCasters.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "-- " + ChatColor.YELLOW + "Flash Flood" + ChatColor.GREEN + " cooldown reset --");
    }

    public boolean makeCircle(Player player, Location loc, Integer r, Integer h, Material m, Boolean hollow, Boolean sphere, Boolean tele) {
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();

        for (int x = cx - r; x <= cx + r; x++)
            for (int z = cz - r; z <= cz + r; z++)
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        Block temp = loc.getWorld().getBlockAt(x, y, z);
                        if (temp.getType() == Material.AIR) {
                            flashFlooded.add(temp);
                            temp.setType(m);
                            temp.setMetadata("flashFlooded", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SkillTesting"), "flashFlood"));
                        }

                    }
                }
        if (tele)
            player.teleport(player.getWorld().getHighestBlockAt(player.getLocation()).getLocation());
        return true;
    }

}
