package org.BrokenWorlds.DungeonGenerator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;

import java.io.File;

public class Tile {
    private TileSet tileSet;
    private String name;
    private Integer width;
    private Integer height;
    private Integer length;
    private int entrances;
    private Biome biome;
    private Integer probability;
    private boolean isRotatedCopy = false;

    private Helper.Rotation rotation = Helper.Rotation.Rotate0;

    public static final int DEFAULT_PROBABILITY = 10;

    public static final int ENTRANCE_NONE = 0;
    public static final int ENTRANCE_NORTH = 1;
    public static final int ENTRANCE_SOUTH = 2;
    public static final int ENTRANCE_WEST = 4;
    public static final int ENTRANCE_EAST = 8;
    public static final int ENTRANCE_ALL = ENTRANCE_NORTH | ENTRANCE_SOUTH | ENTRANCE_WEST | ENTRANCE_EAST;

    public Tile(TileSet tileset, String name) {
        this.tileSet = tileset;
        this.name= name;
    }

    public Tile(Tile copy) {
        this.tileSet = copy.tileSet;
        this.name = copy.name;
        this.width = copy.width;
        this.height = copy.height;
        this.length = copy.length;
        this.entrances = copy.entrances;
        this.biome = copy.biome;
        this.probability = copy.probability;
    }


    //stuff for entrances
    public boolean hasEntrance(int entrance) {
        return (entrances & entrance) == entrance;
    }

    public boolean hasEntrance(int entrances, int ignoredEntrances) {
        //if the entrance isn't ignored and the entrance bit is different return false.
        if((ignoredEntrances & ENTRANCE_NORTH) == 0 && (entrances & ENTRANCE_NORTH) != (this.entrances & ENTRANCE_NORTH))
            return false;
        if((ignoredEntrances & ENTRANCE_SOUTH) == 0 && (entrances & ENTRANCE_SOUTH) != (this.entrances & ENTRANCE_SOUTH))
            return false;
        if((ignoredEntrances & ENTRANCE_WEST) == 0 && (entrances & ENTRANCE_WEST) != (this.entrances & ENTRANCE_WEST))
            return false;
        if((ignoredEntrances & ENTRANCE_EAST) == 0 && (entrances & ENTRANCE_EAST) != (this.entrances & ENTRANCE_EAST))
            return false;

        return true;
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

    //rotation stuff

    public Tile[] getRotatedCopies() {
        Tile[] rotatedCopies = new Tile[3];
        rotatedCopies[0] = new Tile(this);
        rotatedCopies[1] = new Tile(this);
        rotatedCopies[2] = new Tile(this);
        rotatedCopies[0].rotate(Helper.Rotation.Rotate90);
        rotatedCopies[1].rotate(Helper.Rotation.Rotate180);
        rotatedCopies[2].rotate(Helper.Rotation.Rotate270);
        return rotatedCopies;
    }

    private void rotate(Helper.Rotation rotation) {
        this.isRotatedCopy = true;
        this.rotation = rotation;

        //not nice but works :D
        if(rotation == Helper.Rotation.Rotate90) {
            rotateEntrances90();
        } else if(rotation == Helper.Rotation.Rotate180) {
            rotateEntrances90(); rotateEntrances90();
        } else if(rotation == Helper.Rotation.Rotate270) {
            rotateEntrances90(); rotateEntrances90(); rotateEntrances90();
        }
    }

    private void rotateEntrances90() {
        int newEntrances = ENTRANCE_NONE;

        if(hasEntrance(ENTRANCE_NORTH))
            newEntrances |= ENTRANCE_EAST;
        if(hasEntrance(ENTRANCE_EAST))
            newEntrances |= ENTRANCE_SOUTH;
        if(hasEntrance(ENTRANCE_SOUTH))
            newEntrances |= ENTRANCE_WEST;
        if(hasEntrance(ENTRANCE_WEST))
            newEntrances |= ENTRANCE_NORTH;

        this.entrances = newEntrances;
    }

    //end rotation

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

    public void setProbability(Integer probability) {
        this.probability = probability;
    }

    public Integer getProbability() {
        return probability;
    }

    public int getRealProbability() {
        return probability == null ? DEFAULT_PROBABILITY : probability;
    }

    public boolean isRotatedCopy() {
        return isRotatedCopy;
    }

    public SchematicFile getShematicFile() {
        return new SchematicFile(new File(tileSet.getFolder(), getName() + ".schematic"));
    }

    public void pasteTo(Location location) {
       getShematicFile().pasteTo(location, rotation);
    }

    public void pasteTo(World w, int x, int y, int z) {
        getShematicFile().pasteTo(w, x,y,z, rotation);
    }
}
