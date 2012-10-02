package org.BrokenWorlds.Pyromancy;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Meteor implements Listener {
    
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
            if (title.equals("Meteor"))
                fireMeteor(event.getPlayer());
        }
    }
    
    public void fireMeteor(Player shooter) {
        Block target = shooter.getTargetBlock(null, 15);
        Location center = target.getLocation();
        if (center != null) {
            Location targetLoc = target.getLocation();
            Location location = new Location(shooter.getWorld(), targetLoc.getX(), 120, targetLoc.getZ());
            location.setPitch(90.0f);
            location.setYaw(0.0f);
            
            Fireball fireball = shooter.getWorld().spawn(location, Fireball.class);
            fireball.setVelocity(new Vector(0,-1.0,0));
            fireball.setBounce(false);
            fireball.setIsIncendiary(true);
            fireball.setShooter(shooter);
            
            Location location2 = new Location(shooter.getWorld(), targetLoc.getX() + 1, 120, targetLoc.getZ() + 1);
            location2.setPitch(90.0f);
            location2.setYaw(0.0f);
            
            Fireball fireball2 = shooter.getWorld().spawn(location2, Fireball.class);
            fireball2.setVelocity(new Vector(0,-1.0,0));
            fireball2.setBounce(false);
            fireball2.setIsIncendiary(true);
            fireball2.setShooter(shooter);
        }else{
            shooter.sendMessage("Can not cast there!");
        }
    }
}