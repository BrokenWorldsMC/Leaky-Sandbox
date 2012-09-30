package org.BrokenWorlds.Water;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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

import com.google.common.collect.Lists;

public class WaterSpells implements Listener {

    private HashMap<String, WaterWalking> lilyWalkers = new HashMap<String, WaterWalking>();
    
    List<Block> blocks = Lists.newArrayList();
   	List<String> flashCasters = Lists.newArrayList();
    
    private WaterWalking lilies = new WaterWalking();
        
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (lilyWalkers.containsKey(event.getPlayer().getName())) {
            Block block = event.getTo().getBlock();
            boolean moved = lilies.isMoved(block);
            if (moved) {
                lilies.move(block);
            }
        }
        if(event.getPlayer().getLocation().getBlock().hasMetadata("flashFlood")){
        	event.getPlayer().setRemainingAir(0);
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
        switch(sItem) {
        	case "WRITTEN_BOOK":
        		String title = ((CraftItemStack) item).getHandle().tag.getString("title");
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
        			case "Flash Flood":
        				if(flashCasters.contains(player.getName())){
        					player.sendMessage(ChatColor.YELLOW + "-- Flash Flood is on cooldown --");
        					return;
        				}
        					
        				String direction = getCardinalDirection(player);
        				Location center = null;
        				FlashFlood flashFlood = new FlashFlood();
        				switch(direction){
	        				case "N":
	        					center = player.getLocation().getBlock().getRelative(BlockFace.NORTH, 8).getLocation();
	        					break;
	        				case "NE":
	        					center = player.getLocation().getBlock().getRelative(BlockFace.NORTH_EAST, 8).getLocation();
	        					break;
	        				case "E":
	        					center = player.getLocation().getBlock().getRelative(BlockFace.EAST, 8).getLocation();
	        					break;
	        				case "SE":
	        					center = player.getLocation().getBlock().getRelative(BlockFace.SOUTH_EAST, 8).getLocation();
	        					break;
	        				case "S":
	        					center = player.getLocation().getBlock().getRelative(BlockFace.SOUTH, 8).getLocation();
	        					break;
	        				case "SW":
	        					center = player.getLocation().getBlock().getRelative(BlockFace.SOUTH_WEST, 8).getLocation();
	        					break;
	        				case "W":
	        					center = player.getLocation().getBlock().getRelative(BlockFace.WEST, 8).getLocation();
	        					break;
	        				case "NW":
	        					center = player.getLocation().getBlock().getRelative(BlockFace.NORTH_WEST, 8).getLocation();
	        					break;
        				}
        				// Now we create the sphere in the direction they casted the spell
        				if(center != null){
        					blocks = flashFlood.makeSphere(center.getBlockX(), center.getBlockY(), center.getBlockZ(), 4, player);
        					addFlashCaster(player);
            	            Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {

                                public void run() {
                                	removeFlashFlood(blocks);
                                }
                            }, 100L);
            	            Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {
            	            	
            	            	public void run() {
            	            		removeFlashCaster(player);
            	            	}
            	            }, 600L);
        					player.sendMessage(ChatColor.GREEN + "Successfully casted " + ChatColor.YELLOW + "Flash Flood" + ChatColor.GREEN);
        				}
        				else
        					player.sendMessage(ChatColor.RED + "Flash Flood failed to cast!");
        				
        		}
        		
        }

    }
    
    public static String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90) % 360;
        if (rotation < 0) {
            rotation += 360.0;
        }
         if (0 <= rotation && rotation < 22.5) {
            return "N";
        } else if (22.5 <= rotation && rotation < 67.5) {
            return "NE";
        } else if (67.5 <= rotation && rotation < 112.5) {
            return "E";
        } else if (112.5 <= rotation && rotation < 157.5) {
            return "SE";
        } else if (157.5 <= rotation && rotation < 202.5) {
            return "S";
        } else if (202.5 <= rotation && rotation < 247.5) {
            return "SW";
        } else if (247.5 <= rotation && rotation < 292.5) {
            return "W";
        } else if (292.5 <= rotation && rotation < 337.5) {
            return "NW";
        } else if (337.5 <= rotation && rotation < 360.0) {
            return "N";
        } else {
            return null;
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (lilyWalkers.size() > 0 && event.getBlock().getTypeId() == Material.WATER_LILY.getId()) {
            for (WaterWalking lilies : lilyWalkers.values()) {
                if (lilies.contains(event.getBlock())) {
                    event.setCancelled(true);
                    break;
                }
            }
        }
    }
    
    public void removeFlashFlood(List<Block> blocks2) {
    	ListIterator<Block> it = blocks2.listIterator();
    	while(it.hasNext()) {
    		Block next = it.next();
    		next.setType(Material.AIR);
    		//blocks2.remove(next);
    	}
    }
    
    public void addFlashCaster(Player player){
    	flashCasters.add(player.getName());
    }
    
    public void removeFlashCaster(Player player){
    	flashCasters.remove(player.getName());
    	player.sendMessage(ChatColor.GREEN + "-- " + ChatColor.YELLOW + "Flash Flood" + ChatColor.GREEN + " cooldown reset --");
    }

    public void removePlayer(Player player) {
        if (lilyWalkers.containsKey(player.getName())) {
            lilies.remove();
            lilyWalkers.remove(player.getName());
            player.sendMessage(ChatColor.YELLOW + "-- Water walking deactivated --");
        }
    }

    public void addPlayer(Player player) {
        if (!lilyWalkers.containsKey(player.getName())) {
            lilies.move(player.getLocation().getBlock());
            lilyWalkers.put(player.getName(), lilies);
            player.sendMessage(ChatColor.GREEN
                    + "-- Water walking activated --");
        } else {
            player.sendMessage(ChatColor.RED
                    + "-- Water walk already enabled --");
        }
    }

}