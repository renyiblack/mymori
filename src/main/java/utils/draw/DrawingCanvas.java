package utils.draw;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class DrawingCanvas extends JPanel {
    private static final long serialVersionUID = 1L;
    protected static final int PREF_W = 500;
    protected static final int PREF_H = 726;
    public static final Stroke IMG_STROKE = new BasicStroke(10f);
    public static Color ORANGE = new Color(216, 110, 58);

    public Color lineColor;
    protected BufferedImage image;

    public DrawingCanvas() {
        super.setBackground(Color.WHITE);

        lineColor = ORANGE;

        resetImage();

        Brush brush = new Brush(this);
        addMouseListener(brush);
        addMouseMotionListener(brush);
    }

    private void resetImage() {
        image = new BufferedImage(PREF_W, PREF_H, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        graphics.setPaint(new Color(255, 255, 255));
        graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
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
        resetImage();
        super.repaint();
    }

    public void save(String card) throws IOException {
        File f = new File(card + ".png");
        ImageIO.write(image, "PNG", f);
    }
}