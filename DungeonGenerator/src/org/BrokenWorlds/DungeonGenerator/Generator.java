package org.BrokenWorlds.DungeonGenerator;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Generator {
    private World world;
    private TileSet tileSet;
    private int originX = 0;
    private int originY = 100;
    private int originZ = 0;

    private Random random = new Random();

    private final static int WIDTH = 10;
    private final static int LENGTH = 10;


    private Tile[] tiles = new Tile[WIDTH * LENGTH];

    public Generator(World world, TileSet tileSet) {
        this.world = world;
        this.tileSet = tileSet;
    }

    public void setOrigin(int x, int y, int z) {
        originX = x;
        originY = y;
        originZ = z;
    }

    public boolean generate() {
        return generate(System.nanoTime());
    }

    public boolean generate(long seed) {
        random = new Random(seed);

        for(Tile t : tileSet.getTiles()) {
            System.out.println(t.getEntrancesString() + ":" + t.getName());
        }

        int tries = 0;
        while(tries < 10)
        {
            if(fillGrid()) break;
            tries++;
        }

        if(tries == 10) return false;

        pasteToWorld();

        return true;
    }

    private boolean fillGrid() {
        tiles = new Tile[WIDTH * LENGTH];

        for(int x = 0; x < WIDTH; x++) {
            for (int z = 0; z < LENGTH; z++) {
                Tile t = findFittingTile(x,z);
                if(t == null) return false;
                tiles[tileIndex(x,z)] = t;
            }
        }

        return true;
    }

    private void pasteToWorld() {
        for(int x = 0; x < WIDTH; x++) {
            for (int z = 0; z < LENGTH; z++) {
                Tile t = getTileAt(x,z);
                int worldX = originX + x * tileSet.getDefaultWidth();
                int worldY = originY;
                int worldZ = originZ + z * tileSet.getDefaultLength();
                t.pasteTo(new Location(world, worldX, worldY, worldZ));
            }
        }
    }

    private Tile findFittingTile(int x, int z) {
        int entrances = Tile.ENTRANCE_NONE;
        int ignoredEntrances = Tile.ENTRANCE_ALL;

        if(x == 0)
            ignoredEntrances &= ~Tile.ENTRANCE_WEST;
        if(x == WIDTH - 1)
            ignoredEntrances &= ~Tile.ENTRANCE_EAST;
        if(z == 0)
            ignoredEntrances &= ~Tile.ENTRANCE_NORTH;
        if(z == LENGTH - 1)
            ignoredEntrances &= ~Tile.ENTRANCE_SOUTH;

        Tile northTile = getNorthTile(x,z);
        Tile southTile = getSouthTile(x, z);
        Tile westTile = getWestTile(x, z);
        Tile eastTile = getEastTile(x, z);

        if(northTile != null) {
            ignoredEntrances &= ~Tile.ENTRANCE_NORTH;
            entrances |= northTile.hasEntrance(Tile.ENTRANCE_SOUTH) ? Tile.ENTRANCE_NORTH : 0;
        }
        if(southTile != null) {
            ignoredEntrances &= ~Tile.ENTRANCE_SOUTH;
            entrances |= southTile.hasEntrance(Tile.ENTRANCE_NORTH) ? Tile.ENTRANCE_SOUTH : 0;
        }
        if(westTile != null) {
            ignoredEntrances &= ~Tile.ENTRANCE_WEST;
            entrances |= westTile.hasEntrance(Tile.ENTRANCE_EAST) ? Tile.ENTRANCE_WEST : 0;
        }
        if(eastTile != null) {
            ignoredEntrances &= ~Tile.ENTRANCE_EAST;
            entrances |= eastTile.hasEntrance(Tile.ENTRANCE_WEST) ? Tile.ENTRANCE_EAST : 0;
        }

        List<Tile> matchingTiles = tileSet.getMatchingTiles(entrances, ignoredEntrances);
        if(matchingTiles.size() == 0) {
            System.out.println("Couldn't find matching tile for ("+x+"|"+z+") " + entrances + ";" + ignoredEntrances);
            return null;
        }
        return matchingTiles.get(random.nextInt(matchingTiles.size()));
    }

    private Tile getTileAt(int x, int z) {
        if(x < 0 || x >= WIDTH || z < 0 || z >= LENGTH)
            return null;
        return tiles[tileIndex(x,z)];
    }

    private Tile getNorthTile(int x, int z) {
        return getTileAt(x, z-1);
    }
    private Tile getSouthTile(int x, int z) {
        return getTileAt(x, z+1);
    }
    private Tile getWestTile(int x, int z) {
        return getTileAt(x-1, z);
    }
    private Tile getEastTile(int x, int z) {
        return getTileAt(x+1, z);
    }

    private int tileIndex(int x, int z) {
        return z * WIDTH + x;
    }
    private int tileIndexNorth(int x, int z) {
        return tileIndex(x, z-1);
    }
    private int tileIndexSouth(int x, int z) {
        return tileIndex(x, z+1);
    }
    private int tileIndexWest(int x, int z) {
        return tileIndex(x-1, z);
    }
    private int tileIndexEast(int x, int z) {
        return tileIndex(x+1, z);
    }
}
