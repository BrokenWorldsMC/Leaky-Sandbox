package org.BrokenWorlds.Telekinetic;

import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Blink implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        Action action = event.getAction();

        if (!action.toString().equals("LEFT_CLICK_AIR") && !action.toString().equals("LEFT_CLICK_BLOCK"))
            return;

        ItemStack item = player.getItemInHand();
        String sItem = item.getType().name();
        if (sItem.equals("WRITTEN_BOOK")) {
            String title = ((CraftItemStack) item).getHandle().tag.getString("title");
            if (title.equals("Blink")) {
            }
        }
    }
}
