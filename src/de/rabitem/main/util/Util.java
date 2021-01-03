package de.rabitem.main.util;

import de.rabitem.main.xml.XMLUtil;
import de.rabitem.main.xml.enums.XMLELEMENTSCOPYRIGHTCLAIM;
import de.rabitem.main.xml.enums.XMLTAGS;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Felix Huisinga
 */
public class Util {

    /**
     * Returns a random Integer within a given range
     *
     * @param min int min
     * @param max int max
     * @return int random
     */
    public static int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
        // return (int) (Math.random() * (max - min + 1)) + min;
    }

    /**
     * Returns lowest value of an integer array
     *
     * @param values ArrayList<Integer> values
     * @return int lowestValue
     */
    public static int getLowestValue(final ArrayList<Integer> values) {
        int lowestValue = values.get(0);
        for (int i : values) {
            lowestValue = Math.min(i, lowestValue);
        }
        return lowestValue;
    }

    /**
     * Returns highest value of an integer array
     *
     * @param values ArrayList<Integer> values
     * @return int highestValue
     */
    public static int getHighestValue(final ArrayList<Integer> values) {
        int highestValue = values.get(0);
        for (int i : values) {
            highestValue = Math.max(i, highestValue);
        }
        return highestValue;
    }

    /**
     * This method is used to scale an image
     *
     * @param srcImg to be scaled
     * @param w      width to be scaled to
     * @param h      height to be scaled to
     * @return Image width new scaling
     */
    public static Image getScaledImage(Image srcImg, int w, int h) {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    /**
     * This method is used to check if the copyright claim was checked
     *
     * @return boolean hasReadCopyrightClaim
     */
    public static boolean hasReadCopyrightClaim() {
        return Boolean.parseBoolean(XMLUtil.readXML(XMLTAGS.COPYRIGHTCLAIM.toString().toLowerCase(), XMLELEMENTSCOPYRIGHTCLAIM.ACCEPTED.toString().toLowerCase()));
    }

    /**
     * This method is used to accept the copyright claim
     */
    public static void acceptCopyrightClaim() {
        XMLUtil.changeXMLAttribute(XMLTAGS.COPYRIGHTCLAIM.toString().toLowerCase(), XMLELEMENTSCOPYRIGHTCLAIM.ACCEPTED.toString().toLowerCase(), true);
    }

    /**
     * Simple bubbleSort Algorithm for ArrayList containing Integers
     *
     * @param list ArrayList<Integer> list
     * @return ArrayList<Integer> list
     */
    public static ArrayList<Integer> bubbleSort(ArrayList<Integer> list) {
        int n = list.size();
        int temp = 0;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < (n - i - 1); j++) {
                if (list.get(j) > list.get(j + 1)) {
                    temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                }

            }
        }
        return list;
    }
}