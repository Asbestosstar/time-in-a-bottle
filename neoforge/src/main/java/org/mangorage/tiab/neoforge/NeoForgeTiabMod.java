package org.mangorage.tiab.neoforge;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.misc.IRegistrationWrapper;

import static org.mangorage.tiab.common.CommonConstants.MODID;

@Mod(MODID)
public class NeoForgeTiabMod {

    public NeoForgeTiabMod(IEventBus bus) {
        bus.addListener(this::onRegisterEvent);
    }

    public void onRegisterEvent(RegisterEvent event) {
        CommonRegistration.init(new IRegistrationWrapper() {
            @Override
            public <T> void register(ResourceKey<? extends Registry<T>> resourceKey, ResourceLocation resourceLocation, T value) {
                if (event.getRegistryKey() == resourceKey)
                    event.register((ResourceKey<Registry<T>>) event.getRegistry().key(), resourceLocation, () -> value);
            }
        });
    }
}
