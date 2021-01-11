package de.rabitem.main.player.instances;

import java.util.ArrayList;
import java.util.Arrays;

import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;
import de.rabitem.main.util.Util;

/**
 * This class implements the player KerstinLenz and the game strategy.
 * 
 * @author Kerstin
 *
 */
public class Kerstin extends Player {
	
	// Status of game
	boolean newGame = true;
	
	// Arraylists for three groups of cards
	ArrayList<PlayerCard> highCards = new ArrayList<PlayerCard>();
	ArrayList<PlayerCard> middleCards = new ArrayList<PlayerCard>();
	ArrayList<PlayerCard> lowCards = new ArrayList<PlayerCard>();
	
	// Array for already played mouse cards (mouse cards have value from +1 to +10)
	boolean[] mouseCards = new boolean[10];
	
	/**
	 * Constructor for class KerstinLenz
	 * 
	 * @param name
	 */
	public Kerstin(String name) {
		super(name);
	}

	/**
	 * The method returns the card that is played. Card is selected by the value of the animal cards.
	 * 
	 * @param pointCardValue
	 */
	@Override
	public PlayerCard getNextCardFromPlayer(int pointCardValue) {
		// If newGame initialize Arraylists
		if(newGame) {
			newGame = false;
			initCards();
		}
		
		// Initialize card
		PlayerCard card = null;
		
		// Select one of the Arraylists based on the pointCardValue
		switch (pointCardValue) {
			case 10:	
			case 9 :
			case 8 :
			case 7 :
				/*
				 *  If there are more high cards, than the opponents have, 
				 *  and less higher mouse cards than the current one played, 
				 *  select the highest highCard else select a random highCard
				 */
				if(higherThanOpponents() > numMouseCardsLeft(pointCardValue)) {
					card = highCards.get(highCards.size() - 1);
					highCards.remove(card);
				} else {
					card = getCard(highCards);
				}
			break;
			case 6 : 
			case 5 :
			case 4 :
			case -4 :
			case -5 : card = getCard(middleCards);
			break;
			case 3 :
			case 2 :
			case 1 :
			case -1 :
			case -2 :
			case -3 : card = getCard(lowCards);
			break;
		}
		if(pointCardValue > 0) mouseCards[pointCardValue - 1] = true;
		return card;
	}
	
	/**
	 * Initialize Arraylists for cards
	 */
	void initCards() {
		// Clear Arraylists
		highCards.clear();
		middleCards.clear();
		lowCards.clear();
		
		// Clear Array for mouse cards
		Arrays.fill(mouseCards, false);
		
		// Add low cards to Arraylist
		for(int i = 0; i < 6; i++) {
			lowCards.add(getCards().get(i));
		}
		
		// Add middle cards to Arraylist
		for(int i = 6; i < 11; i++) {
			middleCards.add(getCards().get(i));
		}
		
		// Add high cards to Arraylist
		for(int i = 11; i < getCards().size(); i++) {
			highCards.add(getCards().get(i));
		}
	}
	
	/**
	 * Returns card that is played and removes it from Arraylist
	 * 
	 * @param cards
	 * @return
	 */
	PlayerCard getCard(ArrayList<PlayerCard> cards) {
		// Get random card from Arraylist cards
		PlayerCard card = cards.get(Util.random(0, cards.size() - 1));
		
		// Check if opponent has the same card available
		if(hasOpponentCard(card)) {
			card = cards.get(Util.random(0, cards.size() - 1));
		}
		
		// Remove card from Arraylist cards
		cards.remove(card);
		return card;
	}
	
	/**
	 * The method returns true if an opponent has the card available
	 * 
	 * @param card
	 * @return
	 */
	boolean hasOpponentCard(PlayerCard card) {
	// Loop through all opponents
		for(Player player: getOponnents()) {
			// Return true if player has card
			if(player.canUse(card)) return true;
		}
		return false;
	}
	
	/**
	 * Returns the value of the highest card from the opponents
	 * 
	 * @return
	 */
	int highestOpponentValue() {
		int value = 0;
		
		// Loop through all opponents
		for(Player player: getOponnents()) {
			// Check if opponents have at least 1 card and get value from highest card 
			if((player.getCards().size() >= 1) 
					&& (player.getCards().get(player.getCards().size() - 1).getValue() > value)) {
				value = player.getCards().get(player.getCards().size() - 1).getValue();
			}
		}
		return value;
	}
	
	/**
	 * Returns the number of cards that are higher than the opponents
	 * 
	 * @return
	 */
	int higherThanOpponents() {
		int numCards = 0;
		int highestValue = highestOpponentValue();
		
		// Loop through all cards
		for(int i = getCards().size() - 1; i >= 0; i--) {
			/*
			 *  If the value of the card is higher than the value of the highest opponents card,
			 *  increment numCards else break and return 
			 */
			if(getCards().get(i).getValue() > highestValue) {
				numCards++;
			} else {
				break;
			}
		}
		return numCards;
	}
	
	/**
	 * Returns the number of mouse cards that have a higher value than the pointCardValue and weren't played
	 * 
	 * @return
	 */
	int numMouseCardsLeft(int pointCardValue) {
		int numCards = 0;
		
		// Loop through all mouse cards
		for(int i = pointCardValue; i < 10; i++) {
			// If mouse card was already played increment numCards else break and return
			numCards = (mouseCards[i] ? numCards : numCards + 1);
		}
		return numCards;
	}
	
	/**
	 * The method is called after a game ends
	 * resets the variable newGame
	 */
	public void customResets() {
		newGame = true;
	}	
}