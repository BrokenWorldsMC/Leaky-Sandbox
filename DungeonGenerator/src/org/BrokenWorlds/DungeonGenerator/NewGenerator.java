package org.BrokenWorlds.DungeonGenerator;

import org.bukkit.World;

import java.util.*;

public class NewGenerator {
    private World world;
    private TileSet tileSet;
    private Random random;

    private HashMap<Long, TileInfo> tiles;

    private int maxDistance = 5;
    private int originX = 0;
    private int originY = 5;
    private int originZ = 0;

    public NewGenerator(World world) {
        this.world = world;
    }

    public void setOrigin(int x, int y, int z) {
        originX = x; originY = y; originZ = z;
    }

    public void generate(TileSet tileSet) {
        generate(tileSet, System.nanoTime());
    }

    public void generate(TileSet tileSet, long seed) {
        System.out.println("====== START ======");
        System.out.println("start clearing world");
        clearWorld(); //this takes some time
        System.out.println("clearing world done");

        this.tileSet = tileSet;

        random = new Random(seed);

        tiles = new HashMap<Long, TileInfo>();

        Queue<TilePosition> nextTiles = new LinkedList<TilePosition>();

        //spawn tile
        System.out.println("setting up spawn tile");
        Tile spawnTile = pickRandomTile(this.tileSet.getMatchingTiles(Tile.ENTRANCE_ALL, Tile.ENTRANCE_NONE));
        setTileAt(0,0, new TileInfo(spawnTile, new TilePosition(0,0), 0));

        nextTiles.offer(new TilePosition(0,-1));
        nextTiles.offer(new TilePosition(0,+1));
        nextTiles.offer(new TilePosition(-1,0));
        nextTiles.offer(new TilePosition(+1,0));

        //all other tiles
        int iterations = 0;
        System.out.println("starting to generate tiles");
        while(!nextTiles.isEmpty() && iterations < 1000) {
            TilePosition currentPos = nextTiles.poll();

            if(getTileAt(currentPos.X, currentPos.Z) != null) {
                System.out.println("already a tile at ("+currentPos.X+"|"+currentPos.Z+"): " + getTileAt(currentPos.X, currentPos.Z).Tile.getName());
                continue;
            }

            TileInfo newTile = findFittingTile(currentPos.X, currentPos.Z);
            if(newTile == null) continue;

            iterations++;

            setTileAt(currentPos.X, currentPos.Z, newTile);

            //add all connecting tiles to nextTiles, if they aren't set yet
            if(newTile.Tile.hasEntrance(Tile.ENTRANCE_NORTH) && getTileAt(currentPos.X, currentPos.Z - 1) == null)
                nextTiles.offer(new TilePosition(currentPos.X, currentPos.Z - 1));
            if(newTile.Tile.hasEntrance(Tile.ENTRANCE_SOUTH) && getTileAt(currentPos.X, currentPos.Z + 1) == null)
                nextTiles.offer(new TilePosition(currentPos.X, currentPos.Z + 1));
            if(newTile.Tile.hasEntrance(Tile.ENTRANCE_WEST) && getTileAt(currentPos.X - 1, currentPos.Z) == null)
                nextTiles.offer(new TilePosition(currentPos.X - 1, currentPos.Z));
            if(newTile.Tile.hasEntrance(Tile.ENTRANCE_EAST) && getTileAt(currentPos.X + 1, currentPos.Z) == null)
                nextTiles.offer(new TilePosition(currentPos.X + 1, currentPos.Z));
        }
        System.out.println("generation done ("+iterations+" iterations)");

        System.out.println("pasting to world");
        pasteToWorld();
        System.out.println("====== DONE ======");
    }

    private void clearWorld() {
        for(int x = originX - (maxDistance + 1) * tileSet.getDefaultWidth();
            x < originX + (maxDistance + 1) * tileSet.getDefaultWidth(); x++) {
            for(int y = originY; y < originY + tileSet.getDefaultHeight() + (maxDistance + 1); y++) {
                for(int z = originZ - (maxDistance + 1) * tileSet.getDefaultLength();
                    z < originZ + (maxDistance + 1) * tileSet.getDefaultWidth(); z++)
                {
                    world.getBlockAt(x,y,z).setTypeIdAndData(0,(byte)0,false);
                }
            }
        }
    }

    private void pasteToWorld() {
        for(TileInfo tile: tiles.values()) {
            int worldX = originX + tile.Position.X * tileSet.getDefaultWidth();
            int worldY = originY + tile.Distance;
            int worldZ = originZ + tile.Position.Z * tileSet.getDefaultLength();
            tile.Tile.pasteTo(world, worldX, worldY, worldZ);
        }
    }

