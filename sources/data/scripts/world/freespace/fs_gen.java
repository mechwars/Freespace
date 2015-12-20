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
        initFactionRelationships(sector);

        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_terran");
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_vasudan");
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_hammeroflight");
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_newterrandawn");

        new Cocytus().generate(sector);
        new EpsilonPegasi().generate(sector);
        new Aldebaran().generate(sector);
    }

    public static void initFactionRelationships(SectorAPI sector) {
        
        FactionAPI fs_terran = sector.getFaction("fs_terran");
        FactionAPI fs_vasudan = sector.getFaction("fs_vasudan");
        FactionAPI fs_shivan = sector.getFaction("fs_shivan");
        FactionAPI fs_hammeroflight = sector.getFaction("fs_hammeroflight");
        FactionAPI fs_newterrandawn = sector.getFaction("fs_newterrandawn");
/*        FactionAPI hegemony = sector.getFaction(Factions.HEGEMONY);
        FactionAPI tritachyon = sector.getFaction(Factions.TRITACHYON);
        FactionAPI pirates = sector.getFaction(Factions.PIRATES);
        FactionAPI kol = sector.getFaction(Factions.KOL);
        FactionAPI church = sector.getFaction(Factions.LUDDIC_CHURCH);
        FactionAPI path = sector.getFaction(Factions.LUDDIC_PATH);

        //default relation
        List<FactionAPI> allFactions = sector.getAllFactions();
        for (FactionAPI f : allFactions)
        {
            fs_terran.setRelationship(f.getId(), RepLevel.NEUTRAL);
            fs_vasudan.setRelationship(f.getId(), RepLevel.NEUTRAL);
            fs_shivan.setRelationship(f.getId(), RepLevel.VENGEFUL);
            fs_hammeroflight.setRelationship(f.getId(), RepLevel.SUSPICIOUS);
            fs_newterrandawn.setRelationship(f.getId(), RepLevel.SUSPICIOUS);
        }*/

        //standard factions
        fs_terran.setRelationship(Factions.INDEPENDENT, RepLevel.NEUTRAL);
        fs_terran.setRelationship(Factions.TRITACHYON, RepLevel.SUSPICIOUS);
        fs_terran.setRelationship(Factions.HEGEMONY, RepLevel.INHOSPITABLE);
        fs_terran.setRelationship(Factions.DIKTAT, RepLevel.NEUTRAL);
        fs_terran.setRelationship(Factions.LUDDIC_CHURCH, RepLevel.HOSTILE);
        fs_terran.setRelationship(Factions.LUDDIC_PATH, RepLevel.VENGEFUL);
        fs_terran.setRelationship(Factions.PIRATES, RepLevel.HOSTILE);
        fs_terran.setRelationship(Factions.PLAYER, RepLevel.NEUTRAL);

        fs_vasudan.setRelationship(Factions.INDEPENDENT, RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship(Factions.TRITACHYON, RepLevel.NEUTRAL);
        fs_vasudan.setRelationship(Factions.HEGEMONY, RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship(Factions.DIKTAT, RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship(Factions.LUDDIC_CHURCH, RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship(Factions.LUDDIC_PATH, RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship(Factions.PIRATES, RepLevel.SUSPICIOUS);
        fs_vasudan.setRelationship(Factions.PLAYER, RepLevel.NEUTRAL);

        fs_hammeroflight.setRelationship(Factions.INDEPENDENT, RepLevel.INHOSPITABLE);
        fs_hammeroflight.setRelationship(Factions.TRITACHYON, RepLevel.INHOSPITABLE);
        fs_hammeroflight.setRelationship(Factions.HEGEMONY, RepLevel.INHOSPITABLE);
        fs_hammeroflight.setRelationship(Factions.DIKTAT, RepLevel.INHOSPITABLE);
        fs_hammeroflight.setRelationship(Factions.LUDDIC_CHURCH, RepLevel.INHOSPITABLE);
        fs_hammeroflight.setRelationship(Factions.LUDDIC_PATH, RepLevel.WELCOMING);
        fs_hammeroflight.setRelationship(Factions.PIRATES, RepLevel.SUSPICIOUS);
        fs_hammeroflight.setRelationship(Factions.PLAYER, RepLevel.SUSPICIOUS);

        fs_newterrandawn.setRelationship(Factions.INDEPENDENT, RepLevel.INHOSPITABLE);
        fs_newterrandawn.setRelationship(Factions.TRITACHYON, RepLevel.INHOSPITABLE);
        fs_newterrandawn.setRelationship(Factions.HEGEMONY, RepLevel.INHOSPITABLE);
        fs_newterrandawn.setRelationship(Factions.DIKTAT, RepLevel.INHOSPITABLE);
        fs_newterrandawn.setRelationship(Factions.LUDDIC_CHURCH, RepLevel.INHOSPITABLE);
        fs_newterrandawn.setRelationship(Factions.LUDDIC_PATH, RepLevel.SUSPICIOUS);
        fs_newterrandawn.setRelationship(Factions.PIRATES, RepLevel.WELCOMING);
        fs_newterrandawn.setRelationship(Factions.PLAYER, RepLevel.SUSPICIOUS);

        fs_shivan.setRelationship(Factions.INDEPENDENT, RepLevel.HOSTILE);
        fs_shivan.setRelationship(Factions.TRITACHYON, RepLevel.HOSTILE);
        fs_shivan.setRelationship(Factions.HEGEMONY, RepLevel.HOSTILE);
        fs_shivan.setRelationship(Factions.DIKTAT, RepLevel.HOSTILE);
        fs_shivan.setRelationship(Factions.LUDDIC_CHURCH, RepLevel.HOSTILE);
        fs_shivan.setRelationship(Factions.LUDDIC_PATH, RepLevel.HOSTILE);
        fs_shivan.setRelationship(Factions.PIRATES, RepLevel.HOSTILE);
        fs_shivan.setRelationship(Factions.PLAYER, RepLevel.HOSTILE);

        //FS factions
        fs_terran.setRelationship("fs_vasudan", RepLevel.WELCOMING);
        fs_terran.setRelationship("fs_newterrandawn", RepLevel.HOSTILE);
        fs_terran.setRelationship("fs_hammeroflight", RepLevel.INHOSPITABLE);
        fs_terran.setRelationship("fs_shivan", RepLevel.VENGEFUL);
        fs_vasudan.setRelationship("fs_terran", RepLevel.WELCOMING);
        fs_vasudan.setRelationship("fs_newterrandawn", RepLevel.INHOSPITABLE);
        fs_vasudan.setRelationship("fs_hammeroflight", RepLevel.HOSTILE);
        fs_vasudan.setRelationship("fs_shivan", RepLevel.VENGEFUL);
        fs_newterrandawn.setRelationship("fs_hammeroflight", RepLevel.HOSTILE);
        fs_newterrandawn.setRelationship("fs_shivan", RepLevel.VENGEFUL);
        fs_hammeroflight.setRelationship("fs_newterrandawn", RepLevel.HOSTILE);
        fs_hammeroflight.setRelationship("fs_shivan", RepLevel.SUSPICIOUS);

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