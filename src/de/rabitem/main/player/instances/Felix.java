package de.rabitem.main.player.instances;

import de.rabitem.main.HolsDerGeierUtil;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;
import de.rabitem.main.player.rabitembot.MySql;
import de.rabitem.main.player.rabitembot.objects.Bot;
import de.rabitem.main.player.rabitembot.objects.Card;
import de.rabitem.main.player.rabitembot.objects.PlayedCards;
import de.rabitem.main.player.rabitembot.objects.RabitemUtil;
import de.rabitem.main.util.Util;

import java.io.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Objects;

public class Felix extends Player {

    // mysql con
    public static MySql mySql = null;
    // mysql enemy bot
    private Bot enemyBot = null;
    // mysql playedcards
    private PlayedCards playedCards = null;
    // customtactic boolean
    private boolean enabledCustomTactic = false;

    int pointCardValue;

    // mostlikelyenemycards
    private final ArrayList<PlayerCard> mostLikelyCards = new ArrayList<>();

    // works only with PointCards from 1..15 and 2 Player

    /**
     * Constructor of the Rabitem-Bot. Used to establish MySql Con
     *
     * @param name String name
     */
    public Felix(String name) {
        super(name);

        // connect to local mysql database
        RabitemUtil.outputMessage("Establishing MySQL connection...");

        if (Objects.isNull(mySql))
            Felix.mySql = new MySql("127.0.0.1", "holsdergeier", "root", "password");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("/resources/AsciiArtRabitemBot")))) {
            String line = "";
            while (true) {
                try {
                    if ((line = br.readLine()) == null) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called before the Game starts and is used for setting up the Bot and bot related stuff!
     */
    @Override
    public void setupBeforeGameStarts() {
        // asign the enemyBot to use mysql related functions and mrore
        for (Player p : HolsDerGeierUtil.getActivePlayers()) {
            if (!p.getName().equals(this.getName())) {
                enemyBot = new Bot(p);
            }
        }
        // register each enemy in database
        RabitemUtil.outputMessage("Setting up Stats before Game");

        // push the bot to database if not existing
        if (!enemyBot.existsInDatabase())
            enemyBot.pushDatabase();

        playedCards = new PlayedCards(enemyBot);
        if (!playedCards.existsInDatabase())
            playedCards.pushDatabase();

        // output - debug related setup
        RabitemUtil.outputMessage("Successfully set up Stats before Game");

        // Stats output (optional)
        /*
        System.out.println("-----------------Stats-----------------");
        System.out.println(RabitemUtil.PREFIX + "Round played against " + this.enemyBot.getOwner().getName() + ": " + this.enemyBot.getTotalRoundsPlayedAgainst());
        System.out.println(RabitemUtil.PREFIX + "Wins: " + this.enemyBot.getWins());
        System.out.println(RabitemUtil.PREFIX + "Loses: " + this.enemyBot.getLoses());
        System.out.println(RabitemUtil.PREFIX + "Draws: " + this.enemyBot.getDraws());
        System.out.println("---------------------------------------");
        */

        // strategy only after 20 games
        if (!this.enabledCustomTactic && enemyBot.getTotalRoundsPlayedAgainst() > 20 * 15) {
            this.enabledCustomTactic = true;
        }
    }

    /**
     * This method is called after each Round and is used for mapping purposes
     *
     * @param winner ArrayList<Player> winner
     */
    @Override
    public void afterRound(ArrayList<Player> winner) {
        // map round to database
        RabitemUtil.outputMessage("Map Stats after Round");

        // asign playedcards
        playedCards.getCard(enemyBot.getOwner().getLastMove()).incCardUsed(pointCardValue);

        // push to database if win,lose or draw
        if (winner.size() == 1 && winner.get(0).equals(this)) {
            enemyBot.addWin();
        } else if (winner.size() == 1) {
            enemyBot.addLose();
        } else if (winner.size() > 1 && winner.contains(this)) {
            enemyBot.addDraw();
        } else {
            enemyBot.addLose();
        }

        this.mostLikelyCards.clear();
        // output - debug related setup
        RabitemUtil.outputMessage("Successfully mapped Stats after Round");
    }

    /**
     * This method is called after the Game and is used for mapping purposes
     *
     * @param winner ArrayList<Player> winner
     */
    @Override
    public void afterGame(ArrayList<Player> winner) {
        // output - debug related setup
        RabitemUtil.outputMessage("Map Stats after Game");

        // map Game
        enemyBot.setLastPlayed(new Timestamp(System.currentTimeMillis()));

        // output - debug related setup
        RabitemUtil.outputMessage("Successfully mapped Stats after Game");
    }

    /**
     * Outputs the PlayerCard based on different parameters
     *
     * @param pointCardValue int pointCardValue
     * @return PlayerCard
     */
    @Override
    public PlayerCard getNextCardFromPlayer(int pointCardValue) {
        this.pointCardValue = pointCardValue;

        // if custom strategy...
        if (this.enabledCustomTactic && !Objects.isNull(this.getHighestProbability(pointCardValue))) {
            // output - debug related setup
            RabitemUtil.outputMessage("Enabled Custom Tactics!");

            // highest value with a chance of 33% or more
            Card card = this.getHighestProbability(pointCardValue);
            if (Objects.isNull(card)) {
                this.enabledCustomTactic = false;
                return staticStrategy(pointCardValue);
            }
            int highestValue = card.getValue();
            double probability = card.getProbability(pointCardValue);

            if (pointCardValue > 7) {
                if (highestValue < 13 && probability > .333)
                    return getCardsInArray(13, 15);

                if (probability > .333) {
                    switch (pointCardValue) {
                        case 10:
                            // most bots just use 15 if pointcardvlue is 10. Pretend this behaviour and win 8 && 9 instead
                            if (highestValue == 15) return new PlayerCard(13);
                        case 9:
                            if (highestValue < 15) return new PlayerCard(15);
                        case 8:
                            if (highestValue < 14) return new PlayerCard(14);
                    }
                }
                for (int i = 1; i < 3; i++) {
                    if (this.canUse(new PlayerCard(highestValue + i))) {
                        return new PlayerCard(highestValue + i);
                    } else if (highestValue == pointCardValue + 5) {
                        if (this.canUse(new PlayerCard(highestValue)))
                            return new PlayerCard(highestValue);
                    }
                }
            } else if (pointCardValue > 4) { // 5,6,7
                if (probability > .8) {
                    if (highestValue < 10)
                        return getCardsInArray(10, 12);

                    switch (pointCardValue) {
                        case 7:
                            if (highestValue == 12) return new PlayerCard(10);
                        case 6:
                            if (highestValue < 12) return new PlayerCard(12);
                        case 5:
                            if (highestValue < 11) return new PlayerCard(11);
                    }
                }
            } else if (pointCardValue > 1) { // 2, 3, 4
                if (probability > .8) {
                    if (highestValue < 7)
                        return getCardsInArray(7, 9);

                    switch (pointCardValue) {
                        case 7:
                            if (highestValue == 9) return new PlayerCard(7);
                        case 6:
                            if (highestValue < 9) return new PlayerCard(9);
                        case 5:
                            if (highestValue < 8) return new PlayerCard(8);
                    }
                }
            } else if (pointCardValue > -3) { // 1, -1, -2
                if (probability > .333) {
                    if (highestValue > 4)
                        return getCardsInArray(1, 3);

                    switch (pointCardValue) {
                        case 7:
                            if (highestValue == 3) return new PlayerCard(1);
                        case 6:
                            if (highestValue < 3) return new PlayerCard(3);
                        case 5:
                            if (highestValue < 2) return new PlayerCard(2);
                    }
                }
            }else { // -3, -4, -5
                if (probability > .333) {
                    if (highestValue < 4)
                        return getCardsInArray(4, 6);

                    switch (pointCardValue) {
                        case -5:
                            if (highestValue == 6)
                                return new PlayerCard(4);
                            else
                                return new PlayerCard(6);
                        case -4:
                            if (highestValue < 6 && this.canUse(new PlayerCard(6)))
                                return new PlayerCard(6);
                            else
                                return new PlayerCard(5);
                        case 03:
                            if (highestValue < 5 && this.canUse(new PlayerCard(5)))
                                return new PlayerCard(5);
                            else
                                return new PlayerCard(4);
                    }
                }
            }
        }
            // static strategy if no strategy found or not enough games played
            return staticStrategy(pointCardValue);
        }

        /**
         * Checks if the card in a specific array can be used
         *
         * @param highestValue int highestValue
         * @param to           int to
         * @return boolean canUseCardInArray
         */
        public boolean validateMore ( int highestValue, int to){
            boolean canUseCardInArray = false;

            for (int i = highestValue + 1; i < to; i++) {
                canUseCardInArray = this.canUse(new PlayerCard(i));
                if (canUseCardInArray)
                    break;
            }
            return canUseCardInArray;
        }

        /**
         * static strategy, simple switch - case with some randomness
         *
         * @param pointCardValue int pointCardValue
         * @return PlayerCard
         */
        private PlayerCard staticStrategy ( int pointCardValue){
            return switch (pointCardValue) {
                case 10, 9, 8 -> getCardsInArray(13, 15);
                case 7, 6, 5 -> getCardsInArray(10, 12);
                case 4, 3, 2 -> getCardsInArray(7, 9);
                case 1, -1, -2 -> getCardsInArray(1, 3);
                case -3, -4, -5 -> getCardsInArray(4, 6);
                default -> throw new IllegalStateException("Unexpected value: " + pointCardValue);
            };
        }

        /**
         * Returns a PlayerCard with value in an specific range
         *
         * @param from int from
         * @param to   int to
         * @return Card in Array
         */
        private PlayerCard getCardsInArray ( int from, int to){
            PlayerCard playerCard = new PlayerCard(Util.random(from, to));
            return this.canUse(playerCard) ? playerCard : this.getCardsInArray(from, to);
        }

        /**
         * Checks for the Card with the highest Probability
         *
         * @param pointCardValue int
         * @return Card
         */
        private Card getHighestProbability ( int pointCardValue){
            Card card1 = null;
            double highestProperty = 0;
            for (Card card : this.playedCards.getCards()) {
                if (card.getProbability(pointCardValue) >= highestProperty) {
                    highestProperty = card.getProbability(pointCardValue);
                    card1 = card;
                }
            }
            return card1;
        }
    }