    private TileInfo getTileAt(int x, int z) {
        return tiles.get(getTileIndex(x,z));
    }

    private void setTileAt(int x, int z, TileInfo tile) {
        tiles.put(getTileIndex(x,z), tile);
        System.out.println("set tile at ("+x+"|"+z+"): " + tile.Tile.getName());
    }

    private Long getTileIndex(int x, int z) {
        long index = (x & 0xFFFFFFFFL) | (long)z << 32;
        return index;
    }

    private TileInfo findFittingTile(int x, int z) {
        int entrances = Tile.ENTRANCE_NONE;
        int ignoredEntrances = Tile.ENTRANCE_ALL;
        int shortestDistance = Integer.MAX_VALUE;

        TileInfo northTile = getTileAt(x,z-1);
        TileInfo southTile = getTileAt(x,z+1);
        TileInfo  westTile = getTileAt(x-1,z);
        TileInfo  eastTile = getTileAt(x+1,z);

        if(northTile != null) {
            ignoredEntrances &= ~Tile.ENTRANCE_NORTH;
            entrances |= northTile.Tile.hasEntrance(Tile.ENTRANCE_SOUTH) ? Tile.ENTRANCE_NORTH : 0;

            ////if(northTile.Distance < shortestDistance) shortestDistance = northTile.Distance + 1;
            if(northTile.Distance < shortestDistance && northTile.Tile.hasEntrance(Tile.ENTRANCE_SOUTH))
                shortestDistance = northTile.Distance + 1;
        }
        if(southTile != null) {
            ignoredEntrances &= ~Tile.ENTRANCE_SOUTH;
            entrances |= southTile.Tile.hasEntrance(Tile.ENTRANCE_NORTH) ? Tile.ENTRANCE_SOUTH : 0;

            ////if(southTile.Distance < shortestDistance) shortestDistance = southTile.Distance + 1;
            if(southTile.Distance < shortestDistance && southTile.Tile.hasEntrance(Tile.ENTRANCE_NORTH))
                shortestDistance = southTile.Distance + 1;
        }
        if(westTile != null) {
            ignoredEntrances &= ~Tile.ENTRANCE_WEST;
            entrances |= westTile.Tile.hasEntrance(Tile.ENTRANCE_EAST) ? Tile.ENTRANCE_WEST : 0;

            ////if(westTile.Distance < shortestDistance) shortestDistance = westTile.Distance + 1;
            if(westTile.Distance < shortestDistance && westTile.Tile.hasEntrance(Tile.ENTRANCE_EAST))
                shortestDistance = westTile.Distance + 1;
        }
        if(eastTile != null) {
            ignoredEntrances &= ~Tile.ENTRANCE_EAST;
            entrances |= eastTile.Tile.hasEntrance(Tile.ENTRANCE_WEST) ? Tile.ENTRANCE_EAST : 0;

            ////if(eastTile.Distance < shortestDistance) shortestDistance = eastTile.Distance + 1;
            if(eastTile.Distance < shortestDistance && eastTile.Tile.hasEntrance(Tile.ENTRANCE_WEST))
                shortestDistance = eastTile.Distance + 1;
        }

        if(shortestDistance >= this.maxDistance)
            ignoredEntrances = Tile.ENTRANCE_NONE;

        List<Tile> matchingTiles = tileSet.getMatchingTiles(entrances, ignoredEntrances);
        if(matchingTiles.size() == 0) {
            System.out.println("Couldn't find matching tile for ("+x+"|"+z+") " + entrances + ";" + ignoredEntrances);
            return null;
        }
        return new TileInfo(pickRandomTile(matchingTiles), new TilePosition(x,z), shortestDistance);
    }

    private Tile pickRandomTile(List<Tile> tiles) {
        int sum = 0;
        for(Tile t : tiles) sum += t.getRealProbability();
        int randomValue = random.nextInt(sum);
        sum = 0;
        for(Tile t : tiles) {
            if(sum + t.getRealProbability() > randomValue && sum <= randomValue)
                return t;
            sum += t.getRealProbability();
        }
        return tiles.get(tiles.size()-1);
    }

    private class TileInfo {
        public Tile Tile;
        public int Distance;
        public TilePosition Position;

        public TileInfo(Tile tile, TilePosition position, int distance) {
            Tile = tile; Position = position; Distance = distance;
        }
    }

    private class TilePosition {
        public int X, Z;

        public TilePosition(int x, int z) { X = x; Z = z; }
    }
}
