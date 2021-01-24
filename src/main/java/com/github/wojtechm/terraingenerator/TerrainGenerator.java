package com.github.wojtechm.terraingenerator;

import com.github.wojtechm.Field;
import com.github.wojtechm.GeneratedMap;
import com.github.wojtechm.Point;
import com.github.wojtechm.TerrainType;

import java.util.Optional;

/**
 * @author Wojciech Makieła
 */
public class TerrainGenerator {
    private final int ICE_LEVEL = 3000;
    private GeneratedMap terrainMap;

    public void generate(GeneratedMap map) {
        terrainMap = map;
        fillWithIce();
        fillWithDesert();
        fillWithForest();
    }

    private void fillWithForest() {

    }

    private void fillWithDesert() {

    }

    private void fillWithIce() {
        for (int y = 0; y < terrainMap.getHeight(); y++) {
            for (int x = 0; x < terrainMap.getWidth(); x++) {
                Optional<Field> optionalField = terrainMap.fieldAtPosition(new Point(x, y));
                optionalField.ifPresent((field) -> {
                    if (field.getMetersAboveSeaLevel() >= ICE_LEVEL)
                        field.setTerrainType(TerrainType.ICE);
                });
            }
        }
    }

}
