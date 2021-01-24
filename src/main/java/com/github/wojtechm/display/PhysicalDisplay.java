package com.github.wojtechm.display;

import com.diogonunes.jcolor.Attribute;
import com.github.wojtechm.Field;

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
        int lightBlueLimit = -3000;
        if (masl > redLimit) {
            return colorize("  ", Attribute.BACK_COLOR(255, 0, 0));
        } else if (masl > yellowLimit) {
            return colorize("  ", Attribute.BACK_COLOR(255, (masl - yellowLimit) / 255, 0));
        } else if (masl > greenLimit) {
            return colorize("  ", Attribute.BACK_COLOR(255 - ((masl - greenLimit) / 255), 255, 0));
        } else if (masl > lightBlueLimit) {
            return colorize("  ", Attribute.BACK_COLOR(0, 255, 255));
        } else {
            return colorize("  ", Attribute.BACK_COLOR(0, 255 - Math.abs((masl - lightBlueLimit) / 255), 255));
        }
    }
}
