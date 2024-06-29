package com.davigj.peeping_angels.core.other;

import com.davigj.peeping_angels.core.PeepingAngels;
import com.teamabnormals.caverns_and_chasms.common.entity.monster.Peeper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PeepingAngels.MOD_ID)
public class PAEvents {

    @SubscribeEvent
    public static void notCulled(LivingEvent.LivingTickEvent event) {
//        Level level = event.getEntity().level;
//        for (Player player : level.players()) {
//
////            Minecraft mc = (LocalPlayer) player.minecraft;
//        }
//        if (event.getEntity() instanceof Peeper peep) {
//            AABB hitbox = peep.getBoundingBoxForCulling();
//            // if the peep's hitbox is in the player's view
//        }
    }

    @SubscribeEvent
    public static void jacobsLadder(TickEvent.PlayerTickEvent event) {
//        Player player = event.player;
//        Level level = player.getLevel();
//        Vec3 lookAngle = player.getLookAngle();
//        Vec3 orthogonalVec = lookAngle.cross(new Vec3(0, 1, 0)).normalize(); // Cross product with Y-axis to get orthogonal vector
//        Vec3 verticalVec = new Vec3(0, 1, 0); // Vertical direction vector
//
//        for (double v = -2; v <= 2; v += 0.5) { // Vertical dimension
//            for (double h = -2; h <= 2; h += 0.5) { // Horizontal dimension
//                Vec3 origin = player.getEyePosition(1.0F).add(lookAngle.scale(2)).add(orthogonalVec.scale(h)).add(verticalVec.scale(v));
//                for (double d = 0; d < 5; d += 1) { // Depth dimension
//                    Vec3 point = origin.add(lookAngle.scale(d));
//                    level.addParticle(ParticleTypes.SMALL_FLAME, point.x, point.y, point.z, 0, 0, 0);
//                    EnderMan
//                }
//            }
//        }
    }


}