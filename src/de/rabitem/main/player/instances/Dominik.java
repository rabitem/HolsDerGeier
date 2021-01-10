package de.rabitem.main.player.instances;

import de.rabitem.main.Main;
import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.gui.ActionManagerUtil;
import de.rabitem.main.player.Player;
import de.rabitem.main.player.instances.doomutil.OpponentResponse;
import de.rabitem.main.util.Util;

import java.util.*;
import java.util.stream.Collectors;

public class Dominik extends Player {

    private final int totalRounds;
    private boolean first = false;
    private int lastPointValue;

    // different fields for middle field strategy
    private ArrayList<Integer> field1 = new ArrayList<>(); // 10, 11, 12
    private ArrayList<Integer> field2 = new ArrayList<>(); // 13, 14, 15
    private ArrayList<Integer> field3 = new ArrayList<>(); // 7, 8, 9
    private ArrayList<Integer> field4 = new ArrayList<>(); // 1, 2, 3
    private ArrayList<Integer> field5 = new ArrayList<>(); // 4, 5, 6

    private int fieldBias;

    /**
     * statTable is using the following construct:
     * <br>
     * <b>[ Point value | List of {Played card|Weight} ]</b>
     * <br>
     * where <i>weight</i> is the number of times the played card
     * was given for that point value.
     */
    private final HashMap<Integer, ArrayList<OpponentResponse>> statTable = new LinkedHashMap<>(); // points -- list of played cards

    /**
     * Constructor of Player
     *
     * @param name String name
     */
    public Dominik(String name) {
        super(name);
        totalRounds = Integer.parseInt(Main.getOptionsPanel().getTfRounds().getValue().toString());
        customResets();
    }

    @Override
    public void customResets() {
        // Fills the field arrays for the middle-field strategy
        field1 = new ArrayList<>(Arrays.asList(10, 11, 12));
        field2 = new ArrayList<>(Arrays.asList(13, 14, 15));
        field3 = new ArrayList<>(Arrays.asList(7, 8, 9));
        field4 = new ArrayList<>(Arrays.asList(1, 2, 3));
        field5 = new ArrayList<>(Arrays.asList(4, 5, 6));
    }

    @Override
    public PlayerCard getNextCardFromPlayer(int pointCardValue) {
        PlayerCard enemyLast = getOponnents().get(0).getLastMove();

        // if enemyLast is null, we are in the first round
        if (enemyLast == null) {
            first = true;
        } else {
            if (first)
                updateStatistics(lastPointValue);
            else
                updateStatistics(pointCardValue);
        }
        lastPointValue = pointCardValue;

        // Analyze the stats and return card most likely to beat or middle field fallback
        return analyzeStatistics(pointCardValue);
    }

    /**
     * Returns the field reference that should be used for the middle field strategy.
     * If upperBias is true, switch priorities to get higher chances of defeating bots
     * with a similar strategy
     * */
    private ArrayList<Integer> assignField(int pointValue, boolean upperBias) {
        return switch (pointValue) {
            case 8, 9, 10 -> upperBias ? field2 : field1;
            case 7, 6, 5 -> upperBias ? field1: field2;
            case 4, 3, 2 -> field3;
            case 1, -1, -2 -> field4;
            case -3, -4, -5 -> field5;
            default -> null;
        };
    }

    private PlayerCard middleFieldCard(int pointValue) {
        ArrayList<Integer> fieldToUse = assignField(pointValue, false);
        return fieldCard(fieldToUse);
    }

    private PlayerCard fieldCard(ArrayList<Integer> fieldToUse) {
        if (fieldToUse.isEmpty())
            return getCards().get(Util.random(0, getCards().size() - 1));

        int index = new Random().nextInt(fieldToUse.size());
        if (canUse(new PlayerCard(fieldToUse.get(index)))) {
            PlayerCard pC = getCard(fieldToUse.get(index));
            fieldToUse.remove(index);
            return pC;
        } else {
            // The card we would normally use from the field has already been used by the statistical method
            return getCards().get(Util.random(0, getCards().size() - 1));
        }
    }

