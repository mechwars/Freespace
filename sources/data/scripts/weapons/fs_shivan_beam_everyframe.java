package data.scripts.weapons;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameWeaponEffectPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

public class fs_shivan_beam_everyframe implements EveryFrameWeaponEffectPlugin {

    // Variables for beam charging and animation
    private static final Vector2f ZERO = new Vector2f();
    private boolean charging = false;
    private boolean firing = false;

    @Override
    public void advance(float amount, CombatEngineAPI engine, WeaponAPI weapon) {
        if (Global.getCombatEngine().isPaused()) {
            return;
        }

        // Begin Beam Weapon Charging Code
        // Set variables for weapon location vector, current angle, etc.
        Vector2f origin = new Vector2f(weapon.getLocation());
        Vector2f offset = new Vector2f(5f, 0f);
        VectorUtils.rotate(offset, weapon.getCurrAngle(), offset);
        Vector2f.add(offset, origin, origin);

        // Check the value of charging. It starts as false, and then the script actually checks to see if the weapon
        // is charging. If it is, play a sound and display a charge glow until the weapon is being fired.
        if (charging) {
            // Now that the weapon is charging, let's generate things.
            if (firing && weapon.getCooldownRemaining() <= 0f && weapon.getChargeLevel() < 1f) {
                charging = false;
                firing = false;
            } else if (weapon.getChargeLevel() < 1f && weapon.getCooldownRemaining() <= 0f) {
                // The weapon is charging, so display the charge glow
                // Set the RGB color + alpha value of the beam charging glow
                Global.getCombatEngine().addHitParticle(origin, ZERO, (float) Math.random() * 75f + 75f * weapon.getChargeLevel(), weapon.getChargeLevel() *
                                0.3f, 0.2f,
                        new Color(MathUtils.getRandomNumberInRange(220, 255), MathUtils.getRandomNumberInRange(0, 10),
                                MathUtils.getRandomNumberInRange(0, 10), 255));
            } else {
                firing = true;
            }
        } else {
            // The weapon has just started charging to fire, so play the appropriate sound file
            if (weapon.getChargeLevel() > 0f && weapon.getCooldownRemaining() <= 0f) {
                charging = true;
                // Change this for each beam
                Global.getSoundPlayer().playSound("bs_up_1.5", 1f, 1f, origin, weapon.getShip().getVelocity());
            }
        }
        // End Beam Weapon Charging Code

    }
}