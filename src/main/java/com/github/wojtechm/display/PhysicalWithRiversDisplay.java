package com.github.wojtechm.display;

import com.diogonunes.jcolor.Attribute;
import com.github.wojtechm.Field;
import com.github.wojtechm.TerrainType;

import static com.diogonunes.jcolor.Ansi.colorize;

/**
 * @author Wojciech Makieła
 */
class PhysicalWithRiversDisplay extends PhysicalDisplay {

    @Override
    protected String asColoredString(Field field) {
        if (field.getTerrainType() == TerrainType.RIVER) {
            return colorize("  ", Attribute.BACK_COLOR(120, 140, 255));
        }
        return super.asColoredString(field);
    }
}
