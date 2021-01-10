package de.rabitem.main.player.instances;

import de.rabitem.main.Main;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;
import de.rabitem.main.util.Util;
import java.util.ArrayList;


/**
 * @author Filip Grabski
 *
 */
public class Filip extends Player {

    /**
     * Constructor of Rudy
     *
     * @param name String name
     */
    public Filip(final String name) {
        super(name);
    }

    /**
     * Main strategy of the bot:
     *
     * The bot is designed to achieve draws, therefore the winning condition lies within the beginning of the game.
     * In 30% of the total rounds the Bot plays a regular static strategy. If this strategy is successful the game should end in the favor of this bot.
     * While playing the static strategy the method updateArrays is called in every match-up
     * and the Data of enemy moves in respective scenarios is stored.
     * After the number of rounds is higher the 30% the bot begins to predict enemy moves and tries to copy them resulting in a best case scenario in either a draw
     *
     */





    /**
     *
     * Initiation of Variables for the getNextCardFromPlayer Method
     *
     */
    private boolean first = false;
    private int lastPointCardValue;

    /**
     *
     * Arrays used to store moves of the enemy Player.
     * For each Point Value there is a respective Array that stores the Values of the player cards.
     *
     */

    private ArrayList<Integer> cardsForM5 = new ArrayList();
    private ArrayList<Integer> cardsForM4 = new ArrayList();
    private ArrayList<Integer> cardsForM3 = new ArrayList();
    private ArrayList<Integer> cardsForM2 = new ArrayList();
    private ArrayList<Integer> cardsForM1 = new ArrayList();
    private ArrayList<Integer> cardsFor1 = new ArrayList();
    private ArrayList<Integer> cardsFor2 = new ArrayList();
    private ArrayList<Integer> cardsFor3 = new ArrayList();
    private ArrayList<Integer> cardsFor4 = new ArrayList();
    private ArrayList<Integer> cardsFor5 = new ArrayList();
    private ArrayList<Integer> cardsFor6 = new ArrayList();
    private ArrayList<Integer> cardsFor7 = new ArrayList();
    private ArrayList<Integer> cardsFor8 = new ArrayList();
    private ArrayList<Integer> cardsFor9 = new ArrayList();
    private ArrayList<Integer> cardsFor10 = new ArrayList();


    /**
     * Method used to find out how many Rounds are to be played.
     * @return  int Main.getMain().actionManager.roundsPlayed
     */
    private int getRoundsPlayed(){
        return Main.getMain().actionManager.roundsPlayed;
    }

    /**
     * Method to get a random available card in a given from to range.
     * Used in the staticStrategyMethod.
     * @param from
     * @param to
     * @return a random PlayerCard playerCard in the Range of 'from' to 'to'
     */

    private PlayerCard getRandomFromTo(int from, int to) {
        PlayerCard playerCard = new PlayerCard(Util.random(from, to));
        if (this.canUse(playerCard)) {
            return playerCard;
        } else {
            return getRandomFromTo(from, to);
        }

    }


    /**
     * Method used for updating Arrays with the last used card by the enemy.
     *
     * @param p_PointCard
     */
    private void updateArrays(int p_PointCard) {
        int enemyLast = getOponnents().get(0).getLastMove().getValue();
        switch (p_PointCard) {
            case (-5) -> cardsForM5.add(enemyLast);
            case (-4) -> cardsForM4.add(enemyLast);
            case (-3) -> cardsForM3.add(enemyLast);
            case (-2) -> cardsForM2.add(enemyLast);
            case (-1) -> cardsForM1.add(enemyLast);
            case (1) -> cardsFor1.add(enemyLast);
            case (2) -> cardsFor2.add(enemyLast);
            case (3) -> cardsFor3.add(enemyLast);
            case (4) -> cardsFor4.add(enemyLast);
            case (5) -> cardsFor5.add(enemyLast);
            case (6) -> cardsFor6.add(enemyLast);
            case (7) -> cardsFor7.add(enemyLast);
            case (8) -> cardsFor8.add(enemyLast);
            case (9) -> cardsFor9.add(enemyLast);
            case (10) -> cardsFor10.add(enemyLast);
        }
    }


    /**
     * Method used in the beginning of the game.
     * Regular strategy with some randomness.
     *
     * @param p_pointCardValue
     * @return
     */

