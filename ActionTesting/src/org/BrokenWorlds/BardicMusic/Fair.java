package org.BrokenWorlds.BardicMusic;

import java.util.List;
import java.util.ListIterator;

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

public class Fair implements Listener {

	public List<Entity> nearBy = Lists.newArrayList();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Location loc = player.getLocation();
		ItemStack item = player.getItemInHand();
		if (item.getTypeId() == 2260) {
			player.getWorld().playEffect(loc, Effect.RECORD_PLAY, 2260, 10);
			nearBy = player.getNearbyEntities(5, 5, 5);
			ListIterator<Entity> it = nearBy.listIterator();
			while (it.hasNext()) {
				Entity next = it.next();
				if (next instanceof Player) {
					((Player) next).addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
				}
			}
		}
		
	}
	
}
