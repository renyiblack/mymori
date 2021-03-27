package utils.draw;

import javax.swing.*;
import java.awt.*;

public class Drawing extends JPanel {
    private static final long serialVersionUID = 1L;
    public Drawing() {
        DrawingCanvas drawingCanvas = new DrawingCanvas();

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        btnPanel.add(new JButton(new Button(Button.Buttons.BLACK, drawingCanvas)));
        btnPanel.add(new JButton(new Button(Button.Buttons.BLUE, drawingCanvas)));
        btnPanel.add(new JButton(new Button(Button.Buttons.ORANGE, drawingCanvas)));
        btnPanel.add(new JButton(new Button(Button.Buttons.RED, drawingCanvas)));
        btnPanel.add(new JButton(new Button(Button.Buttons.SAVE_QUESTION, drawingCanvas)));
        btnPanel.add(new JButton(new Button(Button.Buttons.SAVE_ANSWER, drawingCanvas)));
        btnPanel.add(new JButton(new Button(Button.Buttons.CLEAR, drawingCanvas)));

        setLayout(new BorderLayout());
        add(drawingCanvas, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.PAGE_END);
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