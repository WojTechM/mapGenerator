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
        GeneratedMap map = new GeneratedMap(300, 180);

        MountainRangeBasedGenerator mountainRangeBasedGenerator = new MountainRangeBasedGenerator();
        mountainRangeBasedGenerator.generate(map);

        WaterGenerator wg = new WaterGenerator();
        wg.generate(map);
      
        TerrainGenerator tg = new TerrainGenerator();
        tg.generate(map);

        DisplayFactory.physicalWithRiversDisplay().print(map);
        DisplayFactory.standardDisplay().print(map);
    }
}
