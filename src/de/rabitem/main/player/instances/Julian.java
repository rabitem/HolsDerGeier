package de.rabitem.main.player.instances;

import de.rabitem.main.HolsDerGeierUtil;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;
import de.rabitem.main.player.mybot.BotStruct;
import de.rabitem.main.player.rabitembot.objects.Bot;
import de.rabitem.main.util.Util;

import java.util.ArrayList;

public class Julian extends Player {
    private BotStruct enemyBot = null;
    private int lastPointCard = 0;
    private int currentPointCard = 0;
    private int totalRounds = 0;
    private float winRate = 0.f;
    private int lastEnemyCardVal = 0;
    boolean useDefaultOnly = false;


    public Julian(String name) {
        super(name);
    }

    public void setupBeforeGameStarts() {
        if (enemyBot == null) {
            for (Player p : HolsDerGeierUtil.getActivePlayers()) {
                if (!p.getName().equals(this.getName())) {
                    enemyBot = new BotStruct(p);
                }
            }
        }

        if (oponnents.size() > 1) {
            System.out.printf("\nMore than one enemy registered! " + this.getName() + " does not support more than one enemy");
        }
    }

    public void afterGame(ArrayList<Player> winner) {
        lastPointCard = 0;
        //reset
    }

    public void afterRound(ArrayList<Player> winner) {


        var test = this.getWinCounter();


        lastPointCard = currentPointCard;
        totalRounds++;

        //  winRate = ((float)wins / (float)(totalRounds);
    }

    @Override
    public PlayerCard getNextCardFromPlayer(final int pointCardValue) {

        if (lastPointCard != 0) {                                       //record the players move for later prediction
            lastEnemyCardVal = enemyBot.getLastCard().getValue();
            enemyBot.recordMove(lastPointCard, lastEnemyCardVal);
        }


        PlayerCard returnCard = selectCard(pointCardValue);
        currentPointCard = pointCardValue;

        return returnCard;
    }

    private PlayerCard selectCard(int cardValue) {

        int totalAbsolutePoints = 0;
        boolean lastRoundDraw = checkDraw();

        if (lastRoundDraw) {
            totalAbsolutePoints = Math.abs(lastPointCard) + Math.abs(cardValue);    //adjust totalPoints
        }

        if (totalAbsolutePoints > 15)
            return getMaxAvailableCard();

        int totalGamesPlayed = getTotalGamesPlayed();

        if (!useDefaultOnly && totalGamesPlayed > 50 && totalGamesPlayed < 60) {
            if (enemyBot.getWinCounter() - this.getWinCounter() > 0)
                useDefaultOnly = true;
        }

        if (useDefaultOnly)
            return defaultStrat(cardValue);


        if (totalRounds != 0 && getTotalGamesPlayed() > 3) {
            float averageResponse = enemyBot.getAverageResponse(cardValue);

            int predictedTrumpVal;

            if (averageResponse > 0) {
                int nearestToAverage = enemyBot.getClosest(Math.round(averageResponse)).getValue();

                int aggressionLevel = 1;

                if (lastRoundDraw && totalAbsolutePoints > 10)
                    aggressionLevel = 2;

                if (Math.abs(nearestToAverage - averageResponse) < 1.3f)
                    predictedTrumpVal = nearestToAverage + aggressionLevel;      //tries to predict the bot´s next pick based on his deck
                else
                    predictedTrumpVal = (Math.round(averageResponse) + aggressionLevel);

                if (predictedTrumpVal > 15) predictedTrumpVal = 15;     //clamp to bounds

                PlayerCard predictedTrump = new PlayerCard(predictedTrumpVal);

                if (canUse(predictedTrump))
                    return predictedTrump;

                else
                    return getClosest(predictedTrumpVal);
            }
        }

        return (defaultStrat(cardValue));
    }

    int i = 0;

    private PlayerCard defaultStrat(int value) {      //default tactic, should only used before bot has adapted

        return switch (value) {
            case 10, 9, 8 -> getRandom(13, 15);
            case 7, 6, 5 -> getRandom(10, 12);
            case 4, -4, -5 -> getRandom(7, 9);
            case -2, -3, 3, 2 -> getRandom(3, 6);
            case 1, -1 -> getRandom(1, 2);

            default -> getRandom(1, 15);  //this shouldn't happen....
        };
    }


    private PlayerCard getMaxAvailableCard() {
        ArrayList<PlayerCard> AvailableCards = this.getCards();
        PlayerCard biggestCard = AvailableCards.get(0);

        for (int index = 1; index != AvailableCards.size(); index++) {
            PlayerCard currentCard = AvailableCards.get(index);
            if (currentCard.getValue() > biggestCard.getValue()) biggestCard = currentCard;
        }
        return biggestCard;
    }

    private PlayerCard getClosest(int val) {
        int closestIndex = Integer.MAX_VALUE;


        for (int i = 1; i < 16; i++)
            if (canUse(new PlayerCard(i)))
                if (Math.abs(val - i) < Math.abs(val - closestIndex))
                    closestIndex = i;

        return new PlayerCard(closestIndex);
    }

    private PlayerCard getRandom(int min, int max) {

        if (!checkCardsInRange(min, max))
            return getClosest(Math.round((min + max) / 2));

        var card = new PlayerCard(Util.random(min, max));

        if (!canUse(card))
            card = getRandom(min, max);

        return card;
    }

    private boolean checkCardsInRange(int min, int max) {
        for (int i = min; i <= max; i++)
            if (canUse(new PlayerCard(i)))
                return true;

        return false;
    }


    private boolean checkDraw() {
        return totalRounds > 0 && lastPointCard != 0 ? this.getLastMove().getValue() == enemyBot.getLastCard().getValue() : false;
    }

    private int getTotalGamesPlayed() {
        return enemyBot.getWinCounter() + this.getWinCounter();
    }
}