    private PlayerCard analyzeStatistics(int pointValue) {
        int currentRound = ActionManagerUtil.getRoundsPlayed();

        // cancel if not enough data has been collected for confidence
        if (currentRound < 10 || currentRound < totalRounds / 10)
            return middleFieldCard(pointValue);

        // Check if there are entries for this point value in the stats table
        if (statTable.containsKey(pointValue)) {
            HashMap<Integer, Integer> responses = new LinkedHashMap<>();

            int sum = 0; // total sum of all weights for this point value
            // convert the object into a HasMap for easier sorting & processing
            for (OpponentResponse o : statTable.get(pointValue)) {
                responses.put(o.getValue(),o.getWeight());
                sum += o.getWeight();
            }

            // Sort the map by value (weight), large to small since high weight means high confidence the enemy will pick it
            // We use the newer Java 8 stream methods for this since it enables us to sort the map without using placeholder arrays
            HashMap<Integer, Integer> sortedMap = responses.entrySet().stream().sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

            // For each value/weight pair in the sorted map
            int elementsLeft = sortedMap.size();
            for (Map.Entry<Integer, Integer> e : sortedMap.entrySet()) {
                elementsLeft--;
                int key = e.getKey();
                // Calculate the percentage of this output from the total weight
                float weightFromTotal = (float) e.getValue() / sum * 100;

                // Is the weight over 30% of all played weights for this value? = max 3 different outputs for one input
                // (for equal distribution) or a very favored pick
                // -> Likely that the bot is not random?
                if (weightFromTotal > 30) {
                    // Can the enemy use that card? If not, try the next lower entry to minimize our bet

                    if (getOponnents().get(0).canUse(new PlayerCard(key))) {
                        PlayerCard ret = cardThatBeats(key, pointValue);

                        // If we can't use a card of key+1 to beat the enemy, try again
                        if (ret == null)
                            continue;
                        else
                            return ret;
                    }

                    /*
                    If there is no lower to try, the bot will either choose an illegal move or randomly.
                    This is because any properly implemented bot will not try to use cards it doesn't have anymore,
                    so for value -> response functions that follow a specific pattern, there should always be a proper
                    option left for the bot to choose from that matches with our observation from previous rounds
                    We already know that random strategies are best beaten by middle field, so we choose from that.
                    */
                    if (elementsLeft == 0)
                        return middleFieldCard(pointValue);

                } else { // Weight not high enough -> likely a random bot or not enough data
                    // Other values need not be looked at, since they are ordered the following ones would have
                    // even worse weight, if there are any
                    return middleFieldCard(pointValue);
                }
            }
        } else {
            // No entries = no stats, so return middle field (= first game of the tournament)
            return middleFieldCard(pointValue);
        }
        // shouldn't get to here normally
        return middleFieldCard(pointValue);
    }

    /**
     * Compares the enemyPrediction to our own middle field choice, allowing to detect
     * similar play styles and switch strategies when a certain threshold is met.
     *
     * @return If the bias is met, the card that is more likely to beat that similar play style,
     * else the normal middle field card.
     * */
    private PlayerCard cardThatBeats(int enemyPrediction, int pointValue) {
        ArrayList<Integer> fieldToUse = assignField(pointValue, false);

        // The enemy has a middle field strat as well and chooses similar for high-value fields
        if (fieldToUse.contains(enemyPrediction) && (fieldToUse.equals(field1) || fieldToUse.equals(field2)))
            fieldBias++;

        // The field to use gets assigned with a bias for higher values, resulting in a higher chance to beat a
        // middle-first strategy
        if (fieldBias > 5) {
            return fieldCard(assignField(pointValue, true));
        } else {
            // same as middle field
            return fieldCard(fieldToUse);
        }
    }

    /**
     * Create or update the statTable entries for the given point value
     * */
    private void updateStatistics(int points) {
        int eV = getOponnents().get(0).getLastMove().getValue();

        if (statTable.containsKey(points)) {
            boolean found = false;
            for (OpponentResponse o : statTable.get(points)) {
                if (o.getValue() == eV) {
                    o.addWeight();
                    found = true;
                }
            }
            if (!found) {
                statTable.get(points).add(new OpponentResponse(eV));
            }
        } else {
            statTable.put(points, new ArrayList<>());
            statTable.get(points).add(new OpponentResponse(eV));
        }
    }
}
