package de.rabitem.main.gui;

import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;

class StyledComboBoxUI extends BasicComboBoxUI {

    protected JButton createArrowButton() {
        JButton button = new BasicArrowButton(BasicArrowButton.SOUTH);
        return button;
    }
}