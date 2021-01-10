package de.rabitem.main.player.instances;

import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;

// @author: Tobias Meier

public class TobiasMeier extends Player {

	/**
	 * Constructor of TobiasMBot
	 */
	public TobiasMeier(final String name) {
		super(name);
	}

	@Override
	public PlayerCard getNextCardFromPlayer(final int pointCardValue) {
		// Aufruf der Methode: determineCategory
		String category = determineCategory(pointCardValue);

		// verschiedene Reaktionen auf die unterschiedlichen Kategorien; Implementieren der eigenen Strategie
		switch (category) {
		case "MustHave":
			return new PlayerCard(pointCardValue + 5);

		case "NicetoHave":
			return new PlayerCard(pointCardValue + 1);

		case "Optional":
			return new PlayerCard(pointCardValue);

		case "CardMinus1":
			return new PlayerCard(4);

		case "Avoid":
			return new PlayerCard(pointCardValue * (-1) + 7);
			// default deckt m�gliche, unwahrscheinliche Fehler ab -> PlayerCard(0) willk�rlich gew�hlt
		default:
			System.out.println("Fehler: category unknown" + category);
			return new PlayerCard(0);
		}
	}

	//diese Methode ordnet die Werte der Punktekarten in meine eigenen Kategorien ein
	private String determineCategory(int pointCardValue) {
		String category;

		if (pointCardValue >= 8 && pointCardValue <= 10) {
			category = "MustHave";

		} else if (pointCardValue >= 4 && pointCardValue <= 7) {
			category = "NicetoHave";

		} else if (pointCardValue >= 1 && pointCardValue <= 3) {
			category = "Optional";

		} else if (pointCardValue >= -5 && pointCardValue <= -2) {
			category = "Avoid";

		} else if (pointCardValue == -1) {
			category = "CardMinus1";

		} else
			category = "undefined";
		return category;

	}

}