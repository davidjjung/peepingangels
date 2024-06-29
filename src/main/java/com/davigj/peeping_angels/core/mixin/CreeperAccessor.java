package com.davigj.peeping_angels.core.mixin;

import net.minecraft.world.entity.monster.Creeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Creeper.class)
public interface CreeperAccessor {
    @Accessor("swell")
    int getSwell();

    @Accessor("swell")
    void setSwell(int value);

    @Accessor("oldSwell")
    void setOldSwell(int value);

    @Accessor("maxSwell")
    int getMaxSwell();
}
