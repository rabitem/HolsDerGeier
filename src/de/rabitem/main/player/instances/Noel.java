package de.rabitem.main.player.instances;

import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Noel extends Player {

    public Noel(String name) {
        super(name);
    }

    @Override
    public PlayerCard getNextCardFromPlayer(int pointCardValue) {
        return switch (pointCardValue) {
            case 10 -> selectCardFromRange(pointCardValue, 15, 15);
            case 9, 8 -> selectCardFromRange(pointCardValue, 13, 14);
            case 7, 6, 5 -> selectCardFromRange(pointCardValue, 10, 12);
            case 4, 3, 2 -> selectCardFromRange(pointCardValue, 7, 9);
            default -> selectCardFromRange(pointCardValue, 1, 6);
        };
    }

    private PlayerCard selectCardFromRange(int pointCardValue, int from, int to) {
        List<Integer> usableValues = IntStream.rangeClosed(from, to)
                .filter(number -> this.canUse(new PlayerCard(number)))
                .boxed()
                .collect(Collectors.toList());

        if (pointCardValue > 0) {
            return new PlayerCard(Collections.max(usableValues));
        } else {
            return new PlayerCard(Collections.min(usableValues));
        }
    }

}