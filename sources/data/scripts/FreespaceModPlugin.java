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

    @Override
    public void onApplicationLoad() {
        ShaderLib.init();
        LightData.readLightDataCSV("data/lights/fs_light_data.csv");
        TextureData.readTextureDataCSV("data/lights/fs_texture_data.csv");
    }

    private static void initFS() {
            new fs_gen().generate(Global.getSector());
    }

    @Override
    public void onNewGame() {

        try {
            //Got Exerelin, so load Exerelin
            Class<?> def = Global.getSettings().getScriptClassLoader().loadClass("exerelin.campaign.SectorManager");
            Method method;
            try {
                method = def.getMethod("getCorvusMode");
                Object result = method.invoke(def);
                if ((boolean) result)
                {
                    // Exerelin running in Corvus mode, go ahead and generate our sector
                    initFS();
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException |
                    InvocationTargetException ex) {
                // check failed, do nothing
            }

        } catch (ClassNotFoundException ex) {
            // Exerelin not found so continue and run normal generation code
            initFS();
        }
    }
}