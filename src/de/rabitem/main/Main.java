package de.rabitem.main;

import de.rabitem.main.gui.ActionManager;
import de.rabitem.main.gui.GUIUtil;
import de.rabitem.main.gui.MenuFrame;
import de.rabitem.main.gui.OptionFrame;
import de.rabitem.main.player.PlayerManager;
import de.rabitem.main.util.GameThread;
import de.rabitem.main.util.StatsManager;
import de.rabitem.main.util.Util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @author Felix Huisinga
 */
public class Main extends HolsDerGeier {

    private static PlayerManager playerManager;
    private static Main main;
    private static HolsDerGeierUtil holsDerGeierUtil;
    private static Thread t1;

    private static JFrame mainMenuFrame = null;
    private static JFrame optionsFrame = null;

    private static GameThread gameThreadRunnable = null;

    public final ActionManager actionManager = new ActionManager();

    private StatsManager statsManager = null;

    public static ArrayList<String> enabledPlayers = new ArrayList<>();

    /**
     * Main method, starting point of the program!
     *
     * @param args String[] args
     */
    public static void main(String[] args) {
        playerManager = new PlayerManager();
        main = new Main();
        main.init();
    }

    /**
     * Returns the PlayerManager
     *
     * @return playerManager
     */
    public static PlayerManager getPlayerManager() {
        return playerManager;
    }

    /**
     * This method is used to initialize the entire framework
     */
    public void init() {
        if (!Util.hasReadCopyrightClaim()) {
            final JLabel message = new JLabel("This project uses images that might be protected by copyright. Herewith I confirm not to abuse them and to use them only in my own sense! \n" +
                    "\n" +
                    "This confirmation will be saved for future use!");
            JOptionPane.showMessageDialog(null, message, "Copyright claims", JOptionPane.INFORMATION_MESSAGE);
            Util.acceptCopyrightClaim();
        }

        File[] files;
        try {
            //If this pathname does not denote a directory, then listFiles() returns null.
            files = new File(Main.class.getResource("/de/rabitem/main/player/instances").toURI()).listFiles();
            for (File file : files) {
                if (file.isFile()) {
                    enabledPlayers.add(file.getName().substring(0, file.getName().length() - 6));
                }
            }
        } catch (URISyntaxException | NullPointerException e) {
            System.out.println(e.getMessage());
        }
        enabledPlayers.sort(Comparator.reverseOrder());

        /**
         * Initialize utility methods
         */
        holsDerGeierUtil = new HolsDerGeierUtil();

        JEditorPane jep = new JEditorPane();
        jep.setEditable(false);

        gameThreadRunnable = new GameThread();
        this.setupThread();

        mainMenuFrame = this.setupFrame(new MenuFrame(), "/resources/iconFrames.png");

        optionsFrame = this.setupFrame(new OptionFrame(), "/resources/iconFrames.png");
        optionsFrame.setVisible(false);
    }

    /**
     * Automatically activates all players that are selected in the options
     */
    public static void activatePlayer() {
        int count = 1;
        for (JComboBox<String> comboBox : Main.getOptionsPanel().getComboBoxes()) {
            Object item = comboBox.getSelectedItem();
            if (item != null && enabledPlayers.contains(item.toString())) {
                try {
                    holsDerGeierUtil.activatePlayer(holsDerGeierUtil.getPlayerInstance(item.toString(), "Player " + count));
                } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                    System.out.println("Failed to create a new instance of " + item.toString() + ":");
                    e.printStackTrace();
                }
            }
            count++;
        }
    }

    public void setupThread() {
        t1 = new Thread(gameThreadRunnable);
    }

    /**
     * This method is used to setup the menu-frame
     */
    public int framesWidth = 900;
    public int framesHeight = 600;

    public JFrame setupFrame(final JPanel jPanel, final String backgroundURL) {
        GUIUtil.setUIManager();
        final JFrame frame = new JFrame();
        frame.setContentPane(jPanel);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(this.framesWidth, this.framesHeight);
        frame.setUndecorated(true);
        frame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        try {
            frame.setIconImage(ImageIO.read(Main.class.getResourceAsStream(backgroundURL)));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return frame;
    }

    /**
     * Returns the HolsDerGeierUtil instance
     *
     * @return HolsDerGeierUtil
     */
    public static HolsDerGeierUtil getHolsDerGeierUtil() {
        return holsDerGeierUtil;
    }

    /**
     * This method is used to return the Main Instance
     *
     * @return Main getMain
     */
    public static Main getMain() {
        return main;
    }

    /**
     * This method is used to return the GameThread
     *
     * @return GameThread getGameThread
     */
    public static Thread getGameThread() {
        return t1;
    }

    public static GameThread getGameThreadRunnable() {
        return gameThreadRunnable;
    }

    /**
     * This method is used to return the MainMenuFrame
     *
     * @return JFrame getMainMenuFrame
     */
    public static JFrame getMainMenuFrame() {
        return mainMenuFrame;
    }

    /**
     * This method is used to return the OptionsFrame
     *
     * @return JFrame OptionsFrame
     */
    public static JFrame getOptionsFrame() {
        return optionsFrame;
    }

    /**
     * This method is used to return the OptionsFrame JPanel
     *
     * @return OptionFrame getOptionsPanel
     */
    public static OptionFrame getOptionsPanel() {
        return (OptionFrame) optionsFrame.getContentPane();
    }

    /**
     * Returns the StatsManager
     *
     * @return StatsManager getStatsManager
     */
    public StatsManager getStatsManager() {
        return statsManager;
    }

    /**
     * Sets the Stats Manager
     *
     * @param statsManager
     */
    public void setStatsManager(StatsManager statsManager) {
        this.statsManager = statsManager;
    }

    @Override
    public void onSetupFinished() {

    }

    /**
     * This method is used to return the ActionManager
     *
     * @return
     */
    public ActionManager getActionManager() {
        return actionManager;
    }
}
