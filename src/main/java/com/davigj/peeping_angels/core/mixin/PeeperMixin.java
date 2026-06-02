package com.davigj.peeping_angels.core.mixin;

import com.davigj.peeping_angels.core.PAConfig;
import com.davigj.peeping_angels.core.other.PeepUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.Peeper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = Peeper.class, remap = false)
public abstract class PeeperMixin extends Creeper {
    @Shadow
    @Final
    private static ResourceLocation FREEZE_MODIFIER_ID;

    @Shadow
    @Final
    private static ResourceLocation SPEED_UP_MODIFIER;

    @Shadow
    @Final
    private static AttributeModifier FREEZE_MODIFIER;

    @Shadow
    private int followingTicks;

    public PeeperMixin(EntityType<? extends Creeper> p_32278_, Level p_32279_) {
        super(p_32278_, p_32279_);
    }

    /**
     * @author DavigJ
     * @reason It was either this or a surreptitious head inject that would've done the same thing. I hope it isn't offensive wheeee
     */
    @Overwrite
    public void tick() {
        Peeper peep = (Peeper) (Object) this;
        CreeperAccessor creep = (CreeperAccessor) peep;
        if (peep.isAlive()) {
            AttributeInstance speedAttribute = peep.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttribute != null && speedAttribute.getModifier(FREEZE_MODIFIER_ID) != null) {
                speedAttribute.removeModifier(FREEZE_MODIFIER_ID);
            }

            LivingEntity target = peep.getTarget();
            if (target instanceof Player player) {
                if (player.hasLineOfSight(this) && PeepUtil.isPlayerFacingEntity(player, peep)) {
                    peep.setDeltaMovement(0, peep.getDeltaMovement().y, 0);

                    peep.xxa = 0.0F;
                    peep.yya = 0.0F;
                    peep.zza = 0.0F;

                    peep.getNavigation().stop();

                    if (speedAttribute != null) {
                        speedAttribute.addTransientModifier(FREEZE_MODIFIER);
                    }
                    peep.getLookControl().setLookAt(target.getX(), target.getEyeY(), target.getZ());
                } else {
                    ++followingTicks;
                    if (peep.distanceToSqr(target) >= 9.0) {
                        peep.setSwellDir(-2);
                    }
                    int multiplier = creep.getSwell() > 3 && PAConfig.COMMON.steamBlow.get() ? PAConfig.COMMON.steamMultiplier.get() : 1;
                    if (speedAttribute != null) {
                        speedAttribute.removeModifier(SPEED_UP_MODIFIER);
                        speedAttribute.addTransientModifier(new AttributeModifier(SPEED_UP_MODIFIER,
                                Math.min((double) this.followingTicks * PAConfig.COMMON.accelerationInterval.get() * multiplier, PAConfig.COMMON.maxSpeedBoost.get()), AttributeModifier.Operation.ADD_VALUE));
                    }
                }
            }

            if (peep.getTarget() == null) {
                if (speedAttribute != null) {
                    speedAttribute.removeModifier(SPEED_UP_MODIFIER);
                }
            }

            creep.setOldSwell(creep.getSwell());
            if (peep.isIgnited()) {
                peep.setSwellDir(1);
            }

            int i = peep.getSwellDir();
            if (i > 0 && creep.getSwell() == 0) {
                peep.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
                peep.gameEvent(GameEvent.PRIME_FUSE);
            }

            creep.setSwell(creep.getSwell() + 1);
            if (creep.getSwell() < 0) {
                creep.setSwell(0);
            }

            if (creep.getSwell() >= creep.getMaxSwell()) {
                creep.setSwell(creep.getMaxSwell());
                peep.explodeCreeper();
            }
        } else if (creep.getSwell() > 0) {
            creep.setSwell(creep.getSwell() - 1);
        }
        super.tick();
    }

    protected float tickHeadTurn(float p_21260_, float p_21261_) {
        Peeper peep = (Peeper) (Object) this;
        AttributeInstance speedAttribute = peep.getAttribute(Attributes.MOVEMENT_SPEED);
        return speedAttribute != null && speedAttribute.getModifier(FREEZE_MODIFIER_ID) != null ? p_21260_ : super.tickHeadTurn(p_21260_, p_21261_);
    }
}