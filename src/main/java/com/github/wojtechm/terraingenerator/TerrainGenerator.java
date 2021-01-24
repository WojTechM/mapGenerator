package com.github.wojtechm.terraingenerator;

import com.github.wojtechm.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Wojciech Makieła
 */
public class TerrainGenerator {
    private final int ICE_LEVEL = 3000;
    private final int DESERT_MAX_LEVEL_DIFF = 500;
    private final int DESERT_MIN_AREA = 500;
    private GeneratedMap terrainMap;

    public void generate(GeneratedMap map) {
        terrainMap = map;
        fillWithIce();
        fillWithDesert();
        fillWithForest();
    }

    private int calculateAvgHeight() {
        int total = 0;
        for (int y = 0; y < terrainMap.getHeight(); y++) {
            for (int x = 0; x < terrainMap.getWidth(); x++) {
                Optional<Field> optionalField = terrainMap.fieldAtPosition(new Point(x, y));
                if (optionalField.isPresent()) {
                    total += optionalField.get().getMetersAboveSeaLevel();
                }
            }
        }
        return total / (terrainMap.getHeight() * terrainMap.getWidth());
    }

    private void fillWithForest() {

    }

    private void fillWithDesert() {
        GeneratedMap desertMap = new GeneratedMap(createPotentialDesertsBoard());
        for (int y = 0; y < desertMap.getHeight(); y++) {
            for (int x = 0; x < desertMap.getWidth(); x++) {
                if (desertMap.fieldAtPosition(new Point(x, y)).isPresent()) {
                    List<Field> desertFields = new ArrayList<>();
                    collectDesertFields(desertMap.fieldAtPosition(new Point(x, y)).get(), desertMap, desertFields);
                    if (desertFields.size() >= DESERT_MIN_AREA) {
                        placeDesertOnMap(desertFields);
                    }
                }
            }
        }
    }

    private void placeDesertOnMap(List<Field> desertFields) {
        for (Field field : desertFields) {
            Optional<Field> optionalField = terrainMap.fieldAtPosition(new Point(field.getPosition().X, field.getPosition().Y));
            optionalField.ifPresent(desertField -> desertField.setTerrainType(TerrainType.DESERT));
        }
    }

    private void collectDesertFields(Field field, GeneratedMap desertMap, List<Field> desertFields) {
        Direction[] directions = new Direction[]{Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH};
        desertFields.add(field);
        desertMap.setFieldNull(field.getPosition());
        for (Direction direction : directions) {
            Optional<Field> neighbourField = desertMap.getNeighbourInDirection(direction, field);
            neighbourField.ifPresent(value -> collectDesertFields(value, desertMap, desertFields));
        }
    }

    private Field[][] createPotentialDesertsBoard() {
        int averageHeight = calculateAvgHeight();
        Field[][] potentialDeserts = new Field[terrainMap.getHeight()][terrainMap.getWidth()];
        for (int y = 0; y < terrainMap.getHeight(); y++) {
            for (int x = 0; x < terrainMap.getWidth(); x++) {
                Optional<Field> optionalField = terrainMap.fieldAtPosition(new Point(x, y));
                if (optionalField.isPresent() && optionalField.get().getTerrainType() == TerrainType.GRASSLAND
                        && isProperHeight(optionalField.get().getMetersAboveSeaLevel(), averageHeight)) {
                    potentialDeserts[y][x] = optionalField.get();
                }
            }
        }
        return potentialDeserts;
    }

    private boolean isProperHeight(int metersAboveSeaLevel, int averageHeight) {
        return (metersAboveSeaLevel < averageHeight + DESERT_MAX_LEVEL_DIFF) && (metersAboveSeaLevel > averageHeight - DESERT_MAX_LEVEL_DIFF);
    }

    private void fillWithIce() {
        for (int y = 0; y < terrainMap.getHeight(); y++) {
            for (int x = 0; x < terrainMap.getWidth(); x++) {
                Optional<Field> optionalField = terrainMap.fieldAtPosition(new Point(x, y));
                optionalField.ifPresent((field) -> {
                    if (field.getMetersAboveSeaLevel() >= ICE_LEVEL && field.getTerrainType() == TerrainType.GRASSLAND)
                        field.setTerrainType(TerrainType.ICE);
                });
            }
        }
    }
}
