package com.github.wojtechm.landformgenerator;

import com.github.wojtechm.Field;
import com.github.wojtechm.GeneratedMap;
import com.github.wojtechm.Point;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Wojciech Makieła
 */
public class LandformGenerator {
    private static final int MAX_STEP = 100;
    private final int maxHeight;
    private final int minHeight;
    private GeneratedMap map;

    public LandformGenerator() {
        maxHeight = 10_000;
        minHeight = -10_000;
    }

    public LandformGenerator(int maxHeight, int minHeight) {
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
    }

    public void generate(GeneratedMap map) {
        this.map = map;
        int prevHeight = ThreadLocalRandom.current().nextInt(-MAX_STEP / 2, MAX_STEP / 2);

        for (int i = 0; i < map.getWidth(); i++) {
            Optional<Field> optionalField = map.fieldAtPosition(new Point(i, 0));
            prevHeight = getPrevHeight(prevHeight, optionalField);
        }

        prevHeight = map.fieldAtPosition(new Point(0, 0)).get().getMetersAboveSeaLevel();
        for (int y = 1; y < map.getHeight(); y++) {
            Optional<Field> optionalField = map.fieldAtPosition(new Point(0, y));
            prevHeight = getPrevHeight(prevHeight, optionalField);
        }

        for (int x = 1; x < map.getWidth(); x++) {
            for (int y = 1; y < map.getHeight(); y++) {
                Optional<Field> optionalField = map.fieldAtPosition(new Point(x, y));
                int topHeight = map.fieldAtPosition(new Point(x, y - 1)).get().getMetersAboveSeaLevel();
                int leftHeight = map.fieldAtPosition(new Point(x - 1, y)).get().getMetersAboveSeaLevel();
                prevHeight = (leftHeight + topHeight) / 2;
                getPrevHeight(prevHeight, optionalField);
            }
        }
    }

    private int getPrevHeight(int prevHeight, Optional<Field> optionalField) {
        prevHeight = getRandomHeight(prevHeight);
        int finalPrevHeight = prevHeight;
        optionalField.ifPresent(field -> field.setMetersAboveSeaLevel(finalPrevHeight));
        return prevHeight;
    }

    private int getRandomHeight(int prevHeight) {
        int random = ThreadLocalRandom.current().nextInt(prevHeight - MAX_STEP / 2, prevHeight + MAX_STEP / 2);
        random = Math.min(random, maxHeight);
        random = Math.max(random, minHeight);
        return random;
    }
}
