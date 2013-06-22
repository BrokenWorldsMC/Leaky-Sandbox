package org.BrokenWorlds.DungeonGenerator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.PluginClassLoader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class DungeonGeneratorMain extends JavaPlugin {
    private File dataFolder;
    private File tileSetsFolder;

    private Map<String, TileSet> tileSets = new HashMap<String, TileSet>();

    @Override
    public void onEnable(){
        dataFolder = getDataFolder();
        tileSetsFolder = new File(dataFolder, "TileSets");

        //create folders
        if(!dataFolder.exists())
            dataFolder.mkdir();
        if(!tileSetsFolder.exists())
            tileSetsFolder.mkdir();

        //load jnbt library to access .schematic-files
        try {
            File jnbt = new File(dataFolder, "jnbt-1.1.jar");

            //if the jnbt-1.1.jar doesn't exist extract it from the resources
            if(!jnbt.exists())
                saveResource("jnbt-1.1.jar", true);

            ((PluginClassLoader) getClassLoader()).addURL(jnbt.toURI().toURL());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        LoadTilesets();
    }

    @Override
    public void onDisable() {
        getLogger().info("onEnable has been invoked!");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("dungeongenerator")) {
            if(args.length == 0) {
                sender.sendMessage("ERROR: Not enough parameters! Usage: " + cmd.getUsage());
                return true;
            }

            if(args[0] == "test")
                return commandTest(sender, cmd, label, args);
            else
                sender.sendMessage("ERROR: Unknown sub command! Usage: " + cmd.getUsage());
            return true;
        } else if(cmd.getName().equalsIgnoreCase("tilesets")) {
            if(args.length == 0) {
                sender.sendMessage("ERROR: Not enough parameters! Usage: " + cmd.getUsage());
                return true;
            }

            if(args[0].equalsIgnoreCase("list"))
                ListTilesets(sender);
            else if(args[0].equalsIgnoreCase("save")) {
                for(TileSet t: tileSets.values())
                    t.save();
            } else if(args[0].equalsIgnoreCase("create")) {
                if(args.length < 2)  {
                    sender.sendMessage("ERROR: Not enough parameters! Usage: " + cmd.getUsage());
                } else {
                    TileSet t = TileSet.Create(args[1], tileSetsFolder);
                    tileSets.put(t.getName(), t);
                }
            }
            else if(args[0].equalsIgnoreCase("addtile")) {
                if(args.length < 3)  {
                    sender.sendMessage("ERROR: Not enough parameters! Usage: " + cmd.getUsage());
                } else {
                    AddTile(sender, args[1], args[2]);
                }
            } else if(args[0].equalsIgnoreCase("reload"))
                LoadTilesets();
            else
                sender.sendMessage("ERROR: Unknown sub command '" +  args[0] + "'! Usage: " + cmd.getUsage());
        }
        return false;
    }

    private boolean commandTest(CommandSender sender, Command cmd, String label, String[] args) {
        //has to be player
        if (!(sender instanceof Player)) {
            sender.sendMessage("ERROR: You must be a player!");
            return true;
        }
        if(args.length < 3) {
            sender.sendMessage("ERROR: Not enough parameters! Usage: " + cmd.getUsage());
            return true;
        }

        String tileSetName = args[1];
        String tileName = args[2];

        File tileSet = new File(tileSetsFolder, tileSetName);
        if(!tileSet.exists()) {
            sender.sendMessage("TileSet '"+tileSetName+"' doesn't exist!");
            return true;
        }

        File file = new File(tileSet, tileName + ".schematic");
        if(!file.exists()) {
            sender.sendMessage("File doesn't exist: " + file.getAbsolutePath());
            return true;
        }
        SchematicFile schematic = new SchematicFile(file);
        sender.sendMessage("Loaded " + file.getName());

        World world = ((Player)sender).getWorld();
        Location location = new Location(world, 0, 100, 0);
        schematic.pasteTo(location);

        return true;

    }

    private void ListTilesets(CommandSender sender) {
        sender.sendMessage("Tilesets:");
        for(TileSet t: tileSets.values()) {
            sender.sendMessage(" - " + t.getName());
        }
    }

    private void LoadTilesets(){
        tileSets = new HashMap<String, TileSet>();

        for(File f: tileSetsFolder.listFiles()) {
            if(f.isDirectory() && f.exists()) {
                TileSet tileset = TileSet.Load(f.getName(), tileSetsFolder);
                if(tileset != null)
                    tileSets.put(tileset.getName(), tileset);
            }
        }
    }

    private void AddTile(CommandSender sender,String tileSetName, String tileName) {
        if(tileSets.containsKey(tileSetName)) {
            TileSet tileSet = tileSets.get(tileSetName);
            tileSet.addTile(tileName);
            tileSet.save();
        } else {
            sender.sendMessage("TileSet '"+tileSetName+"' doesn't exist");
        }
    }
}
