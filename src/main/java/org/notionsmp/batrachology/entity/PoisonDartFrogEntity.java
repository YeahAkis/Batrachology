package org.notionsmp.batrachology.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;
import org.notionsmp.batrachology.entity.ai.FrogJumpGoal;
import org.notionsmp.batrachology.entity.ai.FrogPanicGoal;
import org.notionsmp.batrachology.item.ModItems;

public class PoisonDartFrogEntity extends Animal {
    private static final EntityDataAccessor<String> FROG_TYPE = SynchedEntityData.defineId(PoisonDartFrogEntity.class, EntityDataSerializers.STRING);
    private int jumpCooldown = 0;
    private FrogPanicGoal panicGoal;

    public PoisonDartFrogEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.jumpControl = new FrogJumpControl(this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FROG_TYPE, FrogType.AZURE.getId());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.panicGoal = new FrogPanicGoal(this);
        this.goalSelector.addGoal(1, this.panicGoal);
        this.goalSelector.addGoal(2, new FrogJumpGoal(this));
    }

    @Override
    public void tick() {
        super.tick();

        if (jumpCooldown > 0) {
            jumpCooldown--;
        }

        if (this.hasCustomName() && this.getCustomName().getString().equalsIgnoreCase("akis")) {
            this.setFrogType(FrogType.AKIS);
        }
    }

    @Override
    protected void jumpFromGround() {
        super.jumpFromGround();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);

        if (itemstack.is(Items.BOWL)) {
            ItemStack frogBowl = createFrogBowl();

            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            if (itemstack.isEmpty()) {
                player.setItemInHand(hand, frogBowl);
            } else if (!player.getInventory().add(frogBowl)) {
                player.drop(frogBowl, false);
            }

            this.discard();
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }

        return InteractionResult.PASS;
    }

    private ItemStack createFrogBowl() {
        FrogType type = getFrogType();
        ItemStack bowl = switch (type) {
            case AZURE -> new ItemStack(ModItems.AZURE_FROG_BOWL.get());
            case BUMBLEBEE -> new ItemStack(ModItems.BUMBLEBEE_FROG_BOWL.get());
            case GREEN_BLACK -> new ItemStack(ModItems.GREEN_BLACK_FROG_BOWL.get());
            case GOLDEN -> new ItemStack(ModItems.GOLDEN_FROG_BOWL.get());
            case STRAWBERRY -> new ItemStack(ModItems.STRAWBERRY_FROG_BOWL.get());
            case FIERY -> new ItemStack(ModItems.FIERY_FROG_BOWL.get());
            case NOMINAL -> new ItemStack(ModItems.NOMINAL_FROG_BOWL.get());
            case AKIS -> new ItemStack(ModItems.AKIS_FROG_BOWL.get());
        };

        if (this.hasCustomName()) {
            bowl.setHoverName(this.getCustomName());
        }

        CompoundTag tag = bowl.getOrCreateTag();
        tag.putString("FrogType", type.getId());
        tag.putBoolean("FromEntity", true);

        return bowl;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide && source.getEntity() instanceof LivingEntity attacker) {
            applyPoisonEffect(attacker, 0.5f);

            if (this.panicGoal != null) {
                this.panicGoal.triggerPanic();
            }
        }
        return super.hurt(source, amount);
    }

    public void applyPoisonEffect(LivingEntity target, float durationMultiplier) {
        FrogType type = getFrogType();
        if (type == FrogType.AKIS) return;

        MobEffectInstance effect = type.getEffectInstance(durationMultiplier);
        if (effect != null) {
            target.addEffect(effect);
        }
    }

    public FrogType getFrogType() {
        try {
            return FrogType.valueOf(this.entityData.get(FROG_TYPE).toUpperCase());
        } catch (IllegalArgumentException e) {
            return FrogType.AZURE;
        }
    }

    public void setFrogType(FrogType type) {
        this.entityData.set(FROG_TYPE, type.getId());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("FrogType", getFrogType().getId());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("FrogType")) {
            try {
                this.setFrogType(FrogType.valueOf(tag.getString("FrogType").toUpperCase()));
            } catch (IllegalArgumentException e) {
                this.setFrogType(FrogType.AZURE);
            }
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return null;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.FROG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FROG_DEATH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.FROG_AMBIENT;
    }

    @Override
    protected float getJumpPower() {
        return 0.42F * this.getBlockJumpFactor();
    }

    public float _getJumpPower() {
        return this.getJumpPower();
    }

    public void setJumpCooldown(int cooldown) {
        this.jumpCooldown = cooldown;
    }

    public int getJumpCooldown() {
        return jumpCooldown;
    }

    public void triggerPanic() {
        if (this.panicGoal != null) {
            this.panicGoal.triggerPanic();
        }
    }

    private static class FrogJumpControl extends JumpControl {
        private final PoisonDartFrogEntity frog;

        public FrogJumpControl(PoisonDartFrogEntity frog) {
            super(frog);
            this.frog = frog;
        }

        @Override
        public void tick() {
            if (this.jump) {
                this.frog.jumpFromGround();
                this.jump = false;
            }
        }
    }

    public static boolean checkFrogSpawnRules(EntityType<PoisonDartFrogEntity> entityType, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        if (spawnType == MobSpawnType.SPAWNER) {
            return true;
        }

        if (pos.getY() > level.getSeaLevel()) {
            return false;
        }

        return Animal.checkAnimalSpawnRules(entityType, level, spawnType, pos, random);
    }
}