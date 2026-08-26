package com.ksandr707.carpet_shadow_legacy.mixins.fragility;

import com.ksandr707.carpet_shadow_legacy.CarpetShadowLegacySettings;
import com.ksandr707.carpet_shadow_legacy.Globals;
import com.ksandr707.carpet_shadow_legacy.interfaces.ItemEntitySlot;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShadowItem;
import com.ksandr707.carpet_shadow_legacy.interfaces.ShifingItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Objects;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemEntitySlot, ShifingItem {

    @Unique
    boolean shiftMoving = false;
    @Unique
    private ItemEntity entity = null;

    @Override
    public boolean isShiftMoving() {
        return shiftMoving;
    }

    @Override
    public void setShiftMoving(boolean shiftMoving) {
        this.shiftMoving = shiftMoving;
    }

    @ModifyReturnValue(method = "isSameItemSameComponents", at = @At("RETURN"))
    private static boolean check_combine(boolean original, ItemStack stack, ItemStack otherStack) {
        return Globals.shadow_merge_check(stack, otherStack, original);
    }

    @Override
    public ItemEntity getEntity() {
        return entity;
    }

    @Override
    public void setEntity(ItemEntity entity) {
        this.entity = entity;
    }

    @ModifyReturnValue(method = "isSameItem", at = @At("RETURN"))
    private static boolean check_EqualIgnoreDamage(boolean original, ItemStack left, ItemStack right) {
        return Globals.shadow_merge_check(left, right, original);
    }

    @ModifyReturnValue(method = "matches", at = @At("RETURN"))
    private static boolean check_Equal(boolean original, ItemStack left, ItemStack right) {
        if (CarpetShadowLegacySettings.shadowItemInventoryFragilityFix && original) {
            String shadow1 = ((ShadowItem) (Object) left).getShadowId();
            String shadow2 = ((ShadowItem) (Object) right).getShadowId();
            if (!Objects.equals(shadow1, shadow2)) {
                return false;
            }
        }
        return original;
    }
}