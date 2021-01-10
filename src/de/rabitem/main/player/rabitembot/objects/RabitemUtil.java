package de.rabitem.main.player.rabitembot.objects;

import de.rabitem.main.card.instances.PlayerCard;

import java.util.ArrayList;

public class RabitemUtil {
    public static String PREFIX = "[RABITEMBOT] ";

    public static boolean OUTPUT = false;

    public static void outputMessage(String message) {
        if (OUTPUT)
            System.out.println(PREFIX + message);
    }
}
