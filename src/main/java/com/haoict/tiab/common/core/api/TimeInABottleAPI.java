package com.haoict.tiab.common.core.api;

import com.haoict.tiab.common.config.Constants;
import com.haoict.tiab.common.core.ItemRegistry;
import com.haoict.tiab.common.core.api.interfaces.ITimeInABottleCommandAPI;
import com.haoict.tiab.common.core.api.interfaces.ITimeInABottleItemAPI;
import com.haoict.tiab.common.items.TimeInABottleItem;
import com.haoict.tiab.common.utils.Utils;
import com.magorage.tiab.api.ITimeInABottleAPI;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

/**
    Distributed to any mods wanting access to the API
    ApiRegistry is persistant/never changing on all
    Instances of this class. Should allow for secure
    enough api. Should stop mods from screwing around
    recklessly.
 */
public final class TimeInABottleAPI implements ITimeInABottleAPI {
    private final String API_MOD_ID;
    private final ApiRegistry REGISTRY = ApiRegistry.getApi();

    /**
     * @param API_MOD_ID -> The mod which requested to get an API
     */
    public TimeInABottleAPI(String API_MOD_ID) {
        this.API_MOD_ID = API_MOD_ID;
    }


    @Override
    public RegistryObject<Item> getRegistryObject() {
        return ItemRegistry.timeInABottleItem;
    }

    public String getModID() {
        return Constants.MOD_ID;
    }


    @Override
    public int getTotalTime(ItemStack bottle) {
        var api = REGISTRY.getRegistered(ITimeInABottleItemAPI.class);
        if (api != null) {
            if (bottle.getItem() instanceof TimeInABottleItem)
                return api.getTotalAccumulatedTime(bottle);
        }
        return 0;
    }

    @Override
    public int getStoredTime(ItemStack bottle) {
        var api = REGISTRY.getRegistered(ITimeInABottleItemAPI.class);
        if (api != null) {
            if (bottle.getItem() instanceof TimeInABottleItem)
                return api.getStoredEnergy(bottle);
        }
        return 0;
    }

    @Override
    public boolean canSetTime() {
        return true; // Update this later!
    }

    @Override
    public void setStoredTime(ItemStack bottle, int time) {
        var api = REGISTRY.getRegistered(ITimeInABottleItemAPI.class);
        if (api != null) {
            if (bottle.getItem() instanceof TimeInABottleItem)
                api.setStoredEnergy(bottle, time);
        }
    }

    @Override
    public void setTotalTime(ItemStack bottle, int time) {
        var api = REGISTRY.getRegistered(ITimeInABottleItemAPI.class);
        if (api != null) {
            if (bottle.getItem() instanceof TimeInABottleItem)
                api.setTotalAccumulatedTime(bottle, time);
        }
    }

    @Override
    public int processCommand(Function<ServerPlayer, ItemStack> itemStackFunction, ServerPlayer player, Component messageValue, boolean isAdd) {
        var api = REGISTRY.getRegistered(ITimeInABottleCommandAPI.class);
        if (api != null) {
            return api.processCommand(itemStackFunction, player, messageValue, isAdd);
        }
        return 0;
    }

    @Override
    public Component getTotalTimeTranslated(ItemStack stack) {
        return Utils.getTotalTimeTranslated(stack);
    }

    @Override
    public Component getStoredTimeTranslated(ItemStack stack) {
        return Utils.getStoredTimeTranslated(stack);
    }
}
