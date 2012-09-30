package org.BrokenWorlds.Water;

import java.util.HashSet;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;

public class WaterWalking {

    private Block center = null;
    private HashSet<Block> blocks = new HashSet<Block>();

    public void move(Block center) {
        this.center = center;

        Iterator<Block> iter = blocks.iterator();
        while (iter.hasNext()) {
            Block b = iter.next();
            if (!b.equals(center)) {
                b.setType(Material.AIR);
                iter.remove();
            }
        }

        setToLily(center);
        setToLily(center.getRelative(BlockFace.NORTH));
        setToLily(center.getRelative(BlockFace.SOUTH));
        setToLily(center.getRelative(BlockFace.EAST));
        setToLily(center.getRelative(BlockFace.WEST));
        setToLily(center.getRelative(BlockFace.NORTH_WEST));
        setToLily(center.getRelative(BlockFace.NORTH_EAST));
        setToLily(center.getRelative(BlockFace.SOUTH_WEST));
        setToLily(center.getRelative(BlockFace.SOUTH_EAST));
    }

    private void setToLily(Block block) {
        if (block.getType() == Material.AIR) {
            BlockState state = block.getRelative(BlockFace.DOWN).getState();
            if ((state.getType() == Material.WATER || state.getType() == Material.STATIONARY_WATER) && state.getRawData() == (byte) 0) {
                block.setType(Material.WATER_LILY);
                blocks.add(block);
            }
        }
    }

    public boolean isMoved(Block center) {
        if (this.center == null || !this.center.equals(center)) {
            return true;
        }
        return false;
    }

    public boolean contains(Block block) {
        return blocks.contains(block);
    }

    public void remove() {
        for (Block block : blocks) {
            block.setType(Material.AIR);
        }
        blocks.clear();
    }
	
}
