package de.rabitem.main.player.instances;

import java.util.ArrayList;
import de.rabitem.main.card.instances.PlayerCard;

public class Nico extends de.rabitem.main.player.Player {

	private ArrayList<Integer> remainingCards = new ArrayList<Integer>(15);
	private int lastpoints;
	private PlayerCard lastMoveEnemy;
	private boolean first;
	
	public Nico(String name) {
		super(name);
		for (int i=1;i<=15;i++) {
        	remainingCards.add(i);
        }
		if (id == 1)
			first = true;
		else
			first = false;
	}
	
	/**
	 * Zur�cksetzen nach jedem Spiel
	*/
	@Override
	public void customResets() {
		remainingCards.clear();
		for (int i=1;i<=15;i++) {
	        remainingCards.add(i);
	    }
		lastpoints = 0;
		lastMoveEnemy = null;
	}
		
	/**
	 * Karte ausw�hlen
	 * @return PlayerCard
	 */
	@Override
    public PlayerCard getNextCardFromPlayer(final int pointCardValue) {
    	int value = pointCardValue;
    	//pr�fen ob Unentschieden letzte Runde
    	if (lastMove != null && lastMoveEnemy != null) {
	    	if (first) {
	    		if (oponnents.get(0).getLastMove().getValue() == lastMove.getValue()) {//pr�fen ob es als 1. und als 2. Spieler funktioniert
	    			value += lastpoints;
	    		}
	    	} else {
	    		if (lastMoveEnemy.getValue() == lastMove.getValue()) {
	    			value += lastpoints;
	    		}
	    	}
    	}
    	
    	//Karte anhand von Punkten um die gespielt wird ausw�hlen
    	int card;
    	switch (Math.abs(value)) {
    	case 0:
    		card = 1;
    		break;
    	case 1:
    		card = 3;
    		break;
    	case 2:
    		card = 5;
    		break;
    	case 3:
    		card = 7;
    		break;
    	case 4:
    		card = 9;
    		break;
    	case 5:
    		card = 11;
    		break;
    	case 6:
    		card = 12;
    		break;
    	case 7:
    		card = 13;
    		break;
    	case 8:
    		card = 14;
    		break;
    	case 9:
    		card = 15;
    		break;
    	case 10:
    		card = 1;
    		break;
    	default:
    		if (Math.abs(value) > 10)
    			card = 15;
    		else
    			card = Math.abs(value) + 5;
    	}
    	
    	//�hnliche Karte ausw�hlen wenn Karte nicht mehr vorhanden
    	if (!checkRemainingCards(card)) {
			card = similarCard(card);
		}		
    	
    	//Aktualisierung aller variablen und R�ckgabe
    	lastpoints = value;
    	lastMoveEnemy = oponnents.get(0).getLastMove();
    	int index = remainingCards.indexOf(card);
    	remainingCards.remove(index);
    	
    	PlayerCard finalcard = new PlayerCard(card);
    	return finalcard;
    }
    
    /**
     * �berpr�fen ob Karte noch vorhanden ist
     * @return boolean
     */
  	public boolean checkRemainingCards(int card) {
  		if (remainingCards.contains(card))
  			return true;
  		else
  			return false;
  	}
  	
	/**
	 * Karte in der N�he der vorher ausgew�hlten Karte benutzen wenn diese schon benutzt wurde
	 * @return int card
	 */
	public int similarCard(int card) {
		int startcard = card;
		int r = 1;
		do {
			if (startcard > 7) { 	
				card = startcard - r;
				if (!checkRemainingCards(card)) {
					card = startcard + r;
				}
			} else {
				card = startcard + r;
				if (!checkRemainingCards(card)) {
					card = startcard - r;
				}
			}
			r++;
		} while (!checkRemainingCards(card)); 
		return card;
	}
	
//	public int similarCard(int card) {
//	int startcard = card;
//	int r = 1;
//	if (startcard < 9) {
//		card = startcard - r;
//		if (!checkRemainingCards(card)) {
//			card = startcard + r;
//			if (!checkRemainingCards(card))
//				card = similarCard(startcard, startcard, r+1);
//		}
//	} else {
//		card = startcard + r;
//		if (!checkRemainingCards(card)) {
//			card = startcard - r;
//			if (!checkRemainingCards(card))
//				card = similarCard(startcard, startcard, r+1);
//		}
//	}
//	return card;
//}
//public int similarCard(int card, int startcard, int r) {
//	if (startcard < 9) { 					
//		card = startcard - r;
//		if (!checkRemainingCards(card)) { 							
//			card = startcard + r;
//			if (!checkRemainingCards(card))
//				card = similarCard(startcard, startcard, r+1);
//		}															
//	} else {
//		card = startcard + r;
//		if (!checkRemainingCards(card)) {
//			card = startcard - r;
//			if (!checkRemainingCards(card))
//				card = similarCard(startcard, startcard, r+1);
//		}
//	}
//	return card;
//}
// addPoints(add); xD
}
