package de.rabitem.main;

import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.card.instances.PointsCard;
import de.rabitem.main.exception.IllegalMatchSetup;
import de.rabitem.main.exception.IllegalPlayerSize;
import de.rabitem.main.gui.ActionManagerUtil;
import de.rabitem.main.listener.OnSetupFinished;
import de.rabitem.main.player.Player;
import de.rabitem.main.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Felix Huisinga
 */
public abstract class HolsDerGeier implements OnSetupFinished {
    private OnSetupFinished onSetupFinished;

    protected boolean finishedSetup;

    protected int from = 1;
    protected int to = 15;

    protected int pointCardsFrom = -5;
    protected int pointCardsTo = 10;

    protected Player winningPlayer = null;

    protected int rounds;

    private HolsDerGeierUtil holsDerGeierUtil = null;

    /**
     * Points, to which is played
     */
    private PointsCard currentPointCard = new PointsCard(0);
    private PointsCard lastRoundPointCard = new PointsCard(0);

    protected static ArrayList<Player> activePlayer = new ArrayList<>();

    /**
     * Arraylist which contains all cards (range: -5 --> 10)
     */
    protected static List<PointsCard> pointCards = new ArrayList<>();

    /**
     * Constructor for HolsDerGeier class
     */
    public HolsDerGeier() {
    }

    public void setOnSetupFinishedListener(final OnSetupFinished onSetupFinished) {
        this.onSetupFinished = onSetupFinished;
    }

    /**
     * Game running related methods:
     */

    /**
     * Resets the Game
     */
    public void reset() {
        /**
         * Reset active Player
         */
        for (Player p : HolsDerGeierUtil.getActivePlayers()) {
            p.reset();
            p.customResets();
        }
        /**
         * Clear PointCards List
         */
        pointCards.clear();
    }

    /**
     * Starts the game engine
     */
    public void startGame() throws IllegalPlayerSize, IllegalMatchSetup {
        /**
         * Setup player
         */
        if (ActionManagerUtil.getRoundsPlayed() == 0) {
            Main.activatePlayer();
            HolsDerGeierUtil.getActivePlayers().forEach(Player::setOpponents);
        }
        /**
         * Error handling: check if setup is correct
         */
        if (HolsDerGeierUtil.getActivePlayers().size() < 2) {
            throw new IllegalPlayerSize("Invalid number of players (" + HolsDerGeierUtil.getActivePlayers().size() + ")! The game could not be started.");
        } else if (Main.getPlayerManager().getPlayerCount() > 2) {
            System.out.println("Warning, " + Main.getPlayerManager().getPlayerCount() + " Players! Some Bots might not work...");
        }
        for (Player p : HolsDerGeierUtil.getActivePlayers()) {
            if (!p.isReady())
                throw new IllegalMatchSetup("The game was not set up correctly!");
        }

        // System.out.println("Match setup successful! Starting Game...");
        HolsDerGeierUtil.getActivePlayers().forEach(Player::setupBeforeGameStarts);
        runGame();
    }

