package tk.kiritofeng.analysis;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImagePane extends JPanel {

    private BufferedImage img;

    private ArrayList<Point>points;

    public ImagePane() {
        super();
        points = new ArrayList<>();
    }

    public void colourPoint(Point P) {
        points.add(P);
        repaint();
    }

    public void clearPoints() {
        points.clear();
    }

    public void setImg(BufferedImage newImg) {
        img = newImg;
        repaint();
    }

    public BufferedImage getImg() {
        return img;
    }

    public void paintComponent(Graphics G) {
        if(img == null) {
            G.setColor(Color.BLACK);
            G.fillRect(0, 0, getWidth(), getHeight());
        } else {
            Image scaledImage = img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
            G.drawImage(scaledImage, 0, 0,getWidth(), getHeight(), this);
        }
    }
}
