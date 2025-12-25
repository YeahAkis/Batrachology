package org.notionsmp.batrachology.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.notionsmp.batrachology.entity.PoisonDartFrogEntity;

import java.util.EnumSet;

public class FrogJumpGoal extends Goal {
    private final PoisonDartFrogEntity frog;
    private int jumpDelay = 0;
    private Vec3 targetPos;

    public FrogJumpGoal(PoisonDartFrogEntity frog) {
        this.frog = frog;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.frog.getJumpCooldown() > 0 || !this.frog.onGround()) {
            return false;
        }

        if (jumpDelay > 0) {
            jumpDelay--;
            return false;
        }

        if (this.frog.getRandom().nextInt(100) < 5) {
            this.targetPos = findJumpTarget();
            return targetPos != null;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.frog.onGround();
    }

    @Override
    public void start() {
        Vec3 direction = this.targetPos.subtract(this.frog.position()).normalize();
        double power = 0.6;

        this.frog.setDeltaMovement(
                direction.x * power,
                this.frog._getJumpPower(),
                direction.z * power
        );

        float yRot = (float) (Math.atan2(direction.z, direction.x) * (180.0 / Math.PI)) - 90.0F;
        this.frog.setYRot(yRot);
        this.frog.yHeadRot = yRot;
        this.frog.yBodyRot = yRot;

        this.frog.setJumpCooldown(20 + this.frog.getRandom().nextInt(20));
        this.jumpDelay = 40 + this.frog.getRandom().nextInt(40);
    }

    private Vec3 findJumpTarget() {
        double range = 4.0;
        double x = this.frog.getX() + (this.frog.getRandom().nextDouble() - 0.5) * 2 * range;
        double z = this.frog.getZ() + (this.frog.getRandom().nextDouble() - 0.5) * 2 * range;
        double y = this.frog.getY();

        return new Vec3(x, y, z);
    }
}