    private PlayerCard staticStrategy(int p_pointCardValue) {
        return switch (p_pointCardValue) {
            case 10, 9, 8 -> getRandomFromTo(13, 15);
            case 7, 6, 5 -> getRandomFromTo(10, 12);
            case 4, 3, 2 -> getRandomFromTo(7, 9);
            case 1, -1, -2 -> getRandomFromTo(4, 6);
            case -3, -4, -5 -> getRandomFromTo(1, 3);
            default -> null;
        };

    }

    /**
     * Method used to calculate the "weight" of a certain Card in the Array.
     * The higher the weight is the more often did the enemy play this card in this scenario.
     *
     * @param p_cardsForX
     * @param p_CardInArray
     * @return
     */

    private int calcWeight(ArrayList<Integer> p_cardsForX, int p_CardInArray) {
        int weight = 0;
        for (Integer pCardsForX : p_cardsForX) {
            if (pCardsForX == p_CardInArray) {
                weight = weight + 1;
            }
        }

        //System.out.println("In this scenario the enemy played "+p_CardInArray+" "+weight+" times. Chance For This Card: "+probability+"%" );
        //System.out.println(p_cardsForX.toString());
        return weight;
    }

    /**
     * Method used to select the card with the highest weight that is also playable by the Bot.
     * For the initial 30% of total rounds the method returns the static strategy.
     * This is for filling the arraylists with data before using them.
     *
     * @param p_cardsForX
     * @param p_pointCardValue
     * @return
     */

    private PlayerCard highestWeightCard(ArrayList<Integer> p_cardsForX, int p_pointCardValue) {
        int PercentStaticStrat = getRoundsPlayed()/100 *30;

        if (getRoundsPlayed() > PercentStaticStrat) {
            int highestWeight = 0;
            for (int i = 0; i < p_cardsForX.size(); i++) {
                int w1 = calcWeight(p_cardsForX, p_cardsForX.get(i));
                if (highestWeight < w1 && this.canUse(new PlayerCard (p_cardsForX.get(i)))) {
                    highestWeight = p_cardsForX.get(i);
                }
            }
            PlayerCard cardHighestWeight = new PlayerCard(highestWeight);

            if(this.canUse(cardHighestWeight)) {
                return cardHighestWeight;
            }
            else{

                return getCards().get(Util.random(0, getCards().size() - 1));

            }

        } else
            return staticStrategy(p_pointCardValue);

    }

    /**
     * Method called in the main method of the bot.
     * By using the switch it calls the highestWeightCard method with the respective array.
     *
     * @param p_pointCardValue
     * @return
     */

    private PlayerCard finalStrategy(int p_pointCardValue){
        return switch(p_pointCardValue){
            case(-5) -> highestWeightCard(cardsForM5,-5);
            case (-4)-> highestWeightCard(cardsForM4,-4);
            case (-3)-> highestWeightCard(cardsForM3,-3);
            case (-2)-> highestWeightCard(cardsForM2,-2);
            case (-1)-> highestWeightCard(cardsForM1,-1);
            case (1)-> highestWeightCard(cardsFor1,1);
            case (2)-> highestWeightCard(cardsFor2,2);
            case (3)-> highestWeightCard(cardsFor3,3);
            case (4)-> highestWeightCard(cardsFor4,4);
            case (5)-> highestWeightCard(cardsFor5,5);
            case (6)-> highestWeightCard(cardsFor6,6);
            case (7)-> highestWeightCard(cardsFor7,7);
            case (8)-> highestWeightCard(cardsFor8,8);
            case (9)-> highestWeightCard(cardsFor9,9);
            case (10)-> highestWeightCard(cardsFor10,10);
            default-> null;
        };
    }

    /**
     * This method is the main method of the Class and it returns the next Card to be played by the bot.
     *
     * @param pointCardValue
     * @return
     */
    @Override
    public PlayerCard getNextCardFromPlayer(final int pointCardValue) {
        PlayerCard enemyLast = getOponnents().get(0).getLastMove();
        if(enemyLast == null){
            first= true;

        }   else {
            if(first)
                updateArrays(lastPointCardValue);
            else
                updateArrays(pointCardValue);


        } lastPointCardValue = pointCardValue;
        return finalStrategy(pointCardValue);

    }
}
