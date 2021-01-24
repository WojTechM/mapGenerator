package com.github.wojtechm.display;

import com.github.wojtechm.GeneratedMap;
import com.github.wojtechm.Point;
import com.github.wojtechm.TerrainType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StandardDisplayTest {

    @Test
    public void should__when_() {
        GeneratedMap generatedMap = new GeneratedMap(15, 10);
        for (int i = 0; i < TerrainType.values().length; i++) {
            for (int j = 0; j < generatedMap.getWidth(); j++) {
                generatedMap.fieldAtPosition(new Point(j,i)).get().setTerrainType(TerrainType.values()[i]);
            }
        }
        DisplayFactory.standardDisplay().print(generatedMap);
    }
}