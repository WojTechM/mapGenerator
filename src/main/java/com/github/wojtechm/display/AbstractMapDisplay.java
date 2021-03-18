package com.github.wojtechm.display;

import com.github.wojtechm.map.Field;
import com.github.wojtechm.map.GeneratedMap;
import com.github.wojtechm.map.Point;

/**
 * @author Wojciech Makieła
 */
abstract class AbstractMapDisplay implements Display{

    @Override
    public void print(GeneratedMap map) {
        StringBuilder sb = new StringBuilder();
        for (int y = map.getHeight() - 1; y >= 0; y--) {
            sb.append("\n");
            for (int x = 0; x < map.getWidth(); x++) {
                Field field = map.fieldAtPosition(new Point(x, y)).get();
                sb.append(asColoredString(field));
            }
        }
        System.out.println(sb.toString());
    }

    protected abstract String asColoredString(Field field);
}
