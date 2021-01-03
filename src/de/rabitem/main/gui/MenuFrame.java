package de.rabitem.main.gui;

import de.rabitem.main.Main;

import javax.swing.*;
import java.awt.*;

/**
 * @author Felix Huisinga
 */
public class MenuFrame extends JPanel {
    private String text = "Hols der Geier";
    private int charIndex = 0;

    private JLabel lHSpacer = new JLabel("SPACER");
    private JLabel lDesignBy = new JLabel("design by rabitem");
    private JLabel lHeadline = new JLabel(text);
    private JButton bStartGame = null;
    private JButton bOptions = null;
    private JButton bClose = null;

    /**
     * Constructor of MenuFrame
     */
    public MenuFrame() {
        super();
        /**
         * Setup frame
         */
        // faster drawings (2-Layer drawings)
        this.setDoubleBuffered(true);
        this.setLayout(new GridBagLayout());
        this.setBorder(BorderFactory.createEtchedBorder());

        // Layout
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;

        // init components
        bStartGame = new JButton("Start");
        bOptions = new JButton("Options");
        bClose = new JButton("Close");

        bStartGame.setPreferredSize(new Dimension(180, 30));
        bOptions.setPreferredSize(new Dimension(180, 30));
        bClose.setPreferredSize(new Dimension(180, 30));

        bStartGame.setBackground(new Color(0x2dce98));
        bStartGame.setForeground(Color.white);
        bStartGame.setUI(new StyledButtonUI());
        bOptions.setBackground(new Color(0x2dce98));
        bOptions.setForeground(Color.white);
        bOptions.setUI(new StyledButtonUI());
        bClose.setBackground(new Color(0x2dce98));
        bClose.setForeground(Color.white);
        bClose.setUI(new StyledButtonUI());

        lHeadline.setFont(new Font("Verdana", Font.BOLD, 46));
        lHeadline.setForeground(Color.darkGray);
        lHeadline.setPreferredSize(new Dimension(370, 150));

        // add components to the panel
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        this.add(bStartGame, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        this.add(bOptions, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        this.add(bClose, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        this.add(lDesignBy, constraints);

        constraints.gridx = 0;
        constraints.gridy = 9;
        constraints.gridwidth = 3;
        constraints.anchor = GridBagConstraints.CENTER;
        this.add(lHeadline);

        // Action Listener
        bClose.addActionListener(e -> {
            //TODO: safe stats
            System.exit(0);
        });

        bStartGame.addActionListener(e -> Main.getMain().getActionManager().bStartAction());

        bOptions.addActionListener(e -> Main.getMain().getActionManager().bOptionsAction());
    }

    /**
     * Override this method to paint the background image
     *
     * @param g Graphics g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        final String url = "/resources/menuframe/background_MenuFrame.jpg";
        GUIUtil.drawImageToBackground(this, g, url);
    }
}
