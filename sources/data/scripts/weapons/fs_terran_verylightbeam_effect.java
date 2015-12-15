package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BeamAPI;
import com.fs.starfarer.api.combat.BeamEffectPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class fs_terran_verylightbeam_effect implements BeamEffectPlugin {

    // These are the beam source colors while the beam is firing. Get these from weapons.tbl
    private static final Color COLOR1 = new Color(0, 255, 0);
    private static final Color COLOR2 = new Color(160, 160, 0);
    private static final Color COLOR3 = new Color(255, 255, 255);

    private static final Vector2f ZERO = new Vector2f();

    private boolean firing = false;
    private boolean put=false;
    private final IntervalUtil interval = new IntervalUtil(0.1f, 0.1f);
    private float level = 0f;
    private float sinceLast = 0f;

    @Override
    public void advance(float amount, CombatEngineAPI engine, BeamAPI beam) {
        if (Global.getCombatEngine().isPaused()) {
            return;
        }

        Vector2f origin = new Vector2f(beam.getWeapon().getLocation());
        Vector2f offset = new Vector2f(5f, 0f);
        VectorUtils.rotate(offset, beam.getWeapon().getCurrAngle(), offset);
        Vector2f.add(offset, origin, origin);

        if (firing) {
            if (beam.getBrightness() < level) {
                firing = false;
                if (Global.getCombatEngine().getTotalElapsedTime(false) - sinceLast > 0.3f) {
                    Vector2f origin2 = new Vector2f(beam.getWeapon().getLocation());
                    offset = new Vector2f(5f, 0f);
                    VectorUtils.rotate(offset, beam.getWeapon().getCurrAngle(), offset);
                    Vector2f.add(offset, origin2, origin2);
                    Global.getSoundPlayer().playSound("bt_dwn_1", 1.0f, 1.0f, origin2, new Vector2f());
                }
            }
        } else {
            if (beam.getBrightness() > level) {
                // We're firing now! Yay!
                firing = true;
                if (Global.getCombatEngine().getTotalElapsedTime(false) - sinceLast > 0.3f) {
                    // Play the beam firing sound
                    Global.getSoundPlayer().playSound("bt_lterslash", 1f, 1.5f, origin, new Vector2f());
                }

                sinceLast = Global.getCombatEngine().getTotalElapsedTime(false);
                // Make the beam source glow during firing
                Global.getCombatEngine().addHitParticle(origin, ZERO, 100f, 5f, 0.2f, COLOR1);
                Global.getCombatEngine().addHitParticle(origin, ZERO, 75f, 5f, 0.5f, COLOR2);
                Global.getCombatEngine().addHitParticle(origin, ZERO, 50f, 5f, 0.5f, COLOR3);
            }
        }
        level = beam.getBrightness();

        interval.advance(amount);
        // Make the beam source area do fun glowy things as long as the beam is firing. Grab these from the main colors.
        Global.getCombatEngine().addHitParticle(origin, new Vector2f(), (float) Math.random() * 50f + 50f, 0.2f, 0.2f, new Color(
                MathUtils.getRandomNumberInRange(0, 160), MathUtils.getRandomNumberInRange(160, 255),
                MathUtils.getRandomNumberInRange(0, 10), 255));

        if (interval.intervalElapsed()) {
            // If the beam damaged the target, spawn an explosion where the beam hits
            if (beam.getDamageTarget() != null) {
                Global.getCombatEngine().spawnExplosion(beam.getTo(), new Vector2f(), new Color(MathUtils.getRandomNumberInRange(225, 255),
                                MathUtils.getRandomNumberInRange(100, 150),
                                MathUtils.getRandomNumberInRange(0, 100), 255),
                        (float) Math.random() * 20f + 20f, 0.75f);
            }
        }
    }
}