package tk.kiritofeng.analysis;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GUI extends JFrame {

    ImagePane img;
    JFileChooser imgFile, outputFile;
    JColorChooser colorChooser;
    Color col;
    private void init() {
        img = new ImagePane();
        imgFile = new JFileChooser();
        col = Color.RED;
    }

    public void analyze() {
        BufferedImage toAnalyze = img.getImg();
        for(int i = 0; i < img.getHeight(); ++i) {
            for(int j = 0; j < img.getWidth(); ++j) {
                int rgb = toAnalyze.getRGB(i, j);
                int r = rgb >> 16;
                int g = (rgb >> 8) & 255;
                int b = rgb & 255;
                
            }
        }
    }
}
