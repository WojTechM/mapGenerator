package com.github.wojtechm.display;

/**
 * @author Wojciech Makieła
 */
public class DisplayFactory {

    public static Display standardDisplay() {
        return new StandardDisplay();
    }

    public static Display physicalDisplay() {
        return new PhysicalDisplay();
    }
    public static Display physicalWithRiversDisplay() {
        return new PhysicalWithRiversDisplay();
    }
}

