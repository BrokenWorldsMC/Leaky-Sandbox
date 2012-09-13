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
}