package com.github.wojtechm.watergenerator;

import com.github.wojtechm.Field;
import com.github.wojtechm.GeneratedMap;
import com.github.wojtechm.Point;
import com.github.wojtechm.TerrainType;
import com.github.wojtechm.display.DisplayFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;

/**
 * @author Wojciech Makieła
 */
public class WaterGenerator {
    private GeneratedMap map;
    private int waterFields = 0;

    public void generate(GeneratedMap map) {
        this.map = map;
        fillSpotsWithMBSLWithWater();
        createRivers();
    }

    private void fillSpotsWithMBSLWithWater() {
        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Field field = map.fieldAtPosition(new Point(x, y)).get();
                if (field.getMetersAboveSeaLevel() > 0) {
                    continue;
                }
                field.setTerrainType(TerrainType.OCEAN);
                waterFields++;
                // TODO: implement Lake creation
            }
        }
    }

    private void createRivers() {
        int numberOfRiversToGenerate = getNumberOfRiversToGenerate();
        System.out.println("\n" + numberOfRiversToGenerate);
        PriorityQueue<Field> bestCandidatesForRiverSources = getBestSourceCandidates(numberOfRiversToGenerate);
        while (numberOfRiversToGenerate > 0) {
            Field field = bestCandidatesForRiverSources.poll();
            field.setTerrainType(TerrainType.RIVER);
            numberOfRiversToGenerate--;
            recursivelyCreateRiver(field, 100);

        }
    }

    private void recursivelyCreateRiver(Field field, int limit) {
//        DisplayFactory.physicalWithRiversDisplay().print(map);
        if (limit <= 0) return;
        Optional<Field> optionalNextRiverField = map.getAllNeighbours(field).stream()
                .filter(field1 -> field1.getTerrainType() != TerrainType.RIVER)
                .min(Comparator.comparingInt(Field::getMetersAboveSeaLevel));

        if (optionalNextRiverField.isEmpty()) return;
        Field nextRiverField = optionalNextRiverField.get();

        if (nextRiverField.getTerrainType() == TerrainType.OCEAN) return;
        int newMASL = Math.min(field.getMetersAboveSeaLevel(), nextRiverField.getMetersAboveSeaLevel()) - 1;
        nextRiverField.setMetersAboveSeaLevel(newMASL);
        nextRiverField.setTerrainType(TerrainType.RIVER);
        recursivelyCreateRiver(nextRiverField,  limit - 1);
    }

    int getNumberOfRiversToGenerate() {
        int totalFields = map.getWidth() * map.getHeight();
        int landFields = totalFields - waterFields;
        float riverSourcePerLandFieldRatio = 1.0f / 1_000;
        double waterCoverage = waterFields / (1.0 * totalFields);
        if (waterCoverage > 0.7) {
            return 0;
        } else if (waterCoverage > 0.5) {
            riverSourcePerLandFieldRatio /= 2;
        }
        return (int) (riverSourcePerLandFieldRatio * landFields);
    }

    private PriorityQueue<Field> getBestSourceCandidates(int riversToGenerate) {
        PriorityQueue<Field> pq = new PriorityQueue<>(new Comparator<Field>() {
            @Override
            public int compare(Field o1, Field o2) {
                return o2.getMetersAboveSeaLevel() - o1.getMetersAboveSeaLevel();
            }
        });

        for (int y = 0; y < map.getHeight(); y++) {
            for (int x = 0; x < map.getWidth(); x++) {
                Field field = map.fieldAtPosition(new Point(x, y)).get();
                if (field.getMetersAboveSeaLevel() <= 3000)
                    continue;
                List<Field> allNeighbours = map.getAllNeighbours(field);
                boolean shouldBeAdded = true;
                for (Field neighbour : allNeighbours) {
                    if (pq.contains(neighbour)) {
                        shouldBeAdded = false;
                        break;
                    }
                }
                if (shouldBeAdded)
                    pq.add(field);
            }
        }

        if (riversToGenerate > pq.size()) {
            for (int x = 0; x < map.getWidth(); x++) {
                Field field = map.fieldAtPosition(new Point(x, 0)).get();
                if (field.getTerrainType() == TerrainType.GRASSLAND)
                    pq.add(field);
                field = map.fieldAtPosition(new Point(x, map.getHeight() - 1)).get();
                if (field.getTerrainType() == TerrainType.GRASSLAND)
                    pq.add(field);
            }
            for (int y = 0; y < map.getHeight(); y++) {
                Field field = map.fieldAtPosition(new Point(0, y)).get();
                if (field.getTerrainType() == TerrainType.GRASSLAND)
                    pq.add(field);
                field = map.fieldAtPosition(new Point(map.getWidth() - 1, y)).get();
                if (field.getTerrainType() == TerrainType.GRASSLAND)
                    pq.add(field);
            }
        }
        return pq;
    }
}