    /**
     * Runs the game
     */
    private void runGame() {
        finishedSetup = true;
        // fill cards
        for (int i = Main.getMain().pointCardsFrom; i <= Main.getMain().pointCardsTo; i++) {
            if (i == 0)
                continue;
            pointCards.add(new PointsCard(i));
        }

        holsDerGeierUtil = Main.getHolsDerGeierUtil();

        // rounds to play
        rounds = pointCards.size();

        // current round
        int currentRound = 0;

        // values of each player
        ArrayList<Integer> values = new ArrayList<>();

        // winners of the round
        ArrayList<Player> winner = new ArrayList<>();

        // winning card value
        int winningValue;

        HashMap<Player, PlayerCard> usedCards = new HashMap<Player, PlayerCard>();

        // System.out.println("We are about to play: " + rounds + " rounds with " + holsDerGeierUtil.getActivePlayersSize() + " Players!");
        /**
         * Loop until there are no PointCards left
         */
        while (!pointCards.isEmpty()) {
            currentRound++;
            /**
             * Setup PointCard for this round
             */
            currentPointCard = holsDerGeierUtil.getNextPointCard();

            /* System.out.println("-----------------");
            if (currentPointCard.isMouseCard())
                System.out.println("Who is going to win " + currentPointCard.getValue() + " Points?");
            else
                System.out.println("Who is going to lose " + currentPointCard.getValue() * (-1) + " Points?"); */

            // HolsDerGeierUtil.getActivePlayers().forEach(System.out::println);
            // System.out.println();

            for (Player p : HolsDerGeierUtil.getActivePlayers()) {
                PlayerCard value = p.getNextCard(currentPointCard.getValue());
                values.add(value.getValue());
                usedCards.put(p, value);
            }

            /* usedCards.forEach((k, v) -> {
                System.out.printf("Player: %s, Card: %d%n", k.getName(), v.getValue());
            }); */

            winningValue = currentPointCard.isMouseCard() ? Util.getHighestValue(values) : Util.getLowestValue(values);
            // System.out.println("Winning Value: " + winningValue);

            for (Player p : HolsDerGeierUtil.getActivePlayers()) {
                if (p.getLastMove().getValue() == winningValue) {
                    winner.add(p);
                }
            }

            if (winner.size() > 1) {
                if (winner.size() < HolsDerGeierUtil.getActivePlayers().size()) {
                    if (!currentPointCard.isMouseCard()) {
                        ArrayList<Integer> drawValues = new ArrayList<>();
                        for (Player p : HolsDerGeierUtil.getActivePlayers()) {
                            if (winner.contains(p)) {
                                drawValues.add(p.getLastMove().getValue());
                            }
                        }
                        for (Player p : HolsDerGeierUtil.getActivePlayers()) {
                            if (Util.getLowestValue(drawValues) == p.getLastMove().getValue()) {
                                p.addPoints(currentPointCard.addValue(lastRoundPointCard).getValue());
                            }
                        }
                        drawValues.clear();
                    } else {
                        // System.out.println("Draw! The points are redirected to the upcoming round.");
                        lastRoundPointCard.setValue(lastRoundPointCard.getValue() + currentPointCard.getValue());
                    }
                } else {
                    // System.out.println("Draw! The points are redirected to the upcoming round.");
                    lastRoundPointCard.setValue(lastRoundPointCard.getValue() + currentPointCard.getValue());
                }
            } else {
                if (!currentPointCard.isMouseCard()) {
                    for (Player p : HolsDerGeierUtil.getActivePlayers()) {
                        if (winner.contains(p)) {
                            p.addPoints(currentPointCard.addValue(lastRoundPointCard).getValue());
                        }
                    }
                } else {
                    winner.get(0).addPoints(currentPointCard.addValue(lastRoundPointCard).getValue());
                }

                // System.out.println("The value was: " + winningValue + ". ");

                lastRoundPointCard = new PointsCard(0);
            }
            // activePlayer.forEach(k1 -> System.out.println(k1.getName() + " : " + k1.getPoints()));
            // System.out.println();

            /**
             * Diese Sektion ggf. auslagern!
             */
            HolsDerGeierUtil.getActivePlayers().forEach(p -> p.afterRound(winner));
            winningValue = 0;
            winner.clear();
            values.clear();
            usedCards.clear();
        }

        // System.out.println("Game over!");
        pointCards.clear();

        ArrayList<Integer> mostPointsPlayer = new ArrayList<>();
        for (Player player : HolsDerGeierUtil.getActivePlayers()) {
            mostPointsPlayer.add(player.getPoints());
        }

        final int mostPoints = Util.getHighestValue(mostPointsPlayer);
        mostPointsPlayer.clear();
        winner.clear();

        for (Player p : HolsDerGeierUtil.getActivePlayers()) {
            if (p.getPoints() == mostPoints) {
                winner.add(p);
            }
        }
        HolsDerGeierUtil.getActivePlayers().forEach(p -> p.afterGame(winner));

        /* winner.forEach(player -> {
            if (winner.size() == 1) // ansonsten Unentschieden :)
                System.out.println("Winner: " + player.getName());
            else if (winner.size() > 1)
                System.out.println("Draw");
        }); */
        HashMap<Player, Integer> mapPlayers = new HashMap<>();

        for (Player player : HolsDerGeierUtil.getActivePlayers()) {
            mapPlayers.put(player, player.getPoints());
        }
        // mapping Player
        Main.getHolsDerGeierUtil().mapPlayerPlaces(mapPlayers);

        mapPlayers.forEach((k, v) -> k.mapTotalPoints());
    }
}
