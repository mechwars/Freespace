package data.scripts.world.freespace;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Cocytus {

    public void generate(SectorAPI sector) {
        StarSystemAPI system = sector.createStarSystem("Cocytus");
        system.getLocation().set(9000, 19000);
        system.setBackgroundTextureFilename("graphics/backgrounds/hyperspace1.jpg");

        PlanetAPI cocytus = system.initStar("cocytus", // unique id for this star
                "star_red", // id in planets.json
                1000f,		// radius (in pixels at default zoom)
                1500, // corona radius, from star edge
                5f, // solar wind burn level
                0.5f, // flare probability
                2f); // cr loss mult

        system.setLightColor(new Color(255, 0, 0)); // light color in entire system, affects all entities

        system.addAsteroidBelt(cocytus, 500, 2000, 200, 200, 200);
		/*
		 * addAsteroidBelt() parameters:
		 * 1. What the belt orbits
		 * 2. Number of asteroids
		 * 3. Orbit radius
		 * 4. Belt width
		 * 6/7. Range of days to complete one orbit. Value picked randomly for each asteroid.
		 */

        PlanetAPI shivanhome = system.addPlanet("shivanhome", cocytus, "Enkidu", "lava", 300, 300, 3400, 215);
        shivanhome.getSpec().setGlowColor(new Color(255, 200, 150, 255));
        shivanhome.getSpec().setUseReverseLightForGlow(true);
        shivanhome.applySpecChanges();
        shivanhome.setCustomDescriptionId("fs_shivan_homeworld");

        // Shivan Jumppoint
        JumpPointAPI shivanhome_jumppoint = Global.getFactory().createJumpPoint("shivanhome_jump", "Enkidu Jumppoint");
        shivanhome_jumppoint.setCircularOrbit(system.getEntityById("shivanhome"), 100, 300, 215);
        shivanhome_jumppoint.setRelatedPlanet(shivanhome);

        shivanhome_jumppoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(shivanhome_jumppoint);

        // Shivan Station
        SectorEntityToken shivanStation = system.addCustomEntity("fs_shivan_base",
                "Shivan Origin", "station_jangala_type", "fs_shivan");
        shivanStation.setCircularOrbitPointingDown(system.getEntityById("shivanhome"), 45 + 180, 300, 50);
        shivanStation.setCustomDescriptionId("fs_shivan_base");
        shivanStation.setInteractionImage("illustrations", "pirate_station");

        addMarketplace.addMarketplace("fs_shivan", shivanhome,
                new ArrayList<>(Arrays.asList(shivanStation)),
                "Shivan Origin",
                8,
                new ArrayList<>(Arrays.asList(Conditions.AUTOFAC_HEAVY_INDUSTRY, Conditions.MILITARY_BASE,
                        Conditions.POPULATION_8, Conditions.HEADQUARTERS,
                        Conditions.SPACEPORT, Conditions.ORE_COMPLEX,
                        Conditions.ORE_COMPLEX, Conditions.ORE_REFINING_COMPLEX, Conditions.ORE_REFINING_COMPLEX)),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_STORAGE, Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN)),
                0f
        );

        system.autogenerateHyperspaceJumpPoints(true, true);
    }
}