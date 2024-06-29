package com.davigj.peeping_angels.core.mixin;

import com.davigj.peeping_angels.core.other.PeepUtil;
import com.teamabnormals.caverns_and_chasms.common.entity.ai.goal.PeeperSwellGoal;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Peeper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.*;

import javax.annotation.Nullable;

@Mixin(PeeperSwellGoal.class)
public abstract class PeeperSwellGoalMixin {
    @Mutable
    @Shadow
    @Final
    private final Peeper peeper;
    @Shadow
    @Nullable
    private LivingEntity target;

    protected PeeperSwellGoalMixin(Peeper peeper) {
        this.peeper = peeper;
    }

    /**
     * @author DavigJ
     * @reason Changing peeper offense criteria
     */
    @Overwrite
    public void tick() {
        if (this.target == null) {
            this.peeper.setSwellDir(-1);
        } else if (this.peeper.distanceToSqr(this.target) > 49.0) {
            this.peeper.setSwellDir(-1);
        } else if (!this.peeper.getSensing().hasLineOfSight(this.target)) {
            this.peeper.setSwellDir(-1);
        } else {
            LivingEntity var3;
            if (this.peeper.distanceToSqr(this.target) >= 9.0) {
                var3 = this.target;
                if (var3 instanceof Player player) {
                    if (!(player.hasLineOfSight(peeper) && PeepUtil.isPlayerFacingEntity(player, peeper))) {
                        this.peeper.setSwellDir(-1);
                        return;
                    }
                }
            }

            var3 = this.target;
            if (var3 instanceof Player player) {
                if (player.hasLineOfSight(peeper) && PeepUtil.isPlayerFacingEntity(player, peeper)) {
                    this.peeper.setSwellDir(0);
                    return;
                }
            }

            if (this.peeper.getSwellDir() == 0) {
                this.peeper.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
            }

            this.peeper.setSwellDir(1);
        }
    }
}
