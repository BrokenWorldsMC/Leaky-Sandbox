package org.BrokenWorlds.Telekinetic;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Skip implements Listener {

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		Action action = event.getAction();
		if (!action.toString().equals("LEFT_CLICK_AIR")
				&& !action.toString().equals("LEFT_CLICK_BLOCK"))
			return;

		ItemStack item = player.getItemInHand();
		String sItem = item.getType().name();
		if (sItem.equals("WRITTEN_BOOK")) {
			String title = ((CraftItemStack) item).getHandle().tag
					.getString("title");
			if (title.equals("Skip")) {
				Block target = player.getTargetBlock(null, 8);
				Location newLoc = new Location(player.getWorld(), target.getX(), target.getY(), target.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
				Location oldLoc = player.getLocation();
				player.getWorld().playEffect(oldLoc, Effect.ENDER_SIGNAL, 10);
				player.getWorld().playEffect(oldLoc, Effect.SMOKE, 10);
				player.teleport(newLoc);
			}
		}
	}
}
	