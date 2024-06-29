package com.davigj.peeping_angels.core.other;

import com.davigj.peeping_angels.core.PAConfig;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Peeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class PeepUtil {
    public static boolean isPlayerFacingEntity(Player player, Peeper peep) {
        Vec3 playerLookVec = player.getLookAngle().normalize();
        Vec3 vecToPeeper = player.getEyePosition().subtract(peep.getEyePosition()).normalize();
        double dotProduct = playerLookVec.dot(vecToPeeper);
        return dotProduct < PAConfig.COMMON.lookThreshold.get();
    }
}
