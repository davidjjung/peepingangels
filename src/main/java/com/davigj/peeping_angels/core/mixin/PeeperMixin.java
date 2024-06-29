package com.davigj.peeping_angels.core.mixin;

import com.davigj.peeping_angels.core.PAConfig;
import com.davigj.peeping_angels.core.other.PeepUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Peeper;
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

import java.util.UUID;

@Mixin(Peeper.class)
public abstract class PeeperMixin extends Creeper {
    @Shadow
    private static final UUID FREEZE_MODIFIER_UUID = UUID.fromString("113f0691-d920-423d-acd2-9ca0c577991f");
    @Shadow
    private static final UUID SPEED_UP_MODIFIER_UUID = UUID.fromString("6866925d-f410-42b9-b2f2-7a22c60a6380");
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
    public void tick(){
        Peeper peep = (Peeper) (Object) this;
        CreeperAccessor creep = (CreeperAccessor) peep;
        if (peep.isAlive()) {
            AttributeInstance speedAttribute = peep.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttribute.getModifier(FREEZE_MODIFIER_UUID) != null) {
                speedAttribute.removeModifier(FREEZE_MODIFIER_UUID);
            }

            LivingEntity var3 = peep.getTarget();
            if (var3 instanceof Player player) {
                if (player.hasLineOfSight(this) && PeepUtil.isPlayerFacingEntity(player, peep)) {
                    speedAttribute.addTransientModifier(FREEZE_MODIFIER);
                    peep.getLookControl().setLookAt(peep.getTarget().getX(), peep.getTarget().getEyeY(), peep.getTarget().getZ());
                } else {
                    ++followingTicks;
                    if (peep.distanceToSqr(peep.getTarget()) >= 9.0) {
                        peep.setSwellDir(-2);
                    }
                    int multiplier = creep.getSwell() > 3 && PAConfig.COMMON.steamBlow.get() ? PAConfig.COMMON.steamMultiplier.get() : 1;
                    speedAttribute.removeModifier(SPEED_UP_MODIFIER_UUID);
                    speedAttribute.addTransientModifier(new AttributeModifier(SPEED_UP_MODIFIER_UUID, "Peeper speed boost",
                            Math.min((double)this.followingTicks * PAConfig.COMMON.accelerationInterval.get() * multiplier, PAConfig.COMMON.maxSpeedBoost.get()), AttributeModifier.Operation.ADDITION));
                }
            }

            if (peep.getTarget() == null) {
                speedAttribute.removeModifier(SPEED_UP_MODIFIER_UUID);
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
        return speedAttribute.getModifier(FREEZE_MODIFIER_UUID) != null ? p_21260_ : super.tickHeadTurn(p_21260_, p_21261_);
    }
}
