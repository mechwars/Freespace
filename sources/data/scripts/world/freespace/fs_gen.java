package data.scripts.world.freespace;

import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.RepLevel;
import com.fs.starfarer.api.campaign.SectorAPI;
import com.fs.starfarer.api.campaign.SectorGeneratorPlugin;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import data.scripts.world.freespace.Cocytus;
import data.scripts.world.freespace.EpsilonPegasi;
import data.scripts.world.freespace.Aldebaran;

import java.util.List;

public class fs_gen implements SectorGeneratorPlugin {

    @Override
    public void generate(SectorAPI sector) {
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_terran");
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_vasudan");
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_hammeroflight");
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_newterrandawn");

        initFactionRelationships(sector);

        new Cocytus().generate(sector);
        new EpsilonPegasi().generate(sector);
        new Aldebaran().generate(sector);
    }

    public static void initFactionRelationships(SectorAPI sector) {
        FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
        FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
        FactionAPI pirates = sector.getFaction(Factions.PIRATES);
        FactionAPI kol = sector.getFaction(Factions.KOL);
        FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
        FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);
        FactionAPI fs_terran = sector.getFaction("fs_terran");
        FactionAPI fs_vasudan = sector.getFaction("fs_vasudan");
        FactionAPI fs_shivan = sector.getFaction("fs_shivan");
        FactionAPI fs_hammeroflight = sector.getFaction("fs_hammeroflight");
        FactionAPI fs_newterrandawn = sector.getFaction("fs_newterrandawn");


        //default relation
        List<FactionAPI> allFactions = sector.getAllFactions();
        for (FactionAPI f : allFactions)
        {
            fs_terran.setRelationship(f.getId(), RepLevel.NEUTRAL);
            fs_vasudan.setRelationship(f.getId(), RepLevel.NEUTRAL);
            fs_shivan.setRelationship(f.getId(), RepLevel.VENGEFUL);
            fs_hammeroflight.setRelationship(f.getId(), RepLevel.SUSPICIOUS);
            fs_newterrandawn.setRelationship(f.getId(), RepLevel.SUSPICIOUS);
        }

        //standard factions
        fs_terran.setRelationship("persean", RepLevel.FAVORABLE);
        fs_terran.setRelationship("independent", RepLevel.NEUTRAL);
        fs_terran.setRelationship("tritachyon", RepLevel.SUSPICIOUS);
        fs_terran.setRelationship("hegemony", RepLevel.INHOSPITABLE);
        fs_terran.setRelationship("sindrian_diktat", RepLevel.NEUTRAL);
        fs_terran.setRelationship("lions_guard", RepLevel.NEUTRAL);
        fs_terran.setRelationship("luddic_church", RepLevel.HOSTILE);
        fs_terran.setRelationship("luddic_path", RepLevel.VENGEFUL);
        fs_terran.setRelationship("pirates", RepLevel.HOSTILE);

        fs_vasudan.setRelationship("persean", RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship("independent", RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship("tritachyon", RepLevel.NEUTRAL);
        fs_vasudan.setRelationship("hegemony", RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship("sindrian_diktat", RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship("lions_guard", RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship("luddic_church", RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship("luddic_path", RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship("pirates", RepLevel.SUSPICIOUS);

        //FS factions
        fs_terran.setRelationship("fs_vasudan", RepLevel.WELCOMING);
        fs_terran.setRelationship("fs_newterrandawn", RepLevel.HOSTILE);
        fs_vasudan.setRelationship("fs_terran", RepLevel.WELCOMING);
        fs_vasudan.setRelationship("fs_hammeroflight", RepLevel.HOSTILE);

        //Other modded factions
/*        fs_terran.setRelationship("shadow_industry", RepLevel.WELCOMING);
        fs_terran.setRelationship("syndicate_asp", RepLevel.WELCOMING);
        fs_terran.setRelationship("citadeldefenders", RepLevel.FAVORABLE);
        fs_terran.setRelationship("sun_ice", RepLevel.NEUTRAL);
        fs_terran.setRelationship("pn_colony", RepLevel.NEUTRAL);
        fs_terran.setRelationship("interstellarimperium", RepLevel.SUSPICIOUS);
        fs_terran.setRelationship("neutrinocorp", RepLevel.SUSPICIOUS);
        fs_terran.setRelationship("pack", RepLevel.INHOSPITABLE);
        fs_terran.setRelationship("valkyrian", RepLevel.INHOSPITABLE);
        fs_terran.setRelationship("mayorate", RepLevel.HOSTILE);
        fs_terran.setRelationship("pirateAnar", RepLevel.HOSTILE);
        fs_terran.setRelationship("sun_ici", RepLevel.HOSTILE);
        fs_terran.setRelationship("diableavionics", RepLevel.HOSTILE);
        fs_terran.setRelationship("blackrock_driveyards", RepLevel.HOSTILE);
        fs_terran.setRelationship("junk_pirates", RepLevel.HOSTILE);
        fs_terran.setRelationship("exigency", RepLevel.HOSTILE);
        fs_terran.setRelationship("exipirated", RepLevel.HOSTILE);
        fs_terran.setRelationship("crystanite", RepLevel.VENGEFUL);*/
    }
}