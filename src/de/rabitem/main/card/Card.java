package de.rabitem.main.card;

import java.util.Objects;

/**
 * @author Felix Huisinga
 */
public abstract class Card {
    int value;

    protected Card(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card card = (Card) o;
        return getValue() == card.getValue();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
