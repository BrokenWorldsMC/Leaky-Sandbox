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
        Tile tile = new Tile(this, name);
        tiles.add(tile);

        if(!tile.hasEntrance(Tile.ENTRANCE_ALL)) {
            Tile[] rotatedCopies = tile.getRotatedCopies();
            tiles.add(rotatedCopies[0]);
            tiles.add(rotatedCopies[1]);
            tiles.add(rotatedCopies[2]);
        }
    }

    public Tile getTile(String name) {
        for(Tile t : tiles) {
            if(t.getName().equalsIgnoreCase(name))
                return t;
        }
        return null;
    }

    public List<Tile> getMatchingTiles(int entrances, int ignoredEntrances) {
        List<Tile> matchingTiles = new ArrayList<Tile>();

        for(Tile t : tiles) {
            if(t.hasEntrance(entrances, ignoredEntrances))
                matchingTiles.add(t);
        }

        return matchingTiles;
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

        ArrayList<TileJson> jsonTiles = new ArrayList<TileJson>();
        for(int i = 0; i < this.tiles.size(); i++) {
            Tile t = this.tiles.get(i);
            if(t.isRotatedCopy()) continue;

            TileJson tJson = new TileJson();
            tJson.name = t.getName();
            tJson.width = t.getWidth();
            tJson.height = t.getHeight();
            tJson.length = t.getLength();
            tJson.entrances = t.getEntrancesString();
            tJson.biome = t.getBiome() == null ? null : t.getBiome().name();
            tJson.probability = t.getProbability();
            jsonTiles.add(tJson);
        }
        json.tiles = jsonTiles.toArray(new TileJson[jsonTiles.size()]);


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
                tile.setProbability(t.probability);
                tileset.tiles.add(tile);

                if(!tile.hasEntrance(Tile.ENTRANCE_ALL)) {
                    Tile[] rotatedCopies = tile.getRotatedCopies();
                    tileset.tiles.add(rotatedCopies[0]);
                    tileset.tiles.add(rotatedCopies[1]);
                    tileset.tiles.add(rotatedCopies[2]);
                }
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

    public File getFolder() {
        return this.folder;
    }

    public int getDefaultWidth() {
        return defaultWidth;
    }

    public int getDefaultHeight() {
        return defaultHeight;
    }

    public int getDefaultLength() {
        return defaultLength;
    }

    public List<Tile> getTiles() {
        return tiles;
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
        public Integer probability = null;
    }
}
