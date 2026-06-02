package com.davigj.peeping_angels.core;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class PAConfig {
    public static final PAConfig.Common COMMON;
    static final ModConfigSpec COMMON_SPEC;

    static {
        final Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(PAConfig.Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        public final ModConfigSpec.ConfigValue<Double> lookThreshold;
        public final ModConfigSpec.ConfigValue<Double> maxSpeedBoost;
        public final ModConfigSpec.ConfigValue<Double> accelerationInterval;
        public final ModConfigSpec.ConfigValue<Boolean> steamBlow;
        public final ModConfigSpec.ConfigValue<Integer> steamMultiplier;

        Common(ModConfigSpec.Builder builder) {
            builder.push("changes");
            lookThreshold = builder.comment("Looking threshold.\n Closer to -1 means the player's head must be looking at the peeper more directly to make it freeze").define("Looking threshold", -0.5);
            maxSpeedBoost = builder.comment("Highest the peeper speed bonus can get. \n Default is currently CnC's default, feels a bit slow in this context though").define("Max speed boost", 0.23);
            accelerationInterval = builder.comment("Unit by which peeper's speed increases per tick when moving").define("Acceleration interval", 4.0E-4);
            steamBlow = builder.comment("Peepers move faster while defusing").define("blowing off steam", false);
            steamMultiplier = builder.comment("Mulitiplier for the speed bonus when defusing and able to move").define("defuse multiplier", 2);
            builder.pop();
        }
    }
}
