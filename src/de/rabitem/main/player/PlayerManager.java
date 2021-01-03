package de.rabitem.main.player;

import java.util.ArrayList;

/**
 * @author Felix Huisinga
 */
public class PlayerManager {

    /**
     * Constructor of PlayerManager
     */
    public PlayerManager(){

    }

    /**
     * Player list
     */
    private ArrayList<Player> playerList = new ArrayList<>();

    /**
     * Registers a player
     * @param p Player p
     */
    public void registerPlayer(final Player p) {
        playerList.add(p);
    }

    /**
     * Get Player count
     * @return int PlayerCount
     */
    public int getPlayerCount() {
        return playerList.size();
    }

    /**
     * This method is used to get the PlayerList
     * @return ArrayList<Player> getPlayerList
     */
    public ArrayList<Player> getPlayerList() {
        return playerList;
    }
}
