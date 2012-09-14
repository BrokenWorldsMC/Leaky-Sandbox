package org.BrokenWorlds.BookStats;

public interface PlayerStatSessionStore {

    /**
     * Stores Player's number of PvP Kills in integer
     */
    public void storePlayerPVPKills(String playerName, int kills);

    /**
     * Gets Player's number of PvP Kills in integer
     */
    public int getPlayerPVPKills(String playerName);
    
    /**
     * Stores Player's number of PvP Deaths in integer
     */
    public void storePlayerPVPDeaths(String playerName, int deaths);

    /**
     * Gets Player's number of PvP Deaths in integer
     */
    public int getPlayerPVPDeaths(String playerName);
}