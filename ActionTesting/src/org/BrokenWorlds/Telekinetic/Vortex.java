package org.BrokenWorlds.Telekinetic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Lists;

public class Vortex implements Listener {
    
    List<String> vortexCasters = Lists.newArrayList();
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Action action = event.getAction();
        
        if(!action.toString().equals("LEFT_CLICK_AIR") && !action.toString().equals("LEFT_CLICK_BLOCK"))
            return;
        
        ItemStack item = player.getItemInHand();
        String sItem = item.getType().name();
        if(sItem.equals("WRITTEN_BOOK")){
        String title = ((CraftItemStack) item).getHandle().tag.getString("title");
            if(title.equals("Vortex")){
                if(vortexCasters.contains(player.getName())){
                    player.sendMessage(ChatColor.YELLOW + "-- Vortex is on cooldown --");
                    return;
                }
                
                //Smoke Effect on cast
                player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 15);
                //Sound For future use
                //player.getWorld().playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0F, 0.5F);
                
                //Absorbs Items in a 5 radius
                absorbItems(5, player);
                
                addVortexCaster(player);
                Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {
                    public void run() {
                        removeVortexCaster(player);
                    }
                }, 600L);
                
                player.sendMessage(ChatColor.GREEN + "Successfully casted " + ChatColor.YELLOW + "Vortex" + ChatColor.GREEN);
                
                }
            }
        }
    
    public void addVortexCaster(Player player){
        vortexCasters.add(player.getName());
    }
    
    public void removeVortexCaster(Player player){
        vortexCasters.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "-- " + ChatColor.YELLOW + "Vortex" + ChatColor.GREEN + " cooldown reset --");
    }

    public void absorbItems(int radius, Player p) {
        ArrayList<Item> items = getItems(p.getNearbyEntities(radius, radius, radius));
        if (items != null) {
            for (Item item : items) {
                HashMap<Integer, ItemStack> leftovers = p.getInventory().addItem(new ItemStack[] { item.getItemStack() });
                if (leftovers.isEmpty())
                    item.remove();
                else
                    item.getItemStack().setAmount(((ItemStack) leftovers.get(Integer.valueOf(0))).getAmount());
            }
        }
    }
    
    public ArrayList<Item> getItems(List<Entity> nearbyEntities) {
        ArrayList<Item> items = new ArrayList<Item>();
        for (Entity item : nearbyEntities) {
          if (((item instanceof Item)) && (item != null)) {
            items.add((Item)item);
          }
        }
        return items;
      }
}
