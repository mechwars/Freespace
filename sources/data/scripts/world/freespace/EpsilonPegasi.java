package data.scripts.world.freespace;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class EpsilonPegasi {

    public void generate(SectorAPI sector) {
        StarSystemAPI system = sector.createStarSystem("Epsilon Pegasi");
        system.getLocation().set(8400, -10750);
        system.setBackgroundTextureFilename("graphics/backgrounds/background4.jpg");

        PlanetAPI epsilonpegasi = system.initStar("epsilonpegasi", // unique id for this star
                "star_yellow", // id in planets.json
                500f,		// radius (in pixels at default zoom)
                500, // corona radius, from star edge
                10f, // solar wind burn level
                1f, // flare probability
                3f); // cr loss mult

        system.setLightColor(new Color(255, 220, 100)); // light color in entire system, affects all entities

        PlanetAPI epsilonpegasib = system.addPlanet("epsilonpegasib", epsilonpegasi, "Epsilon Pegasi b", "terran", 300, 200, 3400, 215);
        epsilonpegasib.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "volturn"));
        epsilonpegasib.getSpec().setGlowColor(new Color(255,255,255,255));
        epsilonpegasib.getSpec().setUseReverseLightForGlow(true);
        epsilonpegasib.applySpecChanges();
        epsilonpegasib.setCustomDescriptionId("fs_epsilonpegasib");

        // Terran Station
        SectorEntityToken terranStation = system.addCustomEntity("fs_terran_base",
                "Enif Station", "station_jangala_type", "fs_terran");
        terranStation.setCircularOrbitPointingDown(system.getEntityById("epsilonpegasib"), 45 + 180, 600, 50);
        terranStation.setCustomDescriptionId("fs_terran_base");

        // Pegasi-b Jumppoint
        JumpPointAPI epsilonpegasib_jumppoint = Global.getFactory().createJumpPoint("epsilonpegasib_jump", "Epsilon Pegasi b Jumppoint");
        epsilonpegasib_jumppoint.setCircularOrbit(system.getEntityById("epsilonpegasib"), 300+100, 3400, 215);
        epsilonpegasib_jumppoint.setRelatedPlanet(epsilonpegasib);

        epsilonpegasib_jumppoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(epsilonpegasib_jumppoint);

        PlanetAPI kothar = system.addPlanet("kothar", epsilonpegasi, "Kothar", "rocky_unstable", 200, 150, 6000, 400);
        kothar.getSpec().setGlowColor(new Color(255, 255, 255, 255));
        kothar.getSpec().setUseReverseLightForGlow(true);
        kothar.applySpecChanges();
        kothar.setCustomDescriptionId("fs_kothar");

        // New Terran Dawn Station
        SectorEntityToken ntdStation = system.addCustomEntity("fs_kothar_base",
                "New Terran Dawn Base", "station_pirate_type", "fs_newterrandawn");
        ntdStation.setCircularOrbitPointingDown(system.getEntityById("kothar"), 45, 300, 50);
        ntdStation.setCustomDescriptionId("fs_kothar_base");
        ntdStation.setInteractionImage("illustrations", "pirate_station");

        // Apep Jumppoint
        JumpPointAPI kothar_jumppoint = Global.getFactory().createJumpPoint("kothar_jump", "Kothar Jumppoint");
        kothar_jumppoint.setCircularOrbit(system.getEntityById("kothar"), 200+60, 6000, 400);
        kothar_jumppoint.setRelatedPlanet(kothar);

        kothar_jumppoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(kothar_jumppoint);

        addMarketplace.addMarketplace("fs_terran", epsilonpegasib,
                new ArrayList<>(Arrays.asList(terranStation)),
                "Epsilon Pegasi b",
                7,
                new ArrayList<>(Arrays.asList(Conditions.ARID, Conditions.AUTOFAC_HEAVY_INDUSTRY, Conditions.MILITARY_BASE,
                        Conditions.POPULATION_7, Conditions.HEADQUARTERS,
                        Conditions.SPACEPORT, Conditions.ORE_COMPLEX, Conditions.ORE_REFINING_COMPLEX)),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_STORAGE, Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                0.3f
        );

        addMarketplace.addMarketplace("fs_newterrandawn", kothar,
                new ArrayList<>(Arrays.asList(ntdStation)),
                "Kothar",
                5,
                new ArrayList<>(Arrays.asList(Conditions.ICE, Conditions.AUTOFAC_HEAVY_INDUSTRY, Conditions.MILITARY_BASE,
                        Conditions.POPULATION_5, Conditions.HEADQUARTERS,
                        Conditions.SPACEPORT, Conditions.ORE_COMPLEX, Conditions.ORE_REFINING_COMPLEX)),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_STORAGE, Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                0.3f
        );

        system.autogenerateHyperspaceJumpPoints(true, true);
    }
}