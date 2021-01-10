package de.rabitem.main.player.instances;

import java.util.ArrayList;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;

public class Martin extends Player {

	//Segmenting my own deck of cards into 4 piles ordered by importance
	private ArrayList<Integer> myStrongestCards = new ArrayList<Integer>();
	private ArrayList<Integer> myGoodCards = new ArrayList<Integer>();
	private ArrayList<Integer> myAverageCards = new ArrayList<Integer>();
	private ArrayList<Integer> myWeakestCards = new ArrayList<Integer>();
	
	
	/*
	 * Constructor: providing the constructor of the super class "Player" with my name
	 * and calling the method "customResets()" to set up my deck of cards.
	 */
	public Martin(String name) {
		super("Martin");
		customResets();
		}
	
	//Overriding the method "customResets()" from super class to set up my deck of cards
	public void customResets() {
		myStrongestCards.clear();
		myGoodCards.clear();
		myAverageCards.clear();
		myWeakestCards.clear();
		
		// Filling my ArrayLists:
		 
		// 15, 14 and 13 are my strongest cards.
		for (int i = 15; i > 12; i--) {
			myStrongestCards.add(i);
		}
		
		// 12, 11, 10 and 9 are my good cards.
		for (int i = 12; i > 8; i--) {
			myGoodCards.add(i);
		}
		
		// 8, 7, 6 and 5 are my average cards.
		for (int i = 8; i > 4; i--) {
			myAverageCards.add(i);
		}
		
		// 4, 3, 2 and 1 are my weakest cards.
		for (int i = 4; i > 0; i--) {
			myWeakestCards.add(i);
		}
	}

	// Overriding the method "getNextCardFromPlayer" which decides when to play which card
	
	@Override
	public PlayerCard getNextCardFromPlayer(int pointCardValue) {

		//Initializing the variable "myCard" I want to return eventually each round.
		int myCard = -100;
		
		/*
		 * If there is a 8, 9 or 10 on the table, pick my pile of strongest cards
		 * and take the highest card available from that pile. Afterwards, remove
		 * that card from my pile, so we do not play the same card more than once.
		 */
		
		if (pointCardValue > 7) {
			myCard = myStrongestCards.get(0);
			myStrongestCards.remove(myStrongestCards.indexOf(myCard));
		}
		
		/*
		 * If there is a 7, 6, 5 or a -5 on the table, pick my pile of good cards
		 * and take the highest card available from that pile. Afterwards, remove
		 * that card from my pile, so we do not play the same card more than once.
		 */
		
		else if ((8 > pointCardValue && pointCardValue > 4) || pointCardValue == -5) {
			myCard = myGoodCards.get(0);
			myGoodCards.remove(myGoodCards.indexOf(myCard));
		}
		
		/*
		 * If there is a -4, -3, -2 or a 4 on the table, pick my pile of average cards
		 * and take the highest card available from that pile. Afterwards, remove
		 * that card from my pile, so we do not play the same card more than once.
		 */
		
		else if ((-5 < pointCardValue && pointCardValue < -1) || pointCardValue == 4) {
			myCard = myAverageCards.get(0);
			myAverageCards.remove(myAverageCards.indexOf(myCard));
		}
		
		/*
		 * If there is a 3, 2, 1 or -1 on the table, pick my pile of weakest cards
		 * and take the highest card available from that pile. Afterwards, remove
		 * that card from my pile, so we do not play the same card more than once.
		 */
		
		else if ((4 > pointCardValue && pointCardValue > 0) || pointCardValue == -1) {
			myCard = myWeakestCards.get(0);
			myWeakestCards.remove(myWeakestCards.indexOf(myCard));
		}
		
		// Returning myCard - this is the card I want to play this round!
		return new PlayerCard(myCard);
	}
	
}
