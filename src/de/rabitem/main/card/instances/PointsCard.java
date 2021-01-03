package de.rabitem.main.card.instances;

import de.rabitem.main.card.Card;

/**
 * @author Felix Huisinga
 */
public class PointsCard extends Card {

    /**
     * Constructor of PointsCard
     * @param value int value
     */
    public PointsCard(int value) {
        super(value);
    }

    /**
     * This method is used to find out if the card is a mouse or a vulture card
     * @return
     */
    public boolean isMouseCard(){
        return this.getValue() > 0;
    }

    /**
     * Adds value to the PointCard
     * @param value int Value
     * @return PointCard
     */
    public PointsCard addValue(final int value) {
        this.setValue(this.getValue() + value);
        return this;
    }

    /**
     * Adds value to the PointCard
     * @param pc PointCard pc
     * @return PointCard
     */
    public PointsCard addValue(final PointsCard pc) {
        this.setValue(this.getValue() + pc.getValue());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
