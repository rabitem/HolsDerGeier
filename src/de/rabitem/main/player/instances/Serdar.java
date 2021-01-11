package de.rabitem.main.player.instances;

import de.rabitem.main.HolsDerGeierUtil;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.card.instances.PointsCard;
import de.rabitem.main.player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Serdar Tuzcu
 */

public class Serdar extends Player {

    private Player opponent;
    private int shift;

    public Serdar(final String name) {
        super(name);
    }

    @Override
    public PlayerCard getNextCardFromPlayer(final int pointCardValue) {
        if (getCards().size() == 15)
            shift = new Random().nextInt(14);
        if (getCards().size() > 6)
            return easyPick(pointCardValue);
        return ai(pointCardValue);
    }


    private PlayerCard easyPick(final int pointCardValue) {
        switch (pointCardValue) {
            case 10:
                return getPlayerCard(getShift(15));
            case 9:
                return getPlayerCard(getShift(14));
            case 8:
                return getPlayerCard(getShift(13));
            case 7:
                return getPlayerCard(getShift(12));
            case 6:
                return getPlayerCard(getShift(11));
            case 5:
                return getPlayerCard(getShift(9));
            case 4:
                return getPlayerCard(getShift(7));
            case 3:
                return getPlayerCard(getShift(5));
            case 2:
                return getPlayerCard(getShift(3));
            case 1:
                return getPlayerCard(getShift(1));
            case -1:
                return getPlayerCard(getShift(2));
            case -2:
                return getPlayerCard(getShift(4));
            case -3:
                return getPlayerCard(getShift(6));
            case -4:
                return getPlayerCard(getShift(8));
            case -5:
                return getPlayerCard(getShift(10));
        }
        return null;
    }

    private PlayerCard getPlayerCard(final int x) {
        for (int i = 0; i < getCards().size(); i++) {
            if (getCards().get(i).getValue() == x)
                return getCards().get(i);
        }
        return null;
    }

    private PlayerCard ai(final int pointCardValue) {
        opponent = oponnents.get(0);
        Game game = new Game(HolsDerGeierUtil.getPointCards(), getCards(), opponent.getCards(), getPoints(), opponent.getPoints());
        game.pointCards.add(new PointsCard(pointCardValue));
        int bM = bestMove(game, pointCardValue);
        return getCards().get(bM);
    }

    private int bestMove(Game game, int pointCardValue) {
        int bestScore = Integer.MIN_VALUE;
        int move = -1;
        // für alle meine Karten
        for (int i = 0; i < game.pointCards.size(); i++) {
            Game copyGame = new Game(game);
            copyGame.play(copyGame.cardsP1.get(i).getValue(), pointCardValue, true);
            int score = minimax(copyGame, 0, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
            if (score > bestScore) {
                bestScore = score;
                move = i;
            }
        }
        //System.out.println("Bester berechneter Score: " + bestScore);
        return move;
    }

    private int minimax(Game game, int depth, int alpha, int beta, boolean isMaximizing) {
        Game copyGame = new Game(game);
        String result = checkWin(copyGame);
        if (result.equals(this.toString()))
            return 1;
        else if (result.equals(opponent.toString()))
            return -1;
        else if (result.equals("tie"))
            return 0;

        int bestScore = 0;

        if (isMaximizing) {
            //int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < copyGame.cardsP1.size(); i++) {
                copyGame = new Game(game);
                copyGame.play(copyGame.cardsP1.get(i).getValue(), copyGame.getNextCard(), true);
                int score = minimax(copyGame, depth+1, alpha, beta, false);
                bestScore = bestScore + score;
                //bestScore = Math.max(bestScore, score);
            }
            return bestScore;
        } else {
            //int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < copyGame.cardsP2.size(); i++) {
                copyGame = new Game(game);
                copyGame.play(copyGame.cardsP2.get(i).getValue(), game.getNextCard(), false);
                int score = minimax(copyGame, depth+1, alpha, beta, true);
                bestScore = bestScore + score;
                //bestScore = Math.min(bestScore, score);
            }
            return bestScore;
        }
    }

    private String checkWin(Game game) {
        if (game.pointCards.size() == 0) {
            if (opponent.getPoints() > this.getPoints())
                return opponent.toString();
            else if (opponent.getPoints() < this.getPoints())
                return this.toString();
            else
                return "tie";
        } else {
            return "in game";
        }
    }

    private int getShift(final int i) {
        return (i+shift)%15;
    }

}

