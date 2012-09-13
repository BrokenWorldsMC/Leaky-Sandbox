package org.BrokenWorlds.BookStats;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;
import net.minecraft.server.NBTTagString;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.MemcachedClient;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class BooksStatsMain extends JavaPlugin implements Listener {

    private static final Logger logger = Logger.getLogger("Minecraft");
    private CouchbasePlayerStatsSessionStore store;
    private MemcachedClient memcachedClient;

    public void setUp() throws IOException {
        memcachedClient =
                new MemcachedClient(
                new ConnectionFactoryBuilder().setFailureMode(FailureMode.Retry).build(),
                AddrUtil.getAddresses("localhost:11211"));
        store = new CouchbasePlayerStatsSessionStore();
        store.setMemcachedClient(memcachedClient);
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        try {
            setUp();
        } catch (IOException ex) {
            Logger.getLogger(BooksStatsMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        logger.log(Level.SEVERE, "[BookStats] Plugin Enabled!");
    }

    @Override
    public void onDisable() {
        logger.log(Level.SEVERE, "[BookStats] Plugin Disabled!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!event.getPlayer().hasPlayedBefore()) {
            store.storePlayerPVPKills(event.getPlayer().getName(), 0);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() instanceof Player) {
            Player killer = event.getEntity().getKiller();
            int kills = store.getPlayerPVPKills(killer.getName()) + 1;
            store.storePlayerPVPKills(killer.getName(), kills);
        }
    }

    //Updates on movement right now so always updated. 
    //Need to fix having to open the book twice to update stats then will remove this.
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getTypeId() == 387) {
            ItemStack stack = player.getItemInHand();
            net.minecraft.server.ItemStack book = ((CraftItemStack) stack).getHandle();
            if (book.tag.getString("title").equalsIgnoreCase("Bookstats")
                    || book.tag.getString("title").equalsIgnoreCase(player.getName() + "'s " + "Stats")
                    && book.tag.getString("author").equalsIgnoreCase(player.getName())) {
                CustomBook bi = new CustomBook(new ItemStack(387, 1));
                int kills = store.getPlayerPVPKills(player.getName());
                String linebreak = "\n\247r\2470";
                String doublelinebreak = "\n\247r\2470\n\247r\2470";
                String[] pages = {"\2473" + player.getName() + "\2472\247l Stats" + doublelinebreak
                    + "\2474\247lWorld: \2475" + upperCaseFirst(player.getWorld().getName()) + linebreak
                    + "\2474\247lTotal Kills:\n" + "\2475 " + kills};
                bi.setPages(pages);
                bi.setAuthor(player.getName());
                bi.setTitle(player.getName() + "'s " + "Stats");
                ItemStack writtenbook = bi.getItemStack();
                player.setItemInHand(writtenbook);
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getTypeId() == 387) {
            ItemStack stack = player.getItemInHand();
            net.minecraft.server.ItemStack book = ((CraftItemStack) stack).getHandle();
            if (book.tag.getString("title").equalsIgnoreCase("Bookstats")
                    || book.tag.getString("title").equalsIgnoreCase(player.getName() + "'s " + "Stats")
                    && book.tag.getString("author").equalsIgnoreCase(player.getName())) {
                CustomBook bi = new CustomBook(new ItemStack(387, 1));
                int kills = store.getPlayerPVPKills(player.getName());
                String linebreak = "\n\247r\2470";
                String doublelinebreak = "\n\247r\2470\n\247r\2470";
                String[] pages = {"\2473" + player.getName() + "\2472\247l Stats" + doublelinebreak
                    + "\2474\247lWorld: \2475" + upperCaseFirst(player.getWorld().getName()) + linebreak
                    + "\2474\247lTotal Kills:\n" + "\2475 " + kills};
                bi.setPages(pages);
                bi.setAuthor(player.getName());
                bi.setTitle(player.getName() + "'s " + "Stats");
                ItemStack writtenbook = bi.getItemStack();
                player.setItemInHand(writtenbook);
                player.updateInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand().getTypeId() == 387) {
            ItemStack stack = player.getItemInHand();
            net.minecraft.server.ItemStack book = ((CraftItemStack) stack).getHandle();
            if (book.tag.getString("title").equalsIgnoreCase("Bookstats")
                    || book.tag.getString("title").equalsIgnoreCase(player.getName() + "'s " + "Stats")
                    && book.tag.getString("author").equalsIgnoreCase(player.getName())) {
                CustomBook bi = new CustomBook(new ItemStack(387, 1));
                int kills = store.getPlayerPVPKills(player.getName());
                String linebreak = "\n\247r\2470";
                String doublelinebreak = "\n\247r\2470\n\247r\2470";
                String[] pages = {"\2473" + player.getName() + "\2472\247l Stats" + doublelinebreak
                    + "\2474\247lWorld: \2475" + upperCaseFirst(player.getWorld().getName()) + linebreak
                    + "\2474\247lTotal Kills:\n" + "\2475 " + kills};
                bi.setPages(pages);
                bi.setAuthor(player.getName());
                bi.setTitle(player.getName() + "'s " + "Stats");
                ItemStack writtenbook = bi.getItemStack();
                player.setItemInHand(writtenbook);
                player.updateInventory();
            }
        }
    }

    public void setPages(net.minecraft.server.ItemStack item, String[] newpages) {
        NBTTagCompound tags = item.tag;
        if (tags == null) {
            tags = item.tag = new NBTTagCompound();
        }
        NBTTagList pages = new NBTTagList("pages");
        //we don't want to throw any errors if the book is blank!
        if (newpages.length == 0) {
            pages.add(new NBTTagString("1", ""));
        } else {
            for (int i = 0; i < newpages.length; i++) {
                pages.add(new NBTTagString("" + i + "", newpages[i]));
            }
        }
        tags.set("pages", pages);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = (Player) sender;
        if (cmd.getName().equalsIgnoreCase("book")) {
            CustomBook bi = new CustomBook(new ItemStack(387, 1));
            int kills = store.getPlayerPVPKills(player.getName());
            String linebreak = "\n\247r\2470";
            String doublelinebreak = "\n\247r\2470\n\247r\2470";
            String[] pages = {"\2473" + player.getName() + "\2472\247l Stats" + doublelinebreak 
                    + "\2474\247lWorld: \2475" + upperCaseFirst(player.getWorld().getName()) + linebreak 
                    + "\2474\247lTotal Kills:\n" + "\2475 " + kills};
            bi.setPages(pages);
            bi.setAuthor(player.getName());
            bi.setTitle(player.getName() + "'s " + "Stats");
            ItemStack writtenbook = bi.getItemStack();
            player.getInventory().addItem(writtenbook);
            player.updateInventory();
            return true;
        }
        return false;
    }

    public String upperCaseFirst(String stringtext) {
        char[] stringArray = stringtext.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        return new String(stringArray);
    }

    public String convertToDate(long longtext) {
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(longtext).trim();
    }
}