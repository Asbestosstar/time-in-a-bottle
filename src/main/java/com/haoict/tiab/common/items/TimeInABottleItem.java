package com.haoict.tiab.common.items;

import com.haoict.tiab.common.config.Constants;
import com.haoict.tiab.common.config.NBTKeys;
import com.haoict.tiab.common.config.TiabConfig;
import com.haoict.tiab.common.core.api.ApiRegistry;
import com.haoict.tiab.common.core.api.interfaces.ITimeInABottleItemAPI;
import com.haoict.tiab.common.utils.Utils;
import com.haoict.tiab.common.utils.lang.Styles;
import com.haoict.tiab.common.utils.lang.Translation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public final class TimeInABottleItem extends AbstractTiabItem {

    public TimeInABottleItem() {
        super();
        class Provider implements ITimeInABottleItemAPI {
            final TimeInABottleItem item;
            private Provider(TimeInABottleItem item) {
                this.item = item;
            }

            @Override
            public int getStoredEnergy(ItemStack stack) {
                return item.getStoredEnergy(stack);
            }

            @Override
            public void setStoredEnergy(ItemStack stack, int energy) {
                item.setStoredEnergy(stack, energy);
            }

            @Override
            public void applyDamage(ItemStack stack, int damage) {
                item.applyDamage(stack, damage);
            }

            @Override
            public int getTotalAccumulatedTime(ItemStack stack) {
                return item.getTotalAccumulatedTime(stack);
            }

            @Override
            public void setTotalAccumulatedTime(ItemStack stack, int value) {
                item.setTotalAccumulatedTime(stack, value);
            }
        }
        ApiRegistry.registerAccess(ITimeInABottleItemAPI.class, new Provider(this));
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(itemStack, level, entity, itemSlot, isSelected);
        if (level.isClientSide) {
            return;
        }

        if (level.getGameTime() % Constants.TICK_CONST == 0) {
            int storedTime = this.getStoredEnergy(itemStack);  // TODO: API CALL
            if (storedTime < TiabConfig.COMMON.maxStoredTime.get()) {
                this.setStoredEnergy(itemStack, storedTime + Constants.TICK_CONST); // TODO: API CALL
            }

            int totalAccumulatedTime = this.getTotalAccumulatedTime(itemStack);  // TODO: API CALL
            if (totalAccumulatedTime < TiabConfig.COMMON.maxStoredTime.get()) {
                this.setTotalAccumulatedTime(itemStack, totalAccumulatedTime + Constants.TICK_CONST); // TODO: API CALL
            }
        }

        // remove time if player has other TIAB items in his inventory, check every 10 sec
        if (level.getGameTime() % (Constants.TICK_CONST * 10) == 0) {
            if (!(entity instanceof Player player)) {
                return;
            }

            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack invStack = player.getInventory().getItem(i);
                if (invStack.getItem() == this) {
                    if (invStack != itemStack) {
                        int otherTimeData = this.getStoredEnergy(invStack); // TODO: API CALL
                        int myTimeData = this.getStoredEnergy(itemStack); // TODO: API CALL

                        if (myTimeData < otherTimeData) {
                            setStoredEnergy(itemStack, 0); // TODO: API CALL; Make it be a reset func
                        }
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(itemStack, world, tooltip, flag);

        tooltip.add(Utils.getStoredTimeTranslated(itemStack));
        tooltip.add(Utils.getTotalTimeTranslated(itemStack));
    }

    @Override
    protected int getStoredEnergy(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBTKeys.STORED_TIME);
    }

    @Override
    protected void setStoredEnergy(ItemStack stack, int energy) {
        int newStoredTime = Math.min(energy, TiabConfig.COMMON.maxStoredTime.get());
        stack.getOrCreateTag().putInt(NBTKeys.STORED_TIME, newStoredTime);
    }

    @Override
    protected void applyDamage(ItemStack stack, int damage) {
        setStoredEnergy(stack, getStoredEnergy(stack) - damage);
    }

    private int getTotalAccumulatedTime(ItemStack stack) {
        return stack.getOrCreateTag().getInt(NBTKeys.TOTAL_ACCUMULATED_TIME);
    }

    private void setTotalAccumulatedTime(ItemStack stack, int value) {
        int newValue = Math.min(value, TiabConfig.COMMON.maxStoredTime.get());
        stack.getOrCreateTag().putInt(NBTKeys.TOTAL_ACCUMULATED_TIME, newValue);
    }
}
