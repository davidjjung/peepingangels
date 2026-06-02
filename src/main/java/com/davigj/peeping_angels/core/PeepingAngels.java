package com.davigj.peeping_angels.core;

import com.teamabnormals.blueprint.core.util.registry.RegistryHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddPackFindersEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.file.Path;
import java.util.Optional;

@Mod(PeepingAngels.MOD_ID)
public class PeepingAngels {
    public static final String MOD_ID = "peeping_angels";
    public static final RegistryHelper REGISTRY_HELPER = new RegistryHelper(MOD_ID);
    public static final Logger LOGGER = LogManager.getLogger();

    public PeepingAngels(IEventBus bus, ModContainer container) {
        REGISTRY_HELPER.register(bus);

        bus.addListener(this::addAdvancementOverrides);

        container.registerConfig(ModConfig.Type.COMMON, PAConfig.COMMON_SPEC);
    }

    private void addAdvancementOverrides(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            Path resourcePath = ModList.get().getModFileById(MOD_ID).getFile().findResource("resourcepacks/peeping_overrides");

            PackLocationInfo locationInfo = new PackLocationInfo(
                    MOD_ID + ":peeping_overrides",
                    Component.literal("Peeping Overrides"),
                    PackSource.BUILT_IN,
                    Optional.empty()
            );

            Pack.ResourcesSupplier resourcesSupplier = new PathPackResources.PathResourcesSupplier(resourcePath);
            PackSelectionConfig selectionConfig = new PackSelectionConfig(false, Pack.Position.TOP, false);
            Pack pack = Pack.readMetaAndCreate(
                    locationInfo,
                    resourcesSupplier,
                    PackType.CLIENT_RESOURCES,
                    selectionConfig
            );

            if (pack != null) {
                event.addRepositorySource((source) -> source.accept(pack));
            }
        }
    }
}