package tk.kiritofeng.analysis;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePane extends JFrame {

    private BufferedImage img;

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
            G.drawImage(scaledImage, 0, 0,getWidth(), getHeight(), null);
        }
    }
}
