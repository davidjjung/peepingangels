package com.davigj.peeping_angels.core.mixin;

import com.teamabnormals.caverns_and_chasms.common.entity.monster.Peeper;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.Control;
import net.minecraft.world.entity.ai.control.MoveControl;
import org.checkerframework.common.reflection.qual.Invoke;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(MoveControl.class)
public abstract class MoveControlMixin implements Control {
    @Unique
    private static final UUID FREEZE_MODIFIER_UUID = UUID.fromString("113f0691-d920-423d-acd2-9ca0c577991f");

    @Mutable
    @Final
    @Shadow
    protected final Mob mob;

    protected MoveControlMixin(Mob mob) {
        this.mob = mob;
    }


    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Mob;getJumpControl()Lnet/minecraft/world/entity/ai/control/JumpControl;"), cancellable = true)
    public void doNotJump(CallbackInfo ci){
        if (mob instanceof Peeper peep) {
            AttributeInstance speedAttribute = peep.getAttribute(Attributes.MOVEMENT_SPEED);
            if (speedAttribute.getModifier(FREEZE_MODIFIER_UUID) != null) {
                ci.cancel();
            }
        }
    }
}
