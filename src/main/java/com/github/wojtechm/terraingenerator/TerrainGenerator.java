package com.github.wojtechm.terraingenerator;

import com.github.wojtechm.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Wojciech Makieła
 * @author Piotr Młudzik
 * @author Aleksander Jednaszewski
 */
public class TerrainGenerator {
    private static final int ICE_LEVEL = 3000;
    private static final int DESERT_MAX_LEVEL_DIFF = 500;
    private static final int DESERT_MIN_AREA = 500;
    private static final int FOREST_NUMBER = 5;
    private static final int FOREST_MAX_SIZE = 50;
    private final Set<Field> forestNeighbourFields = new HashSet<>();
    private final List<Field> forestFields = new ArrayList<>();

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
        for (int i = 0; i < FOREST_NUMBER; i++) {
            createSingleForest();
            forestFields.clear();
            forestNeighbourFields.clear();
        }
    }

    private void createSingleForest() {
        Field startField = getStartPosition();
        addFirstForestField(startField);

        int tries = 0;
        while (forestIsToSmall() && ++tries < 500)
            plantForest();
    }

    private Field getStartPosition() {
        Field startField;

        int tries = 0;
        do {
            int startX = ThreadLocalRandom.current().nextInt(terrainMap.getWidth());
            int startY = ThreadLocalRandom.current().nextInt(terrainMap.getHeight());

            startField = terrainMap.fieldAtPosition(new Point(startX, startY)).get();
        } while (!isGrassland(startField) && ++tries < 500);

        return startField;
    }

    private boolean isGrassland(Field startField) {
        return startField.getTerrainType() == TerrainType.GRASSLAND;
    }

    private void addFirstForestField(Field startField) {
        startField.setTerrainType(TerrainType.FOREST);
        forestFields.add(startField);
    }

    private boolean forestIsToSmall() {
        return forestFields.size() < FOREST_MAX_SIZE;
    }

    private void plantForest() {
        getNeighbourFields();
        setForestFromNeighbourFields();
    }

    private void getNeighbourFields() {
        for (Field field : forestFields) {
            for (Direction direction : Direction.values()) {
                if (ThreadLocalRandom.current().nextInt(10) > 8) {
                    Optional<Field> neighbourField = terrainMap.getNeighbourInDirection(direction, field);
                    neighbourField.ifPresent(actualFiled -> {
                        if (isGrassland(actualFiled))
                            forestNeighbourFields.add(actualFiled);
                    });
                }
            }
        }
    }

    private void setForestFromNeighbourFields() {
        for (Field field : forestNeighbourFields) {
            if (field.getTerrainType().equals(TerrainType.GRASSLAND)) {
                field.setTerrainType(TerrainType.FOREST);
                forestFields.add(field);
            }
        }
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
            Optional<Field> optionalField = terrainMap
                    .fieldAtPosition(new Point(field.getPosition().X, field.getPosition().Y));
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
                if (optionalField.isPresent() && isGrassland(optionalField.get())
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
                    if (field.getMetersAboveSeaLevel() >= ICE_LEVEL && isGrassland(field))
                        field.setTerrainType(TerrainType.ICE);
                });
            }
        }
    }
}
