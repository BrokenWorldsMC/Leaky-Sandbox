package org.BrokenWorlds.DungeonGenerator;

import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TileSet {
    private int defaultWidth = 16;
    private int defaultHeight = 16;
    private int defaultLength = 16;
    private Biome biome = Biome.PLAINS;

    private String name = "UNKNOWN";
    private File folder;

    private List<Tile> tiles = new ArrayList<Tile>();

    private TileSet() {

    }

    public void addTile(String name) {
        tiles.add(new Tile(this, name));
    }

    // ==== SAVE/LOAD/CREATE ====================

    public void save() {
        if(!folder.exists())
            folder.mkdir();

        File jsonFile = new File(folder, name + ".json");

        TileSetJson json = new TileSetJson();
        json.defaultWidth = this.defaultWidth;
        json.defaultHeight = this.defaultHeight;
        json.defaultLength = this.defaultLength;
        json.biome = this.biome.name();
        json.tiles = new TileJson[this.tiles.size()];

        for(int i = 0; i < this.tiles.size(); i++) {
            Tile t = this.tiles.get(i);
            json.tiles[i] = new TileJson();
            json.tiles[i].name = t.getName();
            json.tiles[i].width = t.getWidth();
            json.tiles[i].height = t.getHeight();
            json.tiles[i].length = t.getLength();
            json.tiles[i].entrances = t.getEntrancesString();
            json.tiles[i].biome = t.getBiome() == null ? null : t.getBiome().name();
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            FileWriter fileWriter = new FileWriter(jsonFile);
            fileWriter.write(gson.toJson(json));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static TileSet Load(String name, File tileSetFolder) {
        File folder = new File(tileSetFolder, name);
        if(!folder.exists())
            return null;

        File jsonFile = new File(folder, name + ".json");
        if(!jsonFile.exists())
            return null;

        Gson gson = new Gson();
        TileSet tileset = new TileSet();
        tileset.name = name;
        tileset.folder = folder;

        try {
            BufferedReader br = new BufferedReader(new FileReader(jsonFile));
            TileSetJson json = gson.fromJson(br, TileSetJson.class);

            tileset.defaultWidth = json.defaultWidth;
            tileset.defaultHeight = json.defaultHeight;
            tileset.defaultLength = json.defaultLength;
            tileset.biome = Biome.valueOf(json.biome);

            for(TileJson t : json.tiles) {
                Tile tile = new Tile(tileset, t.name);
                tile.setWidth(t.width);
                tile.setHeight(t.height);
                tile.setLength(t.length);
                tile.setEntrances(t.entrances);
                tile.setBiome(t.biome == null ? null : Biome.valueOf(t.biome));
                tileset.tiles.add(tile);
            }

        } catch (FileNotFoundException e) {
            //use default values.
        }

        return tileset;
    }

    public static TileSet Create(String name, File tileSetFolder) {
        TileSet tileset = new TileSet();
        tileset.name = name;
        tileset.folder = new File(tileSetFolder, name);
        tileset.save();
        return tileset;
    }

    // ==== getter/setter ==========================
    public String getName() {
        return name;
    }

    // ==== data classes for json ==========================
    private final static class TileSetJson {
        public int defaultWidth = 16;
        public int defaultHeight = 16;
        public int defaultLength = 16;
        public String biome = "Plains";
        public TileJson[] tiles = new TileJson[] { };
    }

    private final static class TileJson {
        public String name = "UNKNOWN";
        public Integer width = null;
        public Integer height= null;
        public Integer length = null;
        public String entrances = "NSWE";
        public String biome = null;
    }
}