final class Game {

    // Alle Karten im Spiel von -5 bis 10
    List<PointsCard> pointCards;

    ArrayList<PlayerCard> cardsP1;
    ArrayList<PlayerCard> cardsP2;

    int pointsP1;
    int pointsP2;

    int leftPoints;

    ArrayList<Integer> cards = new ArrayList<>();

    public Game(List<PointsCard> pointCards, ArrayList<PlayerCard> cardsP1, ArrayList<PlayerCard> cardsP2, int pointsP1, int pointsP2) {
        this.pointCards = new ArrayList<>(pointCards);
        this.cardsP1 = new ArrayList<>(cardsP1);
        this.cardsP2 = new ArrayList<>(cardsP2);
        this.pointsP1 = pointsP1;
        this.pointsP2 = pointsP2;
    }

    public Game(Game game) {
        this.pointCards = new ArrayList<>(game.pointCards);
        this.cardsP1 = new ArrayList<>(game.cardsP1);
        this.cardsP2 = new ArrayList<>(game.cardsP2);
        this.pointsP1 = game.pointsP1;
        this.pointsP2 = game.pointsP2;
        this.cards = new ArrayList<>(game.cards);
        this.leftPoints = game.leftPoints;
    }

    public void play(int card, int pointCard, boolean isSelfPlaying) {
        if (isSelfPlaying) {
            for (int i = 0; i < cardsP1.size(); i++) {
                if (cardsP1.get(i).getValue() == card) {
                    cardsP1.remove(i);
                    break;
                }
            }
        } else {
            for (int i = 0; i < pointCards.size(); i++) {
                if (pointCards.get(i).getValue() == pointCard) {
                    pointCards.remove(i);
                    break;
                }
            }
            for (int i = 0; i < cardsP2.size(); i++) {
                if (cardsP2.get(i).getValue() == card) {
                    cardsP2.remove(i);
                    break;
                }
            }
        }

        if (cards.size() < 2)
            cards.add(card);
        else
            System.err.println("MEHR ALS 2 KARTEN");

        if (cards.size() == 2) {
            if (this.leftPoints != 0)
                pointCard = pointCard + this.leftPoints;

            int cardP1 = cards.get(0);
            int cardP2 = cards.get(1);

            if (cardP1 > cardP2) {
                if (pointCard > 0)
                    pointsP1 = pointsP1 + pointCard;
                else
                    pointsP2 = pointsP2 + pointCard;
                this.leftPoints = 0;
            } else if (cardP1 < cardP2) {
                if (pointCard < 0)
                    pointsP1 = pointsP1 + pointCard;
                else
                    pointsP2 = pointsP2 + pointCard;
                this.leftPoints = 0;
            } else {
                this.leftPoints = this.leftPoints + pointCard;
            }
            cards.clear();
        }
    }
/*

    public int calculate() {
        int score = pointsP1 - pointsP2;

        int cardsP1Points = 0;
        int cardsP2Points = 0;
        double leftPoints = 0;
        for (int i = 0; i < cardsP1.size(); i++) {
            cardsP1Points = cardsP1Points + cardsP1.get(i).getValue();
            cardsP2Points = cardsP2Points + cardsP2.get(i).getValue();
            if (pointCards.get(i).getValue() < 0)
                leftPoints = (leftPoints + (pointCards.get(i).getValue()*-1));
            else
                leftPoints = leftPoints + pointCards.get(i).getValue();
        }

        double goodness = (leftPoints + this.leftPoints)/70.0 * (cardsP1Points - cardsP2Points);

        //System.out.println("Score = " + score + " + ((" + goodness1 + ") * (" + cardsP1Points + " - " + cardsP2Points + "))");
        score = (score) + (int) goodness;



        return score;
    }

 */

    public int getNextCard() {
        return pointCards.get(pointCards.size()-1).getValue();
    }

}