package org.brokenworlds.recipes;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class RecipesMain extends JavaPlugin implements Listener {

    public Logger logger = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        loadConfiguration();
        AddRecipes();
        logger.info("[BrokenWorlds Recipes] has been enabled!");
    }

    public void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        logger.info("[BrokenWorlds Recipes] has been disabled!");
    }

    public void AddRecipes() {
        ConfigurationSection recipes = getConfig().getConfigurationSection("Recipes");
        if (recipes != null) {
            for (String key : recipes.getKeys(false)) {
                Material material = Material.getMaterial(getConfig().getString("Recipes." + key + ".Output"));
                Integer amountNumber = getConfig().getInt("Recipes." + key + ".Amount");
                ItemStack itemStack;

                if (amountNumber != null) {
                    itemStack = new ItemStack(material, amountNumber);
                } else {
                    itemStack = new ItemStack(material, 1);
                }

                if (material == Material.WRITTEN_BOOK) {
                    String bookAuthor = getConfig().getString("Recipes." + key + ".BookAuthor");
                    String bookPages = getConfig().getString("Recipes." + key + ".BookPages");
                    BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();

                    if (bookAuthor != null) {
                        bookMeta.setAuthor(ChatColor.translateAlternateColorCodes('&', bookAuthor));
                    } else {
                        bookMeta.setAuthor("");
                    }

                    if (bookPages != null) {
                        String[] bookPageArray = ChatColor.translateAlternateColorCodes('&', bookPages).split("&p");
                        bookMeta.setPages(bookPageArray);
                    } else {
                        bookMeta.setPages("");
                    }

                    itemStack.setItemMeta(bookMeta);
                }

                String displayName = getConfig().getString("Recipes." + key + ".DisplayName");

                if (displayName != null) {
                    ItemMeta meta = itemStack.getItemMeta();
                    meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', displayName));
                    itemStack.setItemMeta(meta);
                }

                String shape1 = getConfig().getString("Recipes." + key + ".TopRow");
                String shape2 = getConfig().getString("Recipes." + key + ".Middle");
                String shape3 = getConfig().getString("Recipes." + key + ".Bottom");

                if (shape1 != null && shape2 != null && shape3 != null) {
                    ShapedRecipe recipe = new ShapedRecipe(itemStack);
                    recipe.shape(shape1, shape2, shape3);

                    List<String> charactersList = getConfig().getStringList("Recipes." + key + ".Characters");

                    if (charactersList != null) {
                        List<String> charactersMaterials = getConfig().getStringList("Recipes." + key + ".CharactersMaterials");

                        if (charactersMaterials != null) {
                            for (int id = 0; (id < charactersList.size()) && (id < charactersMaterials.size()); id++) {
                                recipe.setIngredient((charactersList.get(id)).charAt(0), Material.getMaterial(charactersMaterials.get(id)));
                            }

                            getServer().addRecipe(recipe);
                            // logger.info("Added recipe " + key);
                        }
                    }
                }
            }
        }
    }
}
