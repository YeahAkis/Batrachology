package org.notionsmp.batrachology.item.custom;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.notionsmp.batrachology.Batrachology;
import org.notionsmp.batrachology.entity.FrogType;
import org.notionsmp.batrachology.entity.PoisonDartFrogEntity;

public class FrogBowlItem extends Item {
    private final FrogType frogType;

    public FrogBowlItem(Properties properties, FrogType frogType) {
        super(properties.craftRemainder(Items.BOWL).stacksTo(1));
        this.frogType = frogType;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof Player player) {
            if (player instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
            }

            player.awardStat(Stats.ITEM_USED.get(this));

            if (!level.isClientSide) {
                applyPoisonEffect(player);
            }
        }
        return stack;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        InteractionHand hand = context.getHand();
        ItemStack stack = context.getItemInHand();

        if (player != null && player.isShiftKeyDown()) {
            if (!level.isClientSide) {
                Vec3 spawnPos = Vec3.atBottomCenterOf(pos.relative(context.getClickedFace()));

                PoisonDartFrogEntity frog = new PoisonDartFrogEntity(
                        Batrachology.POISON_DART_FROG.get(),
                        level
                );

                frog.setPos(spawnPos);
                frog.setFrogType(frogType);

                if (stack.hasCustomHoverName()) {
                    frog.setCustomName(stack.getHoverName());
                }

                level.addFreshEntity(frog);
                level.gameEvent(player, GameEvent.ENTITY_PLACE, spawnPos);

                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                    ItemStack bowl = new ItemStack(Items.BOWL);
                    if (stack.isEmpty()) {
                        player.setItemInHand(hand, bowl);
                    } else if (!player.getInventory().add(bowl)) {
                        player.drop(bowl, false);
                    }
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }

        return super.useOn(context);
    }

    private void applyPoisonEffect(Player player) {
        MobEffectInstance effect = frogType.getEffectInstance();
        if (effect != null) {
            player.addEffect(effect);
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (player.isShiftKeyDown()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }
        return ItemUtils.startUsingInstantly(level, player, hand);
    }

    public FrogType getFrogType() {
        return frogType;
    }
}