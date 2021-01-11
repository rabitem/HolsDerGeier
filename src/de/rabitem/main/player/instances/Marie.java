package de.rabitem.main.player.instances;

import java.util.ArrayList;

import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;
import de.rabitem.main.util.Util;

public class Marie extends Player {

    ArrayList <PlayerCard>  niedrig = new ArrayList <PlayerCard>();
    ArrayList <PlayerCard>  mittel = new ArrayList <PlayerCard>();
    ArrayList <PlayerCard>  hoch = new ArrayList <PlayerCard>();

    public Marie(String Marie) {
        super(Marie);
    }
    /** erst die 3 ArrayLists erstellt und
     * jetzt die verschiedenen Methoden um die ArrayList zu füllen jeweils*/

    void fuellenNiedrig () {
        for (int i = 0 ; i < 7 ; i++) {
            niedrig.add(getCards().get(i));
        }
    }

    void fuellenMittel () {
        for (int i = 7 ; i < 12 ; i++) {
            mittel.add(getCards().get(i));
        }
    }

    void fuellenHoch () {
        for (int i = 12 ; i < 15 ; i++) {
            hoch.add(getCards().get(i));
        }
    }

    /** Strategie:
     * wenn Geier-/Erdmännchenkarte 8,9,10, lege ich 13,14,15
     * wenn Geier-/Erdmännchenkarten bei -5 - -1, lege ich 8,9,10,11,12
     * wenn Geier-/Erdmännchenkarten 1-7, lege ich 1,2,3,4,5,6,7 */

    @Override
    public PlayerCard getNextCardFromPlayer(int pointCardValue) {
        // Arraylisten für Karten bei Beginn einer Runde füllen, noch keine Karte gespielt
        if (getCards().size() == 15) {
            fuellenNiedrig();
            fuellenMittel();
            fuellenHoch();
        }
        PlayerCard myCard = null;

        /** wenn pointCard 1-7 ist, dann lege ich eine Zufallszahl 1-7
         */
        if (pointCardValue > 0 && pointCardValue < 8   ) {
            myCard = (niedrig.size() > 1 ? niedrig.get(Util.random(0, niedrig.size() - 1)) : niedrig.get(0));
            niedrig.remove(myCard);
            /** remove bedeutet, dass die Zufallszahl
             die vom Computer gewählt wird,
             aus der ArrayList entfernt wird.*/
        }
        /** wenn pointCard -5 - -1 ist, dann lege ich eine Zufallszahl 8-12
         */
        if (pointCardValue <  0) {
            myCard = (mittel.size() > 1 ? mittel.get(Util.random(0, mittel.size() - 1)) : mittel.get(0));
            mittel.remove(myCard);
        }

        /**wenn pointCard 8,9 oder 10 ist,
         *   lege ich eine Zufallszahl zwischen 13 - 15
         */
        if (pointCardValue > 7) {
            myCard = (hoch.size() > 1 ? hoch.get(Util.random(0, hoch.size() - 1)) : hoch.get(0));
            hoch.remove(myCard);
        }

        return myCard ;
    }
}