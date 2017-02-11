package data.scripts.world.freespace;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.EconomyAPI;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import com.fs.starfarer.api.impl.campaign.ids.Conditions;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.ids.Submarkets;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class EpsilonPegasi {

    private static void initFactionRelationships(SectorAPI sector) {
        FactionAPI fs_terran = sector.getFaction("fs_terran");
        FactionAPI fs_vasudan = sector.getFaction("fs_vasudan");
        FactionAPI fs_shivan = sector.getFaction("fs_shivan");
        FactionAPI fs_hammeroflight = sector.getFaction("fs_hammeroflight");
        FactionAPI fs_newterrandawn = sector.getFaction("fs_newterrandawn");

        for (FactionAPI faction : sector.getAllFactions()) {
            if (faction != fs_terran && !faction.isNeutralFaction()) {
                fs_terran.setRelationship(faction.getId(), RepLevel.SUSPICIOUS);
            }
            if (faction != fs_newterrandawn && !faction.isNeutralFaction()) {
                fs_newterrandawn.setRelationship(faction.getId(), RepLevel.INHOSPITABLE);
            }
        }

        fs_terran.setRelationship(Factions.INDEPENDENT, RepLevel.NEUTRAL);
        fs_terran.setRelationship(Factions.HEGEMONY, RepLevel.INHOSPITABLE);
        fs_terran.setRelationship(Factions.DIKTAT, RepLevel.NEUTRAL);
        fs_terran.setRelationship(Factions.LUDDIC_CHURCH, RepLevel.HOSTILE);
        fs_terran.setRelationship(Factions.LUDDIC_PATH, RepLevel.VENGEFUL);
        fs_terran.setRelationship(Factions.PIRATES, RepLevel.HOSTILE);
        fs_terran.setRelationship(Factions.PLAYER, RepLevel.NEUTRAL);

        fs_newterrandawn.setRelationship(Factions.LUDDIC_PATH, RepLevel.SUSPICIOUS);
        fs_newterrandawn.setRelationship(Factions.PIRATES, RepLevel.WELCOMING);
        fs_newterrandawn.setRelationship(Factions.PLAYER, RepLevel.SUSPICIOUS);

        fs_terran.setRelationship("fs_vasudan", RepLevel.WELCOMING);
        fs_terran.setRelationship("fs_newterrandawn", RepLevel.HOSTILE);
        fs_terran.setRelationship("fs_hammeroflight", RepLevel.INHOSPITABLE);
        fs_terran.setRelationship("fs_shivan", RepLevel.VENGEFUL);
        fs_newterrandawn.setRelationship("fs_hammeroflight", RepLevel.HOSTILE);
        fs_newterrandawn.setRelationship("fs_shivan", RepLevel.VENGEFUL);
    }

    private static MarketAPI addMarketplace(String factionID, SectorEntityToken primaryEntity, ArrayList<SectorEntityToken> connectedEntities, String name,
                                            int size, ArrayList<String> marketConditions, ArrayList<String> submarkets, float tarrif) {
        EconomyAPI globalEconomy = Global.getSector().getEconomy();
        String planetID = primaryEntity.getId();
        String marketID = planetID/* + "_market"*/;

        MarketAPI newMarket = Global.getFactory().createMarket(marketID, name, size);
        newMarket.setFactionId(factionID);
        newMarket.setPrimaryEntity(primaryEntity);
        newMarket.setBaseSmugglingStabilityValue(0);
        newMarket.getTariff().modifyFlat("generator", tarrif);

        if (null != submarkets) {
            for (String market : submarkets) {
                newMarket.addSubmarket(market);
            }
        }

        for (String condition : marketConditions) {
            newMarket.addCondition(condition);
        }

        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                newMarket.getConnectedEntities().add(entity);
            }
        }

        globalEconomy.addMarket(newMarket);
        primaryEntity.setMarket(newMarket);
        primaryEntity.setFaction(factionID);

        if (null != connectedEntities) {
            for (SectorEntityToken entity : connectedEntities) {
                entity.setMarket(newMarket);
                entity.setFaction(factionID);
            }
        }

        return newMarket;
    }

    public void generate(SectorAPI sector) {
        StarSystemAPI system = sector.createStarSystem("Epsilon Pegasi");
        system.getLocation().set(16000, 0);
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
        epsilonpegasib_jumppoint.setCircularOrbit(system.getEntityById("epsilonpegasib"), 300+100, 300, 215);
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

        // Kothar Jumppoint
        JumpPointAPI kothar_jumppoint = Global.getFactory().createJumpPoint("kothar_jump", "Kothar Jumppoint");
        kothar_jumppoint.setCircularOrbit(system.getEntityById("kothar"), 200, 600, 400);
        kothar_jumppoint.setRelatedPlanet(kothar);

        kothar_jumppoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(kothar_jumppoint);

        MarketAPI terranMarket = addMarketplace("fs_terran", epsilonpegasib,
                new ArrayList<>(Arrays.asList(terranStation)),
                "Epsilon Pegasi b",
                7,
                new ArrayList<>(Arrays.asList(Conditions.TERRAN, Conditions.AUTOFAC_HEAVY_INDUSTRY, Conditions.MILITARY_BASE,
                        Conditions.POPULATION_7, Conditions.HEADQUARTERS,
                        Conditions.SPACEPORT, Conditions.ORE_COMPLEX, Conditions.ORE_REFINING_COMPLEX)),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_STORAGE, Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                0.3f
        );

        MarketAPI ntdMarket = addMarketplace("fs_newterrandawn", kothar,
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

        SharedData.getData().getMarketsWithoutTradeFleetSpawn().add(ntdMarket.getId());

        system.autogenerateHyperspaceJumpPoints(true, true);

        initFactionRelationships(sector);
    }
}