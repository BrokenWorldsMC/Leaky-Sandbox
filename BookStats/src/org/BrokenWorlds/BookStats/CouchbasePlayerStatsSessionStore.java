package org.BrokenWorlds.BookStats;

import net.spy.memcached.MemcachedClient;

public class CouchbasePlayerStatsSessionStore implements PlayerStatSessionStore {

    public MemcachedClient memcachedClient = null;
    
    public void setMemcachedClient(MemcachedClient memcachedClient) {
        this.memcachedClient = memcachedClient;
    }
    
    @Override
    public void storePlayerPVPKills(String playerName, int kills) {
        memcachedClient.set("PvPKills_" + playerName, 0, kills);
    }

    @Override
    public int getPlayerPVPKills(String playerName) {
        return (Integer) memcachedClient.get("PvPKills_" + playerName);
    } 
}