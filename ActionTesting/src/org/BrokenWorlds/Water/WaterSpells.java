package org.BrokenWorlds.Water;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

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
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.google.common.collect.Lists;

public class WaterSpells implements Listener {
	
	private static final Logger logger = Logger.getLogger("Minecraft");

    private HashMap<String, WaterWalking> lilyWalkers = new HashMap<String, WaterWalking>();
    private List<Block> flashFlooded = Lists.newArrayList();
    
    List<Block> blocks = Lists.newArrayList();
   	List<String> flashCasters = Lists.newArrayList();
    
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerMove(PlayerMoveEvent event) {
    	WaterWalking lilies = lilyWalkers.get(event.getPlayer().getName());
    	if (lilies != null) {
    		Block block = event.getTo().getBlock();
    	    boolean moved = lilies.isMoved(block);
    		if (moved) {
    	        	lilies.move(block);
    		}
    	}
        if(event.getPlayer().getLocation().getBlock().hasMetadata("flashFlooded")){
        	event.getPlayer().damage(1);
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
        				
        				//player.getTargetBlock(arg0, arg1)
        					
        				Block target = player.getTargetBlock(null, 8);
        				
        				Location center = target.getLocation();
        				// Now we create the sphere in the direction they casted the spell
        				if(center != null){
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
        				}
        				else
        					player.sendMessage(ChatColor.RED + "Flash Flood failed to cast!");
        				
        		}
        		
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
    
    public void removeFlashFlood(List<Block> flood) {
    	ListIterator<Block> it = flood.listIterator();
    	while(it.hasNext()) {
    		Block next = it.next();
    		next.setType(Material.AIR);
    		next.removeMetadata("flashFlooded", Bukkit.getPluginManager().getPlugin("SkillTesting"));
    	}
    	flood.clear();
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
            lilyWalkers.remove(player.getName());
            player.sendMessage(ChatColor.YELLOW + "-- Water walking deactivated --");
        }
    }

    public void addPlayer(Player player) {
        if (!lilyWalkers.containsKey(player.getName())) {
            lilyWalkers.put(player.getName(), new WaterWalking());
            player.sendMessage(ChatColor.GREEN
                    + "-- Water walking activated --");
        } else {
            player.sendMessage(ChatColor.RED
                    + "-- Water walk already enabled --");
        }
    }
    
    public boolean makeCircle(Player player, Location loc, Integer r, Integer h, Material m, Boolean hollow, Boolean sphere, Boolean tele) {
        int cx = loc.getBlockX();
        int cy = loc.getBlockY();
        int cz = loc.getBlockZ();
        
        for (int x = cx - r; x <= cx +r; x++)
            for (int z = cz - r; z <= cz +r; z++)
                for (int y = (sphere ? cy - r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r*r && !(hollow && dist < (r-1)*(r-1))) {
                        Block temp = loc.getWorld().getBlockAt(x, y, z);
                        if(temp.getType() == Material.AIR){
                        	flashFlooded.add(temp);
                        		temp.setType(m);
                        		temp.setMetadata("flashFlooded", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SkillTesting"), "flashFlood"));
                        }
                        		
                    }
                }
        if (tele) player.teleport(player.getWorld().getHighestBlockAt(player.getLocation()).getLocation());
        return true;
    }

}