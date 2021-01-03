
package de.rabitem.main.player.instances;

import de.rabitem.main.card.instances.PlayerCard;
import de.rabitem.main.player.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LocalPlayer extends Player {

    /**
     * Constructor of LocalPlayer
     * @param name String name
     */
    public LocalPlayer(final String name) {
        super(name);
    }

    @Override
    public PlayerCard getNextCardFromPlayer(final int pointCardValue) {
        try {
            System.out.print("Your Cards left to play: ");
            this.getCards().forEach(k -> System.out.print(k.getValue() + " "));
            System.out.print("\nYour card: ");
            return new PlayerCard(Integer.parseInt(new BufferedReader(new InputStreamReader(System.in)).readLine()));
        }catch (IOException e) {
            System.out.println(e.getMessage());
            this.getNextCard(pointCardValue);
        }
        return null;
    }
}
