package utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

class DrawingCanvas extends JPanel {
    private static final int PREF_W = 500;
    private static final int PREF_H = 726;
    public static Color LINE_COLOR = Color.ORANGE;
    public static final Stroke IMG_STROKE = new BasicStroke(10f);
    private Color lineColor = LINE_COLOR;
    private BufferedImage image;

    public DrawingCanvas() {
        setBackground(Color.WHITE);
        image = new BufferedImage(PREF_W, PREF_H, BufferedImage.TYPE_INT_ARGB);
        MyMouse myMouse = new MyMouse();
        addMouseListener(myMouse);
        addMouseMotionListener(myMouse);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }
        return new Dimension(PREF_W, PREF_H);
    }

    public void clear() {
        image = new BufferedImage(PREF_W, PREF_H, BufferedImage.TYPE_INT_ARGB);
        repaint();
    }

    public void save() {
        BufferedImage img = image;
        File f = new File("tmpFile.png");
        try {
            ImageIO.write(img, "PNG", f);
        } catch (Exception e) {
            System.out.println("Falha ao salvar");
        }
        setVisible(false); //you can't see me!
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    public void black() {
        LINE_COLOR = Color.BLACK;
        lineColor = LINE_COLOR;
    }

    public void blue() {
        LINE_COLOR = Color.BLUE;
        lineColor = LINE_COLOR;
    }

    public void orange() {
        LINE_COLOR = Color.ORANGE;
        lineColor = LINE_COLOR;
    }

    public void red() {
        LINE_COLOR = Color.RED;
        lineColor = LINE_COLOR;
    }

    private class MyMouse extends MouseAdapter {
        private Graphics2D imgG2d;
        private Point p1;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() != MouseEvent.BUTTON1) {
                return;
            }

            imgG2d = image.createGraphics();
            imgG2d.setColor(lineColor);
            imgG2d.setStroke(IMG_STROKE);
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
            repaint();
        }
    }
}