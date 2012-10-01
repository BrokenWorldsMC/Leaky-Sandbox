package org.BrokenWorlds.BardicMusic;

import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.google.common.collect.Lists;

public class Mellow implements Listener {

	public List<Entity> nearBy = Lists.newArrayList();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		final Location loc = player.getLocation();
		ItemStack item = player.getItemInHand();
		if (item.getTypeId() == 2262) {
			final int id = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {
				@Override
				public void run() {
					player.getWorld().playEffect(loc, Effect.RECORD_PLAY, 2262, 10);
					
				}
			});
			
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Bukkit.getPluginManager().getPlugin("SkillTesting"), new Runnable() {
				@Override
				public void run() {
					Bukkit.getServer().getScheduler().cancelTask(id);
				}
			}, 200L);
			nearBy = player.getNearbyEntities(5, 5, 5);
			ListIterator<Entity> it = nearBy.listIterator();
			while (it.hasNext()) {
				Entity next = it.next();
				if (next instanceof Player) {
					((Player) next).addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 1));
				}
			}
		}
		
	}
	
}
