package org.BrokenWorlds.Recipes;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class RecipesMain extends JavaPlugin implements Listener {

    public Logger logger = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        loadConfiguration();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        AddRecipes();
        logger.info("[BrokenWorldsRecipes] has been enabled!");
    }

    public void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        logger.info("[BrokenWorldsRecipes] has been disabled!");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Set<String> recipes = getConfig().getConfigurationSection("Recipes").getKeys(false);
        if (player.getItemInHand().getTypeId() == 387 && player.getItemInHand().getDurability() >= 1) {
            if (recipes != null) {
                for (String s : recipes) {
                    String bookdura = getConfig().getString("Recipes." + s + ".BookDurability");
                    if (bookdura != null) {
                        if (player.getItemInHand().getDurability() == Short.parseShort(bookdura)) {
                            Material mat1 = Material.getMaterial(getConfig().getString("Recipes." + s + ".Output"));
                            if (mat1 == Material.WRITTEN_BOOK) {
                                String booktitle = getConfig().getString("Recipes." + s + ".BookTitle");
                                String bookauthor = getConfig().getString("Recipes." + s + ".BookAuthor");
                                String bookpage = getConfig().getString("Recipes." + s + ".BookPages");
                                String outnum = getConfig().getString("Recipes." + s + ".Amount");
                                CustomBook custombook;
                                if (outnum != null) {
                                    custombook = new CustomBook(new ItemStack(387, Integer.parseInt(outnum)));
                                } else {
                                    custombook = new CustomBook(new ItemStack(387, 1));
                                }
                                if (booktitle != null) {
                                    custombook.setTitle(booktitle.trim());
                                }
                                if (bookauthor != null) {
                                    custombook.setAuthor(bookauthor.trim());
                                }
                                if (bookpage != null) {
                                    String[] booktitlearray = bookpage.split("(?i)&page");
                                    custombook.setPages(booktitlearray);
                                }
                                ItemStack writtenbook = custombook.getItemStack();
                                player.setItemInHand(writtenbook);
                            }
                        }
                    }
                }
            }
        }
    }

    public void AddRecipes() {
        ConfigurationSection recipes = getConfig().getConfigurationSection("Recipes");
        if (recipes != null)
            for (String key : recipes.getKeys(false)) {
                Material mat1 = Material.getMaterial(getConfig().getString("Recipes." + key + ".Output"));
                String outnum = getConfig().getString("Recipes." + key + ".Amount");
                ItemStack i;
                if (outnum != null) {
                    i = new ItemStack(mat1, Integer.parseInt(outnum));
                } else {
                    i = new ItemStack(mat1, 1);
                }
                String bookdura = new String();
                bookdura = getConfig().getString("Recipes." + key + ".BookDurability");
                
                if (bookdura != null) {
                    i.setDurability(Short.parseShort(getConfig().getString("Recipes." + key + ".BookDurability").toString()));
                }

                String shape1 = getConfig().getString("Recipes." + key + ".TopRow");
                String shape2 = getConfig().getString("Recipes." + key + ".Middle");
                String shape3 = getConfig().getString("Recipes." + key + ".Bottom");

                ShapedRecipe recipe = new ShapedRecipe(i);
                recipe.shape(new String[] { shape1, shape2, shape3 });

                List<String> Characterslist = getConfig().getStringList("Recipes." + key + ".Characters");

                List<String> CharactersMaterials = getConfig().getStringList("Recipes." + key + ".CharactersMaterials");

                for (int id = 0; (id < Characterslist.size()) && (id < CharactersMaterials.size()); id++) {
                    recipe.setIngredient(((String) Characterslist.get(id)).charAt(0), Material.getMaterial((String) CharactersMaterials.get(id)));
                }

                getServer().addRecipe(recipe);
                // logger.info("Added recipe " + key);
            }
    }
}