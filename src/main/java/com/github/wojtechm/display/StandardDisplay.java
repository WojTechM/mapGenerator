package com.github.wojtechm.display;

import com.diogonunes.jcolor.Attribute;
import com.github.wojtechm.map.Field;

import static com.diogonunes.jcolor.Ansi.colorize;

/**
 * @author Wojciech Makieła
 */
class StandardDisplay extends AbstractMapDisplay {

    protected String asColoredString(Field field) {
        switch (field.getTerrainType()) {
            case ICE:
                return colorize("  ", Attribute.WHITE_BACK());
            case RIVER:
                return colorize("  ", Attribute.BLUE_BACK());
            case LAKE:
                return colorize(" ~", Attribute.BRIGHT_BLACK_TEXT(), Attribute.BLUE_BACK());
            case OCEAN:
                return colorize("~~", Attribute.BRIGHT_BLACK_TEXT(), Attribute.TEXT_COLOR(20, 30, 125));
            case FOREST:
                return colorize("  ", Attribute.GREEN_BACK());
            case DESERT:
                return colorize("  ", Attribute.YELLOW_BACK());
            case GRASSLAND:
                return colorize("  ", Attribute.BRIGHT_GREEN_BACK());
            default:
                return colorize("  ", Attribute.BLACK_BACK());
        }
    }
}
