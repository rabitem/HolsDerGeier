package de.rabitem.main.player.instances;

import de.rabitem.main.*;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;
import de.rabitem.main.util.Util;

/**
 * @author Yannick Schulz
 */

public class Yannick extends Player{
	
	private int shiftBy = -1;
	private int wins, opponentwins, rounds;
	private Player opponent;
	
	public Yannick(String name) {
		super(name);
	}

	@Override
	public PlayerCard getNextCardFromPlayer(int pointCardValue) {
		opponent = oponnents.get(0);
		if (getCards().size() == 15) {
			//in den ersten 10 Runden und wenn nach mehr als 100 Runden weniger als die Hälfte gewonnen wurde 
			if ((rounds < 10) || ((rounds > 100) && (wins < (rounds/2)))) {
				//falls letzte Runde verloren wurde
				if ((wins == getWinCounter())) {
				shiftBy++;
				opponentwins++;
				}
				//falls letze Runde unentschieden
				else if ((wins < getWinCounter() ) && (opponentwins < opponent.getWinCounter())) {
					wins++;
					opponentwins++;
					shiftBy++;
				}	
				else
					wins++;
			}
			else
				shiftBy=0;
			rounds++;
		}
		//Gegeneranzahl ermitteln und entsprechende Methode wählen
		if (getCount() == 1) {
			return strategy1(pointCardValue);
		}
		else
			return strategy2(pointCardValue);
	}

	
	private PlayerCard strategy1 (int pointCardValue) {
		//falls mehr als 14 verschoben wird zurücksetzen, da 15 verschieben == 0 verschieben
		if (shiftBy >=15)
			shiftBy = shiftBy - 15;
		return shift(pointCardValue);
	}
	
	
	private PlayerCard shift(int pointCardValue) {
		int add = 0;
		//für positive Karten
		if (pointCardValue > 0) {
			add = shiftBy +5;
			if ((add + pointCardValue) > 15)
				add = add - 15;
		}
		//für negative Karten
		else if (pointCardValue < 0) {
			add = shiftBy + 6;
			if ((add + pointCardValue) > 15)
				add = add - 15;
		}
		PlayerCard playerCard = new PlayerCard(pointCardValue + add);
		return playerCard;
	}

	
	
	
	private PlayerCard strategy2 (int pointCardValue) {
		//zufällige Zuordnung bei mehreren Spielern um Chance auf unentschieden zu verringern
		switch (pointCardValue) {
		case 10:
		case 9:
        case 8:
            return randomCard(13, 15);
        case 7:
        case 6:
        case 5:
            return randomCard(10, 12);
        case 4:
        case 3:
        case 2:
            return randomCard(7, 9);
        case 1:
        case -1:
        case -2:
        	return randomCard(4, 6);
        case -3:
        case -4:
        case -5:
            return randomCard(1, 3);
        default:
		return null;	
	}
}
	
	private PlayerCard randomCard(int from, int to) {
        //zufällige Karte im übergebenen Bereich auswählen
		PlayerCard randomCard = new PlayerCard(Util.random(from, to));
        return this.canUse(randomCard) ? randomCard : this.randomCard(from, to);
    }
	
	private int getCount() {
		//Anzahl der gegnerischen Spieler bestimmen
		return getOponnents().size();
	}
}