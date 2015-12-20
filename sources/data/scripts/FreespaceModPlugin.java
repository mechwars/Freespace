package data.scripts;

import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.PluginPick;
import com.fs.starfarer.api.campaign.CampaignPlugin;
import com.fs.starfarer.api.combat.MissileAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import org.dark.shaders.light.LightData;
import org.dark.shaders.util.ShaderLib;
import org.dark.shaders.util.TextureData;
import data.scripts.world.freespace.fs_gen;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class FreespaceModPlugin extends BaseModPlugin {

    public static final boolean isExerelin;

    static {
        boolean foundExerelin;
        try {
            Global.getSettings().getScriptClassLoader().loadClass("data.scripts.world.ExerelinGen");
            foundExerelin = true;
        } catch (ClassNotFoundException ex) {
            foundExerelin = false;
        }
        isExerelin = foundExerelin;
    }

    @Override
    public void onApplicationLoad() {
        ShaderLib.init();
        LightData.readLightDataCSV("data/lights/fs_light_data.csv");
        TextureData.readTextureDataCSV("data/lights/fs_texture_data.csv");
    }

    @Override
    public void onNewGame() {
        initFS();
    }

    private static void initFS() {
        if (!isExerelin) {
            new fs_gen().generate(Global.getSector());
        }
    }
}