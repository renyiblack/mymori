package utils.draw;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class Brush extends MouseAdapter {
    private final DrawingCanvas drawingCanvas;
    private Graphics2D imgG2d;
    private Point p1;

    public Brush(DrawingCanvas drawingCanvas) {
        this.drawingCanvas = drawingCanvas;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() != MouseEvent.BUTTON1) {
            return;
        }

        imgG2d = drawingCanvas.image.createGraphics();
        imgG2d.setColor(drawingCanvas.lineColor);
        imgG2d.setStroke(DrawingCanvas.IMG_STROKE);
        p1 = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        drawLine(e);
        imgG2d.dispose();
        p1 = null;
        imgG2d = null;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        drawLine(e);
        p1 = e.getPoint();
    }

    private void drawLine(MouseEvent e) {
        if (imgG2d == null || p1 == null) {
            return;
        }
        Point p2 = e.getPoint();
        int x1 = p1.x;
        int y1 = p1.y;
        int x2 = p2.x;
        int y2 = p2.y;
        imgG2d.drawLine(x1, y1, x2, y2);
        drawingCanvas.repaint();
    }
}
