package de.rabitem.main.util;

import de.rabitem.main.player.Player;

import java.util.ArrayList;

public class StatsManager {
    private ArrayList<Player> playerList = new ArrayList<>();

    /**
     * Constructor of StatsManager
     * @param aP ArrayList<Player> aP
     */
    public StatsManager(final ArrayList<Player> aP) {
        this.playerList = aP;
    }

    /**
     * Get total first place
     * null if not over yet
     * @return Player p
     */
    public Player getTotalFirstPlace() {
        ArrayList<Integer> totalPoints = new ArrayList<>();
        for (Player player : playerList) {
            totalPoints.add(player.getTotalPoints());
        }
        Player firstPlace = null;
        for (Player p:playerList) {
            if (p.getTotalPoints() == Util.getHighestValue(totalPoints)){
                return p;
            }
        }
        return null;
    }
}
