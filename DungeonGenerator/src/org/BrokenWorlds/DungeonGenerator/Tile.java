package org.BrokenWorlds.DungeonGenerator;

import org.bukkit.block.Biome;

public class Tile {
    private TileSet tileSet;
    private String name;
    private Integer width;
    private Integer height;
    private Integer length;
    private int entrances;
    private Biome biome;

    private final int ENTRANCE_NORTH = 1;
    private final int ENTRANCE_SOUTH = 2;
    private final int ENTRANCE_WEST = 4;
    private final int ENTRANCE_EAST = 8;

    public Tile(TileSet tileset, String name) {
        this.tileSet = tileset;
        this.name= name;
    }


    //stuff for entrances
    public boolean hasEntrance(int entrance) {
        return (entrances & entrance) == entrance;
    }

    public void setEntrance(int entrance, boolean set) {
        if(set) {
            entrances |= entrance;
        } else {
            entrance &= ~entrance;
        }
    }

    public void setEntrances(String entrances) {
        setEntrance(ENTRANCE_NORTH, entrances.contains("N"));
        setEntrance(ENTRANCE_SOUTH, entrances.contains("S"));
        setEntrance(ENTRANCE_WEST, entrances.contains("W"));
        setEntrance(ENTRANCE_EAST, entrances.contains("E"));
    }

    public String getEntrancesString() {
        String entrancesString = "";
        entrancesString += hasEntrance(ENTRANCE_NORTH) ? "N" : "";
        entrancesString += hasEntrance(ENTRANCE_SOUTH) ? "S" : "";
        entrancesString += hasEntrance(ENTRANCE_WEST) ? "W" : "";
        entrancesString += hasEntrance(ENTRANCE_EAST) ? "E" : "";
        return entrancesString;
    }
    // end stuff for entrances

    //getter/setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getWidth() {
        return width;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getHeight() {
        return height;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getLength() {
        return length;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    public Biome getBiome() {
        return biome;
    }

}
