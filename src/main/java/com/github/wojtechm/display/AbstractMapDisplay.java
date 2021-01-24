package com.github.wojtechm.display;

import com.github.wojtechm.Field;
import com.github.wojtechm.GeneratedMap;
import com.github.wojtechm.Point;

/**
 * @author Wojciech Makieła
 */
abstract class AbstractMapDisplay implements Display{

    @Override
    public void print(GeneratedMap map) {
        for (int y = map.getHeight() - 1; y >= 0; y--) {
            System.out.println();
            for (int x = 0; x < map.getWidth(); x++) {
                Field field = map.fieldAtPosition(new Point(x, y)).get();
                System.out.print(asColoredString(field));
            }
        }
    }

    protected abstract String asColoredString(Field field);
}
