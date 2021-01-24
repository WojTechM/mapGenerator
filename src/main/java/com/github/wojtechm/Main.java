package com.github.wojtechm;

import com.github.wojtechm.landformgenerator.LandformGenerator;
import com.github.wojtechm.terraingenerator.TerrainGenerator;
import com.github.wojtechm.watergenerator.WaterGenerator;

/**
 * @author Wojciech Makieła
 */
class Main {

    public static void main(String[] args) {
        GeneratedMap map = new GeneratedMap(); // TODO: take dimensions from args
        LandformGenerator lg = new LandformGenerator();
        lg.generate(map);
        WaterGenerator wg = new WaterGenerator();
        wg.generate(map);
        TerrainGenerator tg = new TerrainGenerator();
        tg.generate(map);
    }
}
