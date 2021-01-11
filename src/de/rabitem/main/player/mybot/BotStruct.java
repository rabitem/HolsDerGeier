package de.rabitem.main.player.mybot;

import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class BotStruct {

    private HashMap<Integer, Float> averageCardRespone = new HashMap<>();


    private String name;
    private Player bot;
    private int id;
    private int cardsPlayed = 0;


    public BotStruct(Player p) {
        bot = p;
        name = p.getName();
        id = p.getId();
    }

    public String getName() {
        return bot.getName();
    }

    public ArrayList<PlayerCard> getDeck() {
        return bot.getCards();
    }

    public int getWinCounter() { return bot.getWinCounter(); }

    public PlayerCard getLastCard() {
        return bot.getLastMove();
    }

    public void recordMove(int lastPointCard, int lastPlayerCard) {
        int key = lastPointCard;
        float value = (float) lastPlayerCard;

        cardsPlayed++;

        if (averageCardRespone.get(key) == null) {
            averageCardRespone.put(key, value);

        } else {
            float newAverage = (averageCardRespone.get(key) + value) / 2.0f;
            averageCardRespone.put(key, newAverage);
        }
    }

    public float getAverageResponse(int cardValue) {
        if (averageCardRespone.size() < 15)
            return 0;

        else return averageCardRespone.get(cardValue);
    }

    public int getCardsRecorded() {
        return this.cardsPlayed;
    }

    public PlayerCard getClosest(int val) {
        int closestIndex = Integer.MAX_VALUE;

        for (int i = 1; i < 16; i++)
            if (bot.canUse(new PlayerCard(i)))
                if (Math.abs(val - i) < Math.abs(val - closestIndex))
                    closestIndex = i;

        return new PlayerCard(closestIndex);
    }

    public void resetAverageResponse(int cardValue) {
        averageCardRespone.remove(cardValue);
    }

}
