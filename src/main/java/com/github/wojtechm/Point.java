package com.github.wojtechm;

/**
 * @author Wojciech Makieła
 */
public class Point {
    public final int X;
    public final int Y;

    public Point(int x, int y) {
        X = x;
        Y = y;
    }

    public Point moveInDirection(Direction direction) {
        switch (direction) {
            case NORTH: return new Point(X, Y + 1);
            case NORTHEAST: return new Point(X + 1, Y + 1);
            case EAST: return new Point(X + 1, Y);
            case SOUTHEAST: return new Point(X + 1, Y - 1);
            case SOUTH: return new Point(X, Y - 1);
            case SOUTHWEST: return new Point(X - 1, Y - 1);
            case WEST: return new Point(X - 1, Y);
            case NORTHWEST: return new Point(X - 1, Y + 1);
            default: throw new IllegalArgumentException("Unsupported Direction");
        }
    }
}
