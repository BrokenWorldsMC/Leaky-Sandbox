package org.BrokenWorlds.Pyromancy;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

public class FireCircle implements Listener{

	List<Block> fire = Lists.newArrayList();
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {

		final Player player = event.getPlayer();
		Action action = event.getAction();

		if (!action.equals(Action.LEFT_CLICK_AIR)
				&& !action.equals(Action.LEFT_CLICK_BLOCK))
			return;

		ItemStack item = player.getItemInHand();
		String sItem = item.getType().name();
		if (sItem.equals("WRITTEN_BOOK")) {
			String title = ((CraftItemStack) item).getHandle().tag
					.getString("title");
			if (title.equals("Circle of Fire")){
				
				Location target = player.getTargetBlock(null, 5).getLocation();
				World world = player.getWorld();
				int highTarget = world.getHighestBlockYAt(target);
				target = new Location(player.getWorld(), target.getX(), highTarget, target.getZ());
				
				makeCircle(target, player.getWorld());
				
				Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {
					
                    public void run() {
                    	removeFire(fire);
                    }
                }, 100L);
			}
		}
	}
	
	public void makeCircle(Location loc, World w) {
		int radius = 3;

		int radiusSquared = radius * radius;
		 
		for(int x = -radius; x <= radius; x++) {
		    for(int z = -radius; z <= radius; z++) {
		        if( (x*x) + (z*z) <= radiusSquared) {
		        	w.getBlockAt((int)loc.getX() + x, (int)loc.getY() + 0, (int)loc.getZ() + z).setType(Material.FIRE);
		        	fire.add(w.getBlockAt((int)loc.getX() + x, (int)loc.getY() + 0, (int)loc.getZ() + z));
		        }
		    }
		}

	}
	
	public void removeFire(List<Block> fire) {
		ListIterator<Block> it = fire.listIterator();
		while (it.hasNext()) {
			Block next = it.next();
			if(next.getType().equals(Material.FIRE))
					next.setType(Material.AIR);
		}
		fire.clear();
	}
	
}
