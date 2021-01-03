package de.rabitem.main.gui;

import de.rabitem.main.Main;
import de.rabitem.main.util.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GUIUtil {

    public static void drawImageToBackground(final JPanel jPanel, final Graphics g, final String url) {
        final InputStream imageStream = Main.class.getResourceAsStream(url);
        try {
            final ImageIcon img = new ImageIcon(Util.getScaledImage(ImageIO.read(imageStream), Main.getMain().getMainMenuFrame().getWidth(), Main.getMain().getMainMenuFrame().getWidth()));
            g.drawImage(img.getImage(), 0, 0, jPanel.getWidth(), jPanel.getHeight(), null);
            //TODO: better blur effect

            /* final Image image = img.getImage();
            final BufferedImage buffered = (BufferedImage) image;
            g.drawImage(transposedHBlur(buffered), 0, 0, jPanel.getWidth(), jPanel.getHeight(), null); */
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void setUIManager() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, fall back to cross-platform
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static BufferedImage transposedHBlur(BufferedImage im) {
        int height = im.getHeight();
        int width = im.getWidth();
        // result is transposed, so the width/height are swapped
        BufferedImage temp =  new BufferedImage(height, width, BufferedImage.TYPE_INT_RGB);
        float[] k = new float[]{0.00598f, 0.060626f, 0.241843f, 0.383103f, 0.241843f, 0.060626f, 0.00598f};
        // horizontal blur, transpose result
        for (int y = 0; y < height; y++) {
            for (int x = 3; x < width - 3; x++) {
                float r = 0, g = 0, b = 0;
                for (int i = 0; i < 7; i++) {
                    int pixel = im.getRGB(x + i - 3, y);
                    b += (pixel & 0xFF) * k[i];
                    g += ((pixel >> 8) & 0xFF) * k[i];
                    r += ((pixel >> 16) & 0xFF) * k[i];
                }
                int p = (int)b + ((int)g << 8) + ((int)r << 16);
                // transpose result!
                temp.setRGB(y, x, p);
            }
        }
        return temp;
    }
}
