package com.davigj.peeping_angels.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.creeper.Peeper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.Control;
import net.minecraft.world.entity.ai.control.MoveControl;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MoveControl.class)
public abstract class MoveControlMixin implements Control {
    @Unique
    private static final ResourceLocation FREEZE_MODIFIER_ID = ResourceLocation.fromNamespaceAndPath("caverns_and_chasms", "peeper_frozen");

    @Mutable
    @Final
    @Shadow
    protected final Mob mob;

    protected MoveControlMixin(Mob mob) {
        this.mob = mob;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void doNotJump(CallbackInfo ci) {
        if (mob instanceof Peeper peep) {
            AttributeInstance speedAttribute = peep.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttribute != null && speedAttribute.getModifier(FREEZE_MODIFIER_ID) != null) {
                ci.cancel();
            }
        }
    }
}