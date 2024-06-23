package org.mangorage.tiab.fabric;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import org.mangorage.tiab.common.CommonTiabMod;
import org.mangorage.tiab.common.client.renderer.TimeAcceleratorEntityRenderer;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.core.LoaderSide;

public class FabricTiabMod extends CommonTiabMod implements ModInitializer {
    public FabricTiabMod() {
        super(LoaderSide.FABRIC, modid -> FabricLoader.getInstance().isModLoaded(modid));
        ServerLifecycleEvents.SERVER_STARTING.register(CommonRegistration::initServer);
    }

    @Override
    public void onInitialize() {
        CommonRegistration.SERVER_CONFIG.setConfig(FabricTiabConfig.get());
        CommonRegistration.register();

        if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT)
            EntityRendererRegistry.register(CommonRegistration.ACCELERATOR_ENTITY.get(), TimeAcceleratorEntityRenderer::new);
    }
}
