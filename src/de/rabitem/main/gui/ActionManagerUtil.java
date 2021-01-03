package de.rabitem.main.gui;

import de.rabitem.main.Main;

public class ActionManagerUtil {

    public static int getRoundsPlayed() {
        return Main.getMain().actionManager.roundsPlayed;
    }

    public static int addRoundsPlayed(){
        return Main.getMain().actionManager.roundsPlayed++;
    }
}
