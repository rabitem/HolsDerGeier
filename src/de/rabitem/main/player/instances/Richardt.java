package de.rabitem.main.player.instances;

import java.util.ArrayList;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;
import de.rabitem.main.util.Util;

/**
 * @author Richardt Reckling
 */

public class Richardt extends Player {
	int lastCardPlaced = 0;

	public Richardt(final String name) {
		super(name);
	}

	public PlayerCard getNextCardFromPlayer(final int pointCardValue) {
		// If draw with Pointvalue above 10 use highest Card
		if (getLastMove() != null && getLastMove() == getOponnents().get(0).getLastMove() && getPoints() < 20
				&& lastCardPlaced + pointCardValue > 10) {
			while (canUse(playHighestCardNeeded())) {
				lastCardPlaced = pointCardValue;
				return playHighestCardNeeded();
			}
			lastCardPlaced = pointCardValue;
			return playHighestCard();
		}
		// A 50/50 between a random and a static strategy.
		else if (getPoints() < 20) {
			// Keep track of lastCard for draws
			lastCardPlaced = pointCardValue;
			// Static strategy
			switch (pointCardValue) {
			case 10:
				while (canUse(playHighestCardNeeded())) {
					return playHighestCardNeeded();
				}
				return playRandomCard(getCards().size() / 2, getCards().size() - 1);
			case 9:
				while (canUse(new PlayerCard(15))) {
					return getCard(15);
				}
			case 8:
				while (canUse(new PlayerCard(14))) {
					return getCard(14);
				}
			case 7:
				while (canUse(new PlayerCard(13))) {
					return getCard(13);
				}
			case 6:
				while (canUse(new PlayerCard(12))) {
					return getCard(12);
				}
			case 5:
				while (canUse(new PlayerCard(11))) {
					return getCard(11);
				}
			case -5:
				while (canUse(new PlayerCard(10))) {
					return getCard(10);
				}
			case -4:
				while (canUse(new PlayerCard(9))) {
					return getCard(9);
				}
			case 4:
				while (canUse(new PlayerCard(8))) {
					return getCard(8);
				}
			case -3:
				while (canUse(new PlayerCard(7))) {
					return getCard(7);
				}
			case 3:
				while (canUse(new PlayerCard(6))) {
					return getCard(6);
				}
			case -2:
				while (canUse(new PlayerCard(5))) {
					return getCard(5);
				}
			case 2:
				while (canUse(new PlayerCard(4))) {
					return getCard(4);
				}
			case -1:
				while (canUse(new PlayerCard(3))) {
					return getCard(3);
				}
			case 1:
				while (canUse(new PlayerCard(2))) {
					return getCard(2);
				}
			default:
				return playRandomCard();
			}
		}
		// Failsafe
		else {
			return playRandomCard();
		}

	}

	// Returns the card with the highest value
	private PlayerCard playHighestCard() {
		ArrayList<Integer> temp = new ArrayList<>();
		for (PlayerCard p : getCards()) {
			temp.add(p.getValue());
		}
		int value = Util.getHighestValue(temp);
		return new PlayerCard(value);
	}

	// Returns the card with the highest value from specified array
	private PlayerCard getHighestCard(ArrayList<PlayerCard> cards) {
		ArrayList<Integer> temp = new ArrayList<>();
		for (PlayerCard p : cards) {
			temp.add(p.getValue());
		}
		int value = Util.getHighestValue(temp);
		return new PlayerCard(value);
	}

	// Returns the card with the lowest value that always wins, else returns null
	private PlayerCard playHighestCardNeeded() {
		if (getOponnents().get(0).getCards().size() != 0) {
			PlayerCard opponentCard = getHighestCard(getOponnents().get(0).getCards());
			if (opponentCard.getValue() < getHighestCard(getCards()).getValue()) {
				int cardValue = 15;
				for (PlayerCard p : getCards()) {
					if (opponentCard.getValue() < p.getValue()) {
						cardValue = Math.min(cardValue, p.getValue());
					}
				}
				return new PlayerCard(cardValue);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	// Returns a random card
	private PlayerCard playRandomCard() {
		return getCards().get(Util.random(0, getCards().size() - 1));
	}

	// Returns a random card in specified range
	private PlayerCard playRandomCard(int x, int y) {
		return getCards().get(Util.random(x, y));
	}
}
