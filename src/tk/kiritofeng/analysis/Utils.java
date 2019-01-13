package tk.kiritofeng.analysis;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Utils {
    public static boolean isSimilar(Color C1, Color C2, int tol) {
        if(Math.abs(C1.getRed() - C2.getRed()) > tol) return false;
        if(Math.abs(C1.getGreen() - C2.getGreen()) > tol) return false;
        if(Math.abs(C1.getBlue() - C2.getBlue()) > tol) return false;
        return true;
    }

    public static double getIntensity(Color c1) {
        // TODO: use actual method
        float[]hsv = new float[3];
        Color.RGBtoHSB(c1.getRed(),c1.getGreen(),c1.getBlue(),hsv);
        return hsv[2];
    }

    public static BufferedImage openImage(File F) {
        try {
            BufferedImage im = ImageIO.read(F);
            return im;
        } catch (IOException e) {
            return null;
        }
    }

    public static boolean isRed(Color C) {
        return C.getRed() > 60;
    }

}
