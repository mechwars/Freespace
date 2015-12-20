package data.scripts.world.freespace;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.JumpPointAPI;
import com.fs.starfarer.api.campaign.LocationAPI;
import com.fs.starfarer.api.campaign.OrbitAPI;
import com.fs.starfarer.api.campaign.PlanetAPI;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.campaign.StarSystemAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;
import com.fs.starfarer.api.impl.campaign.ids.Terrain;
import com.fs.starfarer.api.impl.campaign.terrain.AsteroidFieldTerrainPlugin;
import com.fs.starfarer.api.impl.campaign.terrain.MagneticFieldTerrainPlugin;
import com.fs.starfarer.api.util.Misc;
import java.awt.Color;
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

        PlanetAPI shivanhome = system.addPlanet("shivanhome", cocytus, "Homeworld", "lava", 300, 300, 3400, 215);
        shivanhome.getSpec().setGlowColor(new Color(255, 200, 150, 255));
        shivanhome.getSpec().setUseReverseLightForGlow(true);
        shivanhome.applySpecChanges();
        shivanhome.setCustomDescriptionId("fs_shivan_homeworld");

        // Shivan Jumppoint
        JumpPointAPI shivanhome_jumppoint = Global.getFactory().createJumpPoint("shivanhome_jump", "Homeworld Jumppoint");
        shivanhome_jumppoint.setCircularOrbit(system.getEntityById("shivanhome"), 100, 300, 215);
        shivanhome_jumppoint.setRelatedPlanet(shivanhome);

        // Shivan Station
        SectorEntityToken shivanStation = system.addCustomEntity("fs_shivan_base",
                "Shivan Origin", "station_jangala_type", "fs_shivan");
        shivanStation.setCircularOrbitPointingDown(system.getEntityById("shivanhome"), 45 + 180, 300, 50);
        shivanStation.setCustomDescriptionId("fs_shivan_base");

        addMarketplace.addMarketplace("fs_shivan", shivanhome,
                new ArrayList<>(Arrays.asList(shivanStation)),
                "Homeworld",
                10,
                new ArrayList<>(Arrays.asList(Conditions.AUTOFAC_HEAVY_INDUSTRY, Conditions.MILITARY_BASE,
                        Conditions.POPULATION_10, Conditions.HEADQUARTERS,
                        Conditions.SPACEPORT, Conditions.ORE_COMPLEX,
                        Conditions.ORE_COMPLEX, Conditions.ORE_REFINING_COMPLEX, Conditions.ORE_REFINING_COMPLEX)),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_STORAGE, Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN)),
                0f
        );

        system.autogenerateHyperspaceJumpPoints(true, true);
    }
}