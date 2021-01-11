package de.rabitem.main.player;

import de.rabitem.main.HolsDerGeierUtil;
import de.rabitem.main.Main;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.exception.IllegalMoveException;

import java.util.ArrayList;

/**
 * @author Felix Huisinga
 */
public abstract class Player {
    private static int count = 0;
    protected int id;
    protected String name;
    protected PlayerCard lastMove = null;
    private int points = 0;
    protected int totalPoints = 0;
    private int winCounter = 0;

    private int placeLastRound = -1;

    protected ArrayList<Player> oponnents = new ArrayList<>();
    /**
     * Constructor of Player
     *
     * @param name String name
     */
    public Player(final String name) {
        this.id = count++ + 1;
        this.name = name;
        Main.getPlayerManager().registerPlayer(this);
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Spieler: " + this.name + " (Id: " + this.id + ")";
    }

    /**
     * Arraylist which contains the Cards
     */
    private final ArrayList<PlayerCard> cards = new ArrayList<>();

    /**
     * Fills Arraylist with Cards
     *
     * @param from int from
     * @param to   int to
     */
    public final void fillArraylist(final int from, final int to) {
        for (int i = from; i <= to; i++) {
            if (i == 0)
                continue;
            cards.add(new PlayerCard(i));
        }
    }

    /**
     * Player can use card? Still in available card?
     *
     * @param c PlayerCard c
     * @return
     */
    public boolean canUse(final PlayerCard c) {
        return cards.contains(c);
    }

    /**
     * This method is used to remove a PlayerCard
     * and checks if it can be used
     *
     * @param c PlayerCard c
     */
    protected final void remove(final PlayerCard c) throws IllegalMoveException {
        if (canUse(c)) {
            cards.remove(c);
            // lastMove = c;
        } else {
            System.out.println("Error while removing Card from " + this.getName());
            throw new IllegalMoveException("This move is not permitted! You already used this card...");
        }

    }

    /**
     * Resets the Player
     */
    public final void reset() {
        cards.clear();
        lastMove = null;
        this.fillArraylist(Main.getHolsDerGeierUtil().fromCards(), Main.getHolsDerGeierUtil().toCards());
        this.placeLastRound = -1;
    }

    /**
     * This method is called on setup before the Game starts, added for the RabitemBot
     */
    public void setupBeforeGameStarts() {}

    /**
     * This method is called after each round, added for the RabitemBot
     */
    public void afterRound(ArrayList<Player> winner) {}

    /**
     * This method is called after the Game, added for the RabitemBot
     */
    public void afterGame(ArrayList<Player> winner) {}


    /**
     * Overridable method for resets after each round
     */
    public void customResets() {

    }

    /**
     * Returns last move
     * null if not available
     *
     * @return getLastMove
     */
    public final PlayerCard getLastMove() {
        return lastMove;
    }

    /**
     * Sets the last move executed
     *
     * @param lastMove
     */
    public final void setLastMove(PlayerCard lastMove) {
        this.lastMove = lastMove;
    }

    /**
     * Determs if the Player is ready to play
     *
     * @return
     */
    public final boolean isReady() {
        return !cards.isEmpty() && lastMove == null;
    }

    /**
     * Returns the next PlayerCard to play
     *
     * @return PlayerCard c
     */

    public final PlayerCard getNextCard(final int pointCardValue) {
        final PlayerCard pC21576 = getNextCardFromPlayer(pointCardValue);
        try {
            this.remove(pC21576);
        } catch (IllegalMoveException e) {
            System.out.println(e.getMessage());
        }
        return pC21576;
    }

    /**
     * Returns the next PlayerCard to play from the custom Players
     *
     * @return PlayerCard c
     */

    public abstract PlayerCard getNextCardFromPlayer(final int pointCardValue);

    /**
     * Returns current Player Points
     *
     * @return int points
     */
    public final int getPoints() {
        return points;
    }

    /**
     * This method is used to set the player points
     *
     * @param points int points
     */
    private final void setPoints(int points) {
        this.points = points;
    }

    /**
     * Adds points to the player
     *
     * @param add int add
     */
    public final void addPoints(final int add) {
        points += add;
    }

    /**
     * Method to get a PlayerCard of a Player
     *
     * @param i int i
     * @return
     */
    public final PlayerCard getCard(final int i) {
        if (canUse(new PlayerCard(i)))
            return cards.get(cards.indexOf(new PlayerCard(i)));
        return null;
    }

    /**
     * Method to get all Cards played
     *
     * @return ArrayList<PlayerCard>
     */
    public final ArrayList<PlayerCard> getCards() {
        return cards;
    }

    /**
     * Method to set a opponent
     */
    public final void setOpponents() {
        oponnents.addAll(HolsDerGeierUtil.getActivePlayers());
        oponnents.remove(this);
    }

    /**
     * Method to get opponents
     *
     * @return ArrayList<Player>
     */
    protected final ArrayList<Player> getOponnents() {
        return oponnents;
    }

    /**
     * Sets the place of last round
     *
     * @param placeLastRound
     */
    public void setPlaceLastRound(int placeLastRound) {
        this.placeLastRound = placeLastRound;
    }

    /**
     * Increments WinCounter by 1
     */
    public void incWinCounter() {
        if (this.placeLastRound == 1) {
            winCounter++;
        }
    }

    /**
     * Returns the place of last round
     *
     * @return int getPlaceLastRound
     */
    public int getPlaceLastRound() {
        return placeLastRound;
    }

    /**
     * Returns the total points across all rounds played
     *
     * @return int totalPoints
     */
    public final int getTotalPoints() {
        return totalPoints;
    }

    /**
     * Adds points to the player total points
     *
     * @param totalPoints int totalPoints
     */
    private final void addTotalPoints(final int totalPoints) {
        this.totalPoints = this.totalPoints + totalPoints;
    }

    /**
     * This method is used to mapTotalPoints
     */
    public final void mapTotalPoints() {
        this.addTotalPoints(this.getPoints());
        this.setPoints(0);
    }

    /**
     * Returns the count of wins
     * @return int getWinCounter
     */
    public int getWinCounter() {
        return winCounter;
    }
}

