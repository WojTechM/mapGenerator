package com.github.wojtechm;

import com.github.wojtechm.display.DisplayFactory;
import com.github.wojtechm.landformgenerator.LandformGenerator;
import com.github.wojtechm.landformgenerator.MountainRangeBasedGenerator;
import com.github.wojtechm.terraingenerator.TerrainGenerator;
import com.github.wojtechm.watergenerator.WaterGenerator;

/**
 * @author Wojciech Makieła
 */
class Main {

    public static void main(String[] args) {
        GeneratedMap map = new GeneratedMap(130, 60); // TODO: take dimensions from args
//        GeneratedMap map = new GeneratedMap(450, 165);

        MountainRangeBasedGenerator lg = new MountainRangeBasedGenerator();
        lg.generate(map);
        DisplayFactory.physicalDisplay().print(map);

        WaterGenerator wg = new WaterGenerator();
        wg.generate(map);
        TerrainGenerator tg = new TerrainGenerator();
        tg.generate(map);

        DisplayFactory.physicalWithRiversDisplay().print(map);
        DisplayFactory.standardDisplay().print(map);
    }
}
