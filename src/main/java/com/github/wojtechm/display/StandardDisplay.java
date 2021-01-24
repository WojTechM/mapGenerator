package com.github.wojtechm.display;

import com.diogonunes.jcolor.Attribute;
import com.github.wojtechm.Field;
import com.github.wojtechm.GeneratedMap;
import com.github.wojtechm.Point;

import java.util.Optional;

import static com.diogonunes.jcolor.Ansi.colorize;

/**
 * @author Wojciech Makieła
 */
class StandardDisplay implements Display {


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

    private String asColoredString(Field field) {
        switch (field.getTerrainType()) {
            case RIVER:return colorize("  ", Attribute.BLUE_BACK());
            case LAKE: return colorize(" ~", Attribute.BRIGHT_BLACK_TEXT(), Attribute.BLUE_BACK());
            case OCEAN: return colorize("~~", Attribute.BRIGHT_BLACK_TEXT(), Attribute.TEXT_COLOR(20, 30, 125));
            case FOREST: return colorize("  ", Attribute.GREEN_BACK());
            case DESERT: return colorize("  ", Attribute.YELLOW_BACK());
            case GRASSLAND: return colorize("  ", Attribute.BRIGHT_GREEN_BACK());
            default: return colorize("  ", Attribute.BLACK_BACK());
        }
    }

}
