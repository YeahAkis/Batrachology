package org.notionsmp.batrachology.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import org.notionsmp.batrachology.entity.PoisonDartFrogEntity;

import java.util.EnumSet;

public class FrogPanicGoal extends Goal {
    private final PoisonDartFrogEntity frog;
    private int panicTime = 0;
    private Vec3 targetPos;

    public FrogPanicGoal(PoisonDartFrogEntity frog) {
        this.frog = frog;
        this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.frog.getJumpCooldown() > 0 || !this.frog.onGround()) {
            return false;
        }

        if (panicTime > 0) {
            panicTime--;
            return true;
        }

        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return panicTime > 0;
    }

    @Override
    public void start() {
        this.targetPos = findPanicTarget();
        if (this.targetPos != null) {
            panicJump();
        }
    }

    @Override
    public void tick() {
        if (this.frog.onGround() && this.frog.getJumpCooldown() == 0 && panicTime > 0) {
            this.targetPos = findPanicTarget();
            if (this.targetPos != null) {
                panicJump();
            }
        }
    }

    private void panicJump() {
        Vec3 direction = this.targetPos.subtract(this.frog.position()).normalize();
        double power = 0.8;

        this.frog.setDeltaMovement(
                direction.x * power,
                this.frog._getJumpPower(),
                direction.z * power
        );

        float yRot = (float) (Math.atan2(direction.z, direction.x) * (180.0 / Math.PI)) - 90.0F;
        this.frog.setYRot(yRot);
        this.frog.yHeadRot = yRot;
        this.frog.yBodyRot = yRot;

        this.frog.setJumpCooldown(10 + this.frog.getRandom().nextInt(10));
        panicTime -= 10;
    }

    private Vec3 findPanicTarget() {
        double range = 6.0;
        double x = this.frog.getX() + (this.frog.getRandom().nextDouble() - 0.5) * 2 * range;
        double z = this.frog.getZ() + (this.frog.getRandom().nextDouble() - 0.5) * 2 * range;
        double y = this.frog.getY();

        return new Vec3(x, y, z);
    }

    public void triggerPanic() {
        this.panicTime = 100 + this.frog.getRandom().nextInt(60);
    }
}