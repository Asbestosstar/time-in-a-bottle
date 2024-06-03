package org.mangorage.tiab.forge;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.misc.IRegistrationWrapper;


@Mod(CommonConstants.MODID)
public class ForgeTiabMod {

    public ForgeTiabMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegister);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClient);
        MinecraftForge.EVENT_BUS.addListener(this::onServer);

        Pair<ForgeTiabConfig, ForgeConfigSpec> cfg = new ForgeConfigSpec.Builder()
                .configure(ForgeTiabConfig::new);

        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, cfg.getRight());
        CommonRegistration.SERVER_CONFIG.setConfig(cfg.getKey());
    }

    public void onRegister(RegisterEvent event) {
        CommonRegistration.init(new IRegistrationWrapper() {
            @Override
            public <T> void register(ResourceKey<? extends Registry<T>> resourceKey, ResourceLocation resourceLocation, T value) {
                if (resourceKey == event.getRegistryKey())
                    event.register(resourceKey, h -> h.register(resourceLocation, value));
            }
        });
    }

    public void onClient(FMLClientSetupEvent event) {
        EntityRenderers.register(CommonRegistration.ACCELERATOR_ENTITY.get(), TimeAcceleratorEntityRenderer::new);
    }

    public void onServer(ServerStartingEvent event) {
        CommonRegistration.initServer(event.getServer());
    }
}
