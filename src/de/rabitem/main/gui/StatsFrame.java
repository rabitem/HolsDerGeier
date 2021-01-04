package de.rabitem.main.gui;

import de.rabitem.main.HolsDerGeierUtil;
import de.rabitem.main.Main;
import de.rabitem.main.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

public class StatsFrame extends JPanel {

    private boolean calledInit = false;
    private boolean cancelTask = false;

    /**
     * Constructor of StatsFrame
     */
    public StatsFrame() {
        this.setDoubleBuffered(true);
        this.setBorder(BorderFactory.createEtchedBorder());
        this.setLayout(null);
        if (this.calledInit)
            return;
        this.calledInit = true;
    }

    /**
     * Init Components,called after starting game!
     */
    public void initComponents() {
        // wait for first game finish
        //TODO: fix
        /* JButton bClose = new JButton("Close");
        bClose.setPreferredSize(new Dimension(140, 40));
        bClose.setBounds(900 / 2 - bClose.getPreferredSize().width / 2, 500, bClose.getPreferredSize().width,
                bClose.getPreferredSize().height);
        this.add(bClose);

        bClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.getMain().getActionManager().getStatsPanel().setVisible(false);
                Main.getOptionsPanel().setVisible(true);
            }
        }); */

        JLabel lHeadline = new JLabel("Stats");
        lHeadline.setFont(new Font("Verdana", Font.BOLD, 46));
        lHeadline.setBounds(Main.getMain().getActionManager().getStatsFrame().getWidth()/2 - lHeadline.getPreferredSize().width/2, 40,
                lHeadline.getPreferredSize().width, lHeadline.getPreferredSize().height);
        this.add(lHeadline);

        while (!Main.getGameThreadRunnable().isFirstGameRun()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < HolsDerGeierUtil.getActivePlayers().size(); i++) {
            this.addBar(new JProgressBar(0, 100), HolsDerGeierUtil.getActivePlayers().get(i).getName());
        }

        final Component[] components = this.getComponents();
        final Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                for (Component component : components) {
                    if (component instanceof JProgressBar) {
                        for (Player p : HolsDerGeierUtil.getActivePlayers()) {
                            JProgressBar bar = (JProgressBar) component;
                            if (bar.getString().equalsIgnoreCase(p.getName())) {
                                double newValue = (double) p.getWinCounter() / (double) (Integer.parseInt(Main.getOptionsPanel().getTfRounds().getValue().toString()) * Main.getMain().getActionManager().gamesPlayed) * 100;
                                bar.setValue((int) newValue);
                                bar.setToolTipText("Player " + p.getName() + " won " + p.getWinCounter() + " rounds!");
                            }
                        }
                    }
                }
                if (cancelTask) {
                    for (Component component : components) {
                        if (component instanceof JProgressBar) {
                            for (Player p : HolsDerGeierUtil.getActivePlayers()) {
                                JProgressBar bar = (JProgressBar) component;
                                if (bar.getString().equalsIgnoreCase(p.getName())) {
                                    if (Main.getMain().getStatsManager().getTotalFirstPlace().equals(p)) {
                                        bar.setBackground(Color.GREEN);
                                    } else {
                                        bar.setBackground(Color.RED);
                                    }
                                }
                            }
                        }
                    }
                    this.cancel();
                    return;
                }
            }
        }, 500, 20);
        JButton bClose = new JButton("Close");
        bClose.setPreferredSize(new Dimension(120, 30));
        bClose.setUI(new StyledButtonUI());
        bClose.setBounds(Main.getMain().getActionManager().getStatsFrame().getWidth()/2 - bClose.getPreferredSize().width/2, Main.getMain().getActionManager().getStatsFrame().getHeight() - 70,
                bClose.getPreferredSize().width, bClose.getPreferredSize().height);
        this.add(bClose);

        bClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.getMain().getActionManager().getStatsPanel().removeAll();
                Main.getMain().getActionManager().getStatsFrame().setVisible(false);
                Main.getMainMenuFrame().setVisible(true);
                Main.getGameThread().interrupt();
                //TODO kill thread
            }
        });
    }

    private int cbsInRow = 0;
    private int y = 140;
    private int x = 0;

    public void addBar(final JProgressBar progressBar, final String text) {
        cbsInRow++;
        if (cbsInRow > 2) {
            cbsInRow = 1;
            y += 70;
        }
        x = cbsInRow * 250;

        progressBar.setString(text);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setOpaque(true);
        progressBar.setBounds(x, y, progressBar.getPreferredSize().width, progressBar.getPreferredSize().height);
        this.add(progressBar);
    }

    /**
     * Override this method to paint the background image
     *
     * @param g Graphics g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        final String url = "/resources/mainframe/background_MainFrame.jpg";
        GUIUtil.drawImageToBackground(this, g, url);
    }

    /**
     * Sets if the Thread needs to be canceled
     *
     * @param cancelTask boolean cancelTask
     */
    public void setCancelTask(boolean cancelTask) {
        this.cancelTask = cancelTask;
    }
}
