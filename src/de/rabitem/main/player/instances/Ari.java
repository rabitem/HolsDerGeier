package de.rabitem.main.player.instances;

import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;

import java.util.ArrayList;

/**
 * @author Ari
 * @version 1.0
 */
public class Ari extends Player {

    private int currentRoundCount;

    private int currentWinCount;

    private int winCountAfterPenultimateRound;

    private enum Strategy {
        MAIN,
        MAPPER,
        HIGH_THEN_HIGH,
        MIDDLE_CARD,
    }

    private Strategy currentStrategy;

    public Ari(String name) {
        super(name);
        this.currentRoundCount = 1;
        this.currentStrategy = Strategy.MAIN;
    }

    @Override
    public void customResets() {
        /*
            Every 10 rounds we'll draw the balance
            and decide whether to adjust our current strategy.
            We evaluate how often we won in the last 10 rounds
            by using our tracking variable currentWinCount.
         */

        if (this.winCountAfterPenultimateRound < this.getWinCounter()) {
            this.currentWinCount++;
            this.winCountAfterPenultimateRound = this.getWinCounter();
        }

        if (currentRoundCount == 10) {
            // Calculating the win ratio for the last 10 games
            float winRatio = (float) (float) this.currentWinCount / 10f;

            if (winRatio <= 0.5) {
                /*
                    If it turns out that our current strategy has no significant effect
                    (50% or more games lost)
                    we'll switch to the next one.
                */
                Strategy[] strategies = Strategy.values();
                currentStrategy = strategies[(currentStrategy.ordinal() + 1) % strategies.length];
            }

            currentRoundCount = 0;
            currentWinCount = 0;
        }

        currentRoundCount++;
    }

    @Override
    public PlayerCard getNextCardFromPlayer(int pointCardValue) {
        PlayerCard nextCard;

        switch (currentStrategy) {
            case MAPPER:
                nextCard = strategyMapper(pointCardValue);
                break;
            case HIGH_THEN_HIGH:
                nextCard = strategyHighThenHigh(pointCardValue);
                break;
            case MIDDLE_CARD:
                nextCard = strategyMiddleCard(pointCardValue);
                break;
            default:
                nextCard = strategyMain(pointCardValue);
        }

        return nextCard;
    }

    /*
        Strategies
     */

    private PlayerCard strategyMain(int pointCardValue) {
        /*
            Let's divide the point-cards up into two groups: the "worth-fighting-for"-cards and the rest.
            If the point card has a value that is greater than 7 (=worth fighting for) we're gonna
            apply our strategy. Otherwise we'll play our lowest card.
         */

        ArrayList<PlayerCard> ownCards = this.getCards();

        PlayerCard nextCard = ownCards.get(0);

        if (pointCardValue > 7) {
            /*
                1. Step: Retrieve the highest card of each opponent.
            */
            ArrayList<PlayerCard> highestCardsOfOpponents = new ArrayList<PlayerCard>();

            ArrayList<Player> opponents = this.getOponnents();

            for (Player opponent : opponents) {
                ArrayList<PlayerCard> opponentCards = opponent.getCards();

                // Since the cards are sorted in ascending order, the highest card is always at the last index.
                int lastIndex = opponentCards.size() - 1;
                if (lastIndex >= 0) {
                    PlayerCard highestCard = opponentCards.get(lastIndex);
                    highestCardsOfOpponents.add(highestCard);
                }

            }

            /*
                2. Step: Get the highest card of all opponents (=maximum of maxima).
            */
            PlayerCard highestCardOfAllOpponents = getHighestCard(highestCardsOfOpponents);

            /*
                3. Step: You don't want to waste your precious high-value cards.
                So we're going to get the lowest card that satisfies the condition
                of being higher than the highest card of all of your opponents.
                By doing that we're preventing the potentially unnecessary usage of high cards.
                If this card doesn't exist we'll check if there's a card that's
                at least equal to the highest card. If - sadly - such a card
                also doesn't exist, we're going to play the lowest card.
             */
            PlayerCard nextHigherOwnCard = getNextHigherCard(highestCardOfAllOpponents, ownCards);

            if (nextHigherOwnCard == null) {
                if (this.canUse(highestCardOfAllOpponents)) {
                    nextCard = highestCardOfAllOpponents;
                }
            }
        }

        return nextCard;
    }

    private PlayerCard strategyMapper(int pointCardValue) {
        /*
            Each player card is assigned to a certain point card.
         */
        PlayerCard nextCard;

        ArrayList<PlayerCard> ownCards = this.getCards();

        int nextCardValue = (pointCardValue >= 1) ? pointCardValue + 5 : pointCardValue * -1;

        nextCard = new PlayerCard(nextCardValue);

        return nextCard;
    }

    private PlayerCard strategyHighThenHigh(int pointCardValue) {
        /*
            If the point card is high, we'll play our highest card.
         */
        ArrayList<PlayerCard> ownCards = this.getCards();

        int amountOfCards = ownCards.size();

        PlayerCard nextCard = pointCardValue > 7 ? ownCards.get(amountOfCards - 1) : ownCards.get(0);

        return nextCard;
    }

    private PlayerCard strategyMiddleCard(int pointCardValue) {
        /*
            We'll always play the card that's in the middle of our deck.
         */
        ArrayList<PlayerCard> ownCards = this.getCards();

        int amountOfCards = ownCards.size();

        PlayerCard nextCard = ownCards.get(amountOfCards / 2);

        return nextCard;
    }

    /*
        Utility functions
     */

    /**
     * @return Returns the highest card of a given ArrayList or null if the ArrayList is empty.
     * @params Accepts an ArrayList of player cards.
     */
    public PlayerCard getHighestCard(ArrayList<PlayerCard> playerCards) {
        if (playerCards.size() == 0) return null;

        PlayerCard highestCard = playerCards.get(0);

        for (int i = 1; i < playerCards.size(); i++) {
            PlayerCard currentCard = playerCards.get(i);

            if (currentCard.getValue() > highestCard.getValue()) {
                highestCard = currentCard;
            }
        }
        return highestCard;
    }

    /**
     * @return Returns null if such a card doesn't exist, the provided card is null or if the ArrayList is empty.
     * @params Accepts a player card and an ArrayList of playerCards sorted in ascending order.
     * @params (Caution : The function doesn ' t check if the provided ArrayList is sorted accordingly !)
     */
    public PlayerCard getNextHigherCard(PlayerCard card, ArrayList<PlayerCard> playerCards) {
        if (card == null || playerCards.size() == 0) return null;

        int cardValue = card.getValue();

        for (PlayerCard playerCard : playerCards) {
            int currentValue = playerCard.getValue();

            if (currentValue > cardValue) {
                return playerCard;
            }
        }
        return null;
    }

}
