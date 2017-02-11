package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.shared.SharedData;
import data.scripts.world.freespace.Aldebaran;
import data.scripts.world.freespace.Cocytus;
import data.scripts.world.freespace.EpsilonPegasi;
import org.dark.shaders.light.LightData;
import org.dark.shaders.util.ShaderLib;
import org.dark.shaders.util.TextureData;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FreespaceModPlugin extends BaseModPlugin {

    public static boolean hasGraphicsLib;

    @Override
    public void onApplicationLoad() {
        hasGraphicsLib = Global.getSettings().getModManager().isModEnabled("shaderLib");

        if (hasGraphicsLib) {
            ShaderLib.init();
            LightData.readLightDataCSV("data/lights/fs_light_data.csv");
            TextureData.readTextureDataCSV("data/lights/fs_texture_data.csv");
        }
    }


    private static void initFS() {
        new EpsilonPegasi().generate(Global.getSector());
        new Aldebaran().generate(Global.getSector());
        new Cocytus().generate(Global.getSector());
    }

    public void onNewGame()
    {
        try {
            Class<?> def = Global.getSettings().getScriptClassLoader().loadClass("exerelin.campaign.SectorManager");
            Method method;
            try {
                method = def.getMethod("getCorvusMode");
                Object result = method.invoke(def);
                if ((Boolean)result)
                {
                    initFS();
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            }

        } catch (ClassNotFoundException ex) {
            initFS();
        }
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_terran");
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_vasudan");
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_shivan");
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_hammeroflight");
        SharedData.getData().getPersonBountyEventData().addParticipatingFaction("fs_newterrandawn");
    }
}