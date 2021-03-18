package com.github.wojtechm;

import com.github.wojtechm.display.DisplayFactory;
import com.github.wojtechm.landformgenerator.LandFormGenerator;
import com.github.wojtechm.map.GeneratedMap;
import com.github.wojtechm.terraingenerator.TerrainGenerator;
import com.github.wojtechm.watergenerator.WaterGenerator;

/**
 * @author Wojciech Makieła
 */
class Main {

    public static void main(String[] args) {
        GeneratedMap map = new GeneratedMap(300, 180);

        LandFormGenerator landFormGenerator = new LandFormGenerator();
        landFormGenerator.generate(map);

        WaterGenerator wg = new WaterGenerator();
        wg.generate(map);
      
        TerrainGenerator tg = new TerrainGenerator();
        tg.generate(map);

        DisplayFactory.physicalWithRiversDisplay().print(map);
        DisplayFactory.standardDisplay().print(map);
    }
}
