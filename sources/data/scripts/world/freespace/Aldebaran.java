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

public class Aldebaran {

    private static void initFactionRelationships(SectorAPI sector) {
        FactionAPI fs_terran = sector.getFaction("fs_terran");
        FactionAPI fs_vasudan = sector.getFaction("fs_vasudan");
        FactionAPI fs_shivan = sector.getFaction("fs_shivan");
        FactionAPI fs_hammeroflight = sector.getFaction("fs_hammeroflight");
        FactionAPI fs_newterrandawn = sector.getFaction("fs_newterrandawn");

        for (FactionAPI faction : sector.getAllFactions()) {
            if (faction != fs_vasudan && !faction.isNeutralFaction()) {
                fs_vasudan.setRelationship(faction.getId(), RepLevel.SUSPICIOUS);
            }
            if (faction != fs_hammeroflight && !faction.isNeutralFaction()) {
                fs_hammeroflight.setRelationship(faction.getId(), RepLevel.INHOSPITABLE);
            }
        }

        fs_vasudan.setRelationship(Factions.TRITACHYON, RepLevel.NEUTRAL);
        fs_vasudan.setRelationship(Factions.PLAYER, RepLevel.NEUTRAL);

        fs_hammeroflight.setRelationship(Factions.LUDDIC_PATH, RepLevel.WELCOMING);
        fs_hammeroflight.setRelationship(Factions.PIRATES, RepLevel.SUSPICIOUS);
        fs_hammeroflight.setRelationship(Factions.PLAYER, RepLevel.SUSPICIOUS);

        fs_vasudan.setRelationship("fs_terran", RepLevel.WELCOMING);
        fs_vasudan.setRelationship("fs_newterrandawn", RepLevel.INHOSPITABLE);
        fs_vasudan.setRelationship("fs_hammeroflight", RepLevel.HOSTILE);
        fs_vasudan.setRelationship("fs_shivan", RepLevel.VENGEFUL);
        fs_hammeroflight.setRelationship("fs_newterrandawn", RepLevel.HOSTILE);
        fs_hammeroflight.setRelationship("fs_shivan", RepLevel.SUSPICIOUS);
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
        StarSystemAPI system = sector.createStarSystem("Aldebaran");
        system.getLocation().set(5000, -13000);
        system.setBackgroundTextureFilename("graphics/backgrounds/background4.jpg");

        PlanetAPI aldebaran = system.initStar("aldebaran", // unique id for this star
                "star_blue", // id in planets.json
                700f,		// radius (in pixels at default zoom)
                800, // corona radius, from star edge
                15f, // solar wind burn level
                1f, // flare probability
                4f); // cr loss mult

        system.setLightColor(new Color(100, 100, 255)); // light color in entire system, affects all entities

        PlanetAPI newvasuda = system.addPlanet("newvasuda", aldebaran, "New Vasuda", "arid", 300, 200, 3400, 215);
        newvasuda.getSpec().setGlowTexture(Global.getSettings().getSpriteName("hab_glows", "volturn"));
        newvasuda.getSpec().setGlowColor(new Color(255,255,255,255));
        newvasuda.getSpec().setUseReverseLightForGlow(true);
        newvasuda.applySpecChanges();
        newvasuda.setCustomDescriptionId("fs_newvasuda");

        // Vasudan Station
        SectorEntityToken vasudanStation = system.addCustomEntity("fs_vasuda_base",
                "Vasudan Naval Command", "station_jangala_type", "fs_vasudan");
        vasudanStation.setCircularOrbitPointingDown(system.getEntityById("newvasuda"), 45 + 180, 300, 50);
        vasudanStation.setCustomDescriptionId("fs_vasuda_base");

        // New Vasuda Jumppoint
        JumpPointAPI newvasuda_jumppoint = Global.getFactory().createJumpPoint("newvasuda_jump", "New Vasuda Jumppoint");
        newvasuda_jumppoint.setCircularOrbit(system.getEntityById("newvasuda"), 200, 300, 215);
        newvasuda_jumppoint.setRelatedPlanet(newvasuda);

        newvasuda_jumppoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(newvasuda_jumppoint);

        PlanetAPI apep = system.addPlanet("apep", aldebaran, "Apep", "frozen", 200, 150, 6000, 400);
        apep.getSpec().setGlowColor(new Color(255, 255, 255, 255));
        apep.getSpec().setUseReverseLightForGlow(true);
        apep.applySpecChanges();
        apep.setCustomDescriptionId("fs_apep");

        // Hammer of Light Station
        SectorEntityToken holStation = system.addCustomEntity("fs_apep_base",
                "Hammer of Light Base", "station_pirate_type", "fs_hammeroflight");
        holStation.setCircularOrbitPointingDown(system.getEntityById("apep"), 45, 300, 50);
        holStation.setCustomDescriptionId("fs_apep_base");
        holStation.setInteractionImage("illustrations", "pirate_station");

        // Apep Jumppoint
        JumpPointAPI apep_jumppoint = Global.getFactory().createJumpPoint("apep_jump", "Apep Jumppoint");
        apep_jumppoint.setCircularOrbit(system.getEntityById("apep"), 200, 400, 400);
        apep_jumppoint.setRelatedPlanet(apep);

        apep_jumppoint.setStandardWormholeToHyperspaceVisual();
        system.addEntity(apep_jumppoint);

        MarketAPI cocytusMarket = addMarketplace("fs_vasudan", newvasuda,
                new ArrayList<>(Arrays.asList(vasudanStation)),
                "New Vasuda",
                7,
                new ArrayList<>(Arrays.asList(Conditions.ARID, Conditions.AUTOFAC_HEAVY_INDUSTRY, Conditions.MILITARY_BASE,
                        Conditions.POPULATION_7, Conditions.HEADQUARTERS,
                        Conditions.SPACEPORT, Conditions.ORE_COMPLEX, Conditions.ORE_REFINING_COMPLEX)),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_STORAGE, Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                0.3f
        );

        MarketAPI holMarket = addMarketplace("fs_hammeroflight", apep,
                new ArrayList<>(Arrays.asList(holStation)),
                "New Vasuda",
                5,
                new ArrayList<>(Arrays.asList(Conditions.ICE, Conditions.AUTOFAC_HEAVY_INDUSTRY, Conditions.MILITARY_BASE,
                        Conditions.POPULATION_5, Conditions.HEADQUARTERS,
                        Conditions.SPACEPORT, Conditions.ORE_COMPLEX, Conditions.ORE_REFINING_COMPLEX)),
                new ArrayList<>(Arrays.asList(Submarkets.SUBMARKET_STORAGE, Submarkets.GENERIC_MILITARY,
                        Submarkets.SUBMARKET_OPEN, Submarkets.SUBMARKET_BLACK)),
                0.3f
        );

        system.autogenerateHyperspaceJumpPoints(true, true);

        initFactionRelationships(sector);
    }
}