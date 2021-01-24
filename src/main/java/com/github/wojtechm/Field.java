package com.github.wojtechm;

/**
 * @author Wojciech Makieła
 */
public class Field {
    private int metersAboveSeaLevel;
    private TerrainType terrainType;
    private Point position;

    public Field(Point position) {
        this.metersAboveSeaLevel = 0;
        this.terrainType = TerrainType.GRASSLAND;
        this.position = position;
    }

    public Point getPosition() {
        return position;
    }

    public int getMetersAboveSeaLevel() {
        return metersAboveSeaLevel;
    }

    public void setMetersAboveSeaLevel(int metersAboveSeaLevel) {
        this.metersAboveSeaLevel = metersAboveSeaLevel;
    }

    public TerrainType getTerrainType() {
        return terrainType;
    }

    public void setTerrainType(TerrainType terrainType) {
        this.terrainType = terrainType;
    }

}
