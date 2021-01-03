package de.rabitem.main.gui;

import de.rabitem.main.Main;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;

public class OptionFrame extends JPanel {
    final private JLabel headline = new JLabel("<html><u style=\"color:white\">Options</u></html>");
    private JLabel emptyLabel = new JLabel("");
    private JComboBox cbPlayer2 = null;
    private JLabel lRounds = null;
    private JLabel lCardsFrom = null;
    private JLabel lCardsTo = null;
    private JButton bAddPlayer = null;
    private JButton bRemPlayer = null;
    private JButton bClose = null;
    private JFormattedTextField tfRounds = null;
    private JFormattedTextField tfCardsFrom = null;
    private JFormattedTextField tfCardsTo = null;

    public int startPlayerCount = 2;
    public int additionalPlayerCount = 0;

    /**
     * Constructor of StatsFrame
     */
    public OptionFrame() {
        this.setLayout(null);
        this.setDoubleBuffered(true);
        this.setBorder(BorderFactory.createEtchedBorder());

        bAddPlayer = new JButton("<html><b><span style=\"color:green; font-size:13px\">+</span> Player</b></html>");
        bRemPlayer = new JButton("<html><b><span style=\"color:red; font-size:13px\">-</span> Player</b></html>");
        lRounds = new JLabel("<html><b style=\"color:white; font-size:13px\">Rounds: </b></html>");
        lCardsFrom = new JLabel("<html><b style=\"color:white; font-size:13px\">Cards from:</b></html>");
        lCardsTo = new JLabel("<html><b style=\"color:white; font-size:13px\">to:</b></html>", SwingConstants.RIGHT);
        bClose = new JButton("<html><b><span style=\"color:red; font-size:13px\">×</span> Close</b></html>");

        final NumberFormat format = NumberFormat.getInstance();
        final NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        // If you want the value to be committed on each keystroke instead of focus lost
        formatter.setCommitsOnValidEdit(true);

        tfRounds = new JFormattedTextField(formatter);
        tfRounds.setPreferredSize(new Dimension(80, 40));
        tfRounds.setValue(1);
        tfRounds.setEditable(true);


        tfCardsFrom = new JFormattedTextField(formatter);
        tfCardsFrom.setValue(1);
        tfCardsFrom.setEditable(true);
        tfCardsTo = new JFormattedTextField(formatter);
        tfCardsTo.setValue(15);
        tfCardsTo.setEditable(true);

        bClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.getOptionsFrame().setVisible(false);
            }
        });

        bAddPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (additionalPlayerCount < 8) { // max. 10 Players
                    if (Main.getMain().getActionManager().gamesPlayed > 0) {
                        JOptionPane.showMessageDialog(null, "You can not add Player because you already played!", "Error while adding Player", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    additionalPlayerCount++;
                    Main.getMain().getOptionsPanel().addComboBox(Main.getMain().getOptionsPanel().createComboBox(Main.enabledPlayers), "Player " + (additionalPlayerCount + startPlayerCount) + ":");
                } else
                    JOptionPane.showMessageDialog(null, "Too many players, a maximum of 10 Players is allowed!", "Error, too many players!", JOptionPane.ERROR_MESSAGE);
            }
        });

        bRemPlayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (additionalPlayerCount > 0) {
                    if (Main.getMain().getActionManager().gamesPlayed > 0) {
                        JOptionPane.showMessageDialog(null, "You can not remove Player because you already played!", "Error while removing Player", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    additionalPlayerCount--;
                    final OptionFrame optionPanel = Main.getMain().getOptionsPanel();
                    optionPanel.remove(optionPanel.getComboBoxes().get(optionPanel.getComboBoxes().size() - 1));
                    optionPanel.remove(optionPanel.getComponents()[optionPanel.getComponents().length - 1]);
                    cbsInRow--;
                    if (cbsInRow < 1 && additionalPlayerCount > 0) {
                        y -= 70;
                        cbsInRow = 2;
                    }
                    optionPanel.repaint();
                }
            }
        });

        bRemPlayer.setBounds(50, 300,
                bRemPlayer.getPreferredSize().width + 25, bRemPlayer.getPreferredSize().height);
        bRemPlayer.setUI(new StyledButtonUI());
        bRemPlayer.setBackground(new Color(0x2dce98));
        bRemPlayer.setForeground(Color.white);
        this.add(bRemPlayer);

        bAddPlayer.setBounds(50, 250,
                bRemPlayer.getWidth(), bAddPlayer.getPreferredSize().height);
        bAddPlayer.setUI(new StyledButtonUI());
        bAddPlayer.setBackground(new Color(0x2dce98));
        bAddPlayer.setForeground(Color.white);
        this.add(bAddPlayer);

        lCardsFrom.setBounds(175, 125,
                lCardsFrom.getPreferredSize().width + 10, bAddPlayer.getPreferredSize().height);
        this.add(lCardsFrom);

        tfCardsFrom.setBounds(10 + lCardsFrom.getX() + lCardsFrom.getWidth(), lCardsFrom.getY(),
                tfCardsTo.getPreferredSize().width + 20, bAddPlayer.getPreferredSize().height);
        this.add(tfCardsFrom);


        lCardsTo.setBounds(75 + tfCardsFrom.getX(), lCardsFrom.getY(),
                lCardsTo.getPreferredSize().width, bAddPlayer.getPreferredSize().height);
        this.add(lCardsTo);

        tfCardsTo.setBounds(10 + lCardsTo.getX() + lCardsTo.getWidth(), lCardsFrom.getY(),
                tfCardsTo.getPreferredSize().width + 20, bRemPlayer.getPreferredSize().height);
        this.add(tfCardsTo);

        lRounds.setBounds(140 + lCardsTo.getX(), lCardsFrom.getY(),
                lRounds.getPreferredSize().width, bAddPlayer.getPreferredSize().height);
        this.add(lRounds);

        tfRounds.setBounds(10 + lRounds.getX() + lRounds.getWidth(), lRounds.getY(),
                tfRounds.getPreferredSize().width, bAddPlayer.getPreferredSize().height);
        this.add(tfRounds);

        bClose.setBounds(Main.getMain().framesWidth - (bClose.getPreferredSize().width + 25) - 35, Main.getMain().framesHeight - 60, bClose.getPreferredSize().width + 25, bClose.getPreferredSize().height);
        bClose.setUI(new StyledButtonUI());
        bClose.setBackground(new Color(0x2dce98));
        bClose.setForeground(Color.white);
        this.add(bClose);

        headline.setFont(new Font(headline.getFont().getFontName(), Font.BOLD, 40));
        headline.setBounds(Main.getMain().framesWidth / 2 - headline.getPreferredSize().width / 2, 50, headline.getPreferredSize().width, headline.getPreferredSize().height);
        this.add(headline);

        final String[] labelTexts = {"Player 1:", "Player 2:"};
        this.addComboBox(this.createComboBox(Main.enabledPlayers), labelTexts[0]);
        this.addComboBox(this.createComboBox(Main.enabledPlayers), labelTexts[1]);
    }

    public JComboBox<String> createComboBox(final ArrayList<String> fulfillment) {
        final JComboBox<String> comboBox = new JComboBox<>();
        for (String bot : fulfillment) {
            comboBox.addItem(bot);
        }
        return comboBox;
    }

    private int y = 250;
    private int x = 0;
    private int cbsInRow = 0;

    public void addComboBox(final JComboBox<String> comboBox, final String labelText) {
        cbsInRow++;
        if (cbsInRow > 2) {
            cbsInRow = 1;
            y += 70;
        }
        x = lCardsFrom.getX() + (cbsInRow - 1) * (250);

        final JLabel jLabel = new JLabel("<html><b style=\"color:white; font-size:13px\">" +
                labelText +
                "</b></html>");
        jLabel.setBounds(x, y,
                85, bAddPlayer.getPreferredSize().height);
        this.add(jLabel);

        comboBox.setBackground(new Color(0x2dce98));
        comboBox.setForeground(Color.white);
        // comboBox.setUI(new StyledComboBoxUI());
        comboBox.setBounds(x + 5 + jLabel.getWidth(), y,
                comboBox.getPreferredSize().width + 10, bAddPlayer.getPreferredSize().height);
        this.add(comboBox);
        this.repaint();
    }

    /**
     * Override this method to paint the background image
     *
     * @param g Graphics g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        final String url = "/resources/optionsframe/background_OptionsFrame.jpg";
        GUIUtil.drawImageToBackground(this, g, url);
    }

    public JFormattedTextField getTfRounds() {
        return tfRounds;
    }

    /**
     * Returns an ArrayList of JComboBoxes in the OptionsPanel
     *
     * @return ArrayList<JComboBox>
     */
    public ArrayList<JComboBox<String>> getComboBoxes() {
        ArrayList<JComboBox<String>> comboBoxes = new ArrayList<>();
        for (Component component : this.getComponents()) {
            if (component instanceof JComboBox) {
                comboBoxes.add((JComboBox<String>) component);
            }
        }
        return comboBoxes;
    }

    public int getTfCardsFrom() {
        return Integer.parseInt(tfCardsFrom.getValue().toString());
    }

    public int getTfCardsTo() {
        return Integer.parseInt(tfCardsTo.getValue().toString());
    }
}
