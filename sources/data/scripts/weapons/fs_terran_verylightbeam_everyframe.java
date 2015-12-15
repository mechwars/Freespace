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

public class fs_terran_verylightbeam_everyframe implements EveryFrameWeaponEffectPlugin {

    // Variables for beam charging and animation
    private static final Vector2f ZERO = new Vector2f();
    private boolean charging = false;
    private boolean firing = false;

    // Variables for beam slicing
    private float TURN_SPEED;
    private ShipAPI SHIP;
    private float ARC;
    private float ARC_FACING;
    private float target_facing;
    private boolean isFiring = false;
    private float timeOffset = 0.0F;
    private boolean runOnce = false;
    private boolean turretMode = true;
    private boolean followShip = false;
    private final Integer SLASH_WIDTH = Integer.valueOf(21);

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
                Global.getCombatEngine().addHitParticle(origin, ZERO, (float) Math.random() * 50f + 50f * weapon.getChargeLevel(), weapon.getChargeLevel() *
                                0.3f, 0.2f,
                        new Color(MathUtils.getRandomNumberInRange(0, 10), MathUtils.getRandomNumberInRange(160, 255),
                                MathUtils.getRandomNumberInRange(0, 160), 255));
            } else {
                firing = true;
            }
        } else {
            // The weapon has just started charging to fire, so play the appropriate sound file
            if (weapon.getChargeLevel() > 0f && weapon.getCooldownRemaining() <= 0f) {
                charging = true;
                // Change this for each beam
                Global.getSoundPlayer().playSound("bt_up_3", 1f, 1f, origin, weapon.getShip().getVelocity());
            }
        }
        // End Beam Weapon Charging Code

        // Begin Slicing Beam Code
        if (!this.runOnce)
        {
            this.TURN_SPEED = weapon.getTurnRate();
            this.SHIP = weapon.getShip();
            this.ARC = weapon.getArc();
            this.ARC_FACING = weapon.getArcFacing();
            if (weapon.getArc() <= this.SLASH_WIDTH.intValue()) {
                this.turretMode = false;
            }
            this.runOnce = true;
        }
        if (!this.turretMode)
        {
            if (weapon.isFiring())
            {
                if (!this.isFiring)
                {
                    this.isFiring = true;

                    float angleInArc = MathUtils.getShortestRotation(this.ARC_FACING + this.SHIP.getFacing(), weapon.getCurrAngle());
                    float positionInArc = Math.min(1.0F, Math.max(-1.0F, 2.0F * angleInArc / this.ARC));
                    this.timeOffset = ((float)-Math.asin(positionInArc));
                }
                this.timeOffset += amount * (this.TURN_SPEED / 6.2831855F);
                float slashing = -(float)Math.sin(this.timeOffset) * this.ARC / 2.0F;

                weapon.setCurrAngle(this.SHIP.getFacing() + this.ARC_FACING + slashing);
            }
            else
            {
                this.isFiring = false;
            }
        }
        else if (weapon.isFiring())
        {
            if (!this.isFiring)
            {
                this.isFiring = true;
                this.target_facing = weapon.getCurrAngle();
                this.followShip = false;
                float aiming = MathUtils.getShortestRotation(this.ARC_FACING + this.SHIP.getFacing(), weapon.getCurrAngle());
                if ((this.ARC != 360.0F) && (aiming > this.ARC / 2.0F - this.SLASH_WIDTH.intValue() / 2))
                {
                    this.target_facing = MathUtils.clampAngle(this.ARC_FACING + this.SHIP.getFacing() + this.ARC / 2.0F - this.SLASH_WIDTH.intValue() / 2 - 1.0F);
                    float angleInArc = MathUtils.getShortestRotation(this.target_facing, weapon.getCurrAngle());
                    float positionInArc = Math.min(1.0F, Math.max(-1.0F, 2.0F * angleInArc / this.SLASH_WIDTH.intValue()));
                    this.timeOffset = ((float)-Math.asin(positionInArc));
                }
                else if ((this.ARC != 360.0F) && (aiming < -this.ARC / 2.0F + this.SLASH_WIDTH.intValue() / 2))
                {
                    this.target_facing = MathUtils.clampAngle(this.ARC_FACING + this.SHIP.getFacing() - this.ARC / 2.0F + this.SLASH_WIDTH.intValue() / 2 + 1.0F);
                    float angleInArc = MathUtils.getShortestRotation(this.target_facing, weapon.getCurrAngle());
                    float positionInArc = Math.min(1.0F, Math.max(-1.0F, 2.0F * angleInArc / this.SLASH_WIDTH.intValue()));
                    this.timeOffset = ((float)-Math.asin(positionInArc));
                }
                else
                {
                    this.timeOffset = 0.0F;
                }
            }
            this.timeOffset += amount * (this.TURN_SPEED / 6.2831855F);
            float slashing = -(float)Math.sin(this.timeOffset) * this.SLASH_WIDTH.intValue() / 2.0F;
            float newAngle = this.target_facing + slashing;
            if ((!this.followShip) && (this.ARC != 360.0F) && (MathUtils.getShortestRotation(this.ARC_FACING + this.SHIP.getFacing(), newAngle) > this.ARC / 2.0F))
            {
                this.timeOffset = -1.5707964F;
                slashing = -(float)Math.sin(this.timeOffset) * this.SLASH_WIDTH.intValue() / 2.0F;
                this.followShip = true;
                this.target_facing = MathUtils.clampAngle(this.ARC_FACING + this.ARC / 2.0F - this.SLASH_WIDTH.intValue() / 2);
            }
            else if ((!this.followShip) && (this.ARC != 360.0F) && (MathUtils.getShortestRotation(this.ARC_FACING + this.SHIP.getFacing(), newAngle) < -this.ARC / 2.0F))
            {
                this.timeOffset = 1.5707964F;
                slashing = -(float)Math.sin(this.timeOffset) * this.SLASH_WIDTH.intValue() / 2.0F;
                this.followShip = true;
                this.target_facing = MathUtils.clampAngle(this.ARC_FACING - this.ARC / 2.0F + this.SLASH_WIDTH.intValue() / 2);
            }
            if (this.followShip) {
                weapon.setCurrAngle(MathUtils.clampAngle(this.SHIP.getFacing() + this.target_facing + slashing));
            } else {
                weapon.setCurrAngle(MathUtils.clampAngle(newAngle));
            }
        }
        else
        {
            this.isFiring = false;
        }
        // End Slicing Beam Code
    }
}