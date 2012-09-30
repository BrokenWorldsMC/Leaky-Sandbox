package org.BrokenWorlds.Water;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.google.common.collect.Lists;

public class FlashFlood {
	
    private static final Logger logger = Logger.getLogger("Minecraft");
    
    List<Block> blocks = Lists.newArrayList();
    
	 public List<Block> makeSphere(int i, int j, int k, int r, Player player){ //i, j, and k are the coordinates of the center of the sphere, r is the radius
         Vector center = new Vector(i, j, k); //this just creates a set of where the points are
         Vector point = null; //declares the point variable we will use later
         Location loc;
         for(int x = -r; x < r; x++){
                 for(int y = -r; y < r; y++){
                         for(int z = -r; z < r; z++){
                                 point = new Vector(i+x, j+y, k+z); //defines the variable we declared earlier
                                 if(center.distance(point) > r) { //if the point is further away from the center than the radius, ignore it
                                         continue;
                                 }
                                 int a = (int) Math.floor(point.getX());
                                 int b = (int) Math.floor(point.getY());
                                 int c = (int) Math.floor(point.getZ());
                                 loc = new Location(player.getWorld(), a, b, c);
                                 Block temp = loc.getBlock();
                                 if(temp.getType() == Material.AIR){
                                	 loc.getBlock().setType(Material.STATIONARY_WATER);
                                	 loc.getBlock().getState().setRawData((byte) 0);
                                	 loc.getBlock().setMetadata("flashFlood", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("SkillTesting"), "flashFlooded"));
                                	 blocks.add(temp);
                                 }
                         }
                 }
         }
         
     
         return blocks;
 }
}
