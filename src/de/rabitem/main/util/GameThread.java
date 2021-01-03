package de.rabitem.main.util;

import de.rabitem.main.HolsDerGeierUtil;
import de.rabitem.main.Main;
import de.rabitem.main.exception.IllegalMatchSetup;
import de.rabitem.main.exception.IllegalPlayerSize;
import de.rabitem.main.gui.ActionManagerUtil;
import de.rabitem.main.player.Player;

import java.util.HashMap;

public class GameThread implements Runnable {
    private boolean firstGameRun = false;
    @Override
    public void run() {
        try {
            for (int i = 0; i < Integer.parseInt(Main.getOptionsPanel().getTfRounds().getValue().toString()); i++) {
                Main.getMain().startGame();
                Main.getMain().reset();
                ActionManagerUtil.addRoundsPlayed();
                if (i < 1)
                    firstGameRun = true;
            }
            this.endMapping();
            Main.getMain().getActionManager().setCancelTask(true);
        } catch (IllegalMatchSetup | IllegalPlayerSize e) {
            System.out.println(e.getMessage());
        }
    }

    private void endMapping() {
        // map total player points
        HashMap<Player, Integer> mapPlayers = new HashMap<>();
        for (Player p : Main.getPlayerManager().getPlayerList()) {
            mapPlayers.put(p, p.getTotalPoints());
        }

        Main.getHolsDerGeierUtil().mapPlayerPlaces(mapPlayers, true);
        Main.getMain().setStatsManager(new StatsManager(HolsDerGeierUtil.getActivePlayers()));
    }

    public boolean isFirstGameRun() {
        return firstGameRun;
    }
}
