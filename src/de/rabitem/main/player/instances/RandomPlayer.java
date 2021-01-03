package de.rabitem.main.player.instances;

import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;
import de.rabitem.main.util.Util;

/**
 * @author Felix Huisinga
 */
public class RandomPlayer extends Player {
    /**
     * Constructor of Player1
     * @param name String name
     */
    public RandomPlayer(final String name) {
        super(name);
    }

    @Override
    public PlayerCard getNextCardFromPlayer(final int pointCardValue) {
        return getCards().get(Util.random(0, getCards().size() - 1));
    }
}
