package com.github.wojtechm;

/**
 * @author Wojciech Makieła
 */
public class Field {
    private int metersAboveSeaLevel;
    private TerrainType terrainType;

    public Field(int metersAboveSeaLevel, TerrainType terrainType) {
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.terrainType = terrainType;
    }

    public Field(int metersAboveSeaLevel) {
        this.metersAboveSeaLevel = metersAboveSeaLevel;
        this.terrainType = TerrainType.GRASSLAND;
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
