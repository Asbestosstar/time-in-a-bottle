package com.magorage.tiab.api;

import com.magorage.tiab.api.events.TimeCommandEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Function;

public interface ITimeInABottleAPI {
    class IMC {
        public static final String GET_API = "get_api";
        public static final String MOD_ID = "tiab";
    }

    RegistryObject<Item> getRegistryObject();
    int getTotalTime(ItemStack bottle);
    int getStoredTime(ItemStack bottle);
    String getModID();
    void setStoredTime(ItemStack bottle, int time);
    void setTotalTime(ItemStack bottle, int time);
    int processCommand(Function<ServerPlayer, ItemStack> itemStackFunction, ServerPlayer player, String messageValue, boolean isAdd);
    Component getTotalTimeTranslated(ItemStack stack);
    Component getStoredTimeTranslated(ItemStack stack);
    void playSound(Level level, BlockPos pos, int nextRate);
    void applyDamage(ItemStack stack, int damage);
    int getEnergyCost(int timeRate);
    boolean canUse();
    TimeCommandEvent createEvent(ItemStack stack, ServerPlayer player, int time, boolean isAdd);
}
