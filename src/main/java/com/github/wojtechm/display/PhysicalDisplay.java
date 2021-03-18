package com.github.wojtechm.display;

import com.diogonunes.jcolor.Attribute;
import com.github.wojtechm.map.Field;

import static com.diogonunes.jcolor.Ansi.colorize;

/**
 * @author Wojciech Makieła
 */
class PhysicalDisplay extends AbstractMapDisplay {
    @Override
    protected String asColoredString(Field field) {
        int masl = field.getMetersAboveSeaLevel();
        int redLimit = 8000;
        int yellowLimit = 4000;
        int greenLimit = 0;
        if (masl > redLimit) {
            return colorize("  ", Attribute.BACK_COLOR(255, 0, 0));
        } else if (masl > yellowLimit) {
            return colorize("  ", Attribute.BACK_COLOR(255, 255 - (int) ((masl - yellowLimit) * (255.f / yellowLimit)), 0));
        } else if (masl > greenLimit) {
            return colorize("  ", Attribute.BACK_COLOR((int) (masl * (255.f / yellowLimit)), 255, 0));
        } else {
            Attribute attribute = Attribute.BACK_COLOR(0, 255 - (int) (Math.abs(masl) * (255.f / 10_000)), 255);
            return colorize("  ", attribute);
        }
    }
}
