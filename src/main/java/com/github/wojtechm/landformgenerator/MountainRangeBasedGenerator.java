package com.github.wojtechm.landformgenerator;

import com.github.wojtechm.Field;
import com.github.wojtechm.GeneratedMap;
import com.github.wojtechm.Point;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Wojciech Makieła
 */
public class MountainRangeBasedGenerator {
    private final int maxHeight = 10_000;
    private final int minHeight = -10_000;


    public void generate(GeneratedMap map) {
        generateDepressions(map);
        generateMountains(map);
        for (int i = 0; i < 3; i++) {
            smoothEntireMap(map);
        }
    }

    private void smoothEntireMap(GeneratedMap map) {
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Point point = new Point(x, y);
                Field field = map.fieldAtPosition(point).orElseThrow(() -> new RuntimeException("Point outside the map border"));
                List<Field> allNeighbours = map.getAllNeighbours(field);
                int height = (allNeighbours.stream().mapToInt(Field::getMetersAboveSeaLevel).sum()
                              + field.getMetersAboveSeaLevel())
                             / (allNeighbours.size() + 1) + randomInRange(- 300, 300);
                field.setMetersAboveSeaLevel(height);
            }
        }
    }

    private void generateMountains(GeneratedMap map) {
        for (int i = 0; i < Math.min(map.getHeight(), map.getWidth()) / 10; i++) {
            Point randomStartPosition = getRandomStartingPoint(map);
            addMountainAtPoint(randomStartPosition, map);
            generateMountainRange(map.fieldAtPosition(randomStartPosition).get(), map);
        }
    }

    private void generateDepressions(GeneratedMap map) {
        for (int i = 0; i < Math.min(map.getHeight(), map.getWidth()) / 10; i++) {
            Point randomStartPosition = getRandomStartingPoint(map);
            addDepressionAtPoint(randomStartPosition, map);
            generateDepressionRange(map.fieldAtPosition(randomStartPosition).get(), map);
        }
    }

    private void generateMountainRange(Field startingPoint, GeneratedMap map) {
        Point vector = new Point(randomInRange(-5, 5), randomInRange(-5, 5));
        Point position = startingPoint.getPosition();
        for (int i = 0; i < Math.min(map.getHeight(), map.getWidth()) / 2; i++) {
            position = offsetPointByVector(position, vector);
            if (map.fieldAtPosition(position).isEmpty()) break;
            addMountainAtPoint(position, map);
            vector = new Point(randomInRange(-5, 5), randomInRange(-5, 5));
        }
    }

    private void generateDepressionRange(Field field, GeneratedMap map) {
        Point vector = new Point(randBool() ? 1 : -1, randBool() ? 1 : -1);
        Point position = field.getPosition();
        for (int i = 0; i < Math.min(map.getHeight(), map.getWidth()) / 2; i++) {
            position = offsetPointByVector(position, vector);
            if (map.fieldAtPosition(position).isEmpty()) break;
            addDepressionAtPoint(position, map);
            vector = mutateVector(vector);
        }
    }

    private void addDepressionAtPoint(Point point, GeneratedMap map) {
        Field startingPoint = createInitialDepression(point, map);
        smoothAreaAroundThePoint(map, startingPoint);
    }

    private Field createInitialDepression(Point point, GeneratedMap map) {
        Field startingPoint = map.fieldAtPosition(point).orElseThrow(() -> new RuntimeException("Point outside the map border"));
        int height = getInitialDepressionHeight();
        startingPoint.setMetersAboveSeaLevel(height);
        return startingPoint;

    }

    private void addMountainAtPoint(Point point, GeneratedMap map) {
        Field startingPoint = createInitialMountain(point, map);
        smoothAreaAroundThePointPreferHigher(map, startingPoint);
    }

    private boolean randBool() {
        return ThreadLocalRandom.current().nextBoolean();
    }

    private Point mutateVector(Point vector) {
        int newX = vector.X + ThreadLocalRandom.current().nextInt(-1, 1);
        int newY = vector.Y + ThreadLocalRandom.current().nextInt(-1, 1);
        return new Point(validVector(newX) ? newX : vector.X, validVector(newY) ? newY : vector.Y);
    }

    private boolean validVector(int newX) {
        return newX != 0 && Math.abs(newX) <= 3;
    }

    private Point offsetPointByVector(Point position, Point vector) {
        return new Point(position.X + vector.X, position.Y + vector.Y);
    }

    private Field createInitialMountain(Point point, GeneratedMap map) {
        Field startingPoint = map.fieldAtPosition(point).orElseThrow(() -> new RuntimeException("Point outside the map border"));
        int height = getInitialMountainHeight();
        startingPoint.setMetersAboveSeaLevel(height);
        return startingPoint;
    }

    private void smoothAreaAroundThePoint(GeneratedMap map, Field startingPoint) {
        Set<Field> visited = new HashSet<>();
        List<Field> currentStep = map.getAllNeighbours(startingPoint);
        List<Field> nextStep = new LinkedList<>();
        float decreaseFactor = 0.85f;
        visited.add(startingPoint);
        int height = startingPoint.getMetersAboveSeaLevel();
        int iterations = Math.min(map.getHeight(), map.getWidth()) / 10;
        while (iterations > 0) {
            iterations--;
            height = (int) (height * decreaseFactor - 300);
            for (Field neighbour : currentStep) {
                if (visited.contains(neighbour)) continue;
                neighbour.setMetersAboveSeaLevel((neighbour.getMetersAboveSeaLevel() + height) / 2);
                nextStep.addAll(map.getAllNeighbours(neighbour));
                visited.add(neighbour);
            }
            currentStep = nextStep;
            nextStep = new LinkedList<>();
        }
    }

    private void smoothAreaAroundThePointPreferHigher(GeneratedMap map, Field startingPoint) {
        Set<Field> visited = new HashSet<>();
        List<Field> currentStep = map.getAllNeighbours(startingPoint);
        List<Field> nextStep = new LinkedList<>();
        float decreaseFactor = 0.85f;
        visited.add(startingPoint);
        int height = startingPoint.getMetersAboveSeaLevel();
        int iterations = 10;
        while (iterations > 0) {
            iterations--;
            height = (int) (height * decreaseFactor - 100);
            for (Field neighbour : currentStep) {
                if (visited.contains(neighbour)) continue;
                neighbour.setMetersAboveSeaLevel(Math.max(height, neighbour.getMetersAboveSeaLevel()));
                nextStep.addAll(map.getAllNeighbours(neighbour));
                visited.add(neighbour);
            }
            currentStep = nextStep;
            nextStep = new LinkedList<>();
        }
    }

    private Point getRandomStartingPoint(GeneratedMap map) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return new Point(random.nextInt(map.getWidth()), random.nextInt(map.getHeight()));
    }

    private int getInitialMountainHeight() {
        return ThreadLocalRandom.current().nextInt((int) (maxHeight * 0.65), maxHeight);
    }

    private int getInitialDepressionHeight() {
        return minHeight;
    }

    private int randomInRange(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}