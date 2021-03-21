package utils.draw;

import utils.Strings;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;

public class Button extends AbstractAction {
    public enum Buttons {
        BLACK,
        ORANGE,
        BLUE,
        RED,
        SAVE_QUESTION,
        SAVE_ANSWER,
        CLEAR
    }

    private enum Functions {
        COLOR,
        SAVE_QUESTION,
        SAVE_ANSWER,
        CLEAR
    }

    private final DrawingCanvas drawingCanvas;
    private final Functions function;
    private final Color lineColor;

    public Button(Buttons buttonAction, DrawingCanvas drawingCanvas) {
        this.drawingCanvas = drawingCanvas;

        switch (buttonAction) {
            case BLACK:
                super.putValue(Action.NAME, "black");
                lineColor = Color.BLACK;
                function = Functions.COLOR;
                break;
            case BLUE:
                super.putValue(Action.NAME, "blue");
                lineColor = Color.BLUE;
                function = Functions.COLOR;
                break;
            case ORANGE:
                super.putValue(Action.NAME, "orange");
                lineColor = DrawingCanvas.ORANGE;
                function = Functions.COLOR;
                break;
            case RED:
                super.putValue(Action.NAME, "red");
                lineColor = Color.RED;
                function = Functions.COLOR;
                break;
            case SAVE_QUESTION:
                super.putValue(Action.NAME, "save Q.");
                lineColor = DrawingCanvas.ORANGE;
                function = Functions.SAVE_QUESTION;
                break;
            case SAVE_ANSWER:
                super.putValue(Action.NAME, "save A.");
                lineColor = DrawingCanvas.ORANGE;
                function = Functions.SAVE_ANSWER;
                break;
            case CLEAR:
                super.putValue(Action.NAME, "clear");
                lineColor = DrawingCanvas.ORANGE;
                function = Functions.CLEAR;
                break;
            default:
                lineColor = DrawingCanvas.ORANGE;
                function = Functions.COLOR;
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (function) {
            case COLOR:
                drawingCanvas.lineColor = lineColor;
            case SAVE_QUESTION:
                try {
                    drawingCanvas.save("question");
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(drawingCanvas, Strings.ERROR_SAVE_CARD);
                }
                break;
            case SAVE_ANSWER:
                try {
                    drawingCanvas.save("answer");
                } catch (IOException ioException) {
                    JOptionPane.showMessageDialog(drawingCanvas, Strings.ERROR_SAVE_CARD);
                }
                break;
            case CLEAR:
                drawingCanvas.clear();
                break;
            default:
                break;
        }
    }
}
