package de.rabitem.main.player.instances;

import de.rabitem.main.HolsDerGeierUtil;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;
import de.rabitem.main.player.rabitembot.MySql;
import de.rabitem.main.player.rabitembot.objects.Bot;
import de.rabitem.main.player.rabitembot.objects.Card;
import de.rabitem.main.player.rabitembot.objects.PlayedCards;
import de.rabitem.main.util.Util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

public class RabitemBot extends Player {

    public static MySql mySql = null;
    private Bot enemyBot = null;
    private PlayedCards playedCards = null;
    private boolean oddlyDistributed = false;

    // works only with PointCards from 1..15 and 2 Player, throw exception, only one rabitembot

    public RabitemBot(String name) {
        super(name);

        // connect to local mysql database
        System.out.println("Establishing MySQL connection...");
        if (Objects.isNull(mySql))
            RabitemBot.mySql = new MySql("127.0.0.1", "holsdergeier", "root", "password");
    }

    public void checkSetup() {

    }

    @Override
    public void setupBeforeGameStarts() {
        this.checkSetup();

        for (Player p : HolsDerGeierUtil.getActivePlayers()) {
            if (!p.equals(this))
                enemyBot = new Bot(HolsDerGeierUtil.getActivePlayers().get(0));
        }
        // register each enemy in database
        System.out.println("Setting up Stats before Game");
        if (!enemyBot.existsInDatabase()) {
            enemyBot.pushDatabase();
        }

        // strategy only after 20 games
        if (enemyBot.getTotalRoundsPlayedAgainst() > 20 * 15) {
            this.oddlyDistributed = this.areNumbersOddlyDistributed();
        }

        System.out.println("Successfully set up Stats before Game");
    }

    @Override
    public void afterRound(ArrayList<Player> winner) {
        // map round to database
        System.out.println("Map Stats after Round");

        PlayedCards playedCards = new PlayedCards(enemyBot);
        playedCards.getCard(enemyBot.getOwner().getLastMove()).incCardUsed();

        if (winner.size() == 1 && winner.get(0).equals(this)) {
            enemyBot.addWin();
        } else if (winner.size() == 1) {
            enemyBot.addLose();
        } else if (winner.size() > 1 && winner.contains(this)) {
            enemyBot.addDraw();
        } else {
            enemyBot.addLose();
        }
        System.out.println("Successfully mapped Stats after Round");
    }

    @Override
    public void afterGame(ArrayList<Player> winner) {
        // map Game
        System.out.println("Map Stats after Game");
        enemyBot.setLastPlayed(new Timestamp(System.currentTimeMillis()));
        System.out.println("Successfully mapped Stats after Game");

        RabitemBot.mySql.close();
    }

    @Override
    public PlayerCard getNextCardFromPlayer(int pointCardValue) {
        return strategy(pointCardValue);
    }

    private PlayerCard strategy(int pointCardValue) {
        return this.staticStreategy(pointCardValue);
    }

    private PlayerCard staticStreategy(int pointCardValue) {
        switch(pointCardValue) {
            case 10:
            case 9:
            case 8: return getCardsInArray(13, 15);
            case 7:
            case 6:
            case 5: return getCardsInArray(10, 12);
            case 4:
            case 3:
            case 2: return getCardsInArray(7, 9);
            case 1:
            case -1:
            case -2:
            case -3:
            case -4:
            case -5: return getCardsInArray(1, 6);
            default:
                return null;
        }
    }

    private PlayerCard getCardsInArray(int from, int to) {
        PlayerCard playerCard = new PlayerCard(Util.random(from, to));
        return this.enemyBot.getOwner().canUse(playerCard) ? playerCard : this.getCardsInArray(from, to);
    }

    private PlayerCard dynamicStrategy() {
        return null;
    }

    private boolean areNumbersOddlyDistributed() {
        for (Card card : playedCards.getCards()) {

        }
        return false;
    }
}
