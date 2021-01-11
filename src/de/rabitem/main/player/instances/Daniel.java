package de.rabitem.main.player.instances;

import de.rabitem.main.HolsDerGeierUtil;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.card.instances.PointsCard;
import de.rabitem.main.player.Player;

import java.util.*;

public class Daniel extends Player {

    final int minAbsoluteValuePointsForWin = 36;
    final int strategyCheckGameFrequency = 10;

    private boolean focusLowValueCards = false;
    private int gamesPlayed;

    private int absPointsLeftOverFromDraw;
    private int myPointsAbsoluteValue;
    private int lastPointCard;

    public Daniel(String name) {
        super(name);
        initializeGameStart();
    }

    private void initializeGameStart()
    {
        myPointsAbsoluteValue = 0;
    }

    @Override
    public void afterGame(ArrayList<Player> winner) {
        initializeGameStart();

        gamesPlayed += 1;

        if(gamesPlayed % strategyCheckGameFrequency == 0){

            //change strategy if loosing
            if(this.getWinCounter() < (gamesPlayed / 2)) {
                focusLowValueCards = !focusLowValueCards;
            }
        }
    }

    @Override
    public void afterRound(ArrayList<Player> winner) {
        boolean draw = (winner.size() > 1);
        if(draw) {
            absPointsLeftOverFromDraw += Math.abs(lastPointCard);
        }
        else {
            absPointsLeftOverFromDraw = 0;

            boolean won = (winner.contains(this) && lastPointCard > 0);
            if(winner.contains(this) && lastPointCard > 0) {
                myPointsAbsoluteValue += Math.abs(lastPointCard);
            }
        }
    }

    @Override
    public PlayerCard getNextCardFromPlayer(int pointCardValue) {
        lastPointCard = pointCardValue;

        ArrayList<Integer> handCardValues = getHandCardValues();
        //sort greatest to smallest
        Collections.sort(handCardValues);
        Collections.reverse(handCardValues);

        int currentAbsPointCardValue = Math.abs(pointCardValue);
        int handIndex = getHigherPriorityPointCardAmount(currentAbsPointCardValue);

        return new PlayerCard(handCardValues.get(handIndex));
    }

    private ArrayList<Integer> getHandCardValues(){
        ArrayList<PlayerCard> myCards = this.getCards();
        ArrayList<Integer> myValues = new ArrayList<Integer>();

        for (PlayerCard card : myCards) {
            myValues.add(card.getValue());
        }
        return myValues;
    }

    private int getHigherPriorityPointCardAmount(int currentAbsPointCardValue){
        ArrayList<Integer> focusedAbsPointCardValues = getFocusedAbsPointCardValues(currentAbsPointCardValue);

        if(focusedAbsPointCardValues.contains(currentAbsPointCardValue)){
            currentAbsPointCardValue += absPointsLeftOverFromDraw;

            int higherPriorityCardCount = 0;
            for (Integer focusedValue: focusedAbsPointCardValues) {
                if(focusedValue > currentAbsPointCardValue){
                    higherPriorityCardCount += 1;
                }
            }
            return  higherPriorityCardCount;
        }
        else {
            return HolsDerGeierUtil.getPointCards().size();
        }
    }

    private ArrayList<Integer> getFocusedAbsPointCardValues(int currentAbsPointCardValue) {
        ArrayList<Integer> pointCardValues = getUnusedAbsPointCardValues();
        pointCardValues.add(currentAbsPointCardValue);

        //sorting from smallest to greatest
        Collections.sort(pointCardValues);
        if(focusLowValueCards == false){
            //reversing to greatest to smallest
            Collections.reverse(pointCardValues);
        }

        int focusedPointsAbsoluteValue = 0;
        ArrayList<Integer> focusedValues = new ArrayList<Integer>();

        for (Integer value : pointCardValues) {
            focusedPointsAbsoluteValue += value;
            focusedValues.add(value);
            if(focusedPointsAbsoluteValue >= minAbsoluteValuePointsForWin - myPointsAbsoluteValue) break;
        }
        return  focusedValues;
    }

    private ArrayList<Integer> getUnusedAbsPointCardValues(){
        List<PointsCard> pointCards = HolsDerGeierUtil.getPointCards();

        ArrayList<Integer> pointCardValues = new ArrayList<Integer>();

        for (PointsCard pointsCard: pointCards) {
            pointCardValues.add(Math.abs(pointsCard.getValue()));
        }
        return  pointCardValues;
    }
}