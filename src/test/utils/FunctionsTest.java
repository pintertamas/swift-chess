package test.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import utils.Functions;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class FunctionsTest {
    Point pInside;
    Point pOutside;

    /**
     * Minden teszt eset előtt lefut ez a kód
     */
    @BeforeEach
    void createDummyData() {
        setpInside(new Point(1, 8));
        setpOutside(new Point(0, 5));
    }

    /**
     * Leteszteli hogy jó koordinátákat ad-e vissza a program pixel és képernyő szélességet/magasságot megadva
     */
    @org.junit.jupiter.api.Test
    void getLocationOnXAndY() {
        assertEquals(1, Functions.getLocationOnX(50, 640));
        assertEquals(6, Functions.getLocationOnX(420, 640));
        assertEquals(8, Functions.getLocationOnX(623, 640));
        assertEquals(1, Functions.getLocationOnY(10, 640));
        assertEquals(5, Functions.getLocationOnY(369, 640));
        assertEquals(7, Functions.getLocationOnY(555, 640));
    }

    /**
     * Leteszteli hogy jól állapítja-e meg a program hogy adott pont kívülre esik a tábláról
     */
    @org.junit.jupiter.api.Test
    void isOutside() {
        Assertions.assertFalse(Functions.isOutside(getpInside()));
        assertTrue(Functions.isOutside(getpOutside()));
    }

    public Point getpInside() {
        return pInside;
    }

    public Point getpOutside() {
        return pOutside;
    }

    public void setpInside(Point pInside) {
        this.pInside = pInside;
    }

    public void setpOutside(Point pOutside) {
        this.pOutside = pOutside;
    }
}