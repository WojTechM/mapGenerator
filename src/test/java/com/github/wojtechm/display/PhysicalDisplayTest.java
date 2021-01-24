package com.github.wojtechm.display;

import com.github.wojtechm.Field;
import com.github.wojtechm.GeneratedMap;
import com.github.wojtechm.Point;
import org.junit.jupiter.api.Test;

class PhysicalDisplayTest {

    @Test
    public void should__when_() {
        GeneratedMap map = new GeneratedMap(10, 10);
        for (int y = map.getHeight() - 1; y >= 0; y--) {
            for (int x = 0; x < map.getWidth(); x++) {
                Field field = map.fieldAtPosition(new Point(x, y)).get();
                int metersAboveSeaLevel = 10_000 - (y * 1000) - (x * 777);
                System.out.println(metersAboveSeaLevel);
                field.setMetersAboveSeaLevel(metersAboveSeaLevel);
            }
        }
        DisplayFactory.physicalDisplay().print(map);
    }
}