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

public class Cocytus {

    private static void initFactionRelationships(SectorAPI sector) {
        FactionAPI fs_shivan = sector.getFaction("fs_shivan");

        for (FactionAPI faction : sector.getAllFactions()) {
            if (faction != fs_shivan) {
                fs_shivan.setRelationship(faction.getId(), RepLevel.HOSTILE);
            }
        }
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
        StarSystemAPI system = sector.createStarSystem("Cocytus");
        system.getLocation().set(-5000, -11500);
        system.setBackgroundTextureFilename("graphics/backgrounds/hyperspace1.jpg");

        PlanetAPI cocytus = system.initStar("cocytus", // unique id for this star
                "star_red", // id in planets.json
                500f,		// radius (in pixels at default zoom)
                500, // corona radius, from star edge
                10f, // solar wind burn level
                1f, // flare probability
                3f); // cr loss mult

        system.setLightColor(new Color(255, 0, 0)); // light color in entire system, affects all entities

        PlanetAPI enkidu = system.addPlanet("enkidu", cocytus, "Enkidu", "lava", 300, 200, 3400, 215);
        enkidu.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "volturn"));
        enkidu.getSpec().setGlowColor(new Color(255, 200, 150, 255));
        enkidu.getSpec().setUseReverseLightForGlow(true);
        enkidu.applySpecChanges();
        enkidu.setCustomDescriptionId("fs_enkidu");

        // Shivan Jumppoint
        JumpPointAPI enkidu_jumppoint = Global.getFactory().createJumpPoint("enkidu_jump", "Enkidu Jumppoint");
        enkidu_jumppoint.setCircularOrbit(system.getEntityById("enkidu"), 100, 300, 215);
        enkidu_jumppoint.setRelatedPlanet(enkidu);

        enkidu_jumppoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(enkidu_jumppoint);

        // Shivan Station
        SectorEntityToken shivanStation = system.addCustomEntity("fs_shivan_base",
                "Shivan Origin", "station_jangala_type", "fs_shivan");
        shivanStation.setCircularOrbitPointingDown(system.getEntityById("enkidu"), 45 + 180, 300, 50);
        shivanStation.setCustomDescriptionId("fs_shivan_base");

        MarketAPI shivanMarket = addMarketplace("fs_shivan", enkidu,
                new ArrayList<>(Arrays.asList(shivanStation)),
                "Shivan Origin",
                10,
                new ArrayList<>(Arrays.asList(Conditions.UNINHABITABLE, Conditions.AUTOFAC_HEAVY_INDUSTRY, Conditions.MILITARY_BASE,
                        Conditions.POPULATION_10, Conditions.HEADQUARTERS,
                        Conditions.SPACEPORT,
                        Conditions.ORE_COMPLEX, Conditions.ORE_REFINING_COMPLEX)),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_STORAGE, Submarkets.GENERIC_MILITARY)),
                0f
        );

        SharedData.getData().getMarketsWithoutTradeFleetSpawn().add(shivanMarket.getId());

        system.autogenerateHyperspaceJumpPoints(true, true);

        initFactionRelationships(sector);
    }
}