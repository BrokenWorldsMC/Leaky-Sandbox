package org.BrokenWorlds.Water;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventPriority;

public class WaterWalk extends JavaPlugin implements Listener {

    private static final Logger logger = Logger.getLogger("Minecraft");
    private HashMap<String, Lilies> lilywalkers;

    @Override
    public void onEnable() {
        lilywalkers = new HashMap<String, Lilies>();
        getServer().getPluginManager().registerEvents(this, this);
        logger.log(Level.SEVERE, "Water Walk Enabled!");
    }

    @Override
    public void onDisable() {
        logger.log(Level.SEVERE, "Water Walk Disabled!");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Lilies lilies = lilywalkers.get(event.getPlayer().getName());
        if (lilies != null) {
            Block block = event.getTo().getBlock();
            boolean moved = lilies.isMoved(block);
            if (moved) {
                lilies.move(block);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Action action = event.getAction();

        if (player.getLocation().getBlock().isLiquid()
                && player.getItemInHand().equals(Material.BOOK_AND_QUILL)) {
            player.sendMessage(ChatColor.RED
                    + "Can't water walk when standing in water!");
            return;
        }

        if (player.getItemInHand().getTypeId() == 387
                && (action.toString().equals("LEFT_CLICK_AIR") || action.toString().equals("LEFT_CLICK_BLOCK"))) {
            addPlayer(player);

            // Create new task to remove player from list in 60 seconds (roughly
            // 1200 server ticks)
            getServer().getScheduler().scheduleAsyncDelayedTask(this,
                    new Runnable() {

                        public void run() {
                            removePlayer(player);
                        }
                    }, 1200L);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (lilywalkers.size() > 0 && event.getBlock().getTypeId() == Material.WATER_LILY.getId()) {
            for (Lilies lilies : lilywalkers.values()) {
                if (lilies.contains(event.getBlock())) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }

    public void removePlayer(Player player) {
        Lilies lilies = lilywalkers.get(player.getName());
        if (lilies != null) {
            lilies.remove();
            lilywalkers.remove(player.getName());
            player.sendMessage(ChatColor.YELLOW + "-- Water walking deactivated --");
        }
    }

    public void addPlayer(Player player) {
        if (!lilywalkers.containsKey(player.getName())) {
            Lilies lilies = new Lilies();
            lilies.move(player.getLocation().getBlock());
            lilywalkers.put(player.getName(), lilies);
            player.sendMessage(ChatColor.GREEN
                    + "-- Water walking activated --");
        } else {
            player.sendMessage(ChatColor.RED
                    + "-- Water walk already enabled --");
        }
    }

    public class Lilies {

        private Block center = null;
        private HashSet<Block> blocks = new HashSet<Block>();

        public void move(Block center) {
            this.center = center;

            Iterator<Block> iter = blocks.iterator();
            while (iter.hasNext()) {
                Block b = iter.next();
                if (!b.equals(center)) {
                    b.setType(Material.AIR);
                    iter.remove();
                }
            }

            setToLily(center);
            setToLily(center.getRelative(BlockFace.NORTH));
            setToLily(center.getRelative(BlockFace.SOUTH));
            setToLily(center.getRelative(BlockFace.EAST));
            setToLily(center.getRelative(BlockFace.WEST));
            setToLily(center.getRelative(BlockFace.NORTH_WEST));
            setToLily(center.getRelative(BlockFace.NORTH_EAST));
            setToLily(center.getRelative(BlockFace.SOUTH_WEST));
            setToLily(center.getRelative(BlockFace.SOUTH_EAST));
        }

        private void setToLily(Block block) {
            if (block.getType() == Material.AIR) {
                BlockState state = block.getRelative(BlockFace.DOWN).getState();
                if ((state.getType() == Material.WATER || state.getType() == Material.STATIONARY_WATER) && state.getRawData() == (byte) 0) {
                    block.setType(Material.WATER_LILY);
                    blocks.add(block);
                }
            }
        }

        public boolean isMoved(Block center) {
            if (this.center == null || !this.center.equals(center)) {
                return true;
            }
            return false;
        }

        public boolean contains(Block block) {
            return blocks.contains(block);
        }

        public void remove() {
            for (Block block : blocks) {
                block.setType(Material.AIR);
            }
            blocks.clear();
        }
    }
}