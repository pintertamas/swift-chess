package utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * Statikus osztály globálisan elérni kívánt funkciókhoz
 */
public class Functions {
    /**
     * Statikus függvény ami visszaad egy ImageIcon-t fájlnév alapján
     *
     * @param filename a fájlnév
     * @return a fájlnévnél található képből készített ImageIcon
     */
    public static ImageIcon getImage(String filename) {
        try {
            return new ImageIcon(ImageIO.read(Objects.requireNonNull(Functions.class.getResourceAsStream(
                    "/" + filename))));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Visszaadja a bábu horizontális pozícióját koordinátákkal
     *
     * @param posX  a bábu X pozíciója pixelben
     * @param width a sakktábla szélessége
     * @return hogy hányadik mezőben van a bábu az X tengelyen
     */
    public static int getLocationOnX(int posX, int width) {
        return (int) Math.ceil((double) posX / width * 8);
    }

    /**
     * Visszaadja a bábu verzikális pozícióját koordinátákkal
     *
     * @param posY   a bábu Y pozíciója pixelben
     * @param height a sakktábla magassága
     * @return hogy hányadik mezőben van a bábu az Y tengelyen
     */
    public static int getLocationOnY(int posY, int height) {
        return (int) Math.ceil((double) posY / height * 8);
    }

    /**
     * Visszaadja hogy egy pont a táblán kívül esik-e
     *
     * @param p a pont
     * @return kívül van-e a táblán
     */
    public static boolean isOutside(Point p) {
        return p.x > 8 || p.x <= 0 || p.y > 8 || p.y <= 0;
    }

    /**
     * Visszaadja hogy a két koordinátából készített pont kívül esik-e a sakktáblán
     *
     * @param newX az x pozíció
     * @param newY az y pozíció
     * @return kívül esik-e a pont a tábláról
     */
    public static boolean isOutside(int newX, int newY) {
        return isOutside(new Point(newX, newY));
    }

    /**
     * Visszaadja hogy kívül esik-e a tábláról egy koordináta
     *
     * @param coordinate a koordináta
     * @return kívül esik-e
     */
    public static boolean isOutside(int coordinate) {
        return coordinate > 8 || coordinate <= 0;
    }
}