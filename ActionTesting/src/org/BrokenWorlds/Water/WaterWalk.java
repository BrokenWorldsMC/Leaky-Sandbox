package org.BrokenWorlds.Water;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.bukkit.block.BlockState;

public class WaterWalk implements Listener {

    private HashMap<String, Lilies> lilywalkers = new HashMap<String, Lilies>();

    @EventHandler(priority = EventPriority.MONITOR)
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
        if(!action.toString().equals("LEFT_CLICK_AIR") && !action.toString().equals("LEFT_CLICK_BLOCK"))
            return;
        
        ItemStack item = player.getItemInHand();
        String sItem = item.getType().name();
        String title = ((CraftItemStack) item).getHandle().tag.getString("title");
        switch(sItem) {
            case "WRITTEN_BOOK":
                switch(title){
                    case "Waterwalking":
                        if (player.getLocation().getBlock().isLiquid())
                             player.sendMessage(ChatColor.RED
                                        + "Can't water walk when standing in water!");
                        addPlayer(player);
                        Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {

                            public void run() {
                                removePlayer(player);
                            }
                        }, 1200L);
                        break;
                }
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
