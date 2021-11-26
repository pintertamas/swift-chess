package utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.util.Objects;

public class Functions {
    public static ImageIcon getImage(String filename) {
        try {
            return new ImageIcon(ImageIO.read(Objects.requireNonNull(Functions.class.getResourceAsStream(
                    "/" + filename))));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getLocationOnX(int posX, int width) {
        return (int)Math.ceil((double)posX / width * 8);
    }

    public static int getLocationOnY(int posY, int height) {
        return (int)Math.ceil((double)posY / height * 8);
    }

    public static boolean isOutside(int newX, int newY) {
        return newX > 8 || newX <= 0 || newY > 8 || newY <= 0;
    }
}