package org.BrokenWorlds.DungeonGenerator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jnbt.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SchematicFile {
    private int width = 0;
    private int height = 0;
    private int length = 0;

    private byte[] blocks;
    private byte[] data;

    private List<Tag> tileEntities;

    public SchematicFile(File file) {
        try {
            FileInputStream fis = new FileInputStream(file);
            NBTInputStream nbt = new NBTInputStream(fis);
            Map<String, Tag> tagCollection = ((CompoundTag) nbt.readTag()).getValue();

            width = (Short) getChildTag(tagCollection, "Width", ShortTag.class).getValue();
            height = (Short) getChildTag(tagCollection, "Height", ShortTag.class).getValue();
            length = (Short) getChildTag(tagCollection, "Length", ShortTag.class).getValue();

            blocks = (byte[]) getChildTag(tagCollection, "Blocks", ByteArrayTag.class).getValue();
            data = (byte[]) getChildTag(tagCollection, "Data", ByteArrayTag.class).getValue();

            // dont care about entities for now...
            // List entities = (List) getChildTag(tagCollection, "Entities", ListTag.class).getValue();

            tileEntities = (List) getChildTag(tagCollection, "TileEntities", ListTag.class).getValue();

            nbt.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Tag getChildTag(Map<String, Tag> items, String key, Class<? extends Tag> expected) {
        Tag tag = items.get(key);
        return tag;
    }

    public void pasteTo(Location location, Helper.Rotation rotation) {
        pasteTo(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), rotation);
    }

    public void pasteTo(World world, int destinationX, int destinationY, int destinationZ, Helper.Rotation rotation) {
        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                for(int z = 0; z < length; z++) {
                    Block b = world.getBlockAt(destinationX + x, destinationY + y, destinationZ + z);

                    int index = getBlockIndex(x,y,z, rotation);
                    b.setTypeIdAndData(blocks[index], data[index], false);
                }
            }
        }

        //TODO: Paste TileEntities.
    }

    private int getBlockIndex(int x, int y, int z, Helper.Rotation rotation)
    {
        if(rotation == Helper.Rotation.Rotate0)
            return y * width * length + z * width + x;
        if(rotation == Helper.Rotation.Rotate90)
            return y * width * length + (length - 1 - x) * width + z;
        if(rotation == Helper.Rotation.Rotate180)
            return y * width * length + (length - 1 - z) * width + (width - 1 - x);
        if(rotation == Helper.Rotation.Rotate270)
            return y * width * length + x * width + (width - 1 - z);
        else return 0;
    }
}