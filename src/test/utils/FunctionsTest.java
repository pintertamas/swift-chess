package test.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import utils.Functions;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class FunctionsTest {
    Point pInside;
    Point pOutside;

    @BeforeEach
    void createDummyData() {
        setpInside(new Point(1, 8));
        setpOutside(new Point(0, 5));
    }

    @org.junit.jupiter.api.Test
    void getLocationOnX() {

    }

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