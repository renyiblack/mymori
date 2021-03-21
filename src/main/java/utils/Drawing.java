package utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Drawing extends JPanel {
    private final DrawingCanvas drawingCanvas = new DrawingCanvas();

    public Drawing() {
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        btnPanel.add(new JButton(new ChangeColorBlack()));
        btnPanel.add(new JButton(new ChangeColorBlue()));
        btnPanel.add(new JButton(new ChangeColorOrange()));
        btnPanel.add(new JButton(new ChangeColorRed()));
        btnPanel.add(new JButton(new ClearAction()));
        btnPanel.add(new JButton(new SaveAction()));

        setLayout(new BorderLayout());
        add(drawingCanvas, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.PAGE_END);
    }

    private class SaveAction extends AbstractAction {
        public SaveAction() {
            super("Save");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            drawingCanvas.save();
        }
    }

    private class ClearAction extends AbstractAction {
        public ClearAction() {
            super("Clear");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            drawingCanvas.clear();
        }
    }

    private class ChangeColorBlue extends AbstractAction {
        public ChangeColorBlue() {
            super("Blue");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            drawingCanvas.blue();
        }
    }

    private class ChangeColorRed extends AbstractAction {
        public ChangeColorRed() {
            super("Red");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            drawingCanvas.red();
        }
    }

    private class ChangeColorOrange extends AbstractAction {
        public ChangeColorOrange() {
            super("Orange");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            drawingCanvas.orange();
        }
    }

    private class ChangeColorBlack extends AbstractAction {
        public ChangeColorBlack() {
            super("Black");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            drawingCanvas.black();
        }
    }

    private static void createAndShowGui() {
        Drawing mainPanel = new Drawing();

        JFrame frame = new JFrame("Drawing Card");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }

    public void draw() {
        SwingUtilities.invokeLater(Drawing::createAndShowGui);
    }
}