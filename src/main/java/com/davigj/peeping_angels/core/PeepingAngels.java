package com.davigj.peeping_angels.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.resource.PathPackResources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;

@Mod(PeepingAngels.MOD_ID)
public class PeepingAngels {
    public static final String MOD_ID = "peeping_angels";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);
    public static final Logger LOGGER = LogManager.getLogger();

    public PeepingAngels() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext context = ModLoadingContext.get();
        MinecraftForge.EVENT_BUS.register(this);

		REGISTRY_HELPER.register(bus);

        bus.addListener(this::addResourcePack);
        context.registerConfig(ModConfig.Type.COMMON, PAConfig.COMMON_SPEC);
    }

    public void addResourcePack(AddPackFindersEvent event) {
        String packName = "resourcepacks/peeping_overrides";

        event.addRepositorySource((packConsumer, constructor) -> {
            Pack pack = Pack.create(MOD_ID + ":" + packName, true, () -> {
                Path path = ModList.get().getModFileById(MOD_ID).getFile().findResource("/" + packName);
                return new PathPackResources(packName, path);
            }, constructor, Pack.Position.TOP, PackSource.DEFAULT);

            if (pack != null) {
                packConsumer.accept(pack);
            } else {
                LOGGER.error(MOD_ID + ": Failed to register pack \"" + packName + "\"");
            }
        });
    }
}