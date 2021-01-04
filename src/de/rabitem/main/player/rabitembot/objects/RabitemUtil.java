package de.rabitem.main.player.rabitembot.objects;

public class RabitemUtil {
    public static String PREFIX = "[RABITEMBOT] ";

    public static boolean OUTPUT = false;

    public static void outputMessage(String message) {
        if (OUTPUT)
            System.out.println(PREFIX + message);
    }

    private void m1(int one, int two) {
        System.out.println("TEST1");
    }

    private void m1(double one, double two) {
        System.out.println("TEST2");
    }

    public void doit() {
        this.m1(1.0, 1);
        this.m1(1.0D, 1);
    }
}
