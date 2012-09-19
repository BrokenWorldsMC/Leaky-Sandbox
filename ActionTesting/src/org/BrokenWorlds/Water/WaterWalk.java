package org.BrokenWorlds.Water;

import java.util.List;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;

public class WaterWalk extends JavaPlugin implements Listener {

	private static final Logger logger = Logger.getLogger("Minecraft");
	private List<String> waterWalking = Lists.newArrayList();
	private List<Location> blocks = Lists.newArrayList();

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		logger.log(Level.SEVERE, "Water Walk Enabled!");
	}

	@Override
	public void onDisable() {
		logger.log(Level.SEVERE, "Water Walk Disabled!");
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {

		if (waterWalking.contains(event.getPlayer().toString())) {
			// water walking! Update lily pads below feet on water
			Player player = event.getPlayer();
			Location loc = player.getLocation();
			Location cloneLoc = loc.clone();
			World world = player.getWorld();
			Vector direction = loc.getDirection();
			Block block = null;
			//logger.info("Direction X: " + direction.getX() + " Direction Z: " + direction.getZ());
			// Player is moving quickly, add 3 or 4 lily pads
			if (player.isSprinting() && world.getBlockAt( (int)cloneLoc.getX(), (int)cloneLoc.getY() - 1, (int)cloneLoc.getZ()).isLiquid()) {
				// Player is moving North, (-1, 0)
				if (Math.round(direction.getX()) == 0 && Math.round(direction.getZ()) == 1){
					// add the blocks in the direction player is headed
					blocks.add(new Location(world, cloneLoc.getX(), cloneLoc.getY(), cloneLoc.getZ() + 1.0D));
					blocks.add(new Location(world, cloneLoc.getX(), cloneLoc.getY(), cloneLoc.getZ() + 2.0D));
					blocks.add(new Location(world, cloneLoc.getX(), cloneLoc.getY(), cloneLoc.getZ() + 3.0D));
					
					// Go through list of locations and set the block to lily pad
					for(Location tempLoc : blocks){
						block = world.getBlockAt(tempLoc);
						if(block.isEmpty() && block.getRelative(BlockFace.DOWN).isLiquid()){
							block.setTypeId(111);
							block.setMetadata("waterWalkLily", new FixedMetadataValue(this, true));
						}
					}
					
				}
				// Player is moving East, (-1,0)
				if (Math.round(direction.getX()) == -1 && Math.round(direction.getZ()) == 0){
					// add the blocks in the direction player is headed
					blocks.add(new Location(world, cloneLoc.getX() - 1.0D, cloneLoc.getY(), cloneLoc.getZ()));
					blocks.add(new Location(world, cloneLoc.getX() - 2.0D, cloneLoc.getY(), cloneLoc.getZ()));
					blocks.add(new Location(world, cloneLoc.getX() - 3.0D, cloneLoc.getY(), cloneLoc.getZ()));
					
					// Go through list of locations and set the block to lily pad
					for(Location tempLoc : blocks){
						block = world.getBlockAt(tempLoc);
						if(block.isEmpty() && block.getRelative(BlockFace.DOWN).isLiquid()){
							block.setTypeId(111);
							block.setMetadata("waterWalkLily", new FixedMetadataValue(this, true));
						}
					}
					
				}
				// Player is moving South, (0, -1)
				if (Math.round(direction.getX()) == 0 && Math.round(direction.getZ()) == -1){
					// add the blocks in the direction player is headed
					blocks.add(new Location(world, cloneLoc.getX(), cloneLoc.getY(), cloneLoc.getZ() - 1.0D));
					blocks.add(new Location(world, cloneLoc.getX(), cloneLoc.getY(), cloneLoc.getZ() - 2.0D));
					blocks.add(new Location(world, cloneLoc.getX(), cloneLoc.getY(), cloneLoc.getZ() - 3.0D));
					
					// Go through list of locations and set the block to lily pad
					for(Location tempLoc : blocks){
						block = world.getBlockAt(tempLoc);
						if(block.isEmpty() && block.getRelative(BlockFace.DOWN).isLiquid()){
							block.setTypeId(111);
							block.setMetadata("waterWalkLily", new FixedMetadataValue(this, true));
						}
					}
				}
					
				// Player is moving West, (1, 0)
				if (Math.round(direction.getX()) == 1 && Math.round(direction.getZ()) == 0){
					// add the blocks in the direction player is headed
					blocks.add(new Location(world, cloneLoc.getX() + 1.0D, cloneLoc.getY(), cloneLoc.getZ()));
					blocks.add(new Location(world, cloneLoc.getX() + 2.0D, cloneLoc.getY(), cloneLoc.getZ()));
					blocks.add(new Location(world, cloneLoc.getX() + 3.0D, cloneLoc.getY(), cloneLoc.getZ()));
					
					// Go through list of locations and set the block to lily pad
					for(Location tempLoc : blocks){
						block = world.getBlockAt(tempLoc);
						if(block.isEmpty() && block.getRelative(BlockFace.DOWN).isLiquid()){
							block.setTypeId(111);
							block.setMetadata("waterWalkLily", new FixedMetadataValue(this, true));
						}
					}
				}
			}
			// Player is walking slowly, only add two lily pads
			else {
				// Player is moving North, (0, 1)
				if (Math.round(direction.getX()) == 0 && Math.round(direction.getZ()) == 1){
					// add the blocks in the direction player is headed
					blocks.add(new Location(world, cloneLoc.getX(), cloneLoc.getY(), cloneLoc.getZ() + 1.0D));
					blocks.add(new Location(world, cloneLoc.getX(), cloneLoc.getY(), cloneLoc.getZ() + 2.0D));
					
					// Go through list of locations and set the block to lily pad
					for(Location tempLoc : blocks){
						block = world.getBlockAt(tempLoc);
						if(block.isEmpty() && block.getRelative(BlockFace.DOWN).isLiquid()){
							block.setTypeId(111);
							block.setMetadata("waterWalkLily", new FixedMetadataValue(this, true));
						}
					}
					
				}
				// Player is moving East, (-1,0)
				if (Math.round(direction.getX()) == -1 && Math.round(direction.getZ()) == 0){
					// add the blocks in the direction player is headed
					blocks.add(new Location(world, cloneLoc.getX() - 1.0D, cloneLoc.getY(), cloneLoc.getZ()));
					blocks.add(new Location(world, cloneLoc.getX() - 2.0D, cloneLoc.getY(), cloneLoc.getZ()));
					
					// Go through list of locations and set the block to lily pad
					for(Location tempLoc : blocks){
						block = world.getBlockAt(tempLoc);
						if(block.isEmpty() && block.getRelative(BlockFace.DOWN).isLiquid()){
							block.setTypeId(111);
							block.setMetadata("waterWalkLily", new FixedMetadataValue(this, true));
						}
					}
					
				}
				// Player is moving South, (0, -1)
				if (Math.round(direction.getX()) == 0 && Math.round(direction.getZ()) == -1){
					// add the blocks in the direction player is headed
					blocks.add(new Location(world, cloneLoc.getX(), cloneLoc.getY(), cloneLoc.getZ() - 1.0D));
					blocks.add(new Location(world, cloneLoc.getX(), cloneLoc.getY(), cloneLoc.getZ() - 2.0D));
					
					// Go through list of locations and set the block to lily pad
					for(Location tempLoc : blocks){
						block = world.getBlockAt(tempLoc);
						if(block.isEmpty() && block.getRelative(BlockFace.DOWN).isLiquid()){
							block.setTypeId(111);
							block.setMetadata("waterWalkLily", new FixedMetadataValue(this, true));
						}
					}
				}
					
				// Player is moving West, (1, 0)
				if (Math.round(direction.getX()) == 1 && Math.round(direction.getZ()) == 0){
					// add the blocks in the direction player is headed
					blocks.add(new Location(world, cloneLoc.getX() + 1.0D, cloneLoc.getY(), cloneLoc.getZ()));
					blocks.add(new Location(world, cloneLoc.getX() + 2.0D, cloneLoc.getY(), cloneLoc.getZ()));
					
					// Go through list of locations and set the block to lily pad
					for(Location tempLoc : blocks){
						block = world.getBlockAt(tempLoc);
						if(block.isEmpty() && block.getRelative(BlockFace.DOWN).isLiquid()){
							block.setTypeId(111);
							block.setMetadata("waterWalkLily", new FixedMetadataValue(this, true));
							removeLily(blocks, player);
							
						}
					}
				}
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
				&& (action.toString().equals("LEFT_CLICK_AIR") || action
						.toString().equals("LEFT_CLICK_BLOCK"))) {
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
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		
		if (event.getBlock().hasMetadata("waterWalkLily"))
			return;
		
	}
	
	/**
	 * Method to remove water waking lily pads after
	 * 3 seconds if player isn't nearby anymore
	 */
	public int removeLily(final List<Location> blocks, final Player player) {
		
		int id = getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {

			   public void run() {
			   
					for(Location tempLoc:blocks) {
						// If player is more than 3 blocks away, remove it
						if(player.getLocation().distance(tempLoc) > 3 && tempLoc.getBlock().equals(Material.getMaterial(111))){
							tempLoc.getBlock().breakNaturally();
						}
					}
				   
			   }
			}, 60L, 60L);
		
		return id;
		
	}
	
	public void removePlayer(Player player) {
		waterWalking.remove(player.toString());
		player.sendMessage(ChatColor.YELLOW + "-- Water walking deactivated --");
	}

	public void addPlayer(Player player) {
		if (!waterWalking.contains(player.toString())) {
			waterWalking.add(player.toString());
			player.sendMessage(ChatColor.GREEN
					+ "-- Water walking activated --");
		} else {
			player.sendMessage(ChatColor.RED
					+ "-- Water walk already enabled --");
		}
	}

}
