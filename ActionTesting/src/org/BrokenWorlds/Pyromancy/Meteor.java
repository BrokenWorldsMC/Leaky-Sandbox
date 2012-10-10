package org.BrokenWorlds.Pyromancy;

import org.bukkit.Bukkit;
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
            if (title.equals("Meteor")){
                final Block target = player.getTargetBlock(null, 15);
                fireMeteor(player, target);
                Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {
                    
                    public void run() {
                        fireMeteor(player, target);
                    }
                }, 5L);
                Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {
                    
                    public void run() {
                        fireMeteor(player, target);
                    }
                }, 10L);
                Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {
                    
                    public void run() {
                        fireMeteor(player, target);
                    }
                }, 15L);
                Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {
                    
                    public void run() {
                        fireMeteor(player, target);
                    }
                }, 20L);
                Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {
                    
                    public void run() {
                        fireMeteor(player, target);
                    }
                }, 25L);
                Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {
                    
                    public void run() {
                        fireMeteor(player, target);
                    }
                }, 30L);
            }
        }
    }
    
    public void fireMeteor(Player shooter, Block target) {
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
        }else{
            shooter.sendMessage("Can not cast there!");
        }
    }
}