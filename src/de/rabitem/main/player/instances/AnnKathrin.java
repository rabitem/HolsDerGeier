package de.rabitem.main.player.instances;

//import java.util.ArrayList;

import de.rabitem.main.card.instances.PlayerCard;

import de.rabitem.main.player.Player;
import de.rabitem.main.util.Util;


/**
 * Meine Strategie war einfach, dass ich bei den Karten 5,6,7,8 eine Karte lege, die vom Wert 5 höher ist.
 * Bei den Karten 3,4 ist der Wert um 4 höher.
 * Bei 1,2 ist der Wert um 2 höher.
 * Die beiden höchsten Karten habe ich auf die 9,10 verteilt und die übrigen Karten auf die Minuskarten.
 * Von den übrigen Karten die höchsten beiden Karten, habe ich jeweils auf die meisten Minuspunkte verteilt und ziehe
 * aus diesem Bereich jeweils eine Random Karte.
 */

public class AnnKathrin extends Player {

    @Override
    public PlayerCard getNextCardFromPlayer(int pointCardValue) {

        int[] valuesLeft = {1, 2, 5};
        int[] valuesLeft2 = {6, 9};

        return switch (pointCardValue) {
            case 10 -> new PlayerCard(15);                              // 15
            case 9 -> new PlayerCard(14);                               // 14
            case 5, 6, 7, 8 -> new PlayerCard(pointCardValue + 5);      // 10..13
            case 3, 4 -> new PlayerCard(pointCardValue + 4);            // 7,8
            case 1, 2 -> new PlayerCard(pointCardValue + 2);            // 3,4
            case -1, -2, -3 -> this.getRandomInArray(valuesLeft);            // 1,2,5
            case -4, -5 -> this.getRandomInArray(valuesLeft2);               // 6,9
            default -> throw new IllegalStateException("Unerwarteter Wert: " + pointCardValue); // error, unerwarteter Wert
        };
    }

    public AnnKathrin(final String FirstPlayer) {
        super(FirstPlayer);
    }

    public PlayerCard getRandomInArray(int[] arr) {
        // fetch a random card in the given array
        int random = Util.random(0, arr.length - 1);
        PlayerCard playerCard = new PlayerCard(arr[random]);
        // check if the card was already used -> if not, return this card else try the same again with a new random card
        return this.canUse(playerCard) ? playerCard : this.getRandomInArray(arr);
    }

}