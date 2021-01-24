package com.github.wojtechm;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Wojciech Makieła
 */
public class GeneratedMap {
    private Field[][] map;
    private final int width;
    private final int height;

    public GeneratedMap() {
        this(200, 100);
    }

    public GeneratedMap(int width, int height) {
        this.map = new Field[height][width];
        this.width = width;
        this.height = height;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                map[y][x] = new Field(new Point(x, y));
            }
        }
    }

    public Optional<Field> fieldAtPosition(Point point) {
        if (isPointOutsideOfMapBorder(point)) {
            return Optional.empty();
        }
        return Optional.of(map[point.Y][point.X]);
    }

    public Optional<Field> getNeighbourInDirection(Direction direction, Field field) {
        Point point = field.getPosition().moveInDirection(direction);
        return fieldAtPosition(point);
    }

    private boolean isPointOutsideOfMapBorder(Point point) {
        return point.Y < 0 || point.Y >= height || point.X < 0 || point.X >= width;
    }

    public List<Field> getAllNeighbours(Field field) {
        return Arrays.stream(Direction.values())
                .map(direction -> getNeighbourInDirection(direction, field))
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

}
