package de.rabitem.main.player.instances.doomutil;

/**
     * OpponentResponse is a helper object used
     * to give a weight (number of occurences) to
     * an opponents' card value.
     * <br>
     * It helps prevent excessive nesting with the HashMap+ArrayList
     * it is stored in
     * */
    public class OpponentResponse {
    private final int value;
    private int weight;

    /**
     * Creates a new opponent response with weight 1
     * @param value The value for which weight should be tracked
     * */
    public OpponentResponse(int value) {
        this.value = value;
        weight = 1;
    }

    /**
     * @return The current weight for this OpponentResponse value
     * */
    public int getWeight() {
        return weight;
    }

    /**
     * Adds one to the weight
     * */
    public void addWeight() {
        weight++;
    }

    public int getValue() {
        return value;
    }
}